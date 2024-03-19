package fr.cotedazur.univ.polytech.startingpoint.characters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum Colors {
    GREEN("\u001B[32m"),
    RED("\u001B[31m"),
    BLUE("\u001B[34m"),
    YELLOW("\u001B[33m"),
    PURPLE("\u001B[35m"),
    GRAY("\u001B[37;1m"),
    RESET("\u001B[0m"),


    ;

    private static final Random random = new Random();
    static List<Colors> listOfColors = new ArrayList<>();
    String colorDisplay;

    Colors(String colorDisplay) {
        this.colorDisplay = colorDisplay;
    }

    public static Colors getRandomColorCode() {
        Colors[] allowedColors = {
                Colors.BLUE,
                Colors.GREEN,
                Colors.RED,
                Colors.YELLOW
        };
        int randomIndex = random.nextInt(allowedColors.length);
        return allowedColors[randomIndex];
    }

    public static List<Colors> getListOfColors() {
        listOfColors.add(Colors.GREEN);
        listOfColors.add(Colors.RED);
        listOfColors.add(Colors.BLUE);
        listOfColors.add(Colors.PURPLE);
        listOfColors.add(Colors.YELLOW);
        return listOfColors;


    }

    public String getColorDisplay() {
        return colorDisplay;
    }


}
