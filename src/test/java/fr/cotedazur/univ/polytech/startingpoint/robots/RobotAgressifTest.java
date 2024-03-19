package fr.cotedazur.univ.polytech.startingpoint.robots;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RobotAgressifTest {

    @Test
    void tryBuildForRedDistrcit() {
        RobotAgressif robotAgressif = new RobotAgressif("TestRobot");
        robotAgressif.setGolds(10); // Set initial golds
        robotAgressif.getDistrictInHand().add(DistrictsType.CHATEAU);
        robotAgressif.getDistrictInHand().add(DistrictsType.TEMPLE);
        robotAgressif.getDistrictInHand().add(DistrictsType.ECHOPPE);
        robotAgressif.getDistrictInHand().add(DistrictsType.TOUR_DE_GUET);
        String warResult = robotAgressif.tryBuild();
        assertEquals("a new Tour de guet", warResult);
        assertTrue(robotAgressif.getCity().contains(DistrictsType.TOUR_DE_GUET));
        assertEquals(9, robotAgressif.getGolds());
    }


    @Test
    void pickDistrictCardTest() {

        RobotAgressif robotAgressif = new RobotAgressif("TestRobot");


        robotAgressif.setGolds(10);
        robotAgressif.setNumberOfCardsChosen(2); // Assuming the number of cards chosen is 2
        List<DistrictsType> listDrawn = new ArrayList<>();
        listDrawn.add(DistrictsType.COURT_DES_MIRACLES);
        listDrawn.add(DistrictsType.TOUR_DE_GUET);
        DeckDistrict deck = new DeckDistrict();
        List<DistrictsType> picked = robotAgressif.pickDistrictCard(listDrawn, deck);
        assertTrue(picked.contains(DistrictsType.TOUR_DE_GUET));

    }

    @Test
    void chooseVictimForCondottiere() {

        RobotAgressif robotAgressif = new RobotAgressif("TestRobot");
        Robot robot1 = mock(Robot.class);
        when(robot1.getNumberOfDistrictInCity()).thenReturn(3);
        Robot robot2 = mock(Robot.class);
        when(robot2.getNumberOfDistrictInCity()).thenReturn(5);
        Robot robot3 = mock(Robot.class);
        when(robot3.getNumberOfDistrictInCity()).thenReturn(4);
        List<Robot> robots = Arrays.asList(robot1, robot2, robot3);
        Robot victim = robotAgressif.chooseVictimForCondottiere(robots);
        assertEquals(robot2, victim);
    }


    @Test
    void generateChoice() {
        RobotAgressif sarsor = new RobotAgressif("Sara");
        sarsor.setGolds(4);
        int choice = sarsor.generateChoice();
        assertEquals(1, choice);
        sarsor.setGolds(2);
        int choice2 = sarsor.generateChoice();
        assertEquals(1, choice2);
    }

    @Test
    void chooseVictimForAssassin() {
        // Create three test robots with different amounts of golds
        Robot bot1 = new RobotRandom("Bot1");
        bot1.setCharacter(CharactersType.ASSASSIN);
        bot1.setGolds(5);

        Robot bot2 = new RobotRandom("Bot2");
        bot2.setCharacter(CharactersType.ASSASSIN);
        bot2.setGolds(8);

        Robot bot3 = new RobotRandom("Bot3");
        bot3.setCharacter(CharactersType.ASSASSIN);
        bot3.setGolds(6);

        List<Robot> bots = new ArrayList<>();
        bots.add(bot1);
        bots.add(bot2);
        bots.add(bot3);


        RobotAgressif sarsor = new RobotAgressif("Sara");
        Robot victim = sarsor.chooseVictimForAssassin(bots, CharactersType.ASSASSIN.getNumber());


        assertEquals(bot2, victim);
    }


}