package fr.cotedazur.univ.polytech.startingpoint.gamestats;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class WriteStatsByLine {

    public static void writeDataLineByLine(String[][] data) {
        Path relative = Paths.get("stats", "gamestats.csv");
        File file = new File(relative.toString());

        try {
            if (!file.exists()) {
                Files.createDirectories(relative.getParent());
                Files.createFile(relative);
            }

            FileWriter outputfile = new FileWriter(file, true);
            CSVWriter writer = new CSVWriter(outputfile);

            if (file.length() == 0) {
                String[] header = {"Nom", "Nombre de parties gagnées", "Pourcentage de parties gagnées(%)", "Nombre d'égalités", "Pourcentage d'égalités(%)", "Nombre de parties perdues", "Pourcentage de parties perdues(%)", "Score moyen"};
                writer.writeNext(header, false);
            }

            writer.writeAll(java.util.Arrays.asList(data), false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}