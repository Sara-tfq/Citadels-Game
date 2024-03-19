package fr.cotedazur.univ.polytech.startingpoint.robots;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.characters.DeckCharacters;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RobotChoiceOfCharacterTest {

    @Test
    void testPickMarchand() {
        RobotChoiceOfCharacter robotChoiceOfCharacter = new RobotChoiceOfCharacter("Robocop");
        robotChoiceOfCharacter.addGold(1000);
        robotChoiceOfCharacter.addDistrict(DistrictsType.TAVERNE);
        robotChoiceOfCharacter.addDistrict(DistrictsType.ECHOPPE);
        robotChoiceOfCharacter.tryBuild();
        robotChoiceOfCharacter.tryBuild();
        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> listCharacters = deckCharacters.getCharactersInHand();
        robotChoiceOfCharacter.pickCharacterBasedOfNumberOfDistrict(listCharacters);
        assertEquals(CharactersType.MARCHAND, robotChoiceOfCharacter.getCharacter());
    }

    @Test
    void testPickEveque() {
        RobotChoiceOfCharacter robotChoiceOfCharacter = new RobotChoiceOfCharacter("Robocop");
        robotChoiceOfCharacter.addGold(1000);
        robotChoiceOfCharacter.addDistrict(DistrictsType.EGLISE);
        robotChoiceOfCharacter.addDistrict(DistrictsType.TEMPLE);
        robotChoiceOfCharacter.tryBuild();
        robotChoiceOfCharacter.tryBuild();
        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> listCharacters = deckCharacters.getCharactersInHand();
        robotChoiceOfCharacter.pickCharacterBasedOfNumberOfDistrict(listCharacters);
        assertEquals(CharactersType.EVEQUE, robotChoiceOfCharacter.getCharacter());
    }

    @Test
    void testPickRoi() {
        RobotChoiceOfCharacter robotChoiceOfCharacter = new RobotChoiceOfCharacter("Robocop");
        robotChoiceOfCharacter.addGold(1000);
        robotChoiceOfCharacter.addDistrict(DistrictsType.CHATEAU);
        robotChoiceOfCharacter.addDistrict(DistrictsType.MANOIR);
        robotChoiceOfCharacter.tryBuild();
        robotChoiceOfCharacter.tryBuild();
        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> listCharacters = deckCharacters.getCharactersInHand();
        robotChoiceOfCharacter.pickCharacterBasedOfNumberOfDistrict(listCharacters);
        assertEquals(CharactersType.ROI, robotChoiceOfCharacter.getCharacter());
    }

    @Test
    void testPickCondottiere() {
        RobotChoiceOfCharacter robotChoiceOfCharacter = new RobotChoiceOfCharacter("Robocop");
        robotChoiceOfCharacter.addGold(1000);
        robotChoiceOfCharacter.addDistrict(DistrictsType.CASERNE);
        robotChoiceOfCharacter.addDistrict(DistrictsType.PRISON);
        robotChoiceOfCharacter.tryBuild();
        robotChoiceOfCharacter.tryBuild();
        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> listCharacters = deckCharacters.getCharactersInHand();
        robotChoiceOfCharacter.pickCharacterBasedOfNumberOfDistrict(listCharacters);
        assertEquals(CharactersType.CONDOTTIERE, robotChoiceOfCharacter.getCharacter());
    }

    @Test
    void testPickAssassin() {
        RobotChoiceOfCharacter robotChoiceOfCharacter = new RobotChoiceOfCharacter("Robocop");
        robotChoiceOfCharacter.addGold(1000);
        robotChoiceOfCharacter.addDistrict(DistrictsType.CHATEAU);
        robotChoiceOfCharacter.addDistrict(DistrictsType.EGLISE);
        robotChoiceOfCharacter.addDistrict(DistrictsType.CASERNE);
        robotChoiceOfCharacter.addDistrict(DistrictsType.TAVERNE);
        robotChoiceOfCharacter.tryBuild();
        robotChoiceOfCharacter.tryBuild();
        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> listCharacters = deckCharacters.getCharactersInHand();
        robotChoiceOfCharacter.pickCharacter(listCharacters, new ArrayList<>());
        assertEquals(CharactersType.MAGICIEN, robotChoiceOfCharacter.getCharacter());
    }

    @Test
    void test() {
        RobotChoiceOfCharacter robocop = new RobotChoiceOfCharacter("Robocop");
        robocop.addDistrict(DistrictsType.EGLISE);
        robocop.addDistrict(DistrictsType.TAVERNE);
        robocop.addDistrict(DistrictsType.FORTRESSE);
        robocop.addDistrict(DistrictsType.LABORATOIRE);
        robocop.addDistrict(DistrictsType.BIBLIOTHEQUE);
        robocop.addDistrict(DistrictsType.CIMETIERE);
        robocop.addGold(1000);
        for (int i = 0; i < 6; i++) {
            robocop.tryBuild();
        }
        assertFalse(robocop.canFinishNextTurn());
        robocop.addDistrict(DistrictsType.CHATEAU);
        robocop.addDistrict(DistrictsType.PALAIS);
        assertTrue(robocop.canFinishNextTurn());
    }

    @Test
    void testPickKingWhenBotCanFinishNextTurn() {
        RobotChoiceOfCharacter robocop = new RobotChoiceOfCharacter("Robocop");
        robocop.addDistrict(DistrictsType.EGLISE);
        robocop.addDistrict(DistrictsType.TAVERNE);
        robocop.addDistrict(DistrictsType.FORTRESSE);
        robocop.addDistrict(DistrictsType.LABORATOIRE);
        robocop.addDistrict(DistrictsType.BIBLIOTHEQUE);
        robocop.addDistrict(DistrictsType.CIMETIERE);
        robocop.addGold(1000);
        for (int i = 0; i < 6; i++) {
            robocop.tryBuild();
        }
        assertFalse(robocop.canFinishNextTurn());
        robocop.addDistrict(DistrictsType.CHATEAU);
        robocop.addDistrict(DistrictsType.PALAIS);
        assertTrue(robocop.canFinishNextTurn());

        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> listCharacters = deckCharacters.getCharactersInHand();
        listCharacters.remove(CharactersType.MAGICIEN);
        robocop.pickCharacter(listCharacters, new ArrayList<>());
        assertEquals(robocop.getCharacter(), CharactersType.ROI);
    }

    @Test
    void testPickAssassinWhenBotCanFinishThisTurn() {
        RobotChoiceOfCharacter victim = new RobotChoiceOfCharacter("victim");
        victim.setCharacter(CharactersType.ASSASSIN);

        victim.addDistrict(DistrictsType.TAVERNE);
        victim.addDistrict(DistrictsType.FORTRESSE);
        victim.addDistrict(DistrictsType.CIMETIERE);
        victim.addDistrict(DistrictsType.LABORATOIRE);
        victim.addDistrict(DistrictsType.BIBLIOTHEQUE);
        victim.addDistrict(DistrictsType.CHATEAU);
        victim.addGold(1000);
        for (int i = 0; i < 6; i++) {
            victim.tryBuild();
        }
        RobotChoiceOfCharacter robotCondottière = new RobotChoiceOfCharacter("condottière");
        robotCondottière.setCharacter(CharactersType.CONDOTTIERE);
        RobotChoiceOfCharacter robotPourMeubler = new RobotChoiceOfCharacter("robotPourMeubler");
        robotCondottière.setCharacter(CharactersType.MARCHAND);
        List<Robot> listBots = new ArrayList<>();
        listBots.add(robotCondottière);
        listBots.add(robotPourMeubler);
        listBots.add(victim);
        assertEquals(robotCondottière.chooseVictimForCondottiere(listBots), victim);
    }

    @Test
    public void testChooseVictimForCondottière() {
        RobotChoiceOfCharacter victim = new RobotChoiceOfCharacter("victim");
        victim.addDistrict(DistrictsType.EGLISE);
        victim.addDistrict(DistrictsType.TAVERNE);
        victim.addDistrict(DistrictsType.FORTRESSE);
        victim.addDistrict(DistrictsType.LABORATOIRE);
        victim.addDistrict(DistrictsType.BIBLIOTHEQUE);
        victim.addDistrict(DistrictsType.CIMETIERE);
        victim.addDistrict(DistrictsType.CHATEAU);
        victim.addGold(1000);
        for (int i = 0; i < 7; i++) {
            victim.tryBuild();
        }
        assertFalse(victim.canFinishThisTurn());
        victim.addDistrict(DistrictsType.PALAIS);
        assertTrue(victim.canFinishThisTurn());

        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> listCharacters = deckCharacters.getCharactersInHand();
        listCharacters.remove(CharactersType.MAGICIEN);
        victim.pickCharacter(listCharacters, new ArrayList<>());
        assertEquals(victim.getCharacter(), CharactersType.ASSASSIN);

        List<Robot> listBots = new ArrayList<>();
        Robot bo1 = new RobotChoiceOfCharacter("bot1");
        Robot bot2 = new RobotChoiceOfCharacter("bot2");
        Robot bot3 = new RobotChoiceOfCharacter("bot3");
        listBots.add(bo1);
        listBots.add(bot2);
        listBots.add(bot3);
        for (Robot bot : listBots) {
            List<DistrictsType> listDistrict = Arrays.asList(DistrictsType.TAVERNE, DistrictsType.PALAIS, DistrictsType.MANOIR, DistrictsType.CATHEDRALE);
            bot.setCity(listDistrict);
        }

        assertEquals(victim.chooseVictimForCondottiere(listBots), bo1);


    }

    @Test
    public void pickCondottièreWhenBotWillSoonFinish() {
        RobotChoiceOfCharacter victim = new RobotChoiceOfCharacter("victim");
        victim.addDistrict(DistrictsType.EGLISE);
        victim.addDistrict(DistrictsType.TAVERNE);
        victim.addDistrict(DistrictsType.FORTRESSE);
        victim.addDistrict(DistrictsType.LABORATOIRE);
        victim.addDistrict(DistrictsType.BIBLIOTHEQUE);
        victim.addDistrict(DistrictsType.CIMETIERE);
        victim.addDistrict(DistrictsType.CHATEAU);
        victim.addGold(1000);
        for (int i = 0; i < 6; i++) {
            victim.tryBuild();
        }

        RobotChoiceOfCharacter robotCondottière = new RobotChoiceOfCharacter("condottière");
        robotCondottière.setCharacter(CharactersType.CONDOTTIERE);
        RobotChoiceOfCharacter robotPourMeubler = new RobotChoiceOfCharacter("robotPourMeubler");
        robotCondottière.setCharacter(CharactersType.MARCHAND);

        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> listCharacters = deckCharacters.getCharactersInHand();
        listCharacters.remove(CharactersType.MAGICIEN);


        List<Robot> listBots = new ArrayList<>();
        listBots.add(robotCondottière);
        listBots.add(robotPourMeubler);
        listBots.add(victim);

        robotCondottière.pickCharacter(listCharacters, listBots);
        assertEquals(robotCondottière.getCharacter(), CharactersType.CONDOTTIERE);

    }
}
