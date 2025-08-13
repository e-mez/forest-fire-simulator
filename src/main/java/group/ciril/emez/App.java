package group.ciril.emez;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.List;

public class App {
    private static final String INPUT_DIR_NAME = "input/";
    private static final String OUTPUT_DIR_NAME = "output/";

    public static void main(String[] args) throws Exception {
        File folder = new File(INPUT_DIR_NAME);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Le chemin du fichier n'est pas valid");
            return;
        }

        File[] inputFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (inputFiles == null || inputFiles.length == 0) {
            System.err.println("Aucun fichier d'entrée trouvé");
            return;
        }

        for (File file : inputFiles) {
            Forest forest = new Forest(INPUT_DIR_NAME + file.getName());
            FireSimulator fireSimulator = new FireSimulator(forest);
            fireSimulator.run();
            writeTransitionsToFile(forest);
        }

    }

    private static void writeTransitionsToFile(Forest forest) {
        String outputFilePath = OUTPUT_DIR_NAME + forest.getInputFileName();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
            bw.write(forest.getRows() + " " + forest.getColumns() + "\n");
            bw.write(forest.getFireSpreadChance() + "\n\n");

            List<Character[][]> transitionedBlocks = forest.getTransitionedBlocks();
            for (Character[][] tb : transitionedBlocks) {
                for (Character[] characters : tb) {
                    StringBuilder line = new StringBuilder();
                    for (int j = 0; j < tb[0].length; j++) {
                        line.append(characters[j]);
                    }
                    bw.write(line + "\n");
                }
                bw.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Erreur d'écriture: " + e.getMessage());
        }
    }
}
