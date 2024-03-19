package fr.cotedazur.univ.polytech.startingpoint.robots;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.characters.DeckCharacters;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.game.ActionOfBotDuringARound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

public class RobotRushTest {

    private RobotRush robotRush;
    private ActionOfBotDuringARound actionMock;


    @BeforeEach
    public void setUp() {
        robotRush = new RobotRush("TestBot");
        robotRush.setGolds(5);

        actionMock = mock(ActionOfBotDuringARound.class);
        robotRush.setAction(actionMock);
    }

    @Test
    public void testPickCharacter() {
        List<CharactersType> availableCharacters = Arrays.asList(CharactersType.values());
        List<CharactersType> copyAvailableCharacters = new ArrayList<>(availableCharacters);
        robotRush.pickCharacter(copyAvailableCharacters, null);
        assertNotEquals(null, robotRush.getCharacter());
    }


    @Test
    public void testPickMarchandWhenLowOnGolds() {
        RobotRush robotRush = new RobotRush("rush");
        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> charactersList = deckCharacters.getCharactersInHand();
        charactersList.remove(CharactersType.ARCHITECTE);
        robotRush.pickCharacter(charactersList, new ArrayList<>());
        assertEquals(robotRush.getCharacter(), CharactersType.MARCHAND);
    }

    @Test
    public void testPickKingWhen6District() {
        RobotRush robotRush = new RobotRush("rush");
        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> charactersList = deckCharacters.getCharactersInHand();
        charactersList.remove(CharactersType.ARCHITECTE);
        robotRush.setGolds(100);
        robotRush.pickCharacter(charactersList, new ArrayList<>());
        assertEquals(robotRush.getCharacter(), CharactersType.ROI);
    }

    @Test
    public void testPickEvequeWhen6DistrictAndNoKing() {
        RobotRush robotRush = new RobotRush("rush");
        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> charactersList = deckCharacters.getCharactersInHand();
        charactersList.remove(CharactersType.ARCHITECTE);
        charactersList.remove(CharactersType.ROI);
        robotRush.setGolds(100);
        robotRush.pickCharacter(charactersList, new ArrayList<>());
        assertEquals(robotRush.getCharacter(), CharactersType.EVEQUE);
    }

    @Test
    public void testPickAssassinWhenNothing() {
        RobotRush robotRush = new RobotRush("rush");
        DeckCharacters deckCharacters = new DeckCharacters();
        List<CharactersType> charactersList = deckCharacters.getCharactersInHand();
        charactersList.remove(CharactersType.ARCHITECTE);
        charactersList.remove(CharactersType.ROI);
        charactersList.remove(CharactersType.EVEQUE);
        robotRush.setGolds(100);
        System.out.println(charactersList.get(0));
        robotRush.pickCharacter(charactersList, new ArrayList<>());
        //Le marchand a la priorité sur les autres personnages
        assertEquals(robotRush.getCharacter(), CharactersType.MARCHAND);
    }

}
