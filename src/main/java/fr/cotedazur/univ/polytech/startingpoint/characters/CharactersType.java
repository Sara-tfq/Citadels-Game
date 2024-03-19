package fr.cotedazur.univ.polytech.startingpoint.characters;

/**
 * Cet enum englobe toutes les cartes personnages dont on aura besoin
 * le long de la partie
 * pour cette première version , tout les numero sont mis a 0 car il ne seront
 * pas pris en considération lors de la partie , de même pour la couleur
 * Version 0.1 du jeu Citadelle.
 */
public enum CharactersType {

    ASSASSIN(1, "Assassin", Colors.GRAY, "assassin"),
    VOLEUR(2, "Voleur", Colors.GRAY, "voleur"),
    MAGICIEN(3, "Magicien", Colors.GRAY, "magicien"),
    ROI(4, "Roi", Colors.YELLOW, "noble"),
    EVEQUE(5, "Eveque", Colors.BLUE, "religieux"),
    MARCHAND(6, "Marchand", Colors.GREEN, "marchand"),
    ARCHITECTE(7, "Architecte", Colors.GRAY, "architecte"),
    CONDOTTIERE(8, "Condottiere", Colors.RED, "militaire");


    int number;

    Colors color;
    String role;

    String type;


    /**
     * @param number numéro du personnage
     * @param role   role du personnage
     * @param color  couleur du personnage
     * @param type   type du personnage
     */

    CharactersType(int number, String role, Colors color, String type) {
        this.number = number;
        this.role = role;
        this.color = color;
        this.type = type;
    }

    /**
     * @return le numéro du personnage
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return le role du personnage
     */
    public String getRole() {
        return role;
    }

    /**
     * @return la couleur du personnage
     */
    public String getType() {
        return type;
    }

    /**
     * @return la couleur du personnage
     */
    public Colors getColor() {
        return color;
    }


}
