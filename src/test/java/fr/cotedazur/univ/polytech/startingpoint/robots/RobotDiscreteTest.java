package fr.cotedazur.univ.polytech.startingpoint.robots;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RobotDiscreteTest {

    private RobotDiscrete robot;
    private final List<Robot> bots = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        robot = new RobotDiscrete("TestRobot");
    }

    @Test
    public void testCountDistrictsByType() {
        robot.getCity().addAll(Arrays.asList(DistrictsType.MANOIR, DistrictsType.CHATEAU, DistrictsType.PALAIS, DistrictsType.PORT, DistrictsType.MONASTERE));
        int count = robot.countDistrictsByType("noble");
        assertEquals(3, count);
    }

    @Test
    public void testCountDistrictsInHandByType() {
        robot.getDistrictInHand().addAll(Arrays.asList(DistrictsType.MANOIR, DistrictsType.CHATEAU, DistrictsType.PALAIS, DistrictsType.PORT, DistrictsType.MONASTERE));
        int count = robot.countDistrictsInHandByType("noble");
        assertEquals(3, count);
    }

    @Test
    void testPickCharacter() {
        List<CharactersType> availableCharacters = new ArrayList<>(Arrays.asList(CharactersType.ROI, CharactersType.EVEQUE, CharactersType.CONDOTTIERE, CharactersType.MARCHAND));
        robot.getCity().addAll(Arrays.asList(DistrictsType.MANOIR, DistrictsType.CHATEAU, DistrictsType.PALAIS, DistrictsType.PORT, DistrictsType.MONASTERE));
        List<CharactersType> availableCharactersCopy = new ArrayList<>(availableCharacters);
        robot.pickCharacter(availableCharactersCopy, bots);
        assertEquals(CharactersType.ROI, robot.getCharacter());
    }

    @Test
    void tryBuild_shouldBuildDistrictIfAffordableAndNotAlreadyBuilt() {
        RobotDiscrete robot = new RobotDiscrete("TestRobot");
        robot.setGolds(10);
        robot.setCharacter(CharactersType.ROI);

        robot.addDistrict(DistrictsType.PALAIS);

        String result = robot.tryBuild();

        assertEquals("a new Palais", result);
        assertEquals(5, robot.getCity().get(0).getCost());
        assertEquals(5, robot.getGolds());
        assertTrue(robot.getDistrictInHand().isEmpty());
    }

    @Test
    void tryBuild_shouldNotBuildDistrictIfNotAffordable() {
        // Arrange
        RobotDiscrete robot = new RobotDiscrete("TestRobot");
        robot.setGolds(2);
        robot.setCharacter(CharactersType.ROI);
        robot.addDistrict(DistrictsType.CASERNE);

        String result = robot.tryBuild();

        assertEquals("nothing", result);
        assertTrue(robot.getCity().isEmpty());
        assertEquals(2, robot.getGolds());
        assertEquals(1, robot.getDistrictInHand().size()); // District should remain in hand
    }

    @Test
    void tryBuild_shouldNotBuildDistrictIfAlreadyBuilt() {
        // Arrange
        RobotDiscrete robot = new RobotDiscrete("TestRobot");
        robot.setGolds(10);
        robot.setCharacter(CharactersType.ROI);
        robot.addDistrict(DistrictsType.CASERNE);
        robot.getCity().add(DistrictsType.CASERNE);

        // Act
        String result = robot.tryBuild();

        // Assert
        assertEquals("nothing", result);
        assertEquals(1, robot.getCity().size());
        assertEquals(10, robot.getGolds());
        assertFalse(robot.getDistrictInHand().isEmpty());
    }

    @Test
    void tryBuild_shouldBuildDistrictIfSameTypeAsCharacter() {
        // Arrange
        RobotDiscrete robot = new RobotDiscrete("TestRobot");
        robot.setGolds(10);
        robot.setCharacter(CharactersType.ROI);
        robot.addDistrict(DistrictsType.MANOIR);
        robot.addDistrict(DistrictsType.CASERNE);
        robot.addDistrict(DistrictsType.PALAIS);

        // Act
        String result = robot.tryBuild();

        // Assert
        assertEquals("a new Palais", result);
        assertEquals(1, robot.getCity().size());
        assertEquals(5, robot.getGolds());
        assertFalse(robot.getDistrictInHand().isEmpty());
    }


    @Test
    void generateChoice() {
        assertEquals(0, robot.generateChoice());
    }

    @Test
    void compareByType() {
    }

    @Test
    void pickDistrictCard() {
        Robot robot = new RobotDiscrete("TestRobot");
        DeckDistrict deck = new DeckDistrict();
        robot.setCharacter(CharactersType.EVEQUE);

        List<DistrictsType> listDistrict = new ArrayList<>();

        // le personnage est de type religieux et l'un des quartiers choisis est religieux
        listDistrict.add(DistrictsType.CASERNE);
        listDistrict.add(DistrictsType.MONASTERE);

        robot.getCity().add(DistrictsType.CHATEAU);
        robot.getDistrictInHand().add(DistrictsType.MONASTERE);
        List<DistrictsType> listDistrictPicked = robot.pickDistrictCard(listDistrict, deck);

        assertTrue(listDistrictPicked.contains(DistrictsType.CASERNE));
        assertEquals(1, listDistrictPicked.size());
        assertEquals(1, listDistrict.size());

    }

    @Test
    void pickDistrictCard_shouldNotPickDistrictsIfNotEnoughGold() {
        Robot robot = new RobotDiscrete("TestRobot");
        DeckDistrict deck = new DeckDistrict();
        robot.setCharacter(CharactersType.EVEQUE);

        List<DistrictsType> listDistrict = new ArrayList<>();
        listDistrict.add(DistrictsType.CASERNE);
        listDistrict.add(DistrictsType.TEMPLE);

        robot.getCity().add(DistrictsType.CHATEAU);
        robot.getDistrictInHand().add(DistrictsType.PALAIS);
        List<DistrictsType> listDistrictPicked = robot.pickDistrictCard(listDistrict, deck);

        assertTrue(listDistrictPicked.contains(DistrictsType.TEMPLE));
        assertEquals(1, listDistrict.size());

        // le personnage est de type noble et l'un des quartiers choisis est noble
        listDistrict.add(DistrictsType.MANOIR);
        robot.setCharacter(CharactersType.ROI);
        robot.getDistrictInHand().add(DistrictsType.MANOIR);
        List<DistrictsType> listDistrictPickedWithType = robot.pickDistrictCard(listDistrict, deck);
        assertTrue(listDistrictPickedWithType.contains(DistrictsType.CASERNE));
        assertEquals(1, listDistrictPickedWithType.size());
        assertEquals(1, listDistrict.size());
    }

}