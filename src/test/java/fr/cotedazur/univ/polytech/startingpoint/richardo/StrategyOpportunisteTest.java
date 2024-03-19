package fr.cotedazur.univ.polytech.startingpoint.richardo;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.game.Round;
import fr.cotedazur.univ.polytech.startingpoint.robots.Robot;
import fr.cotedazur.univ.polytech.startingpoint.robots.RobotRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StrategyOpportunisteTest {

    private StrategyOpportuniste strategyOpportuniste;
    private RobotRichardo robot;

    @BeforeEach
    public void setUp() {
        strategyOpportuniste = new StrategyOpportuniste();
        robot = new RobotRichardo("TestRobot");
    }

    @Test
    public void testIsOpportuniste_WithSixDistrictsInCity_ShouldSetOpportunisteToTrue() {
        robot.getCity().addAll(Arrays.asList(DistrictsType.MANOIR, DistrictsType.CHATEAU, DistrictsType.PALAIS, DistrictsType.PORT, DistrictsType.MONASTERE, DistrictsType.TAVERNE));
        strategyOpportuniste.isOpportuniste(robot);

        assertTrue(robot.isOpportuniste());
    }

    @Test
    public void testIsOpportuniste_WithNoGold_ShouldSetOpportunisteToTrue() {
        robot.getCity().addAll(Arrays.asList(DistrictsType.MANOIR, DistrictsType.PALAIS, DistrictsType.PORT, DistrictsType.MONASTERE, DistrictsType.TAVERNE));
        robot.setGolds(0);

        strategyOpportuniste.isOpportuniste(robot);

        assertTrue(robot.isOpportuniste());
    }

    @Test
    public void testPickOpportuniste_PrioritizesEveque() {
        List<CharactersType> availableCharacters = Arrays.asList(CharactersType.EVEQUE, CharactersType.CONDOTTIERE);

        RobotRichardo robot = new RobotRichardo("testRobot");
        robot.getCity().addAll(Arrays.asList(DistrictsType.MANOIR, DistrictsType.EGLISE, DistrictsType.CATHEDRALE, DistrictsType.MONASTERE, DistrictsType.TAVERNE));
        robot.getDistrictInHand().addAll(Arrays.asList(DistrictsType.TEMPLE, DistrictsType.OBSERVATOIRE));

        List<CharactersType> availableCharactersCopy = new ArrayList<>(availableCharacters);
        boolean result = strategyOpportuniste.pickOpportuniste(availableCharactersCopy, robot);

        assertTrue(result);
        assertEquals(CharactersType.EVEQUE, robot.getCharacter());
    }

    @Test
    public void testPickOpportuniste_PrioritizesCondottiere() {
        List<CharactersType> availableCharacters = Arrays.asList(CharactersType.EVEQUE, CharactersType.CONDOTTIERE);

        RobotRichardo robot = new RobotRichardo("testRobot");
        robot.getCity().addAll(Arrays.asList(DistrictsType.MANOIR, DistrictsType.CASERNE, DistrictsType.PRISON, DistrictsType.TOUR_DE_GUET, DistrictsType.TAVERNE));
        robot.getDistrictInHand().addAll(Arrays.asList(DistrictsType.COMPTOIR, DistrictsType.OBSERVATOIRE));

        List<CharactersType> availableCharactersCopy = new ArrayList<>(availableCharacters);
        boolean result = strategyOpportuniste.pickOpportuniste(availableCharactersCopy, robot);

        assertTrue(result);
        assertEquals(CharactersType.CONDOTTIERE, robot.getCharacter());
    }

    @Test
    public void testPickOpportuniste_PrioritizesVoleur() {
        List<CharactersType> availableCharacters = Arrays.asList(CharactersType.VOLEUR, CharactersType.MARCHAND);

        RobotRichardo robot = new RobotRichardo("testRobot");
        robot.getCity().addAll(Arrays.asList(DistrictsType.MANOIR, DistrictsType.CASERNE, DistrictsType.PRISON, DistrictsType.TOUR_DE_GUET, DistrictsType.TAVERNE));
        robot.getDistrictInHand().addAll(Arrays.asList(DistrictsType.COMPTOIR, DistrictsType.OBSERVATOIRE));

        List<CharactersType> availableCharactersCopy = new ArrayList<>(availableCharacters);
        boolean result = strategyOpportuniste.pickOpportuniste(availableCharactersCopy, robot);

        assertTrue(result);
        assertEquals(CharactersType.VOLEUR, robot.getCharacter());
    }

    @Test
    public void testPickOpportuniste_ReturnFalse() {
        List<CharactersType> availableCharacters = Arrays.asList(CharactersType.ROI, CharactersType.MARCHAND);

        RobotRichardo robot = new RobotRichardo("testRobot");
        robot.getCity().addAll(Arrays.asList(DistrictsType.MANOIR, DistrictsType.CASERNE, DistrictsType.PRISON, DistrictsType.TOUR_DE_GUET, DistrictsType.TAVERNE));
        robot.getDistrictInHand().addAll(Arrays.asList(DistrictsType.COMPTOIR, DistrictsType.OBSERVATOIRE));

        List<CharactersType> availableCharactersCopy = new ArrayList<>(availableCharacters);
        boolean result = strategyOpportuniste.pickOpportuniste(availableCharactersCopy, robot);

        assertFalse(result);
    }

    @Test
    public void testTryBuildOpportuniste_WithReligiousDistrictInHandAndEnoughGold() {
        RobotRichardo robot = new RobotRichardo("TestRobot");
        List<DistrictsType> listDistrictInHand = Arrays.asList(DistrictsType.PALAIS, DistrictsType.TEMPLE, DistrictsType.MONASTERE, DistrictsType.OBSERVATOIRE);
        List<DistrictsType> listDistrictInCity = new ArrayList<>();
        List<DistrictsType> listDistrictInHandCopy = new ArrayList<>(listDistrictInHand);
        listDistrictInCity.add(DistrictsType.TEMPLE);
        robot.setDistrictInHand(listDistrictInHandCopy);
        robot.setCity(listDistrictInCity);
        robot.setGolds(5);

        String result = strategyOpportuniste.tryBuildOpportuniste(robot);

        assertEquals("a new Monastere", result);
        assertTrue(listDistrictInCity.contains(DistrictsType.MONASTERE));
        assertFalse(listDistrictInHandCopy.contains(DistrictsType.MONASTERE));
        assertEquals(2, robot.getGolds());
    }

    @Test
    public void testTryBuildOpportuniste_WithNoAppropriateDistrictInHand() {
        RobotRichardo robot = new RobotRichardo("TestRobot");
        List<DistrictsType> listDistrictInHand = Arrays.asList(DistrictsType.PALAIS, DistrictsType.PORT, DistrictsType.TAVERNE, DistrictsType.OBSERVATOIRE);
        List<DistrictsType> listDistrictInCity = new ArrayList<>();
        List<DistrictsType> listDistrictInHandCopy = new ArrayList<>(listDistrictInHand);
        listDistrictInCity.add(DistrictsType.TEMPLE);
        robot.setDistrictInHand(listDistrictInHandCopy);
        robot.setCity(listDistrictInCity);
        robot.setGolds(5);

        String result = strategyOpportuniste.tryBuildOpportuniste(robot);

        assertEquals("a new Palais", result);
        assertFalse(listDistrictInCity.isEmpty());
    }

    @Test
    public void testPickDistrictCardOpportuniste_WithSameTypeAvailable() {
        List<DistrictsType> listDistrict = List.of(DistrictsType.MONASTERE, DistrictsType.OBSERVATOIRE);
        RobotRichardo robot = new RobotRichardo("TestRobot");
        robot.setCharacter(CharactersType.EVEQUE);
        robot.setNumberOfCardsChosen(1);
        List<DistrictsType> listDistrictInHandCopy = new ArrayList<>(listDistrict);

        List<DistrictsType> result = strategyOpportuniste.pickDistrictCardOpportuniste(listDistrictInHandCopy, new DeckDistrict(), robot);

        assertEquals(1, result.size());
        assertEquals(DistrictsType.MONASTERE, result.get(0));
    }

    @Test
    public void testPickDistrictCardOpportuniste_WithSpecialDistrictAvailable_ShouldReturnEmptyList() {
        List<DistrictsType> listDistrict = List.of(DistrictsType.PALAIS, DistrictsType.OBSERVATOIRE);
        RobotRichardo robot = new RobotRichardo("TestRobot");
        robot.setCharacter(CharactersType.EVEQUE);
        robot.setNumberOfCardsChosen(1);
        List<DistrictsType> listDistrictInHandCopy = new ArrayList<>(listDistrict);


        List<DistrictsType> result = strategyOpportuniste.pickDistrictCardOpportuniste(listDistrictInHandCopy, new DeckDistrict(), robot);

        assertEquals(1, result.size());
        assertEquals(DistrictsType.PALAIS, result.get(0));
    }

    @Test
    public void testPickDistrictCardOpportuniste_WithNoSpecialDistrictAvailable() {
        List<DistrictsType> listDistrict = List.of(DistrictsType.LABORATOIRE, DistrictsType.OBSERVATOIRE);
        RobotRichardo robot = new RobotRichardo("TestRobot");
        robot.setCharacter(CharactersType.EVEQUE);
        robot.setNumberOfCardsChosen(1);
        List<DistrictsType> listDistrictInHandCopy = new ArrayList<>(listDistrict);

        List<DistrictsType> result = strategyOpportuniste.pickDistrictCardOpportuniste(listDistrictInHandCopy, new DeckDistrict(), robot);

        assertEquals(1, result.size());
        assertEquals(DistrictsType.LABORATOIRE, result.get(0));
    }

    @Test
    public void testChooseVictimForVoleur_WithBots() {
        RobotRichardo robot = new RobotRichardo("TestRobot");
        Robot bot1 = new RobotRandom("Bot1");
        Robot bot2 = new RobotRandom("Bot2");
        Robot bot3 = new RobotRandom("Bot3");
        bot1.setCharacter(CharactersType.MAGICIEN);
        bot2.setCharacter(CharactersType.MARCHAND);
        bot3.setCharacter(CharactersType.EVEQUE);
        robot.setCharacter(CharactersType.ASSASSIN);
        List<Robot> bots = new ArrayList<>();
        bots.add(bot1);
        bots.add(bot2);
        bots.add(bot3);
        bots.add(robot);
        Round round = new Round(bots);
        round.playTurns();
        bot1.setCharacter(CharactersType.MAGICIEN);
        bot2.setCharacter(CharactersType.CONDOTTIERE);
        bot3.setCharacter(CharactersType.ROI);
        robot.setCharacter(CharactersType.ASSASSIN);
        round.playTurns();

        CharactersType result = strategyOpportuniste.chooseVictimForVoleur(bots, robot);

        assertEquals(CharactersType.MAGICIEN, result);
        assertFalse(result.equals(CharactersType.ASSASSIN) || result.equals(CharactersType.VOLEUR));
    }

    @Test
    public void testChooseVictimForVoleur_WithBots_AssassinType() {
        RobotRichardo robot = new RobotRichardo("TestRobot");
        Robot bot1 = new RobotRandom("Bot1");
        Robot bot2 = new RobotRandom("Bot2");
        Robot bot3 = new RobotRandom("Bot3");
        bot1.setCharacter(CharactersType.MAGICIEN);
        bot2.setCharacter(CharactersType.MARCHAND);
        bot3.setCharacter(CharactersType.EVEQUE);
        robot.setCharacter(CharactersType.ASSASSIN);
        List<Robot> bots = new ArrayList<>();
        bots.add(bot1);
        bots.add(bot2);
        bots.add(bot3);
        bots.add(robot);
        Round round = new Round(bots);
        round.playTurns();
        bot1.setCharacter(CharactersType.ARCHITECTE);
        bot2.setCharacter(CharactersType.CONDOTTIERE);
        bot3.setCharacter(CharactersType.ROI);
        robot.setCharacter(CharactersType.ASSASSIN);
        round.playTurns();

        CharactersType result = strategyOpportuniste.chooseVictimForVoleur(bots, robot);

        assertNotEquals(CharactersType.ASSASSIN, result);
        assertFalse(result.equals(CharactersType.ASSASSIN) || result.equals(CharactersType.VOLEUR));
    }


}
