package fr.cotedazur.univ.polytech.startingpoint.richardo;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.characters.DeckCharacters;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.game.Round;
import fr.cotedazur.univ.polytech.startingpoint.robots.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RobotRichardoTest {


    private RobotRichardo richardo;
    private DeckCharacters deckCharacters;
    private DeckDistrict deckDistrict;

    @BeforeEach
    void setUp() {
        richardo = new RobotRichardo("richardo");
        deckCharacters = new DeckCharacters();
        deckDistrict = new DeckDistrict();
    }

    @Test
    public void testChooseVictimForMagicien() {
        List<Robot> bots = new ArrayList<>();
        Robot victim = new RobotRandom("Victim");
        bots.add(victim);
        richardo.setAgressif(true);

        Robot result = richardo.chooseVictimForMagicien(bots);

        assertEquals(victim, result);
    }

    @Test
    public void testChooseVictimForVoleur_WhenOpportunisteIsTrue() {
        richardo.setOpportuniste(true);
        Robot bot1 = new RobotRandom("Bot1");
        Robot bot2 = new RobotRandom("Bot2");
        Robot bot3 = new RobotRandom("Bot3");
        bot1.setCharacter(CharactersType.MAGICIEN);
        bot2.setCharacter(CharactersType.MARCHAND);
        bot3.setCharacter(CharactersType.EVEQUE);
        richardo.setCharacter(CharactersType.ASSASSIN);
        List<Robot> bots = new ArrayList<>();
        bots.add(bot1);
        bots.add(bot2);
        bots.add(bot3);
        bots.add(richardo);
        Round round = new Round(bots);
        round.playTurns();


        CharactersType result = richardo.chooseVictimForVoleur(bots);

        assertEquals(CharactersType.EVEQUE, result);
    }

    @Test
    public void testChooseVictimForVoleur_WhenOpportunisteIsFalse() {
        List<Robot> bots = new ArrayList<>();
        CharactersType result = richardo.chooseVictimForVoleur(bots);

        assertNotNull(result);
    }

    @Test
    void testRichardPickKing() {
        List<CharactersType> listCharacters = deckCharacters.getCharactersInHand();

        StrategyBatisseur batisseur = new StrategyBatisseur();

        batisseur.pickBatisseur(listCharacters, richardo);
        assertEquals(richardo.getCharacter(), CharactersType.MARCHAND);
        assertEquals(listCharacters.size(), 7);

        richardo.setCharacter(null);
        batisseur.pickBatisseur(listCharacters, richardo);
        assertEquals(richardo.getCharacter(), CharactersType.ROI);

        richardo.setCharacter(null);
        richardo.setGolds(6);
        richardo.addDistrict(deckDistrict.getDistrictsInDeck());
        richardo.addDistrict(deckDistrict.getDistrictsInDeck());
        richardo.addDistrict(deckDistrict.getDistrictsInDeck());
        batisseur.pickBatisseur(listCharacters, richardo);
        assertEquals(richardo.getCharacter(), CharactersType.ARCHITECTE);
    }

    @Test
    void testRichardoWhenKingPickNobleCard() {
        richardo.setCharacter(CharactersType.ROI);
        StrategyBatisseur batisseur = new StrategyBatisseur();
        batisseur.isBatisseur(richardo);
        List<DistrictsType> listOfNobleCards = new ArrayList<>();
        listOfNobleCards.add(DistrictsType.CHATEAU);
        listOfNobleCards.add(DistrictsType.TAVERNE);
        richardo.addDistrict(richardo.pickDistrictCard(listOfNobleCards, deckDistrict));
        assertTrue(richardo.getDistrictInHand().contains(DistrictsType.CHATEAU));
    }


    @Test
    public void testChooseVictimForCondottiere_ChoosesCorrectVictim() {

        List<CharactersType> availableCharacters = new ArrayList<>();
        availableCharacters.add(CharactersType.CONDOTTIERE);

        List<Robot> bots = new ArrayList<>();
        Robot bot1 = Mockito.mock(Robot.class);
        when(bot1.getNumberOfDistrictInCity()).thenReturn(4);
        when(bot1.getCharacter()).thenReturn(CharactersType.ROI);
        bots.add(bot1);
        Robot bot2 = Mockito.mock(Robot.class);
        when(bot2.getNumberOfDistrictInCity()).thenReturn(5);
        when(bot2.getCharacter()).thenReturn(CharactersType.ASSASSIN);
        bots.add(bot2);
        RobotRichardo richardo = new RobotRichardo("Richard");
        richardo.setAvailableCharacters(availableCharacters);
        richardo.pickCharacter(availableCharacters, bots);
        Robot chosenVictim = richardo.chooseVictimForCondottiere(bots);
        assertEquals(bot1, chosenVictim);
    }


    @Test
    void testPickCharacterWithoutVoleurAndNoNeedForCondottiere() {
        List<Robot> bots = new ArrayList<>();
        List<CharactersType> charactersToPickFrom = new ArrayList<>();
        charactersToPickFrom.add(CharactersType.ROI);
        charactersToPickFrom.add(CharactersType.EVEQUE);
        charactersToPickFrom.add(CharactersType.CONDOTTIERE);
        charactersToPickFrom.add(CharactersType.ASSASSIN);

        Robot sarsor = new RobotAgressif("Sara");

        Robot gentil = new RobotDiscrete("Stacy");
        Robot choice = new RobotChoiceOfCharacter("Alban");
        RobotRichardo richardo = new RobotRichardo("Richardo");
        bots.add(sarsor);
        bots.add(gentil);
        bots.add(choice);
        bots.add(richardo);
        richardo.setAgressif(true);
        assertTrue(richardo.getAgressive());
        richardo.setHasCrown(true);
        richardo.pickCharacter(charactersToPickFrom, bots);

        assertEquals(CharactersType.ASSASSIN, richardo.getCharacter());

    }


    @Test
    void testScenarioArchitecte() {
        RobotRichardo botNearFinishing = new RobotRichardo("richardo1");
        RobotRichardo richardo2 = new RobotRichardo("richardo2");
        RobotRichardo richardo3 = new RobotRichardo("richardo3");
        RobotRichardo richardo4 = new RobotRichardo("richardo4");

        List<CharactersType> listCharacter = deckCharacters.getCharactersInHand();

        List<Robot> listBots = new ArrayList<>();
        listBots.add(botNearFinishing);
        listBots.add(richardo2);
        listBots.add(richardo3);
        listBots.add(richardo4);

        botNearFinishing.setGolds(1000);
        while (botNearFinishing.getNumberOfDistrictInCity() < 5 || botNearFinishing.getNumberOfDistrictInHand() < 1) {
            botNearFinishing.addDistrict(deckDistrict.getDistrictsInDeck());
            botNearFinishing.tryBuild();
        }

        assertTrue(richardo2.scenarioArchitecte(listBots));


        botNearFinishing.setCharacter(CharactersType.CONDOTTIERE);
        richardo2.setCharacter(CharactersType.EVEQUE);

        listCharacter.remove(CharactersType.EVEQUE);
        listCharacter.remove(CharactersType.CONDOTTIERE);
        listCharacter.remove(CharactersType.ROI);

        richardo2.pickCharacter(listCharacter, listBots);
        assertEquals(richardo2.getCharacter(), CharactersType.ASSASSIN);

        listCharacter.add(CharactersType.ROI);
        richardo2.pickCharacter(listCharacter, listBots);
        assertEquals(richardo2.getCharacter(), CharactersType.ARCHITECTE);

    }

    @Test
    public void testPickDistrictCard_WhenBatisseurAndCharacterIsRoi() {
        richardo.setBatisseur(true);
        richardo.setCharacter(CharactersType.ROI);
        List<DistrictsType> listDistrict = new ArrayList<>();
        listDistrict.add(DistrictsType.CASERNE);
        listDistrict.add(DistrictsType.CHATEAU);
        listDistrict.add(DistrictsType.TAVERNE);
        DeckDistrict deck = new DeckDistrict();

        List<DistrictsType> result = richardo.pickDistrictCard(listDistrict, deck);

        assertEquals(1, result.size());
        assertEquals(DistrictsType.CHATEAU.getName(), result.get(0).getName());
    }

    @Test
    public void testPickDistrictCard_WhenOpportuniste() {
        richardo.setOpportuniste(true);
        richardo.setCharacter(CharactersType.EVEQUE);
        List<DistrictsType> listDistrict = new ArrayList<>();
        listDistrict.add(DistrictsType.CASERNE);
        listDistrict.add(DistrictsType.CHATEAU);
        listDistrict.add(DistrictsType.MONASTERE);
        DeckDistrict deck = new DeckDistrict();

        List<DistrictsType> result = richardo.pickDistrictCard(listDistrict, deck);

        assertEquals(DistrictsType.MONASTERE, result.get(0));
    }

    @Test
    public void testPickDistrictCard_WhenRegularScenario() {
        richardo.setCharacter(CharactersType.MARCHAND);
        richardo.setBatisseur(false);
        richardo.setOpportuniste(false);
        richardo.setGolds(8);
        List<DistrictsType> listDistrict = new ArrayList<>();
        listDistrict.add(DistrictsType.CASERNE);
        listDistrict.add(DistrictsType.CHATEAU);
        listDistrict.add(DistrictsType.TAVERNE);
        DeckDistrict deck = new DeckDistrict();

        List<DistrictsType> result = richardo.pickDistrictCard(listDistrict, deck);

        assertEquals(1, result.size());
        assertEquals(DistrictsType.CHATEAU, result.get(0));
    }

    @Test
    public void testPickCharacter_WhenOpportunisteStrategyIsApplied() {
        List<CharactersType> availableCharacters = Arrays.asList(CharactersType.MARCHAND, CharactersType.EVEQUE);
        richardo.setOpportuniste(true);
        List<CharactersType> availableCharactersCopy = new ArrayList<>(availableCharacters);
        richardo.pickCharacter(availableCharactersCopy, new ArrayList<>());

        assertEquals(CharactersType.EVEQUE, richardo.getCharacter());

    }

    @Test
    public void testPickCharacter_WhenBatisseurStrategyIsApplied() {
        List<CharactersType> availableCharacters = Arrays.asList(CharactersType.MARCHAND, CharactersType.ROI);
        List<Robot> bots = new ArrayList<>();
        List<CharactersType> availableCharactersCopy = new ArrayList<>(availableCharacters);
        richardo.setBatisseur(true);

        richardo.pickCharacter(availableCharactersCopy, bots);

        assertEquals(CharactersType.MARCHAND, richardo.getCharacter());

        assertFalse(richardo.getAgressive());
        assertTrue(richardo.isBatisseur());
        assertFalse(richardo.isOpportuniste());
    }

}
