package fr.cotedazur.univ.polytech.startingpoint.game;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.characters.DeckCharacters;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.robots.Robot;
import fr.cotedazur.univ.polytech.startingpoint.robots.RobotRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {
    private GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        gameEngine = new GameEngine();
    }

    @Test
    void testInitializeBots() {
        for (Robot bot : gameEngine.getBots()) {
            assertEquals(4, bot.getNumberOfDistrictInHand(), "Chaque robot doit avoir 4 districts uniques");
        }
    }

    @Test
    void testassignCrown() {
        gameEngine.robotsPickCharacters();
        gameEngine.assignCrown();
        int numberOfCrown = 0;
        for (Robot bot : gameEngine.getBots()) {
            if (bot.getHasCrown()) {
                numberOfCrown++;
            }
        }
        assertEquals(1, numberOfCrown);
    }


    @Test
    void testIsBuiltEigthDistrict_OneRobotHasEightDistricts() {
        Robot bot = gameEngine.getBots().get(1);

        bot.setCharacter(CharactersType.MARCHAND);
        bot.setGolds(40);
        bot.addDistrict(DistrictsType.PRISON);
        bot.addDistrict(DistrictsType.PALAIS);
        bot.addDistrict(DistrictsType.BIBLIOTHEQUE);
        bot.addDistrict(DistrictsType.TEMPLE);
        bot.addDistrict(DistrictsType.PORT);
        bot.addDistrict(DistrictsType.TAVERNE);
        bot.addDistrict(DistrictsType.MANOIR);
        bot.addDistrict(DistrictsType.CHATEAU);


        for (int i = 0; i < 8; i++) {
            bot.tryBuild();
        }

        assertTrue(gameEngine.isBuiltEigthDistrict(), "Un robot devrait avoir construit 8 districts.");

    }


    @Test
    void testRobotPickCharacters() {
        gameEngine.robotsPickCharacters();
        gameEngine.assignCrown();

        for (Robot bot : gameEngine.getBots()) {
            assertNotNull(bot.getCharacter(), "Chaque robot doit avoir un personnage.");
        }

        Robot robotWithCrown = null;
        for (Robot bot : gameEngine.getBots()) {
            if (bot.getHasCrown()) {
                robotWithCrown = bot;
                break;
            }
        }

        assertNotNull(robotWithCrown, "Il devrait y avoir un robot avec la couronne.");

        CharactersType characterOfCrowned = robotWithCrown.getCharacter();
        assertNotNull(characterOfCrowned, "Le robot avec la couronne doit avoir un personnage.");
    }


    @Test
    void testGameTurns() {
        for (Robot bot : gameEngine.getBots()) {
            assertEquals(0, bot.getNumberOfDistrictInCity(), "Les robots ne devraient pas avoir de districts construits au début.");
        }
        gameEngine.robotsPickCharacters();
        gameEngine.gameTurns();

        boolean atLeastOneDistrictBuilt = gameEngine.getBots().stream()
                .anyMatch(bot -> bot.getNumberOfDistrictInCity() > 0);
        assertTrue(atLeastOneDistrictBuilt, "Au moins un robot devrait avoir construit des districts.");

        assertTrue(gameEngine.isBuiltEigthDistrict(),
                "Le jeu devrait se terminer par un robot ayant construit 8 districts.");
    }

    @Test
    void testClearBots() {
        gameEngine.clearBots();
        assertEquals(0, gameEngine.getBots().size(), "La liste des robots doit être vide");
    }

    @Test
    void testAddRobot() {
        gameEngine.clearBots();
        Robot robot = new RobotRandom("Robot");
        gameEngine.addRobot(robot);
        assertEquals(1, gameEngine.getBots().size(), "La liste des robots doit contenir un robot");
    }

    @Test
    void testDestroyCharacters() {
        GameEngine gameEngine = new GameEngine();

        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> charactersInHand = deckCharacters.getCharactersInHand();

        int originalSize = charactersInHand.size();

        gameEngine.destroyCharacters(charactersInHand);
        assertEquals(originalSize - 3, charactersInHand.size(), "2 personnages doivent être retirés de la main.");

        assertTrue(charactersInHand.contains(CharactersType.ROI), "Le personnage 'ROI' doit être présent dans la main.");

        int roiCount = Collections.frequency(charactersInHand, CharactersType.ROI);
        assertEquals(1, roiCount, "Il ne doit y avoir qu'un seul 'ROI' dans la main.");
    }

    @Test
    void testOnlyOneCharacterLeftAfterCharactersPicked() {

        GameEngine gameEngine = new GameEngine();
        List<CharactersType> listCharacters = gameEngine.getDeckCharacters().getCharactersInHand();
        gameEngine.destroyCharacters(listCharacters);
        Collections.shuffle(listCharacters);

        for (Robot bot : gameEngine.getBots()) {
            if (bot.getHasCrown()) {
                List<Robot> listOfThreeBots = new ArrayList<>(gameEngine.getBots());
                listOfThreeBots.remove(bot);
                bot.pickCharacter(listCharacters, listOfThreeBots);
            }
        }
        for (Robot bot : gameEngine.getBots()) {
            if (!bot.getHasCrown()) {

                List<Robot> listOfThreeBots = new ArrayList<>(gameEngine.getBots());
                listOfThreeBots.remove(bot);
                bot.pickCharacter(listCharacters, listOfThreeBots);
            }
        }
        assertEquals(0, listCharacters.size());

        gameEngine = new GameEngine(true, true);
        listCharacters = gameEngine.getDeckCharacters().getCharactersInHand();
        gameEngine.destroyCharacters(listCharacters);
        Collections.shuffle(listCharacters);

        for (Robot bot : gameEngine.getBots()) {
            if (bot.getHasCrown()) {
                List<Robot> listOfThreeBots = new ArrayList<>(gameEngine.getBots());
                listOfThreeBots.remove(bot);
                bot.pickCharacter(listCharacters, listOfThreeBots);
            }
        }
        for (Robot bot : gameEngine.getBots()) {
            if (!bot.getHasCrown()) {

                List<Robot> listOfThreeBots = new ArrayList<>(gameEngine.getBots());
                listOfThreeBots.remove(bot);
                bot.pickCharacter(listCharacters, listOfThreeBots);
            }
        }
    }


}