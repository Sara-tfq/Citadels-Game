package fr.cotedazur.univ.polytech.startingpoint.game;

import fr.cotedazur.univ.polytech.startingpoint.characters.CharactersType;
import fr.cotedazur.univ.polytech.startingpoint.districts.DistrictsType;
import fr.cotedazur.univ.polytech.startingpoint.robots.Robot;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActionOfBotDuringARound {


    private static final Logger logger = Logger.getLogger(ActionOfBotDuringARound.class.getName());
    private final Robot bot;
    private List<DistrictsType> listDistrictDrawn;
    private List<DistrictsType> listDistrictPicked;


    public ActionOfBotDuringARound(Robot bot, boolean systemPrint) {
        this.bot = bot;
        System.setProperty("java.util.logging.SimpleFormatter.format", "\u001B[37m %5$s%6$s%n \u001B[0m");
        if (!systemPrint) logger.setLevel(Level.OFF);
    }

    public void startTurnOfBot() {
        logger.info("\u001B[32m------------------------------------------------------------The turn of " + bot.getName() + " is starting -----------------------------------------------------\n");
        showStatusOfBot();
    }

    public void showStatusOfBot() {
        logger.info(bot.statusOfPlayer());
    }

    public void showStatusOfBot(Robot bot) {
        logger.info(bot.statusOfPlayer());
    }

    public void showCityOfBot(Robot bot) {
        logger.info("City of " + bot.getName() + " : " + getStringOfListOfDistrict(bot.getCity()));
    }

    public void addListOfDistrict(List<DistrictsType> listDistrictDrawn, List<DistrictsType> listDistrictPicked) {
        this.listDistrictDrawn = listDistrictDrawn;
        listDistrictDrawn.addAll(listDistrictPicked);
        this.listDistrictPicked = listDistrictPicked;
    }

    public void printActionOfBotWhoHasBuilt() {
        String cardDrawn = "";
        String cardPicked = "";

        cardDrawn = getStringFromListOfDistrict(cardDrawn, listDistrictDrawn);
        logger.info(bot.getName() + " drew the following cards : {" + cardDrawn + "}");
        cardPicked = getStringFromListOfDistrict(cardPicked, listDistrictPicked);

        logger.info(bot.getName() + " choose to pick : {" + cardPicked + "}");
        logger.info(bot.getName() + " has now in hand: " + bot.getNumberOfDistrictInHand() + " districts");
    }

    private String getStringFromListOfDistrict(String cardPicked, List<DistrictsType> listDistrictPicked) {

        for (int i = 0; i < listDistrictPicked.size(); i++) {
            DistrictsType districtInListDistrict = listDistrictPicked.get(i);
            cardPicked += districtInListDistrict.getColor().getColorDisplay() + districtInListDistrict + districtInListDistrict.getColorReset();
            if (i < listDistrictPicked.size() - 1) cardPicked += ",";
        }
        return cardPicked;

    }


    private String getStringOfListOfDistrict(List<DistrictsType> listOfDistrict) {
        String stringOfDistricts = "";
        stringOfDistricts = getStringFromListOfDistrict(stringOfDistricts, listOfDistrict);
        return stringOfDistricts;
    }

    public void printActionOfBotWhoGainedGold(int goldGained) {
        logger.info(bot.getName() + " earned \u001B[33m" + goldGained + " golds \u001B[37m. Total golds now: " + bot.getGolds());
    }

    public void printActionOfSellerBotWhoGainedGold() {
        logger.info("By being a merchant, " + bot.getName() + " earned 1 gold. Total golds now: " + bot.getGolds());
    }

    public void printBuildingAndPowerOfBot(String hasBuilt, int goldsWon) {
        if (!hasBuilt.equals("nothing")) {
            int golds = bot.getGolds() - goldsWon;
            logger.info(bot.getName() + " built " + hasBuilt + " and now has " + golds + " golds and has in hand: " + bot.getNumberOfDistrictInHand() + " districts");
        }

        if (goldsWon > 0)
            logger.info(bot.getName() + " has won " + goldsWon + " golds by " + bot.getCharacter().getType() + " buildings and has now " + bot.getGolds() + " golds");
        logger.info(bot.statusOfPlayer());
        logger.info("\n\u001B[32m  ------------------------------------------------------------The turn of " + bot.getName() + " is over ------------------------------------------------------------------");
    }


    public void printBuildingOfBot(String hasBuilt) {
        if (!hasBuilt.equals("nothing")) {
            logger.info(bot.getName() + " built " + hasBuilt + " and now has " + bot.getGolds() + " golds and has in hand: " + bot.getNumberOfDistrictInHand() + " districts");
        }
    }


    public void printActionOfDestroyDistrict(Robot victim, DistrictsType district, int goldsRestarts) {
        logger.info(bot.getName() + " destroyed " + district + " of " + victim.getName() + " and now has " + goldsRestarts + " golds");
    }

    public void printActionOfNoneDistrictDestroyed(Robot victim, int destructorGolds) {
        logger.info(bot.getName() + " can't destroy the districts" + " of " + victim.getName() + " because he has only " + destructorGolds + " golds or the district is a Donjon or because " + victim.getName() + " has already 8 districts");
    }

    public void printEvequeImmune(Robot victim, DistrictsType district) {
        logger.info(bot.getName() + " can't destroy " + district + " of " + victim.getName() + " because he is " + victim.getCharacter().getRole());
    }

    public void printVictimAssassined(CharactersType character) {
        logger.info(bot.getName() + " chose to kill " + character.getRole());

    }

    public void printMagicianSwap(Robot victim) {
        logger.info(bot.getName() + " swapped cards with " + victim.getName());
    }


    public void printThiefStill(Robot victim) {
        logger.info(bot.getName() + " stole golds from " + victim.getName() + ". " + bot.getName() + " has now " + bot.getGolds() + " golds and " + victim.getName() + " has 0 gold");
    }


    public String getNameOfCharacterFromNumber(int number) {
        String[] listName = {"Assassin", "Voleur", "Magicien", "Roi", "Eveque", "Marchand", "Architecte", "Condottiere"};
        return listName[number - 1];
    }

    public void printCantAffectVictim(Robot victim) {
        logger.info(bot.getName() + " can't steal " + victim.getName() + " because he has been assassinated");
    }

    public void printDistrictRecovered(Robot victim, DistrictsType district) {
        logger.info(victim.getName() + " got district " + district.getName() + " back into his hand by paying 1 gold thanks to district Cimetiere.");
    }

    public void printLaboratoryAction(List<DistrictsType> listOfDistrictRemoved) {
        logger.info("Thanks to the laboratory, " + bot.getName() + " has removed " + getStringOfListOfDistrict(listOfDistrictRemoved) + " and has gained one gold");
    }

    public void printManufactureAction(List<DistrictsType> listOfDistrictPicked) {
        logger.info("Thanks to the manufacture, " + bot.getName() + " lost 3 golds but added {" + getStringOfListOfDistrict(listOfDistrictPicked) + "} to his hand");
    }


    public void printPrioritizesRed() {
        logger.info(bot.getName() + " prioritizes War district");
    }

    public void printBotBonus() {
        logger.info(bot.getName() + " is aiming for 5 colors bonus ");
    }


    public void printPickAnyDistrict(DistrictsType currentDistrict) {
        logger.info(bot.getName() + " chose " + currentDistrict.getName() + " because it is not in his hand or city.");
    }

    public void printPickSpecialDistrict(DistrictsType currentDistrict) {
        logger.info(bot.getName() + " chose " + currentDistrict.getName() + " because it is a special district and the most expensive and is not in his hand or city.");
    }

    public void printPickDistrictByType(DistrictsType currentDistrict) {
        logger.info(bot.getName() + " chose " + currentDistrict.getName() + " because it is the same type as his character.");
    }

    public void printCantPickDistrict() {
        logger.info(bot.getName() + " can't choose any district because they are already in his hand or city ");
    }

    public void printBuildDistrictWithSameType(String district) {
        logger.info(bot.getName() + " build " + district + " because it is the same type as his character.");

    }

    public void printBuildFrequentTypeDistrict(String district) {
        logger.info(bot.getName() + " build " + district + " because it is the most frequent type.");

    }


    public void printCanFinishThisTurn() {
        logger.info(bot.getName() + " can finish this turn so " + bot.getName() + " picked " + bot.getCharacter().getRole());
    }

    public void printCanFinishNextTurn() {
        logger.info(bot.getName() + " can finish next turn so " + bot.getName() + " picked " + bot.getCharacter().getRole());
    }

    public void printTurnHasBeenSkipped() {
        logger.info(bot.getName() + " has been killed so " + bot.getName() + "'s turn is skipped");
    }

    public void printPickCondottiere(Robot victim) {
        logger.info(bot.getName() + " has picked the condottiere because " + victim.getName() + " is soon going to have 8 districts.");
    }

    public void printPickCharacterBasedOnNumberOfBuildings() {
        logger.info(bot.getName() + " has picked " + bot.getCharacter().getRole() + " because " + bot.getName() + " has 2 or more " + bot.getCharacter().getType() + " districts in his city");
    }


    public void printCharacterChoice() {
        CharactersType character = bot.getCharacter();
        if (character == CharactersType.ARCHITECTE) {
            logger.info(bot.getName() + " has picked the Architect to build more buildings quickly.");
        } else if (character == CharactersType.ROI || character == CharactersType.EVEQUE) {
            logger.info(bot.getName() + " is close to having 8 districts and picked " + character.getRole() + " for protection.");
        } else if (character == CharactersType.MARCHAND) {
            logger.info(bot.getName() + " picked the Merchant to increase gold.");
        } else {
            logger.info(bot.getName() + " picked " + character.getRole() + " based on the highest priority available.");
        }
    }

    public void printDistrictChoice(List<DistrictsType> listDistrictPicked) {
        List<DistrictsType> pickedDistricts = listDistrictPicked; // districts choisis
        if (!pickedDistricts.isEmpty()) {
            for (DistrictsType pickedDistrict : pickedDistricts) {
                String reason = getReasonForPickingDistrict(pickedDistrict);
                logger.info("Reason for choosing " + pickedDistrict.getName() + ": " + reason);
            }
        } else {
            logger.info(bot.getName() + " did not pick any district.");
        }
    }

    private String getReasonForPickingDistrict(DistrictsType district) {
        if (district.getCost() <= bot.getGolds()) {
            return "It was affordable and fits the strategy to build quickly.";
        } else {
            return "It was the best option available considering the current strategy and resources.";
        }
    }


    public void printActionChoice(int choice) {
        if (choice == 0) {
            logger.info(bot.getName() + " chose to try to build a district.");
        } else if (choice == 1) {
            logger.info(bot.getName() + " chose to take resources as building a district was not possible.");
        }
    }


    public void printSpecialDistrictsConsideration() {
        logger.info(bot.getName() + " is considering building special districts if it's advantageous");
    }

    public void printBlockOpponentStrategy(String districtName) {
        if (!"nothing".equals(districtName)) {
            logger.info(bot.getName() + " built " + districtName + " to block opponents from gaining an advantage");
        }
    }

    public void printEfficiencyBasedBuilding(String districtName) {
        if (!"nothing".equals(districtName)) {
            logger.info(bot.getName() + " built " + districtName + " based on cost-efficiency and scoring potential");
        }
    }


    public void printCharacterPredictionAndChoice(CharactersType predictedCharacter, CharactersType chosenCharacter) {
        if (predictedCharacter != null) {
            logger.info(bot.getName() + " predicts opponents might choose " + predictedCharacter.getRole() + " next");
        }
        if (chosenCharacter != null) {
            //logger.info(bot.getName() + " chose " + chosenCharacter.getRole() + " based on strategic considerations and predictions");
        }
    }

    public void printScenarioArchitecte() {
        logger.info(bot.getName() + "has picked the " + bot.getCharacter().getRole() + " otherwise someone can finish with the Architecte");
    }


    public void printVictimCondottiere(Robot victim) {
        logger.info(bot.getName() + " decided to attack" + victim.getName() + " because they almost finished building their district");
    }

    public void printRichardPickCondottiere(Robot target) {
        logger.info(bot.getName() + " decided to pick Condottiere because " + target.getName() + "  is in lead and it's getting tense ");
    }

    public void printRichardoPickAssassin() {
        logger.info(bot.getName() + " decided to pick Assassin because it smells like thief");
    }

    public void printRichardPickEveque() {
        logger.info(bot.getName() + " decided to pick Eveque because he's now trying to stop ");
    }

    public void printVictimeForMagicien(Robot victim) {
        logger.info(bot.getName() + " decide to pick Magicien because he has nothing in hand and " + victim.getName() + "has a lot of disctricts in hand");
    }

    public void printPrioritizeTYpe(CharactersType chosenCharacter) {
        logger.info(bot.getName() + " prioritizes " + chosenCharacter);
    }


    public void printChoiceOfThief(Robot bot, int numberOfCharacter) {
        logger.info(bot.getName() + " chose to steal from " + getNameOfCharacterFromNumber(numberOfCharacter));
    }

}