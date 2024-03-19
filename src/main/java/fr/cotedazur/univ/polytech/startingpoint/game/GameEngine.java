package fr.cotedazur.univ.polytech.startingpoint.game;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.characters.DeckCharacters;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.richardo.RobotRichardo;
import fr.cotedazur.univ.polytech.startingpoint.robots.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * cette classe représente le moteur du jeu
 */
public class GameEngine {


    private static final Logger logger = Logger.getLogger(GameEngine.class.getName());
    private final ArrayList<Robot> bots;
    private final DeckDistrict deckDistricts;
    private final DeckCharacters deckCharacters;
    private final int[] list = {4, 2, 2, 2, 2};
    private Round round;
    private boolean systemPrint = false;

    /**
     * Constructeur de la classe GameEngine
     * On initialise le deck de districts
     * On initialise le deck de personnages
     * On initialise la liste des robots*
     * On initialise le round
     * On initialise les robots
     */
    public GameEngine(boolean systemPrint, boolean onlyDiscretBot) {
        this.systemPrint = systemPrint;
        System.setProperty("java.util.logging.SimpleFormatter.format", "\u001B[37m %5$s%6$s%n \u001B[0m");
        if (!systemPrint) logger.setLevel(Level.OFF);
        deckDistricts = new DeckDistrict();
        deckCharacters = new DeckCharacters();
        this.bots = new ArrayList<>();
        round = new Round(bots);
        if (onlyDiscretBot) initialiazeBotsDiscrets();
        else initializeBots();

    }

    public GameEngine() {
        this(true, false);
    }


    /**
     * cette méthode permet d'initialiser les robots
     * On ajoute 4 robots dans la liste des robots
     * On ajoute 4 districts dans la main de chaque robot
     * On mélange les districts
     */
    public void initializeBots() {
        Robot sarsor = new RobotAgressif("Sara");
        Robot discrete = new RobotDiscrete("Stacy");
        Robot choice = new RobotChoiceOfCharacter("Alban");
        Robot richardo = new RobotRichardo("Richardo");
        Robot analyze = new RobotAnalyzer("Nora");

        addCardsToBot(sarsor, discrete, analyze, choice, richardo);
    }

    /**
     * cette méthode permet d'ajouter
     * au début du jeu
     * les 4 cartes dans la main de chaque robot
     */
    private void addCardsToBot(Robot robot1, Robot robot2, Robot robot3, Robot robot4, Robot robot5) {
        bots.add(robot1);
        bots.add(robot2);
        bots.add(robot3);
        bots.add(robot4);
        bots.add(robot5);


        for (Robot bot : bots) {
            for (int k = 0; k < 4; k++) {
                bot.addDistrict(deckDistricts.getDistrictsInDeck());
            }
        }
    }

    public void initialiazeBotsDiscrets() {
        Robot RobotDiscret1 = new RobotDiscrete("RobotDiscret1");
        Robot RobotDiscret2 = new RobotDiscrete("RobotDiscret2");
        Robot RobotDiscret3 = new RobotDiscrete("RobotDiscret3");
        Robot RobotDiscret4 = new RobotDiscrete("RobotDiscret4");
        Robot RobotDiscret5 = new RobotDiscrete("RobotDiscret5");

        addCardsToBot(RobotDiscret1, RobotDiscret2, RobotDiscret3, RobotDiscret4, RobotDiscret5);
    }

    /**
     * @return la liste des robots
     */
    public List<Robot> getBots() {
        return bots;
    }

    public DeckCharacters getDeckCharacters() {
        return deckCharacters;
    }

