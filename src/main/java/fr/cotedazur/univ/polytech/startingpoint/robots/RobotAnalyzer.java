package fr.cotedazur.univ.polytech.startingpoint.robots;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.characters.Colors;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.game.ActionOfBotDuringARound;

import java.util.*;
import java.util.stream.Collectors;

public class RobotAnalyzer extends Robot {
    private final ActionOfBotDuringARound action;

    public RobotAnalyzer(String name) {
        super(name);
        this.action = new ActionOfBotDuringARound(this, true);

    }


    @Override
    public String tryBuild() {
        List<DistrictsType> districtsInHand = new ArrayList<>(getDistrictInHand());
        List<DistrictsType> uniqueDistrictTypesInCity = getCity();


        String builtDistrictName = "nothing";

        //districts spéciaux
        List<DistrictsType> specialDistricts = districtsInHand.stream()
                .filter(d -> d.getColor() == Colors.PURPLE && d.getCost() <= getGolds())
                .collect(Collectors.toList());
        if (!specialDistricts.isEmpty()) {
            builtDistrictName = buildFirstAvailableDistrict(specialDistricts, uniqueDistrictTypesInCity);
            action.printSpecialDistrictsConsideration();
        }


        if (builtDistrictName.equals("nothing")) {
            List<DistrictsType> blockingDistricts = districtsInHand.stream()
                    .filter(d -> d.getType().equals(this.getCharacter().getType()) && d.getCost() <= getGolds())
                    .collect(Collectors.toList());
            if (!blockingDistricts.isEmpty()) {
                builtDistrictName = buildFirstAvailableDistrict(blockingDistricts, uniqueDistrictTypesInCity);
                action.printBlockOpponentStrategy(builtDistrictName);
            }
        }

        //district plus rentable
        if (builtDistrictName.equals("nothing")) {
            for (DistrictsType district : districtsInHand) {
                if (district.getCost() <= getGolds() && !uniqueDistrictTypesInCity.contains(district)) {
                    buildDistrict(district);
                    builtDistrictName = district.getName();
                    action.printEfficiencyBasedBuilding(builtDistrictName);
                    break;
                }
            }
        }

        return "nothing".equals(builtDistrictName) ? "nothing" : "a new " + builtDistrictName;
    }


    private String buildFirstAvailableDistrict(List<DistrictsType> districts, List<DistrictsType> uniqueDistrictTypesInCity) {
        for (DistrictsType district : districts) {
            if (!uniqueDistrictTypesInCity.contains(district)) {
                buildDistrict(district);
                return district.getName();
            }
        }
        return "nothing";
    }


    private void buildDistrict(DistrictsType district) {
        getCity().add(district);
        setGolds(getGolds() - district.getCost());
        getDistrictInHand().remove(district);
    }


    @Override
    public List<DistrictsType> pickDistrictCard(List<DistrictsType> listDistrict, DeckDistrict deck) {
        List<DistrictsType> uniqueDistrictTypesInCity = getCity();
        Map<String, Integer> districtTypeFrequencyInCity = getCity().stream().collect(Collectors.groupingBy(DistrictsType::getType, Collectors.summingInt(e -> 1)));

        listDistrict.sort(Comparator.comparingInt((DistrictsType d) -> districtTypeFrequencyInCity.getOrDefault(d.getType(), 0))
                .thenComparingInt(DistrictsType::getScore).reversed());

        List<DistrictsType> chosenDistricts = new ArrayList<>();
        int indice = 0;

        //strat de choix de carte basée sur le coût, la diversité et pour perturber les adversaires
        for (DistrictsType district : listDistrict) {
            if (!uniqueDistrictTypesInCity.contains(district) && specialDistrict(district)) {
                chosenDistricts.add(district);
                listDistrict.remove(district);
                indice++;
                break;
            }


        }
        if (indice < getNumberOfCardsChosen()) {
            for (DistrictsType district : listDistrict) {
                if (district.getCost() <= getGolds() && (!uniqueDistrictTypesInCity.contains(district))) {
                    chosenDistricts.add(district);
                    listDistrict.remove(district);
                    indice++;
                }
                if (indice == getNumberOfCardsChosen()) break;

            }
        }
        if (indice < getNumberOfCardsChosen()) {
            for (DistrictsType district : listDistrict) {
                if (!uniqueDistrictTypesInCity.contains(district)) {
                    chosenDistricts.add(district);
                    listDistrict.remove(district);
                    indice++;
                }
                if (indice == getNumberOfCardsChosen()) break;

            }
        }
        listDistrict.stream().filter(district -> !chosenDistricts.contains(district)).forEach(deck::addDistrictToDeck);
        action.printDistrictChoice(chosenDistricts);
        return chosenDistricts;
    }


