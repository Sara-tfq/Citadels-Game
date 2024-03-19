package fr.cotedazur.univ.polytech.startingpoint.robots;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.game.ActionOfBotDuringARound;

import java.util.ArrayList;
import java.util.List;

public class RobotChoiceOfCharacter extends Robot {


    public RobotChoiceOfCharacter(String name) {
        super(name);
    }

    @Override
    public String tryBuild() {
        for (int i = 0; i < getDistrictInHand().size(); i++) {
            DistrictsType district = getDistrictInHand().get(i);
            if (district.getCost() <= getGolds() && !city.contains(district)) {
                district.powerOfDistrict(this, 1);
                getCity().add(district);
                setGolds(getGolds() - district.getCost());
                getDistrictInHand().remove(i);
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
        int i = 0;
        while (i < listDistrict.size()) {
            if (listDistrict.get(i).getCost() - costOfDistrictToBeBuilt <= getGolds()) {
                costOfDistrictToBeBuilt += listDistrict.get(i).getCost();
                listDistrictToBuild.add(listDistrict.remove(i));
                i--;
                indice++;
                if (indice == getNumberOfCardsChosen()) break;

            }
            i++;
        }
        while (listDistrictToBuild.size() < getNumberOfCardsChosen()) {
            listDistrictToBuild.add(listDistrict.remove(listDistrict.size() - 1));
        }
        for (DistrictsType districtNonChosen : listDistrict) {
            deck.addDistrictToDeck(districtNonChosen);
        }
        return listDistrictToBuild;
    }

    @Override
    public int generateChoice() {
        if (canFinishNextTurn()) return 1;
        if (getDistrictInHand().isEmpty()) return 0;
        if (!canBuildADistrictInHand()) return 1;
        return (int) (Math.random() * 2);
    }

    @Override
    public void pickCharacter(List<CharactersType> availableCharacters, List<Robot> bots) {
        boolean hasPickedMagician = tryToPick(CharactersType.MAGICIEN, availableCharacters);
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(this, true);
        if (hasPickedMagician) {
            return;
        }
        if (canFinishThisTurn()) {
            boolean hasPickedAssassin = tryToPick(CharactersType.ASSASSIN, availableCharacters);
            if (hasPickedAssassin) {
                action.printCanFinishThisTurn();
                return;
            }
        }

        if (canFinishNextTurn()) {
            boolean hasPickedKing = tryToPick(CharactersType.ROI, availableCharacters);
            if (hasPickedKing) {
                action.printCanFinishNextTurn();
                return;
            }
        }

        boolean hasPickedCharacter = pickCharacterBasedOfNumberOfDistrict(availableCharacters);
        if (hasPickedCharacter) {
            action.printPickCharacterBasedOnNumberOfBuildings();
            return;
        }

        for (Robot bot : bots) {
            if (bot.city.size() == 6 && !bot.getName().equals(getName())) {
                boolean hasPicked = tryToPick(CharactersType.CONDOTTIERE, availableCharacters);
                if (hasPicked) {
                    action.printPickCondottiere(bot);
                    return;
                }
            }
        }
        setCharacter(availableCharacters.get(0));
        availableCharacters.remove(0);
    }

    public boolean canFinishThisTurn() {
        return canBuildADistrictInHand() && (getNumberOfDistrictInCity() == 7);
    }

    public boolean canFinishNextTurn() {
        if (getNumberOfDistrictInHand() < 2 || getNumberOfDistrictInCity() < 6) return false;
        int totalGolds = getGolds() + 2;
        int numberOfDistrictThatCanBeBuilt = 0;
        for (DistrictsType districtInHand : districtInHand) {
            if (districtInHand.getCost() <= totalGolds) {
                totalGolds -= districtInHand.getCost();
                numberOfDistrictThatCanBeBuilt++;
            }
        }
        return (numberOfDistrictThatCanBeBuilt >= 2);
    }

    public boolean tryToPick(CharactersType characterToPick, List<CharactersType> availableCharacters) {
        for (CharactersType charactersType : availableCharacters) {
            if (charactersType.equals(characterToPick) && availableCharacters.contains(charactersType)) {
                setCharacter(charactersType);
                availableCharacters.remove(charactersType);
                return true;
            }
        }
        return false;
    }

    @Override
    public Robot chooseVictimForCondottiere(List<Robot> bots) {
        Robot victim = bots.get(0);
        for (Robot bot : bots) {
            if (bot.getNumberOfDistrictInCity() == 6 && bot.getCharacter() != CharactersType.CONDOTTIERE && !bot.hasEightDistrict()) {
                return bot;
            }
        }
        return victim;
    }

    public boolean pickCharacterBasedOfNumberOfDistrict(List<CharactersType> availableCharacters) {
        for (CharactersType charactersType : availableCharacters) {
            setCharacter(charactersType);//On assigne le personnage au joueur temporairement pour calculer le nombre de district dans la cité du joueur de même couleur que celle du personnage
            if (countBuildingsByType() >= 2) {
                availableCharacters.remove(charactersType);
                return true;
            }
        }
        return false;
    }
}
