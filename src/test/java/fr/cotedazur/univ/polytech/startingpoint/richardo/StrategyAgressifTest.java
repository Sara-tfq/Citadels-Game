package fr.cotedazur.univ.polytech.startingpoint.richardo;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.robots.Robot;
import fr.cotedazur.univ.polytech.startingpoint.robots.RobotRandom;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrategyAgressifTest {


    @Test
    void isAgressifCaseNeedsMagicien() {
        List<Robot> bots = new ArrayList<>();
        RobotRichardo richardo = new RobotRichardo("richard");
        RobotRandom bot = new RobotRandom("A");
        bots.add(richardo);
        bots.add(bot);
        bot.getDistrictInHand().add(DistrictsType.MARCHE);
        richardo.getStrategyAgressif().isAgressif(bots, richardo);
        assertTrue(richardo.getAgressive());


    }

    @Test
    void isAgressifCaseNeedsAssassin() {
        List<Robot> bots = new ArrayList<>();
        RobotRichardo richardo = new RobotRichardo("richard");
        RobotRandom bot = new RobotRandom("A");
        RobotRandom bot2 = new RobotRandom("B");
        bots.add(richardo);
        bots.add(bot);
        bots.add(bot2);
        bot.setCharacter(CharactersType.VOLEUR);
        bot2.setCharacter(CharactersType.EVEQUE);
        richardo.getStrategyAgressif().isAgressif(bots, richardo);
        assertTrue(richardo.getAgressive());
    }


    @Test
    void testIsNotAggressiveWhenConditionsNotMet() {
        List<Robot> bots = new ArrayList<>();
        RobotRichardo richardo = new RobotRichardo("Richardo");
        RobotRandom bot = new RobotRandom("R");
        bot.getDistrictInHand().add(DistrictsType.MARCHE);
        bot.getDistrictInHand().add(DistrictsType.MARCHE);
        bots.add(bot);
        StrategyAgressif strategyAgressif = richardo.getStrategyAgressif();
        strategyAgressif.isAgressif(bots, richardo);
        richardo.getStrategyAgressif().isAgressif(bots, richardo);
        assertFalse(richardo.getAgressive());
    }


    @Test
    void testRichardoIsAgressifAndChoosesWhichCharacterToPickAndTheVictim() {
        //Initialisation d'un tour de jeu
        List<Robot> bots = new ArrayList<>();
        ArrayList<DistrictsType> listOfDistrcits = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            listOfDistrcits.add(DistrictsType.MARCHE);
        }
        List<CharactersType> characters = new ArrayList<>(Arrays.asList(CharactersType.values()));
        RobotRichardo richardo = new RobotRichardo("richardo");
        RobotRandom bot1 = new RobotRandom("Satcy");
        RobotRandom bot2 = new RobotRandom("bot2");
        RobotRandom bot3 = new RobotRandom("bot3");
        bots.add(bot1);
        bots.add(bot2);
        bots.add(bot3);
        bots.add(richardo);
        //Le robot 2 prend de l'avance dans sa cité
        bot1.setCity(listOfDistrcits);
        //Normalement avec l'avancement du Bot2 richardo se doit de devenir agressif contre lui
        richardo.getStrategyAgressif().isAgressif(bots, richardo);
        assertTrue(richardo.getAgressive());
        //Apres qu'il soit agressif , Richardo doit choisir quel personnage pioher , dans ce il choit
        //de prendre condottiere s'il est disponible méthode déjà testée
        richardo.pickCharacter(characters, bots);
        assertEquals(richardo.getCharacter(), CharactersType.CONDOTTIERE);
        //Une fois Condottière le Robot choisit alors la victime.
        Robot victim = richardo.chooseVictimForCondottiere(bots);
        assertEquals(bot1, victim);
    }


    @Test
    void testRichardoIsAgressifAndMakeThoughDecisions() {
        //Initialisation d'un tour de jeu
        List<Robot> bots = new ArrayList<>();
        ArrayList<DistrictsType> listOfDistrcits = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            listOfDistrcits.add(DistrictsType.MARCHE);
        }
        List<CharactersType> characters = new ArrayList<>();

        characters.add(CharactersType.VOLEUR) ;
        characters.add(CharactersType.CONDOTTIERE) ;
        characters.add(CharactersType.ARCHITECTE) ;
        characters.add(CharactersType.ASSASSIN) ;
        RobotRichardo richardo = new RobotRichardo("richardo") ;
        RobotRandom bot1 = new RobotRandom("Stacy") ;
        RobotRandom bot2 = new RobotRandom("bot2") ;
        RobotRandom bot3 = new RobotRandom("bot3") ;
        bots.add(bot1) ;
        bots.add(bot2) ;
        bots.add(bot3) ;
        bots.add(richardo) ;
        richardo.setBatisseur(true);


        //Dis donc  , il est riche le richard , je voudrais bien le voler
        richardo.setGolds(45);
        //OH OH Y'a Un voleur dans le jeu.
        bot2.setCharacter(CharactersType.VOLEUR);
        bot1.setCharacter(CharactersType.MARCHAND);
        bot3.setCharacter(CharactersType.EVEQUE);
        //Le robot 2 n'est pas tant en avance que ça
        bot1.setCity(listOfDistrcits);
        //Je donne la couronne comme ça il pourra appliquer sa super methode qui lui permet de deduire si y'a un voleur dans le jeu
        richardo.setHasCrown(true);
        //Donc la Richardo check si y'a un voleur , la méthode à déja été testée , la c'est evident qu'il y'a un voleur
        boolean result = richardo.thereIsA(CharactersType.VOLEUR, characters);
        assertTrue(result);
        //Normalement comme , il y'a un voleur et que richard n'a pas trop envie de se faire voler sa fortune; il pioche Assassin
        richardo.getStrategyAgressif().isAgressif(bots, richardo);
        assertTrue(richardo.getAgressive());
        //Apres qu'il soit agressif , Richardo doit choisir quel personnage pioher , dans ce il choit
        //de prendre assassin
        richardo.pickCharacter(characters, bots);
        assertEquals(richardo.getCharacter(), CharactersType.ASSASSIN);
        //Une fois assassin il tue le voleur.
        Robot victim = richardo.chooseVictimForAssassin(bots, 0);
        assertEquals(bot2, victim);

    }


}