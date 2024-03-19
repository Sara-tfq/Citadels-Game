package fr.cotedazur.univ.polytech.startingpoint.arguments;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CitadelleArgumentsTest {

    @Test
    public void test_2ThousandsGame_WithTrueValue() {
        CitadelleArguments arguments = new CitadelleArguments();
        arguments._2Thousands = true;

        boolean result = arguments._2ThousandsGame();

        assertTrue(result);
    }

    @Test
    public void test_2ThousandsGame_WithFalseValue() {
        CitadelleArguments arguments = new CitadelleArguments();
        arguments._2Thousands = false;

        boolean result = arguments._2ThousandsGame();

        assertFalse(result);
    }

    @Test
    public void testIsDemoMode_WithTrueValue() {
        CitadelleArguments arguments = new CitadelleArguments();
        arguments.demoMode = true;

        boolean result = arguments.isDemoMode();

        assertTrue(result);
    }

    @Test
    public void testIsDemoMode_WithFalseValue() {
        CitadelleArguments arguments = new CitadelleArguments();
        arguments.demoMode = false;

        boolean result = arguments.isDemoMode();

        assertFalse(result);
    }

    @Test
    public void testGetCsvFilePath_WithTrueValue() {
        CitadelleArguments arguments = new CitadelleArguments();
        arguments.csvFilePath = true;

        boolean result = arguments.getCsvFilePath();

        assertTrue(result);
    }

    @Test
    public void testGetCsvFilePath_WithFalseValue() {
        CitadelleArguments arguments = new CitadelleArguments();
        arguments.csvFilePath = false;

        boolean result = arguments.getCsvFilePath();

        assertFalse(result);
    }
}
