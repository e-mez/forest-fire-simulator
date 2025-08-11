package group.ciril.emez;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Forest {
    private int rows;
    private int columns;
    private boolean[][] blocks;
    private double fireSpreadChance;

    public Forest(String filePath) throws IOException, IllegalArgumentException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        // try set the rows and columns properties
        String line = br.readLine().trim();
        String[] dimensions = line.split(" ");
        rows = Integer.parseInt(dimensions[0]);
        columns = Integer.parseInt(dimensions[1]);
        if (rows < 1 || columns < 1) {
            throw new IllegalArgumentException("Les dimensions de la grille ne sont pas bons.");
        }

        blocks = new boolean[rows][columns];

        // set the probability of forest fire propagation
        fireSpreadChance = Double.parseDouble(br.readLine().trim());

        // set the forest blocks
        int i = 0;
        while (i < rows) {
            line = br.readLine().trim();
            if (!line.matches("[.x]*")) {
                throw new IllegalArgumentException("La grille ne peut etre representé que par des '.' et 'x'.");
            }

            if (line.length() != columns) {
                throw new IllegalArgumentException("Le nombre de cases sur une ligne n'est pas correct.");
            }

            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == 'x') { // implies this block is on fire
                    blocks[i][j] = true;
                }
            }
            i++;
        }
    }

    public void transitionBlocks() {}

    public boolean[][] getBlocks() {
        return blocks;
    }

    private void setBlocks(boolean[][] _blocks) {
        blocks = _blocks;
    }

    /**
     * Un forêt est en feu s'il y a au moins une case en feu.
     * @return true si le foret est en feu et non sinon
     */
    public boolean isOnFire() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (blocks[r][c]) return true;
            }
        }
        return false;
    }
}
