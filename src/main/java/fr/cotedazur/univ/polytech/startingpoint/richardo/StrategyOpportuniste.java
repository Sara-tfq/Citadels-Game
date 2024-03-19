package fr.cotedazur.univ.polytech.startingpoint.richardo;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.game.ActionOfBotDuringARound;
import fr.cotedazur.univ.polytech.startingpoint.robots.Robot;

import java.util.*;

import static fr.cotedazur.univ.polytech.startingpoint.richardo.RobotRichardo.MILITARY;
import static fr.cotedazur.univ.polytech.startingpoint.richardo.RobotRichardo.RELIGIOUS;


public class StrategyOpportuniste {

    public StrategyOpportuniste() {
    }

    public void isOpportuniste(RobotRichardo bot) {
        bot.setOpportuniste(bot.getNumberOfDistrictInCity() >= 6 || bot.getGolds() == 0);
    }


    public String tryBuildOpportuniste(RobotRichardo bot) {
        List<DistrictsType> listDistrictInHand = bot.getDistrictInHand();
        List<DistrictsType> listDistrictInCity = bot.getCity();
        listDistrictInHand.sort(Comparator.comparingInt(DistrictsType::getCost).reversed());
        for (DistrictsType district : listDistrictInHand) {
            if (!listDistrictInCity.contains(district) && ((district.getType().equals(RELIGIOUS) || district.getType().equals(MILITARY)) && district.getCost() <= bot.getGolds())) {
                district.powerOfDistrict(bot, 1);
                listDistrictInCity.add(district);
                bot.setGolds(bot.getGolds() - district.getCost());
                listDistrictInHand.remove(district);
                return "a new " + district.getName();

            }

        }

        return bot.buildDistrictAndRetrieveItsName();
    }