    public boolean specialDistrict(DistrictsType district) {
        return district.getType().equals("noble") || district.getType().equals("religieux") || district.getType().equals("marchand") || district.getType().equals("militaire");
    }


    @Override
    public int generateChoice() {
        List<DistrictsType> buildableDistricts = getDistrictInHand().stream()
                .filter(district -> district.getCost() <= getGolds())
                .collect(Collectors.toList());

        if (getDistrictInHand().isEmpty() || getDistrictInHand().stream().allMatch(getCity()::contains)) return 0;

        if (buildableDistricts.isEmpty()) {
            return 1;//ressources
        }
        return 1;
    }


    @Override
    public void pickCharacter(List<CharactersType> availableCharacters, List<Robot> bots) {
        CharactersType chosenCharacter = null;
        Map<Integer, CharactersType> characterFrequency = new HashMap<>();

        int minCost = getDistrictInHand().stream()
                .filter(district -> !getCity().contains(district))
                .mapToInt(DistrictsType::getCost)
                .min()
                .orElse(Integer.MAX_VALUE);


        boolean needMoreGold = this.getGolds() < minCost;
        boolean needMoreCards = this.getDistrictInHand().size() < 3;
        boolean opponentCloseToWinning = bots.stream().anyMatch(bot -> bot.getCity().size() >= 7);

        for (Robot bot : bots) {
            characterFrequency.put(countOpponentNextCharacter(bot.getName()), predictOpponentNextCharacter(bot.getName()));
        }

        CharactersType maxCharacterFrequency = Collections.max(characterFrequency.entrySet(), Map.Entry.comparingByKey()).getValue();
        //action.printCharacterPrediction(maxCharacterFrequency);

        chosenCharacter = chooseCharacterDefault(availableCharacters, needMoreGold, needMoreCards, opponentCloseToWinning);

        if (chosenCharacter == null) {
            chosenCharacter = availableCharacters.get(0);
            availableCharacters.remove(0);
        }
        action.printCharacterPredictionAndChoice(maxCharacterFrequency, chosenCharacter);
        setCharacter(chosenCharacter);
    }

    private CharactersType chooseCharacterDefault(List<CharactersType> availableCharacters, boolean needMoreGold, boolean needMoreCards, boolean opponentCloseToWinning) {
        CharactersType chosenCharacter = null;
        if (needMoreGold && availableCharacters.contains(CharactersType.MARCHAND)) {
            chosenCharacter = CharactersType.MARCHAND;
            availableCharacters.remove(chosenCharacter);
        } else if (needMoreCards && availableCharacters.contains(CharactersType.ARCHITECTE)) {
            chosenCharacter = CharactersType.ARCHITECTE;
            availableCharacters.remove(chosenCharacter);
        } else if (opponentCloseToWinning) {
            if (availableCharacters.contains(CharactersType.ASSASSIN)) {
                chosenCharacter = CharactersType.ASSASSIN;
                availableCharacters.remove(chosenCharacter);
            } else if (availableCharacters.contains(CharactersType.CONDOTTIERE)) {
                chosenCharacter = CharactersType.CONDOTTIERE;
                availableCharacters.remove(chosenCharacter);
            }
        } else {
            chosenCharacter = availableCharacters.get(0);
            availableCharacters.remove(chosenCharacter);

        }
        return chosenCharacter;
    }
}