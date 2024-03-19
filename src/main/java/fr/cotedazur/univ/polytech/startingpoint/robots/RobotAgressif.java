package fr.cotedazur.univ.polytech.startingpoint.robots;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.characters.Colors;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.game.ActionOfBotDuringARound;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RobotAgressif extends Robot {

    ActionOfBotDuringARound action = new ActionOfBotDuringARound(this, true);


    public RobotAgressif(String name) {
        super(name);
    }

    @Override
    public String tryBuild() {
        List<DistrictsType> listToBuildFrom = new ArrayList<>(this.districtInHand); // Make a copy to avoid concurrent modification

        long redCount = this.city.stream()
                .filter(district -> district.getColor() == Colors.RED)
                .count();

        if (redCount < 3) {

            for (DistrictsType district : listToBuildFrom) {
                if (district.getColor() == Colors.RED && district.getCost() <= this.getGolds()) {
                    district.powerOfDistrict(this, 1);
                    this.city.add(district);
                    this.setGolds(this.getGolds() - district.getCost());
                    this.districtInHand.remove(district);
                    action.printPrioritizesRed();
                    return "a new " + district.getName();
                }
            }
        }

        List<Colors> listOfColors = Colors.getListOfColors();
        for (DistrictsType district : this.getCity()) {
            listOfColors.remove(district.getColor());
        }


        for (DistrictsType district : listToBuildFrom) {
            if (listOfColors.contains(district.getColor()) && district.getCost() <= this.getGolds()) {
                district.powerOfDistrict(this, 1);
                this.city.add(district);
                this.setGolds(this.getGolds() - district.getCost());
                this.districtInHand.remove(district);
                action.printBotBonus();
                return "a new " + district.getName();
            }
        }
        List<String> listDistrictName = this.city.stream()
                .map(DistrictsType::getName)
                .collect(Collectors.toList());

        for (int i = 0; i < listToBuildFrom.size(); i++) {
            DistrictsType district = listToBuildFrom.get(i);
            if (district.getCost() <= this.getGolds() && !listDistrictName.contains(district.getName())) {
                district.powerOfDistrict(this, 1);
                this.city.add(district);
                this.setGolds(this.getGolds() - district.getCost());
                this.districtInHand.remove(i);
                return "a new " + district.getName();
            }
        }

        return "nothing";

    }


    @Override
    public List<DistrictsType> pickDistrictCard(List<DistrictsType> listDistrict, DeckDistrict deck) {
        listDistrict.sort(compareByCost().reversed());
        List<DistrictsType> listDistrictToBuild = new ArrayList<>();
        int costOfDistrictToBeBuilt = 0;
        int indice = 0;
        boolean hasRedCards = listDistrict.stream().anyMatch(district -> district.getColor().equals(Colors.RED));
        for (int i = 0; i < listDistrict.size(); i++) {
            DistrictsType currentDistrict = listDistrict.get(i);
            if (hasRedCards && currentDistrict.getColor().equals(Colors.RED) && currentDistrict.getCost() - costOfDistrictToBeBuilt <= getGolds()) {
                costOfDistrictToBeBuilt += currentDistrict.getCost();
                listDistrictToBuild.add(listDistrict.remove(i));
                i--;
                indice++;
                if (indice == getNumberOfCardsChosen()) break;
            }
        }
        if (listDistrictToBuild.isEmpty()) {
            Set<Colors> colorsInCity = getColorsInCity();

            for (int i = 0; i < listDistrict.size(); i++) {
                DistrictsType currentDistrict = listDistrict.get(i);
                if (!colorsInCity.contains(currentDistrict.getColor()) && currentDistrict.getCost() - costOfDistrictToBeBuilt <= getGolds()) {
                    costOfDistrictToBeBuilt += currentDistrict.getCost();
                    listDistrictToBuild.add(listDistrict.remove(i));
                    i--;
                    indice++;
                    if (indice == getNumberOfCardsChosen()) break;
                }
            }
        }

        while (listDistrictToBuild.size() < getNumberOfCardsChosen()) {
            listDistrictToBuild.add(listDistrict.remove(listDistrict.size() - 1));
        }
        for (DistrictsType districtNonChosen : listDistrict) {
            deck.addDistrictToDeck(districtNonChosen);
        }
        return listDistrictToBuild;
    }


    private Set<Colors> getColorsInCity() {
        return getCity().stream().map(DistrictsType::getColor).collect(Collectors.toSet());
    }


    @Override
    public Robot chooseVictimForCondottiere(List<Robot> bots) {
        Robot victim = bots.get(0);
        int maxDistricts = victim.getNumberOfDistrictInCity();
        for (Robot bot : bots) {
            int currentDistricts = bot.getNumberOfDistrictInCity();
            if (currentDistricts > maxDistricts && bot.getCharacter() != CharactersType.CONDOTTIERE) {
                victim = bot;
                maxDistricts = currentDistricts;
            }
        }
        return victim;
    }


    @Override
    public Robot chooseVictimForAssassin(List<Robot> bots, int numberOfTheCharacterToKill) {
        Robot victim = null;
        int maxGold = Integer.MIN_VALUE;
        for (Robot bot : bots) {
            if (bot.getCharacter().getNumber() == numberOfTheCharacterToKill && bot.getGolds() > maxGold) {
                victim = bot;
                maxGold = bot.getGolds();
            }
        }
        return victim;


    }


    @Override
    public int generateChoice() {
        if (this.getGolds() < 5) {
            return 1;
        } else {
            return 0;
        }
    }


    public void pickCharacter(List<CharactersType> availableCharacters, List<Robot> bots) {
        if (getHasCrown()) {
            setCharacter(availableCharacters.get(0));
            availableCharacters.remove(0);
        } else {
            CharactersType preferredCharacter = availableCharacters.stream()
                    .filter(character -> isPreferredCharacter(character))
                    .findFirst()
                    .orElse(availableCharacters.get(0));

            setCharacter(preferredCharacter);
            availableCharacters.remove(preferredCharacter);
        }
    }


    private boolean isPreferredCharacter(CharactersType character) {
        return (character.getType().equals(CharactersType.CONDOTTIERE.getType()) && getGolds() > 7) ||
                (character.getType().equals(CharactersType.ASSASSIN.getType()) && getGolds() > 5) ||
                (character.getType().equals(CharactersType.VOLEUR.getType()));
    }


}
