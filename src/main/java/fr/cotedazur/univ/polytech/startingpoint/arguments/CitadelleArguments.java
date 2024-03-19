package fr.cotedazur.univ.polytech.startingpoint.arguments;

import com.beust.jcommander.Parameter;

public class CitadelleArguments {
    @Parameter(names = {"--2thousands"}, description = "Joue 2000 parties")
    public boolean _2Thousands;

    @Parameter(names = {"--demo"}, description = "Affiche une partie démo")
    public boolean demoMode;

    @Parameter(names = {"--csv"}, description = "Update les statistiques dans le fichier stats/gamestats.csv")
    public boolean csvFilePath;

    public boolean _2ThousandsGame() {
        return _2Thousands;
    }

    public boolean isDemoMode() {
        return demoMode;
    }

    public boolean getCsvFilePath() {
        return csvFilePath;
    }

}