package fr.cotedazur.univ.polytech.startingpoint.districts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * cette classe représente le deck de districts
 */
public class DeckDistrict {

    private final List<DistrictsType> districtsInDeck;

    /**
     * Constructeur de la classe DeckDistrict
     * On ajoute les cartes dans le deck
     * On mélange le deck
     */
    public DeckDistrict() {
        this.districtsInDeck = new ArrayList<>();
        addAllDistrictToDeck();
        Collections.shuffle(districtsInDeck);
    }


    public void addAllDistrictToDeck() {
        for (int numbOfCard = 0; numbOfCard < 5; numbOfCard++) {
            if (numbOfCard < 1) {
                districtsInDeck.add(DistrictsType.COURT_DES_MIRACLES);
                districtsInDeck.add(DistrictsType.LABORATOIRE);
                districtsInDeck.add(DistrictsType.MANUFACTURE);
                districtsInDeck.add(DistrictsType.OBSERVATOIRE);
                districtsInDeck.add(DistrictsType.CIMETIERE);
                districtsInDeck.add(DistrictsType.BIBLIOTHEQUE);
                districtsInDeck.add(DistrictsType.ECOLE_DE_MAGIE);
                districtsInDeck.add(DistrictsType.UNIVERSITE);
                districtsInDeck.add(DistrictsType.DRACOPORT);
            }

            if (numbOfCard < 2) {
                districtsInDeck.add(DistrictsType.CATHEDRALE);
                districtsInDeck.add(DistrictsType.PALAIS);
                districtsInDeck.add(DistrictsType.HOTEL_DE_VILLE);
                districtsInDeck.add(DistrictsType.FORTRESSE);
                districtsInDeck.add(DistrictsType.DONJON);
            }

            if (numbOfCard < 3) {
                districtsInDeck.add(DistrictsType.TEMPLE);
                districtsInDeck.add(DistrictsType.MONASTERE);
                districtsInDeck.add(DistrictsType.ECHOPPE);
                districtsInDeck.add(DistrictsType.COMPTOIR);
                districtsInDeck.add(DistrictsType.PORT);
                districtsInDeck.add(DistrictsType.TOUR_DE_GUET);
                districtsInDeck.add(DistrictsType.PRISON);
                districtsInDeck.add(DistrictsType.CASERNE);
            }

            if (numbOfCard < 4) {
                districtsInDeck.add(DistrictsType.EGLISE);
                districtsInDeck.add(DistrictsType.CHATEAU);
                districtsInDeck.add(DistrictsType.MARCHE);
            }
            //Cas 5 cartes
            districtsInDeck.add(DistrictsType.MANOIR);
            districtsInDeck.add(DistrictsType.TAVERNE);
        }
    }

    /**
     * @return la liste des districts dans le deck
     */
    public DistrictsType getDistrictsInDeck() {
        if (districtsInDeck.isEmpty()) addAllDistrictToDeck();
        return districtsInDeck.remove(0);
    }

    /**
     * @param district le district à ajouter au deck
     *                 ajoute un district au deck
     */
    public void addDistrictToDeck(DistrictsType district) {
        this.districtsInDeck.add(district);
    }
}
