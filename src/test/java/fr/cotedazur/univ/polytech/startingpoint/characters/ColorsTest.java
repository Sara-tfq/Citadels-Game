package fr.cotedazur.univ.polytech.startingpoint.characters;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ColorsTest {


    @Test
    public void testGetRandomColorCode() {
        Set<Colors> allowedColorsSet = new HashSet<>();
        allowedColorsSet.add(Colors.BLUE);
        allowedColorsSet.add(Colors.GREEN);
        allowedColorsSet.add(Colors.RED);
        allowedColorsSet.add(Colors.YELLOW);
        for (int i = 0; i < 100; i++) { // Run it 100 times for variety
            Colors randomColor = Colors.getRandomColorCode();
            assertTrue(allowedColorsSet.contains(randomColor));
        }
    }


    @Test
    void getColorDisplay() {

        assertEquals("\u001B[32m", Colors.GREEN.getColorDisplay());
        assertEquals("\u001B[31m", Colors.RED.getColorDisplay());
        assertEquals("\u001B[34m", Colors.BLUE.getColorDisplay());
        assertEquals("\u001B[33m", Colors.YELLOW.getColorDisplay());
        assertEquals("\u001B[35m", Colors.PURPLE.getColorDisplay());
        assertEquals("\u001B[37;1m", Colors.GRAY.getColorDisplay());
        assertEquals("\u001B[0m", Colors.RESET.getColorDisplay());
    }
}
