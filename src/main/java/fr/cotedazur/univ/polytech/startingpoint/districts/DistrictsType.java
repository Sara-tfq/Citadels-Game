package fr.cotedazur.univ.polytech.startingpoint.districts;

import fr.cotedazur.univ.polytech.startingpoint.characters.Colors;
import fr.cotedazur.univ.polytech.startingpoint.robots.Robot;


/**
 * Cet enum englobe toutes les cartes quartiers dont on aura besoin
 * le long de la partie
 * les quartiers posséde , un nom , un cout , une couleur et un score
 * les couleurs sont null car elles ne seront pas prise en considération
 * dans cette première verion du jeu .
 * Version 0.1 du jeu citadelle.
 */
public enum DistrictsType {

    MANOIR("Manoir", 3, Colors.YELLOW, 3, "noble"),
    CHATEAU("Chateau", 4, Colors.YELLOW, 4, "noble"),
    PALAIS("Palais", 5, Colors.YELLOW, 5, "noble"),

    // RELIGION
    TEMPLE("Temple", 1, Colors.BLUE, 1, "religieux"),
    EGLISE("Eglise", 2, Colors.BLUE, 2, "religieux"),
    MONASTERE("Monastere", 3, Colors.BLUE, 3, "religieux"),
    CATHEDRALE("Cathedrale", 5, Colors.BLUE, 5, "religieux"),
    // TRADE DISTRICTS
    TAVERNE("Taverne", 1, Colors.GREEN, 1, "marchand"),
    ECHOPPE("Echoppe", 2, Colors.GREEN, 2, "marchand"),
    MARCHE("Marche", 2, Colors.GREEN, 2, "marchand"),
    COMPTOIR("Comptoir", 3, Colors.GREEN, 3, "marchand"),
    PORT("Port", 4, Colors.GREEN, 4, "marchand"),
    HOTEL_DE_VILLE("Hotel de ville", 5, Colors.GREEN, 5, "marchand"),


    // GUERRE
    TOUR_DE_GUET("Tour de guet", 1, Colors.RED, 1, "militaire"),
    PRISON("Prison", 2, Colors.RED, 2, "militaire"),
    CASERNE("Caserne", 3, Colors.RED, 3, "militaire"),
    FORTRESSE("Forteresse", 5, Colors.RED, 5, "militaire"),


    // SPECIAL
    COURT_DES_MIRACLES("Cour des miracles", 2, Colors.PURPLE, 2, "default"),
    DONJON("Donjon", 3, Colors.PURPLE, 3, "default"),
    BIBLIOTHEQUE("Bibliotheque", 6, Colors.PURPLE, 6, "default"),
    ECOLE_DE_MAGIE("Ecole de magie", 6, Colors.PURPLE, 6, "ecole"),
    LABORATOIRE("Laboratoire", 5, Colors.PURPLE, 5, "default"),
    MANUFACTURE("Manufacture", 5, Colors.PURPLE, 5, "default"),
    OBSERVATOIRE("Observatoire", 5, Colors.PURPLE, 5, "default"),
    CIMETIERE("Cimetiere", 5, Colors.PURPLE, 5, "default"),
    UNIVERSITE("Universite", 6, Colors.PURPLE, 8, "default"),
    DRACOPORT("Dracoport", 6, Colors.PURPLE, 8, "default");


    int cost;
    String name;
    Colors color;

    String colorReset = "\u001B[37m";
    int score;
    String type;


    /**
     * Il s'agit du constructeur de l'enum DistrictsType
     *
     * @param name  nom du quartier
     * @param cost  cout du quartier
     * @param color couleur du quartier
     * @param score score du quartier
     * @param type  type du quartier
     */

    DistrictsType(String name, int cost, Colors color, int score, String type) {
        this.cost = cost;
        this.name = name;
        this.color = color;
        this.score = score;
        this.type = type;
    }

    /**
     * @return le cout du quartier
     */
    public int getCost() {
        return cost;
    }

    /**
     * @return le nom du quartier
     */
    public String getName() {
        return name;
    }

    /**
     * @return la couleur du quartier
     */
    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    /**
     * @return le score du quartier
     */
    public int getScore() {
        return score;
    }

    /**
     * @return la couleur de reset
     */
    public String getColorReset() {
        return colorReset;
    }

    /**
     * @param player le joueur qui possède le quartier
     *               on vérifie si le quartier est un quartier spécial c'est à dire un quartier qui a un pouvoir
     */
    public void powerOfDistrict(Robot player, int val) {
        if (name.equals("Observatoire")) {
            player.setNumberOfCardsDrawn(player.getNumberOfCardsDrawn() + val);
        }
        if (name.equals("Bibliotheque")) {
            player.setNumberOfCardsChosen(player.getNumberOfCardsChosen() + val);
        }

    }

    /**
     * @return le nom et le cout du quartier
     */
    public String toString() {
        return "(" + getName() + ", " + getCost() + ")";
    }

    /**
     * @return le type du quartier
     */
    public String getType() {
        return type;
    }


}