    public boolean pickOpportuniste(List<CharactersType> availableCharacters, RobotRichardo robot) {
        isOpportuniste(robot);
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(robot, true);
        Map<CharactersType, Integer> characterCounts = new HashMap<>();
        characterCounts.put(CharactersType.EVEQUE, robot.countDistrictsByType(RELIGIOUS) + robot.countDistrictsInHandByType(RELIGIOUS));
        characterCounts.put(CharactersType.CONDOTTIERE, robot.countDistrictsByType(MILITARY) + robot.countDistrictsInHandByType(MILITARY));

        List<CharactersType> priorityOrder = characterCounts.entrySet().stream()
                .sorted(Map.Entry.<CharactersType, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();

        CharactersType chosenCharacter = null;

        for (CharactersType character : priorityOrder) {
            if (availableCharacters.contains(character)) {
                chosenCharacter = character;
                availableCharacters.remove(character);
                action.printPrioritizeTYpe(chosenCharacter);
                robot.setCharacter(chosenCharacter);
                return true;
            }
        }

        if (availableCharacters.contains(CharactersType.VOLEUR)) {
            chosenCharacter = CharactersType.VOLEUR;
            availableCharacters.remove(CharactersType.VOLEUR);
            robot.setCharacter(chosenCharacter);
            robot.setAvailableCharacters(availableCharacters);
            action.printPrioritizeTYpe(chosenCharacter);
            return true;
        }

        return false;
    }

    public List<DistrictsType> pickDistrictCardOpportuniste(List<DistrictsType> listDistrict, DeckDistrict deck, RobotRichardo robot) {
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(robot, true);
        listDistrict.sort(Comparator.comparing(DistrictsType::getCost).reversed());
        List<DistrictsType> listDistrictToBuild = new ArrayList<>();
        int indice = 0;

        indice = chooseDistrictByType(listDistrict, indice, listDistrictToBuild, robot);

        indice = chooseSpecialDistrict(listDistrict, indice, listDistrictToBuild, robot);

        indice = chooseAnyDistrict(listDistrict, indice, listDistrictToBuild, robot);

        if (indice < robot.getNumberOfCardsChosen()) {
            action.printCantPickDistrict();

        }

        for (DistrictsType districtNonChosen : listDistrict) {
            deck.addDistrictToDeck(districtNonChosen);
        }
        return listDistrictToBuild;
    }

    private int chooseAnyDistrict(List<DistrictsType> listDistrict, int indice, List<DistrictsType> listDistrictToBuild, RobotRichardo robot) {
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(robot, true);
        if (indice < robot.getNumberOfCardsChosen()) {
            Iterator<DistrictsType> iterator = listDistrict.iterator();
            while (iterator.hasNext()) {
                DistrictsType currentDistrict = iterator.next();
                if (!isDistrictInCityOrHand(currentDistrict, robot)) {
                    indice = chooseDistrict(currentDistrict, listDistrictToBuild, indice);
                    iterator.remove();
                    action.printPickAnyDistrict(currentDistrict);
                }
                if (indice == robot.getNumberOfCardsChosen()) break;
            }
        }
        return indice;
    }

    private int chooseSpecialDistrict(List<DistrictsType> listDistrict, int indice, List<DistrictsType> listDistrictToBuild, RobotRichardo robot) {
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(robot, true);
        Iterator<DistrictsType> iterator = listDistrict.iterator();
        if (indice < robot.getNumberOfCardsChosen()) {
            while (iterator.hasNext()) {
                DistrictsType currentDistrict = iterator.next();
                if (!isDistrictInCityOrHand(currentDistrict, robot) && isSpecialDistrictType(currentDistrict.getType())) {
                    indice = chooseDistrict(currentDistrict, listDistrictToBuild, indice);
                    iterator.remove();
                    action.printPickSpecialDistrict(currentDistrict);
                }
                if (indice == robot.getNumberOfCardsChosen()) break;
            }
        }

        return indice;
    }

    private int chooseDistrictByType(List<DistrictsType> listDistrict, int indice, List<DistrictsType> listDistrictToBuild, RobotRichardo robot) {
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(robot, true);
        Iterator<DistrictsType> iterator = listDistrict.iterator();
        while (iterator.hasNext()) {
            DistrictsType currentDistrict = iterator.next();
            if (!isDistrictInCityOrHand(currentDistrict, robot) && currentDistrict.getType().equals(robot.getCharacter().getType())) {
                indice = chooseDistrict(currentDistrict, listDistrictToBuild, indice);
                iterator.remove();
                action.printPickDistrictByType(currentDistrict);
            }
            if (indice == robot.getNumberOfCardsChosen()) break;
        }

        return indice;
    }


    private boolean isDistrictInCityOrHand(DistrictsType district, RobotRichardo robot) {
        return robot.getCity().contains(district) || robot.getDistrictInHand().contains(district);
    }

    private boolean isSpecialDistrictType(String type) {
        return type.equals(RELIGIOUS) || type.equals(MILITARY);
    }

    private int chooseDistrict(DistrictsType currentDistrict, List<DistrictsType> listDistrictToBuild, int indice) {
        listDistrictToBuild.add(currentDistrict);
        indice++;
        return indice;
    }

    public CharactersType chooseVictimForVoleur(List<Robot> bots, RobotRichardo robot) {
        List<CharactersType> characters = new ArrayList<>(Arrays.asList(CharactersType.values()));
        characters.remove(CharactersType.ASSASSIN);
        characters.remove(CharactersType.VOLEUR);
        Collections.shuffle(characters);
        Map<Integer, CharactersType> characterFrequency = new HashMap<>();

        for (Robot bot : bots) {
            if (!bot.getName().equals(robot.getName())) {
                characterFrequency.put(robot.countOpponentNextCharacter(bot.getName()), robot.predictOpponentNextCharacter(bot.getName()));
            }
        }

        List<Map.Entry<Integer, CharactersType>> sortedEntries = new ArrayList<>(characterFrequency.entrySet());
        sortedEntries.sort(Map.Entry.<Integer, CharactersType>comparingByKey().reversed());

        for (Map.Entry<Integer, CharactersType> entry : sortedEntries) {
            CharactersType character = entry.getValue();
            if (!character.equals(CharactersType.ASSASSIN) && !character.equals(CharactersType.VOLEUR)) {
                return character;
            }
        }

        return characters.get(0);


    }

}
