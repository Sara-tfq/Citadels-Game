package fr.cotedazur.univ.polytech.startingpoint.gamestats;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DisplayFullGameStats {
    public static void parseFullStats() throws Exception {
        Path relative = Paths.get("stats", "gamestats.csv");
        File file = new File(relative.toString());

        List<String[]> allRows;
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            if (!file.exists()) {
                Files.createDirectories(relative.getParent());
                Files.createFile(relative);
            }

            allRows = reader.readAll();
        }

        for (String[] row : allRows) {
            System.out.println(Arrays.toString(row));
        }
    }
}
