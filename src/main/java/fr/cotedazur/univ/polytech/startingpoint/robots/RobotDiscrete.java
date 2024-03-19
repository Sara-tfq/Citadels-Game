package fr.cotedazur.univ.polytech.startingpoint.robots;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.game.ActionOfBotDuringARound;

import java.util.*;
import java.util.stream.Stream;

public class RobotDiscrete extends Robot {

    private static final String NOBLE = "noble";
    private static final String RELIGIOUS = "religieux";
    private static final String MILITARY = "militaire";
    private static final String MERCHANT = "marchand";

    public RobotDiscrete(String name) {
        super(name);
    }

    @Override
    public String tryBuild() {
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(this, true);
        List<String> listDistrictName = new ArrayList<>();
        for (DistrictsType districtsType : getCity()) {
            listDistrictName.add(districtsType.getName());
        }

        List<DistrictsType> districtsOfType = getDistrictInHand().stream()
                .filter(district -> district.getType().equals(getCharacter().getType()))
                .toList();

        if (!districtsOfType.isEmpty()) {

            String district = buildDistrict(listDistrictName, districtsOfType);
            if (district != null) {
                action.printBuildDistrictWithSameType(district);
                return district;
            }

        }

        List<DistrictsType> allDistricts = new ArrayList<>(Stream.concat(getCity().stream(), getDistrictInHand().stream())
                .toList());

        allDistricts.sort(Comparator.comparingInt(district -> Collections.frequency(allDistricts, district.getType())));

        String district = buildDistrict(listDistrictName, allDistricts);
        if (district != null) {
            action.printBuildFrequentTypeDistrict(district);
            return district;
        }

        return "nothing";
    }

    private String buildDistrict(List<String> listDistrictName, List<DistrictsType> allDistricts) {
        List<DistrictsType> listDistrict = new ArrayList<>(allDistricts);
        listDistrict.sort(Comparator.comparingInt(DistrictsType::getCost).reversed());
        for (DistrictsType district : listDistrict) {
            if (!listDistrictName.contains(district.getName()) && district.getCost() <= getGolds()) {
                district.powerOfDistrict(this, 1);
                getCity().add(district);
                setGolds(getGolds() - district.getCost());
                getDistrictInHand().remove(district);
                return "a new " + district.getName();
            }
        }
        return null;
    }


    @Override
    public int generateChoice() {
        int minCost = getDistrictInHand().stream()
                .filter(district -> !getCity().contains(district))
                .mapToInt(DistrictsType::getCost)
                .min()
                .orElse(Integer.MAX_VALUE);

        if (getDistrictInHand().isEmpty() || getDistrictInHand().stream().allMatch(getCity()::contains)) return 0;
        else if (getGolds() < minCost) return 1;
        else return (int) (Math.random() * 2);
    }

    public int countDistrictsByType(String type) {
        return (int) getCity().stream()
                .filter(district -> district.getType().equals(type))
                .count();
    }

    public int countDistrictsInHandByType(String type) {
        return (int) getDistrictInHand().stream()
                .filter(district -> district.getType().equals(type))
                .count();
    }

