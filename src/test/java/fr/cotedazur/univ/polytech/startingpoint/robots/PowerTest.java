package fr.cotedazur.univ.polytech.startingpoint.robots;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.game.ActionOfBotDuringARound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PowerTest {

    @Mock
    private Robot mockedBot;
    @Mock
    private ActionOfBotDuringARound mockedAction;
    @InjectMocks
    private Power power;

    @Test
    void canDestroyDistrict() {
        RobotRandom destructor = new RobotRandom("destructor");
        RobotRandom victim = new RobotRandom("victim");
        Power power = new Power(destructor, new ActionOfBotDuringARound(destructor, true));
        destructor.setCharacter(CharactersType.CONDOTTIERE);
        victim.setCharacter(CharactersType.MAGICIEN);
        victim.addDistrict(DistrictsType.TAVERNE);
        victim.tryBuild();
        victim.addDistrict(DistrictsType.PALAIS);
        victim.tryBuild();
        victim.addDistrict(DistrictsType.PRISON);
        victim.tryBuild();

        assertTrue(power.canDestroyDistrict(victim, DistrictsType.TAVERNE));
        destructor.setGolds(1);
        assertFalse(power.canDestroyDistrict(victim, DistrictsType.PRISON));
        assertFalse(power.canDestroyDistrict(victim, DistrictsType.PALAIS));
        destructor.setGolds(5);
        assertFalse(power.canDestroyDistrict(victim, DistrictsType.MANOIR));


    }

    //Sans mockito
    @Test
    void condottiere() {
        RobotRandom destructor = new RobotRandom("destructor");
        RobotRandom victim = new RobotRandom("victim");
        Power power = new Power(destructor, new ActionOfBotDuringARound(destructor, true));
        destructor.setCharacter(CharactersType.CONDOTTIERE);
        victim.setCharacter(CharactersType.MARCHAND);
        victim.setGolds(30);
        victim.addDistrict(DistrictsType.TAVERNE);
        victim.tryBuild();
        victim.addDistrict(DistrictsType.PALAIS);
        victim.tryBuild();
        victim.addDistrict(DistrictsType.PRISON);
        victim.tryBuild();
        victim.addDistrict(DistrictsType.CIMETIERE);
        victim.tryBuild();

        destructor.setGolds(5);
        power.condottiere(victim);
        assertEquals(1, destructor.getGolds());
        victim.setCharacter(CharactersType.CONDOTTIERE);
        power.condottiere(victim);
        assertEquals(0, destructor.getGolds());
        power.condottiere(victim);
        assertEquals(0, destructor.getGolds());

        victim.setCharacter(CharactersType.EVEQUE);
        destructor.setGolds(5);
        power.condottiere(victim);
        assertEquals(5, destructor.getGolds());
    }

    @Test
    public void tryDestroyDonjon() {
        RobotRandom destructor = new RobotRandom("destructor");
        RobotRandom victim = new RobotRandom("victim");
        Power power = new Power(destructor, new ActionOfBotDuringARound(destructor, true));
        destructor.setCharacter(CharactersType.CONDOTTIERE);
        victim.setCharacter(CharactersType.MARCHAND);
        victim.setGolds(30);
        victim.addDistrict(DistrictsType.DONJON);
        victim.tryBuild();

        destructor.setGolds(5);
        power.condottiere(victim);
        assertEquals(5, destructor.getGolds());
        assertEquals(1, victim.getNumberOfDistrictInCity());
        victim.addDistrict(DistrictsType.CASERNE);
        victim.tryBuild();
        victim.addDistrict(DistrictsType.CHATEAU);
        victim.tryBuild();
        power.condottiere(victim);
        assertEquals(2, destructor.getGolds());
        assertEquals(2, victim.getNumberOfDistrictInCity());
    }

    @Test
    public void marchand() {
        RobotRandom marchand = new RobotRandom("marchand");
        Power power = new Power(marchand, new ActionOfBotDuringARound(marchand, true));
        marchand.setCharacter(CharactersType.MARCHAND);
        assertEquals(2, marchand.getGolds());
        power.marchand();
        assertEquals(3, marchand.getGolds());
        marchand.winGoldsByTypeOfBuildings();
        assertEquals(3, marchand.getGolds());
        marchand.addDistrict(DistrictsType.TAVERNE);
        marchand.addGold(1);
        marchand.addDistrict(DistrictsType.ECHOPPE);
        marchand.addGold(2);
        marchand.tryBuild();
        marchand.tryBuild();
        marchand.winGoldsByTypeOfBuildings();
        assertEquals(5, marchand.getGolds());
    }

    @Test
    void testAssassin() {
        RobotRandom assassin = new RobotRandom("Assassin");
        RobotRandom victim = new RobotRandom("Victim");
        Power power = new Power(assassin, new ActionOfBotDuringARound(assassin, true));
        assassin.setCharacter(CharactersType.ASSASSIN);
        victim.setCharacter(CharactersType.MARCHAND);
        assertFalse(victim.getIsAssassinated());
        power.assassin(victim);
        assertTrue(victim.getIsAssassinated());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Test avec mockito
    @Test
    void testMarchand() {
        when(mockedBot.getGolds()).thenReturn(0);
        doNothing().when(mockedBot).addGold(1);
        doNothing().when(mockedAction).printActionOfSellerBotWhoGainedGold();
        power.marchand();
        verify(mockedBot).addGold(1);
        verify(mockedAction).printActionOfSellerBotWhoGainedGold();
    }


    @Test
    void testCondottiereWithVictim8District() {
        RobotRandom robotDestructeur = new RobotRandom("Condottiere");
        robotDestructeur.setCharacter(CharactersType.CONDOTTIERE);
        RobotRandom robotVictim = new RobotRandom("Victim");
        robotVictim.setCharacter(CharactersType.MAGICIEN);
        robotVictim.setGolds(100);
        robotDestructeur.setGolds(100);
        DeckDistrict deckDistrict = new DeckDistrict();
        while (robotVictim.getNumberOfDistrictInCity() < 7) {
            robotVictim.addDistrict(deckDistrict.getDistrictsInDeck());
            robotVictim.tryBuild();
        }
        assertEquals(7, robotVictim.getNumberOfDistrictInCity());
        ActionOfBotDuringARound action = new ActionOfBotDuringARound(robotDestructeur, true);
        Power destruction = new Power(robotDestructeur, action);
        destruction.condottiere(robotVictim);
        assertEquals(6, robotVictim.getNumberOfDistrictInCity());
        //La victime a vu un de ses batiments se faire détruire

        while (robotVictim.getNumberOfDistrictInCity() < 8) {
            robotVictim.addDistrict(deckDistrict.getDistrictsInDeck());
            robotVictim.tryBuild();
        }

        assertEquals(8, robotVictim.getNumberOfDistrictInCity());
        destruction.condottiere(robotVictim);
        assertEquals(8, robotVictim.getNumberOfDistrictInCity());
        //Le condottière ne peut pas attaquer un joueur qui a déjà fini de construire ses 8 batiments
    }

    @Test
    void testCondottiereDetruitObservatoire() {
        RobotRandom robotDestructeur = new RobotRandom("Condottiere");
        robotDestructeur.setCharacter(CharactersType.CONDOTTIERE);
        RobotRandom robotVictim = new RobotRandom("Victim");
        robotVictim.setCharacter(CharactersType.MAGICIEN);
        robotVictim.setGolds(100);
        robotDestructeur.setGolds(100);
        robotVictim.addDistrict(DistrictsType.OBSERVATOIRE);
        assertEquals(2, robotVictim.numberOfCardsDrawn);
        robotVictim.tryBuild();
        assertEquals(3, robotVictim.numberOfCardsDrawn);
        Power power = new Power(robotDestructeur, new ActionOfBotDuringARound(robotDestructeur, true));
        power.condottiere(robotVictim);
        assertEquals(0, robotVictim.getNumberOfDistrictInCity());
        assertEquals(2, robotVictim.numberOfCardsDrawn);
    }

    @Test
    void testCondottiereDetruitBibliotheque() {
        RobotRandom robotDestructeur = new RobotRandom("Condottiere");
        robotDestructeur.setCharacter(CharactersType.CONDOTTIERE);
        RobotRandom robotVictim = new RobotRandom("Victim");
        robotVictim.setCharacter(CharactersType.MAGICIEN);
        robotVictim.setGolds(100);
        robotDestructeur.setGolds(100);
        robotVictim.addDistrict(DistrictsType.BIBLIOTHEQUE);
        assertEquals(1, robotVictim.numberOfCardsChosen);
        robotVictim.tryBuild();
        assertEquals(2, robotVictim.numberOfCardsChosen);
        Power power = new Power(robotDestructeur, new ActionOfBotDuringARound(robotDestructeur, true));
        power.condottiere(robotVictim);
        assertEquals(0, robotVictim.getNumberOfDistrictInCity());
        assertEquals(1, robotVictim.numberOfCardsChosen);
    }

}