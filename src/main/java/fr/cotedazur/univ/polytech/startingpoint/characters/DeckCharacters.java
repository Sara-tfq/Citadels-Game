package fr.cotedazur.univ.polytech.startingpoint.characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * cette classe représente le deck de personnages
 */
public class DeckCharacters {
    private final List<CharactersType> charactersInHand;


    /**
     * Constructeur de la classe DeckCharacters
     */
    public DeckCharacters() {
        this.charactersInHand = new ArrayList<>(Arrays.asList(CharactersType.values()));
    }

    public List<CharactersType> getCharactersInHand() {
        return new ArrayList<>(charactersInHand);

    }

}

