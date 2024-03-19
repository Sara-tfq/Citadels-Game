package fr.cotedazur.univ.polytech.startingpoint.richardo;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.game.ActionOfBotDuringARound;
import fr.cotedazur.univ.polytech.startingpoint.robots.Robot;

import java.util.ArrayList;
import java.util.List;

public class RobotRichardo extends Robot {
    public static final String RELIGIOUS = "religieux";

    public static final String MILITARY = "militaire";

    private final StrategyBatisseur strategyBatisseur;
    private final StrategyAgressif strategyAgressif;
    private final StrategyOpportuniste strategyOpportuniste;
    private boolean opportuniste = false;
    private boolean agressif = false;
    private boolean batisseur = false;
    private List<CharactersType> availableCharacters;

    public RobotRichardo(String name) {
        super(name);
        strategyBatisseur = new StrategyBatisseur();
        strategyAgressif = new StrategyAgressif();

        strategyOpportuniste = new StrategyOpportuniste();
    }


    public StrategyAgressif getStrategyAgressif() {
        return strategyAgressif;
    }

    public void setAgressif(boolean agressif) {
        this.agressif = agressif;
    }

    public boolean getAgressive() {
        return agressif;
    }

    public boolean isBatisseur() {
        return batisseur;
    }

    public void setBatisseur(boolean batisseur) {
        this.batisseur = batisseur;
    }

    public boolean isOpportuniste() {
        return opportuniste;
    }


    public void setOpportuniste(boolean opportuniste) {
        this.opportuniste = opportuniste;
    }

    public List<CharactersType> getAvailableCharacters() {
        return availableCharacters;
    }

    public void setAvailableCharacters(List<CharactersType> availableCharacters) {
        this.availableCharacters = availableCharacters;
    }

    /**
     * cette méthode permet de construire le premier district possible
     */
    public String buildDistrictAndRetrieveItsName() {
        for (int i = 0; i < this.getDistrictInHand().size(); i++) {
            DistrictsType district = this.getDistrictInHand().get(i);
            if (district.getCost() <= this.getGolds() && !this.getCity().contains(district)) {
                district.powerOfDistrict(this, 1);
                this.getCity().add(district);
                this.setGolds(this.getGolds() - district.getCost());
                this.getDistrictInHand().remove(i);
                return "a new " + district.getName();
            }
        }
        return "nothing";
    }

    /**
     * Si jamais Richard est en mode batisseur ou opportuniste, il faut qu'il construise selon sa stratégie actuelle
     * S'il est en mode agressif, il construit le premier district disponible
     */
    @Override
    public String tryBuild() {
        if (batisseur) return strategyBatisseur.tryBuildBatisseur(this);
        if (opportuniste) return strategyOpportuniste.tryBuildOpportuniste(this);
        return buildDistrictAndRetrieveItsName();
    }

    /**
     * Si jamais Richard a moins de 6 golds, il choisit de prendre des golds
     * Sinon, il choisit de piocher des quartiers
     */
    @Override
    public int generateChoice() {

        if (this.getGolds() < 6) {
            return 1;
        } else {
            return 0;

        }
    }

    /**
     * Cette méthode permet à Richard de choisir son personnage
     * Selon s'il est agressif, batisseur ou opportuniste, il essaie de piocher le meilleur personnage selon sa stratégie
     * Par exemple, s'il est batisseur, il essaie de piocher le marchand en priorité, puis le roi et enfin l'architecte
     * Si jamais il n'arrive pas un piocher un de ses personnages, il essaie de piocher les personnages selon les différents scénarios
     * Par exemple, s'il reconnait le scénario de l'architecte, il essaie de piocher l'assassin pour viser l'arhitecte par exemple
     * Si jamais il ne pioche pas de personnage, il choisit le premier personnage disponible
     */
    @Override
    public void pickCharacter(List<CharactersType> availableCharacters, List<Robot> bots) {
        this.availableCharacters = new ArrayList<>(availableCharacters);


        this.strategyAgressif.isAgressif(bots, this);
        if (!this.agressif) {
            this.strategyBatisseur.isBatisseur(this);
        }
        if (!this.batisseur) {
            strategyOpportuniste.isOpportuniste(this);

        }
        if (agressif) {
            if (strategyAgressif.pickAgressif(availableCharacters, bots, this)) return;

        } else if (opportuniste) {
            if (strategyOpportuniste.pickOpportuniste(availableCharacters, this)) return;

        } else if (batisseur && (strategyBatisseur.pickBatisseur(availableCharacters, this))) {
            return;

        }


        ActionOfBotDuringARound action = new ActionOfBotDuringARound(this, true);

        if (scenarioArchitecte(bots) && availableCharacters.size() == 5) {
            if (availableCharacters.contains(CharactersType.ARCHITECTE) && availableCharacters.contains(CharactersType.ASSASSIN)) {
                pickCharacterCard(availableCharacters, CharactersType.ASSASSIN);
                action.printScenarioArchitecte();

                return;
            }
            if (availableCharacters.contains(CharactersType.ARCHITECTE)) {
                pickCharacterCard(availableCharacters, CharactersType.ARCHITECTE);
                action.printScenarioArchitecte();
                return;
            }

        } else if (scenarioRoi(bots)) {
            if (availableCharacters.contains(CharactersType.ROI)) {
                pickCharacterCard(availableCharacters, CharactersType.ROI);
                return;
            } else {
                setAgressif(true);
                setBatisseur(false);
                setOpportuniste(false);
            }

        }


        setCharacter(availableCharacters.get(0));
        availableCharacters.remove(0);


    }

