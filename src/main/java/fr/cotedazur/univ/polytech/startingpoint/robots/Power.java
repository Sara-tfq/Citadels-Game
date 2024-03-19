package fr.cotedazur.univ.polytech.startingpoint.robots;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DeckDistrict;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.game.ActionOfBotDuringARound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Power {
    public static final String MILITATE = "militaire";
    public static final String RELIGIOUS = "religieux";
    public static final String ASSASSIN = "assassin";
    private final Robot bot;
    private ActionOfBotDuringARound action;


    public Power(Robot bot, ActionOfBotDuringARound action) {
        this.bot = bot;
        this.action = action;
    }


    public void marchand() {

        bot.addGold(1);
        action.printActionOfSellerBotWhoGainedGold();

    }


    public void architecte(Robot bot, DeckDistrict deck) {
        int i = bot.getChoice();
        if (i == 0) {
            bot.setChoice(7);
            List<DistrictsType> listDistrictDrawn = bot.pickListOfDistrict(deck);
            listDistrictDrawn.addAll(bot.pickListOfDistrict(deck));
            List<DistrictsType> listDistrictPicked = bot.pickDistrictCard(listDistrictDrawn, deck);
            action.addListOfDistrict(listDistrictDrawn, listDistrictPicked);
            bot.addDistrict(listDistrictPicked);
            action.printActionOfBotWhoHasBuilt();
        }

        if (i == 1) {
            bot.setChoice(0);
            bot.addGold(2);
            action = new ActionOfBotDuringARound(bot, true);
            action.printActionOfBotWhoGainedGold(2);

        }
        String hasBuilt = bot.tryBuild();
        action.printBuildingOfBot(hasBuilt);
        String hasBuilt2 = bot.tryBuild();
        action.printBuildingOfBot(hasBuilt2);
    }


    public boolean canDestroyDistrict(Robot victim, DistrictsType district) {
        int destructorGolds = bot.getGolds();
        boolean districtInCity = victim.getCity().contains(district);
        return destructorGolds >= (district.getCost() - 1) && districtInCity && !victim.hasEightDistrict();
    }


    public void condottiere(Robot victim) {
        int destructorGolds = bot.getGolds();
        List<DistrictsType> victimDistricts = victim.getCity();

        victimDistricts.sort(Comparator.comparingInt(DistrictsType::getCost).reversed());
        action.showCityOfBot(victim);
        for (DistrictsType district : victimDistricts) {
            boolean verify = canDestroyDistrict(victim, district);
            if (!district.getName().equals("Donjon") && (verify)) {
                if (bot.getCharacter().getType().equals(MILITATE) &&
                        !victim.getCharacter().getType().equals(RELIGIOUS)) {
                    district.powerOfDistrict(victim, -1);
                    victim.getCity().remove(district);
                    int goldsAfterDestruction = destructorGolds - district.getCost();
                    bot.setGolds(goldsAfterDestruction + 1);
                    action.printActionOfDestroyDistrict(victim, district, bot.getGolds());

                    if (victim.getCity().stream().anyMatch(d -> d.getName().equals("Cimetière")) &&
                            victim.getGolds() >= 1 &&
                            !victim.getCharacter().equals(CharactersType.CONDOTTIERE)) {
                        victim.addDistrict(district);
                        victim.setGolds(victim.getGolds() - 1);
                        action.printDistrictRecovered(victim, district);
                    }
                } else {
                    action.printEvequeImmune(victim, district);
                }
                return;
            }
        }
        action.printActionOfNoneDistrictDestroyed(victim, bot.getGolds());
    }


    public void swapCards(Robot victim) {
        List<DistrictsType> botDistrictInHand = bot.getDistrictInHand();
        bot.setDistrictInHand(victim.getDistrictInHand());
        victim.setDistrictInHand(botDistrictInHand);
    }

    public boolean doublon(Robot victim) {
        for (DistrictsType district : bot.getCity()) {
            for (DistrictsType victimDistrict : victim.getDistrictInHand()) {
                if (district == victimDistrict) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean doublonInHand() {
        List<DistrictsType> hand = bot.getDistrictInHand();
        List<DistrictsType> handCopy = bot.getDistrictInHand();
        Collections.reverse(hand);
        for (int i = 0; i < hand.size(); i++) {
            for (int j = 0; j < handCopy.size(); j++) {
                if (hand.get(i) == handCopy.get(j) && i != j) {
                    return true;
                }
            }
        }
        return false;
    }


    public void magicien(List<Robot> bots, DeckDistrict deck) {
        Robot victim = bot.chooseVictimForMagicien(bots);
        //int i = bot.generateChoice();
        if (bot.getNumberOfDistrictInHand() < victim.getNumberOfDistrictInHand()) {
            swapCards(victim);
            action.printMagicianSwap(victim);
            action.showStatusOfBot();
            action.showStatusOfBot(victim);
        } else if (doublon(bot) || doublonInHand()) {
            int numberOfDistrictInHand = bot.getNumberOfDistrictInHand();
            List<DistrictsType> listDistrictHandMagician = new ArrayList<>(bot.getDistrictInHand());
            bot.emptyListOfCardsInHand();
            for (; numberOfDistrictInHand > 0; numberOfDistrictInHand--) bot.addDistrict(deck.getDistrictsInDeck());
            while (!listDistrictHandMagician.isEmpty()) deck.addDistrictToDeck(listDistrictHandMagician.remove(0));
        }
    }

    public void assassin(Robot victim) {
        if (bot.getCharacter().getType().equals(ASSASSIN)) {
            victim.setIsAssassinated(true);
        }
    }


    public void voleur(Robot victim) {
        if (!victim.getIsAssassinated()) {
            int stolenGold = victim.getGolds();
            bot.addGold(stolenGold);
            action.printThiefStill(victim);
            victim.setGolds(0);
        } else action.printCantAffectVictim(victim);
    }

}