    /**
     * cette méthode permet de distribuer les personnages aux robots
     * On mélange les personnages
     * on donne au robot qui a la couronne son personnage en premier
     * on donne aux autres robots leurs personnages
     * on affiche le personnage de chaque robot
     */
    public void robotsPickCharacters() {
        List<CharactersType> listCharacters = deckCharacters.getCharactersInHand();
        destroyCharacters(listCharacters);
        Collections.shuffle(listCharacters);
        for (Robot bot : bots) {
            if (bot.getHasCrown()) {
                List<Robot> listOfThreeBots = new ArrayList<>(bots);
                listOfThreeBots.remove(bot);
                bot.pickCharacter(listCharacters, listOfThreeBots);
                logger.info(bot.getName() + " With crown Picked " + bot.getCharacter().getColor().getColorDisplay() + bot.getCharacter().getRole() + bot.getRESET());

            }
        }
        for (Robot bot : bots) {
            if (!bot.getHasCrown()) {
                List<Robot> listOfThreeBots = new ArrayList<>(bots);
                listOfThreeBots.remove(bot);
                bot.pickCharacter(listCharacters, listOfThreeBots);
                logger.info(bot.getName() + " Picked " + bot.getCharacter().getColor().getColorDisplay() + bot.getCharacter().getRole() + bot.getRESET());
            }
        }
    }


    /**
     * cette méthode permet de donner la couronne à un robot
     * On mélange les robots
     * On donne la couronne au premier robot de la liste
     * On trie les robots par ordre croissant de numéro de personnage
     */
    public void assignCrown() {
        Collections.shuffle(bots);
        bots.get(0).setHasCrown(true);

    }

    /**
     * @return true si un robot a construit 8 districts
     */
    public boolean isBuiltEigthDistrict() {
        for (Robot bot : bots) {
            if (bot.getNumberOfDistrictInCity() == 8) {
                return true;
            }
        }
        return false;
    }


    /**
     * cette méthode permet de jouer les tours du jeu
     * On crée un nouveau round
     * On donne la couronne au premier robot de la liste
     * <p>
     * On appelle la méthode robotsPickCharacters pour que les robots choisissent leurs personnages
     * On appelle la méthode playTurns pour que les robots jouent leurs tours
     * On répète les étapes précédentes jusqu'à ce qu'un robot construise 8 districts
     */
    public void gameTurns() {
        round = new Round(bots, systemPrint, deckDistricts);
        String turnStarting = "\u001B[32m++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++Turn ";
        String turnEnding = "\u001B[32m+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n";
        logger.info("=============================================================================GAME IS STARTING====================================================================\n");
        int comptTurn = 1;
        assignCrown();

        while (!isBuiltEigthDistrict()) {


            for (Robot bot : bots) {
                if (bot.getHasCrown()) {
                    logger.info(bot.getName() + " has crown and start the call of the characters");
                }
            }
            robotsPickCharacters();
            logger.info(turnStarting + comptTurn + " is starting" + turnEnding);
            bots.sort(Comparator.comparingInt(bot -> bot.getCharacter().getNumber()));

            round.playTurns();
            logger.info(turnStarting + comptTurn + " is over" + turnEnding);
            comptTurn++;

            round = new Round(bots, systemPrint, deckDistricts);

        }

        int i = 0;
        for (Robot bot : bots) {
            if (bot.hasEightDistrict()) {
                bot.setScore(bot.getScore() + list[i]);
                if (i != 0) {
                    logger.info(bot.getName() + " gets 2 extra points for having 8 districts");
                } else {
                    logger.info(bot.getName() + " ended the game and earns 4 extra points");
                }
                i++;
            }
        }


    }

    /**
     * cette méthode permet de vider la liste des robots
     */
    public void clearBots() {
        bots.clear();
    }


    /**
     * @param robot le robot à ajouter
     *              cette méthode permet d'ajouter un robot à la liste des robots
     */
    public void addRobot(Robot robot) {
        this.bots.add(robot);
    }


    /**
     * @param charactersInHand la liste des personnages
     *                         cette méthode permet de détruire 3 personnages
     */
    public void destroyCharacters(List<CharactersType> charactersInHand) {
        charactersInHand.remove(CharactersType.ROI);
        Collections.shuffle(charactersInHand, new Random());
        for (int i = 0; i < 3; i++) {
            if (!charactersInHand.isEmpty()) {
                CharactersType destroyedCharacter = charactersInHand.remove(0);
                logger.info("Destroyed character: " + destroyedCharacter.getColor().getColorDisplay() + destroyedCharacter.getRole() + bots.get(0).getRESET());

            }
        }

        charactersInHand.add(CharactersType.ROI);
    }


}