    public boolean thereIsA(CharactersType character, List<CharactersType> availableCharacters) {
        return (this.hasCrown && availableCharacters.contains(character));
    }

    /**
     * Si jamais le personnage peut être encore choisit, Richard choisit le personnage passé en paramètre
     */
    public void pickCharacterCard(List<CharactersType> availableCharacters, CharactersType character) {
        if (availableCharacters.contains(character)) {
            this.setCharacter(character);
            availableCharacters.remove(character);
        }

    }


    public int countDistrictsByType(String type) {
        return (int) getCity().stream()
                .filter(district -> district.getType().equals(type))
                .count();
    }

    public int countDistrictsInHandByType(String type) {
        return (int) getDistrictInHand().stream()
                .filter(district -> district.getType().equals(type))
                .count();
    }

    /**
     * Cette méthode permet, après avoir pioché 2 cartes ou plus depuis la pioche, de mettre dans sa main en priorité les cartes que le robot peut construire
     * Dans le cas ou richard est un batisseur et qu'il a le roi ou le marchand, il essaie en priorité de choisir les quartiers nobles/marchands
     */
    @Override
    public List<DistrictsType> pickDistrictCard(List<DistrictsType> listDistrict, DeckDistrict deck) {
        if (batisseur && (character == CharactersType.ROI || character == CharactersType.MARCHAND))
            return strategyBatisseur.pickDistrictCardBatisseur(listDistrict, deck, this);
        if (opportuniste) return strategyOpportuniste.pickDistrictCardOpportuniste(listDistrict, deck, this);
        listDistrict.sort(compareByCost().reversed());
        List<DistrictsType> listDistrictToBuild = new ArrayList<>();
        int costOfDistrictToBeBuilt = 0;
        int indice = 0;
        int i = 0;
        while (i < listDistrict.size()) {
            if (listDistrict.get(i).getCost() - costOfDistrictToBeBuilt <= getGolds()) {
                costOfDistrictToBeBuilt += listDistrict.get(i).getCost();
                listDistrictToBuild.add(listDistrict.remove(i));
                i--;
                indice++;
                if (indice == getNumberOfCardsChosen()) break;

            }
            i++;
        }
        while (listDistrictToBuild.size() < getNumberOfCardsChosen()) {
            listDistrictToBuild.add(listDistrict.remove(listDistrict.size() - 1));
        }
        for (DistrictsType districtNonChosen : listDistrict) {
            deck.addDistrictToDeck(districtNonChosen);
        }
        return listDistrictToBuild;
    }

    /**
     * Cette méthode permet de renvoyer le bot qui doit être assassiné
     * Si jamais l'assassin s'est trompé sur sa cible, on renvoit null
     */
    @Override
    public Robot chooseVictimForAssassin(List<Robot> bots,int numberOfTheCharacterToKill){
        return this.strategyAgressif.chooseVictimForAssassin(bots,this);

    }

    /**
     * Cette méthode permet de choisir le numéro du personnage que Richard choisit de tuer
     * Dans le cas du scénario de l'architecte, on choisit de tuer l'architecte
     * Sinon, on choisit de tuer dans certains cas le voleur, le condottière ou le magicien si jamais Richard a beaucoup de cartes dans sa main
     */
    @Override
    public int getNumberOfCharacterToKill(List<Robot> bots) {
        if (scenarioArchitecte(bots)) return 7;
        for (Robot bot : bots) {
            if (thereIsA(CharactersType.VOLEUR, getAvailableCharacters())) {
                return 2;

            } else if (thereIsA(CharactersType.CONDOTTIERE, getAvailableCharacters()) || strategyAgressif.hasMaxDistricts(bots, this)) {
                return 8;

            } else {
                {
                    if (bot.getNumberOfDistrictInHand() <= 1 || getNumberOfDistrictInHand() == 3) {
                        return 3;
                    }
                }
            }
        }
        return super.getNumberOfCharacterToKill(bots);
    }

    @Override
    public Robot chooseVictimForCondottiere(List<Robot> bots) {
        return this.strategyAgressif.chooseVictimForCondottiere(bots, this);
    }

    @Override
    public Robot chooseVictimForMagicien(List<Robot> bots) {
        return this.strategyAgressif.chooseVictimForMagicien(bots, this);
    }

    @Override
    public CharactersType chooseVictimForVoleur(List<Robot> bots) {
        if (opportuniste) {
            return strategyOpportuniste.chooseVictimForVoleur(bots, this);
        }

        return super.chooseVictimForVoleur(bots);
    }

    /**
     * Cette méthode permet de savoir si le scénario de l'architecte apparait
     * Le scénario de l'architecte apparait quand un bot a un quartier ou plus dans sa main, 4 golds ou plus et qu'il a déjà construit 5 quartiers ou plus
     */
    public boolean scenarioArchitecte(List<Robot> bots) {
        for (Robot bot : bots) {
            if (bot.getNumberOfDistrictInHand() >= 1 && bot.getGolds() >= 4 && bot.getNumberOfDistrictInCity() >= 5 && !bot.equals(this))
                return true;
        }
        return false;
    }


    /**
     * Cette méthode permet de savoir si le scénario du roi apparait
     * Le scénario du roi apparait quand un bot a 6 quartiers dans sa cité
     */
    public boolean scenarioRoi(List<Robot> bots) {
        for (Robot bot : bots) {
            if (bot.getNumberOfDistrictInCity() == 6) {
                return true;
            }
        }
        return false;
    }


}