package fr.cotedazur.univ.polytech.startingpoint.game;


import fr.cotedazur.univ.polytech.startingpoint.characters.Colors;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.robots.Robot;
import fr.cotedazur.univ.polytech.startingpoint.robots.RobotRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class WinnerTest {
    private GameEngine gameEngine;
    private Winner winner;

    @BeforeEach
    void setUp() {
        gameEngine = new GameEngine();
        winner = new Winner(gameEngine.getBots(), true);
    }

    @Test
    void testCalculateScores() {
        gameEngine.robotsPickCharacters();
        gameEngine.assignCrown();
        gameEngine.robotsPickCharacters();
        Round round = new Round(gameEngine.getBots());
        round.playTurns();
        winner.printScore();
        int maxScore = 0;
        for (Robot bot : gameEngine.getBots()) {
            if (bot.calculateScore() > maxScore) {
                maxScore = bot.calculateScore();
            }
        }
        List<String> winners = winner.getWinners();
        for (Robot bot : gameEngine.getBots()) {
            if (bot.calculateScore() == maxScore) {
                assertTrue(winners.contains(bot.getName()));
            }
        }
    }


    @Test
    void testGetWinner() {
        gameEngine.robotsPickCharacters();
        gameEngine.assignCrown();
        gameEngine.robotsPickCharacters();
        Round round = new Round(gameEngine.getBots());
        round.playTurns();
        winner.setScores();
        int maxScore = 0;
        for (Robot bot : gameEngine.getBots()) {
            if (bot.calculateScore() > maxScore) {
                maxScore = bot.calculateScore();
            }
        }
        List<String> winners = winner.getWinners();
        for (Robot bot : gameEngine.getBots()) {
            if (bot.calculateScore() == maxScore) {
                assertTrue(winners.contains(bot.getName()));
            }
        }
    }


    @Test
    void testShowWinners() {
        Robot robot1 = new RobotRandom("Robot1");
        Robot robot2 = new RobotRandom("Robot2");
        robot1.setScore(10);
        robot2.setScore(10);
        Winner winner = new Winner(List.of(robot1, robot2), true);
        assertEquals("There is an equality o:! The winners are: Robot1, Robot2", winner.showWinners());

    }


    @Test
    void testSetScoresIncludeBonus() {

        List<Robot> robots = new ArrayList<>();
        Robot robot1 = new RobotRandom("Robot1");
        Robot robot2 = new RobotRandom("Robot2");
        Robot robot3 = new RobotRandom("Robot3");
        robots.addAll(Arrays.asList(robot1, robot2, robot3));


        Winner winner = new Winner(robots, true);


        robot1.getCity().add(DistrictsType.MANOIR);
        robot1.getCity().add(DistrictsType.MARCHE);
        robot1.getCity().add(DistrictsType.CHATEAU);
        robot1.getCity().add(DistrictsType.PORT);
        robot1.getCity().add(DistrictsType.TAVERNE);

        robot2.getCity().add(DistrictsType.MANOIR);
        robot2.getCity().add(DistrictsType.CATHEDRALE);
        robot2.getCity().add(DistrictsType.PORT);
        robot2.getCity().add(DistrictsType.MARCHE);

        robot3.getCity().add(DistrictsType.CHATEAU);
        robot3.getCity().add(DistrictsType.TAVERNE);
        robot3.getCity().add(DistrictsType.PORT);
        robot3.getCity().add(DistrictsType.MARCHE);

        winner.setScores();
        assertEquals(14, robot1.getScore()); // Actual score depends on the implementation of calculateScore
        assertEquals(14, robot2.getScore());  // Actual score depends on the implementation of calculateScore
        assertEquals(11, robot3.getScore());  // Actual score depends on the implementation of calculateScore
    }


    @Test
    void testMiracleDistrictEffect() {

        Robot robot1 = new RobotRandom("Robot1");
        Robot robot2 = new RobotRandom("Robot2");
        Robot robot3 = new RobotRandom("Robot3");


        robot1.getCity().add(DistrictsType.MARCHE);
        robot1.getCity().add(DistrictsType.COURT_DES_MIRACLES);
        robot1.getCity().add(DistrictsType.PORT);

        robot2.getCity().add(DistrictsType.MARCHE);
        robot2.getCity().add(DistrictsType.PORT);
        robot2.getCity().add(DistrictsType.COURT_DES_MIRACLES);

        robot3.getCity().add(DistrictsType.MARCHE);
        robot3.getCity().add(DistrictsType.PORT);
        robot3.getCity().add(DistrictsType.TAVERNE);


        Winner winner = new Winner(List.of(robot1, robot2, robot3), true);


        winner.miracleDistrictEffect();


        Colors[] allowedColors = {Colors.GREEN, Colors.BLUE, Colors.RED, Colors.YELLOW};


        for (Robot bot : List.of(robot1, robot2, robot3)) {
            if (bot.getCity().contains(DistrictsType.COURT_DES_MIRACLES)) {
                int index = bot.getCity().indexOf(DistrictsType.COURT_DES_MIRACLES);
                if (index != bot.getCity().size() - 1) {
                    Colors updatedColor = bot.getCity().get(index).getColor();
                    assertNotNull(updatedColor);
                    assertTrue(isColorAllowed(updatedColor, allowedColors));
                    System.out.println(bot.getName() + "'s miracle card color is now: " + updatedColor);
                }
            }
        }
    }

    private boolean isColorAllowed(Colors color, Colors[] allowedColors) {
        for (Colors allowedColor : allowedColors) {
            if (allowedColor == color) {
                return true;
            }
        }
        return false;
    }


}



