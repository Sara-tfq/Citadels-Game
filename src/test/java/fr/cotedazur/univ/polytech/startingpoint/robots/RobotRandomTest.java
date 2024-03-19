package fr.cotedazur.univ.polytech.startingpoint.robots;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.game.ActionOfBotDuringARound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RobotRandomTest {
    private RobotRandom RobotNora;
    private DeckDistrict deck;

    @BeforeEach
    void setUp() {
        RobotNora = new RobotRandom("testRobot");
        deck = new DeckDistrict();
    }

    @Test
    void getDistrictInHand() {
        assertTrue(RobotNora.getDistrictInHand().isEmpty());
    }

    @Test
    void getScore() {
        assertEquals(0, RobotNora.getScore());
    }

    @Test
    void getRESET() {
        assertEquals("\u001B[37m", RobotNora.getRESET());
    }

    @Test
    void getName() {
        assertEquals("testRobot", RobotNora.getName());
    }


    @Test
    void getNumberOfCardsDrawn() {
        assertEquals(2, RobotNora.getNumberOfCardsDrawn());
    }

    @Test
    void setNumberOfCardsDrawn() {
        RobotNora.setNumberOfCardsDrawn(3);
        assertEquals(3, RobotNora.getNumberOfCardsDrawn());
    }

    @Test
    void getNumberOfCardsChosen() {
        assertEquals(1, RobotNora.getNumberOfCardsChosen());
    }

    @Test
    void setNumberOfCardsChosen() {
        RobotNora.setNumberOfCardsChosen(2);
        assertEquals(2, RobotNora.getNumberOfCardsChosen());
    }

    @Test
    void setScore() {
        RobotNora.setScore(10);
        assertEquals(10, RobotNora.getScore());
    }

    @Test
    void addGold() {
        RobotNora.addGold(3);
        assertEquals(5, RobotNora.getGolds());
    }

    @Test
    void setCharacter() {
        RobotNora.setCharacter(CharactersType.ROI);
        assertEquals(CharactersType.ROI, RobotNora.getCharacter());
    }

    @Test
    void getCharacter() {
        RobotNora.setCharacter(CharactersType.ROI);
        assertEquals(CharactersType.ROI, RobotNora.getCharacter());
    }

    @Test
    void addDistrict() {
        RobotNora.addDistrict(DistrictsType.CHATEAU);
        assertEquals(1, RobotNora.getNumberOfDistrictInHand());
    }

    @Test
    void getNumberOfDistrictInHand() {
        RobotNora.addDistrict(DistrictsType.CHATEAU);
        RobotNora.addDistrict(DistrictsType.BIBLIOTHEQUE);
        assertEquals(2, RobotNora.getNumberOfDistrictInHand());
    }

    @Test
    void getNumberOfDistrictInCity() {
        RobotNora.addDistrict(DistrictsType.MANOIR);
        RobotNora.addGold(4);
        RobotNora.tryBuild();
        assertEquals(1, RobotNora.getNumberOfDistrictInCity());
    }

    @Test
    void pickDistrictCard() {
        List<DistrictsType> listDistrict = new ArrayList<>();
        listDistrict.add(DistrictsType.CHATEAU);
        listDistrict.add(DistrictsType.MANOIR);
        List<DistrictsType> pickedDistricts = RobotNora.pickDistrictCard(listDistrict, deck);
        assertEquals(1, pickedDistricts.size());
        assertTrue(pickedDistricts.contains(DistrictsType.CHATEAU) || pickedDistricts.contains(DistrictsType.MANOIR));
    }

    @Test
    void pickListOfDistrict() {
        List<DistrictsType> pickedDistricts = RobotNora.pickListOfDistrict(deck);
        assertEquals(2, pickedDistricts.size());
    }

    @Test
    void getHasCrown() {
        RobotNora.setHasCrown(true);
        assertTrue(RobotNora.getHasCrown());
    }

    @Test
    void countBuildingsByType() {
        RobotNora.setCharacter(CharactersType.MARCHAND);
        RobotNora.getCity().add(DistrictsType.MANOIR); // marchand
        RobotNora.getCity().add(DistrictsType.TAVERNE); // marchand
        RobotNora.getCity().add(DistrictsType.CASERNE); // militaire
        assertEquals(1, RobotNora.countBuildingsByType());
    }

    @Test
    void winGoldsByTypeOfBuildings() {
        RobotNora.setCharacter(CharactersType.MARCHAND);
        RobotNora.getCity().add(DistrictsType.MARCHE); // marchand
        RobotNora.getCity().add(DistrictsType.TAVERNE); // marchand
        RobotNora.getCity().add(DistrictsType.CASERNE); // militaire
        RobotNora.winGoldsByTypeOfBuildings();
        assertEquals(4, RobotNora.getGolds());
    }

    @Test
    void getChoice() {
        int choice = RobotNora.getChoice();
        assertTrue(choice == 0 || choice == 1);
    }

    @Test
    void showStatus() {

        RobotRandom robotRandom = new RobotRandom("Bot avec 8 golds");
        robotRandom.setCharacter(CharactersType.ASSASSIN);
        robotRandom.addGold(6);
        System.out.println(robotRandom.statusOfPlayer());
        assertEquals("\u001B[37m[Status of Bot avec 8 golds : role (Assassin" + robotRandom.getRESET() + "), \u001B[33m8 golds\u001B[37m, hand {}, city {}]", robotRandom.statusOfPlayer(false));
        DistrictsType district = DistrictsType.BIBLIOTHEQUE;
        robotRandom.addDistrict(district);
        System.out.println(robotRandom.statusOfPlayer());
        assertEquals("\u001B[37m[Status of Bot avec 8 golds : role (Assassin" + robotRandom.getRESET() + "), \u001B[33m8 golds\u001B[37m, hand {(Bibliotheque,6)}, city {}]", robotRandom.statusOfPlayer(false));
        robotRandom.tryBuild();
        System.out.println(robotRandom.statusOfPlayer());
        assertEquals("\u001B[37m[Status of Bot avec 8 golds : role (Assassin" + robotRandom.getRESET() + "), \u001B[33m2 golds\u001B[37m, hand {}, city {(Bibliotheque,6)}]", robotRandom.statusOfPlayer(false));
    }

    @Test
    void testTryBuild() {
        Robot RobotNora = new RobotRandom("TestRobotNora");

        // Assuming you have some districts in the deck for testing
        DistrictsType districtWithCost2 = DistrictsType.EGLISE;
        DistrictsType districtWithCost3 = DistrictsType.MANOIR;
        DistrictsType districtWithCost5 = DistrictsType.PALAIS;
        RobotNora.addDistrict(districtWithCost2);
        RobotNora.addDistrict(districtWithCost5);
        RobotNora.addDistrict(districtWithCost3);
        assertEquals(2, RobotNora.getGolds());
        String builtDistrictName1 = RobotNora.tryBuild();
        assertEquals("a new " + districtWithCost2.getName(), builtDistrictName1);


    }


    @Test
    void calculateScore() {
        RobotNora.setCharacter(CharactersType.ASSASSIN);
        DistrictsType districtWithCost2 = DistrictsType.EGLISE;
        DistrictsType districtWithCost3 = DistrictsType.MANOIR;
        DistrictsType districtWithCost5 = DistrictsType.PALAIS;
        RobotNora.addDistrict(districtWithCost2);
        RobotNora.addDistrict(districtWithCost5);
        RobotNora.addDistrict(districtWithCost3);
        RobotNora.tryBuild();
        RobotNora.tryBuild();
        RobotNora.tryBuild();
        assertEquals(2, RobotNora.calculateScore());
        RobotNora.setGolds(2);
        RobotNora.tryBuild();
        System.out.println(RobotNora.statusOfPlayer());
        assertEquals(2, RobotNora.calculateScore());
    }


    @Test
    void testGetGolds() {
        RobotNora.setGolds(5);
        assertEquals(5, RobotNora.getGolds());
    }

    @Test
    void testGetName() {
        assertEquals("testRobot", RobotNora.getName());
    }

    @Test
    void testGetCharacter() {
        RobotNora.setCharacter(CharactersType.ASSASSIN);
        assertEquals(CharactersType.ASSASSIN, RobotNora.getCharacter());
    }

    @Test
    void testGetCity() {
        RobotNora.addDistrict(DistrictsType.TAVERNE);
        RobotNora.tryBuild();
        assertEquals(1, RobotNora.getCity().size());
    }

    @Test
    void testSetHasCrown() {
        RobotNora.setHasCrown(true);
        assertTrue(RobotNora.getHasCrown());
    }

    @Test
    void testAddGold() {
        RobotNora.addGold(5);
        assertEquals(7, RobotNora.getGolds());
    }


    @Test
    void testAddDistrict() {
        RobotNora.addDistrict(DistrictsType.BIBLIOTHEQUE);
        assertEquals(1, RobotNora.getNumberOfDistrictInHand());
    }

    @Test
    void testSetGolds() {
        RobotNora.setGolds(5);
        assertEquals(5, RobotNora.getGolds());
    }


    @Test
    public void testCountBuildingsByType() {
        Robot RobotNora = new RobotRandom("TestRobotNora");
        RobotNora.setCharacter(CharactersType.MARCHAND);

        RobotNora.getCity().add(DistrictsType.CHATEAU); // noble
        RobotNora.getCity().add(DistrictsType.CHATEAU); // noble
        RobotNora.getCity().add(DistrictsType.CASERNE); // militaire
        RobotNora.getCity().add(DistrictsType.TAVERNE); // marchand

        // ordre des compteurs : noble, religieux, marchand, militaire, default
        assertEquals(1, RobotNora.countBuildingsByType());
    }


    @Test
    public void testCountBuildingsWithMagicSchool() {
        Robot RobotNora = new RobotRandom("TestRobotNora");

        RobotNora.setCharacter(CharactersType.ROI);

        RobotNora.getCity().add(DistrictsType.CHATEAU); // noble
        RobotNora.getCity().add(DistrictsType.CASERNE); // militaire
        RobotNora.getCity().add(DistrictsType.ECOLE_DE_MAGIE); // l'école de magie, qui sera comptée comme "noble" (pcq ROI)

        // ordre des compteurs : noble, religieux, marchand, militaire, default
        assertEquals(2, RobotNora.countBuildingsByType());
    }

    @Test
    public void testWinGoldsByTypeOfBuildings() {
        RobotNora.setCharacter(CharactersType.ROI);

        RobotNora.getCity().add(DistrictsType.CHATEAU); // noble
        RobotNora.getCity().add(DistrictsType.CASERNE); // militaire
        RobotNora.getCity().add(DistrictsType.ECOLE_DE_MAGIE); // l'école de magie, qui sera comptée comme "noble" (pcq ROI)

        RobotNora.winGoldsByTypeOfBuildings();

        assertEquals(4, RobotNora.getGolds());

        Robot RobotNoraEveque = new RobotRandom("TestRobotNora2");
        RobotNoraEveque.setCharacter(CharactersType.EVEQUE);
        RobotNoraEveque.getCity().add(DistrictsType.EGLISE); // religieux
        RobotNoraEveque.getCity().add(DistrictsType.CASERNE); // militaire
        RobotNoraEveque.getCity().add(DistrictsType.ECOLE_DE_MAGIE); // l'école de magie, qui sera comptée comme "religieux" (pcq EVEQUE)
        RobotNoraEveque.getCity().add(DistrictsType.TAVERNE); // marchand
        RobotNoraEveque.getCity().add(DistrictsType.TEMPLE);// religieux
        RobotNoraEveque.getCity().add(DistrictsType.CATHEDRALE);// religieux

        RobotNoraEveque.winGoldsByTypeOfBuildings();

        assertEquals(6, RobotNoraEveque.getGolds());

    }

    @Test
    void testScoreAvecUniversiteEtDracoport() {
        RobotNora.setGolds(100);
        RobotNora.setCharacter(CharactersType.ASSASSIN);
        RobotNora.addDistrict(DistrictsType.DRACOPORT);
        RobotNora.addDistrict(DistrictsType.UNIVERSITE);
        RobotNora.tryBuild();
        RobotNora.tryBuild();
        assertEquals(16, RobotNora.calculateScore());
    }

    @Test
    void testNumberOfCardsDrawnWithObservatoire() {
        RobotNora.setCharacter(CharactersType.ASSASSIN);
        RobotNora.setGolds(1000);
        RobotNora.addDistrict(DistrictsType.OBSERVATOIRE);
        assertEquals(2, RobotNora.getNumberOfCardsDrawn());
        RobotNora.tryBuild();
        assertEquals(3, RobotNora.getNumberOfCardsDrawn());
    }

    @Test
    void districtAlreadyInCity() {
        RobotNora.setCharacter(CharactersType.ASSASSIN);
        RobotNora.setGolds(1000);
        RobotNora.addDistrict(DistrictsType.OBSERVATOIRE);
        RobotNora.addDistrict(DistrictsType.OBSERVATOIRE);
        RobotNora.tryBuild();
        RobotNora.tryBuild();
        assertEquals(1, RobotNora.getCity().size());
        RobotNora.addDistrict(DistrictsType.TAVERNE);
        RobotNora.tryBuild();
        assertEquals(2, RobotNora.getCity().size());
    }

    @Test
    void setChoice() {
        Robot bot = new RobotRandom("Vermouth");
        bot.setChoice(2);
        assertEquals(bot.getChoice(), 2);
    }

    @Test
    void pickThreeDistrictWithObservatoire() {
        RobotNora.addGold(1000);
        List<DistrictsType> listOfDistrictPicked = RobotNora.pickListOfDistrict(deck);
        assertEquals(2, listOfDistrictPicked.size());
        RobotNora.addDistrict(DistrictsType.OBSERVATOIRE);
        RobotNora.tryBuild();
        listOfDistrictPicked = RobotNora.pickListOfDistrict(deck);
        assertEquals(3, listOfDistrictPicked.size());
    }

    @Test
    void chooseTwoDistrictWithBibliothèque() {
        RobotNora.addGold(1000);
        List<DistrictsType> listDistrictChosen = RobotNora.pickDistrictCard(RobotNora.pickListOfDistrict(deck), deck);
        assertEquals(1, listDistrictChosen.size());
        RobotNora.addDistrict(DistrictsType.BIBLIOTHEQUE);
        RobotNora.tryBuild();
        listDistrictChosen = RobotNora.pickDistrictCard(RobotNora.pickListOfDistrict(deck), deck);
        assertEquals(2, listDistrictChosen.size());
    }

    @Test
    void testWithObservatoireAndBibliothèque() {
        RobotNora.setGolds(1000);
        RobotNora.tryBuild();
        List<DistrictsType> listOfDistrictPicked = RobotNora.pickListOfDistrict(deck);
        assertEquals(2, listOfDistrictPicked.size());
        List<DistrictsType> listDistrictChosen = RobotNora.pickDistrictCard(listOfDistrictPicked, deck);
        assertEquals(1, listDistrictChosen.size());
        RobotNora.addDistrict(DistrictsType.BIBLIOTHEQUE);
        RobotNora.addDistrict(DistrictsType.OBSERVATOIRE);
        RobotNora.tryBuild();
        RobotNora.tryBuild();
        listOfDistrictPicked = RobotNora.pickListOfDistrict(deck);
        assertEquals(3, listOfDistrictPicked.size());
        listDistrictChosen = RobotNora.pickDistrictCard(listOfDistrictPicked, deck);
        assertEquals(2, listDistrictChosen.size());
    }

    @Test
    void testManufacture() {
        RobotNora.setGolds(10);
        RobotNora.getCity().add(DistrictsType.MANUFACTURE);
        DeckDistrict deck = new DeckDistrict();

        RobotNora.manufacture(deck);

        assertEquals(7, RobotNora.getGolds(), "Le robot devrait avoir 7 pièces d'or après l'utilisation de MANUFACTURE");
        assertEquals(3, RobotNora.getDistrictInHand().size(), "Le robot devrait avoir pioché 3 cartes de district");
    }

    @Test
    void testLaboratoireWithoutBuildingInHand() {
        int goldsBeforeCallOfLaboratoire = RobotNora.getGolds();
        RobotNora.laboratoire(new DeckDistrict());
        assertEquals(goldsBeforeCallOfLaboratoire, RobotNora.getGolds());
    }

    @Test
    void testLaboratoire() {
        RobotNora.addDistrict(DistrictsType.LABORATOIRE);
        RobotNora.setGolds(5);
        RobotNora.tryBuild();
        int goldsBeforeCallOfLaboratoire = RobotNora.getGolds();
        RobotNora.addDistrict(DistrictsType.TAVERNE);
        assertEquals(1, RobotNora.getNumberOfDistrictInHand());
        RobotNora.setGolds(0);
        RobotNora.laboratoire(new DeckDistrict());
        assertEquals(0, RobotNora.getNumberOfDistrictInHand());
        assertEquals(RobotNora.getGolds(), goldsBeforeCallOfLaboratoire + 1);
    }

    @Test
    void testPickCharacter() {
        List<CharactersType> availableCharacters = new ArrayList<>();
        availableCharacters.add(CharactersType.ASSASSIN);
        availableCharacters.add(CharactersType.VOLEUR);
        availableCharacters.add(CharactersType.MAGICIEN);
        availableCharacters.add(CharactersType.EVEQUE);
        availableCharacters.add(CharactersType.MARCHAND);
        RobotNora.pickCharacter(availableCharacters, new ArrayList<>());
        assertEquals(CharactersType.ASSASSIN, RobotNora.getCharacter());
    }

    @Test
    void testEmptyListOfCardsInHand() {
        List<DistrictsType> listDistrictInHand = Arrays.asList(DistrictsType.BIBLIOTHEQUE, DistrictsType.CASERNE);
        List<DistrictsType> listDistrictInHandCopy = new ArrayList<>(listDistrictInHand);
        RobotNora.setDistrictInHand(listDistrictInHandCopy);
        RobotNora.emptyListOfCardsInHand();
        assertEquals(0, RobotNora.getNumberOfDistrictInHand());
    }

    @Test
    void testSpecialCards() {
        RobotRandom robotRandom = new RobotRandom("random");
        robotRandom.addDistrict(DistrictsType.LABORATOIRE);
        robotRandom.addGold(3);
        robotRandom.tryBuild();
        robotRandom.addDistrict(DistrictsType.PALAIS);
        robotRandom.specialCards(new DeckDistrict(),new ActionOfBotDuringARound(robotRandom,true));
        assertEquals(robotRandom.getGolds(),1);
        assertEquals(robotRandom.getNumberOfDistrictInHand(),0);


        robotRandom.setGolds(5);
        robotRandom.addDistrict(DistrictsType.MANUFACTURE);
        robotRandom.city.remove(DistrictsType.LABORATOIRE);
        robotRandom.tryBuild();
        robotRandom.addGold(3);
        robotRandom.specialCards(new DeckDistrict(),new ActionOfBotDuringARound(robotRandom,true));
        assertEquals(robotRandom.getGolds(),0);
        assertEquals(robotRandom.getNumberOfDistrictInHand(),3);
    }

}