    @Override
    public void pickCharacter(List<CharactersType> availableCharacters, List<Robot> bots) {
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(this, true);
        Map<CharactersType, Integer> characterCounts = new HashMap<>();
        characterCounts.put(CharactersType.ROI, countDistrictsByType(NOBLE) + countDistrictsInHandByType(NOBLE));
        characterCounts.put(CharactersType.EVEQUE, countDistrictsByType(RELIGIOUS) + countDistrictsInHandByType(RELIGIOUS));
        characterCounts.put(CharactersType.CONDOTTIERE, countDistrictsByType(MILITARY) + countDistrictsInHandByType(MILITARY));
        characterCounts.put(CharactersType.MARCHAND, countDistrictsByType(MERCHANT) + countDistrictsInHandByType(MERCHANT));

        List<CharactersType> priorityOrder = characterCounts.entrySet().stream()
                .sorted(Map.Entry.<CharactersType, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();

        CharactersType chosenCharacter = null;

        for (CharactersType character : priorityOrder) {
            if (availableCharacters.contains(character)) {
                chosenCharacter = character;
                availableCharacters.remove(character);
                break;
            }
        }

        if (chosenCharacter == null && !availableCharacters.isEmpty()) {
            chosenCharacter = availableCharacters.get(0);
            availableCharacters.remove(0);
        }

        setCharacter(chosenCharacter);
    }


    @Override
    public List<DistrictsType> pickDistrictCard(List<DistrictsType> listDistrict, DeckDistrict deck) {
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(this, true);
        listDistrict.sort(compareByCost().reversed());
        List<DistrictsType> listDistrictToBuild = new ArrayList<>();
        int indice = 0;

        indice = chooseDistrictByType(listDistrict, indice, listDistrictToBuild);

        indice = chooseSpecialDistrict(listDistrict, indice, listDistrictToBuild);

        indice = chooseAnyDistrict(listDistrict, indice, listDistrictToBuild);

        if (indice < getNumberOfCardsChosen()) {
            action.printCantPickDistrict();

        }

        for (DistrictsType districtNonChosen : listDistrict) {
            deck.addDistrictToDeck(districtNonChosen);
        }
        return listDistrictToBuild;
    }

    private int chooseAnyDistrict(List<DistrictsType> listDistrict, int indice, List<DistrictsType> listDistrictToBuild) {
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(this, true);
        if (indice < getNumberOfCardsChosen()) {
            Iterator<DistrictsType> iterator = listDistrict.iterator();
            while (iterator.hasNext()) {
                DistrictsType currentDistrict = iterator.next();
                if (!isDistrictInCityOrHand(currentDistrict)) {
                    indice = chooseDistrict(currentDistrict, listDistrictToBuild, indice);
                    iterator.remove();
                    action.printPickAnyDistrict(currentDistrict);
                }
                if (indice == getNumberOfCardsChosen()) break;
            }
        }
        return indice;
    }

    private int chooseSpecialDistrict(List<DistrictsType> listDistrict, int indice, List<DistrictsType> listDistrictToBuild) {
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(this, true);
        if (indice < getNumberOfCardsChosen()) {
            Iterator<DistrictsType> iterator = listDistrict.iterator();
            while (iterator.hasNext()) {
                DistrictsType currentDistrict = iterator.next();
                if (!isDistrictInCityOrHand(currentDistrict) && isSpecialDistrictType(currentDistrict.getType())) {
                    indice = chooseDistrict(currentDistrict, listDistrictToBuild, indice);
                    iterator.remove();
                    action.printPickSpecialDistrict(currentDistrict);
                }
                if (indice == getNumberOfCardsChosen()) break;
            }
        }
        return indice;
    }

    private int chooseDistrictByType(List<DistrictsType> listDistrict, int indice, List<DistrictsType> listDistrictToBuild) {
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(this, true);
        Iterator<DistrictsType> iterator = listDistrict.iterator();
        while (iterator.hasNext()) {
            DistrictsType currentDistrict = iterator.next();
            if (!isDistrictInCityOrHand(currentDistrict) && currentDistrict.getType().equals(this.getCharacter().getType())) {
                indice = chooseDistrict(currentDistrict, listDistrictToBuild, indice);
                iterator.remove();
                action.printPickDistrictByType(currentDistrict);
            }
            if (indice == getNumberOfCardsChosen()) break;
        }
        return indice;
    }


    private boolean isDistrictInCityOrHand(DistrictsType district) {
        return this.getCity().contains(district) || this.getDistrictInHand().contains(district);
    }

    private boolean isSpecialDistrictType(String type) {
        return type.equals(NOBLE) || type.equals(RELIGIOUS) || type.equals(MERCHANT) || type.equals(MILITARY);
    }

    private int chooseDistrict(DistrictsType currentDistrict, List<DistrictsType> listDistrictToBuild, int indice) {
        listDistrictToBuild.add(currentDistrict);
        indice++;
        return indice;
    }


}
