package group.ciril.emez;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Forest {
    private String inputFileName;
    private int rows;
    private int columns;
    private Boolean[][] blocks; // will be modified during transitions
    private Boolean[][] originalBlocks; // remains unchanged throughout transitions
    private double fireSpreadChance; // probability of fire propagation
    private Set<Integer> blocksOnFire;
    private boolean[] putOutBlocks;
    private List<Boolean[][]> transitionedBlocks; // all forest block transitions

    private static final String FOREST_ROW_REGEX = "[.x]*";
    private static final String ROW_AND_COLUMN_VALUE_REGEX = "^[1-9]\\d{0,3}$";
    private static final String PROBABILITY_REGEX = "^(?:0(?:\\.\\d+)?|1(?:\\.0+)?)$";

    private void addTransitionBlock() {
        var tBlock = new Boolean[rows][columns];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                tBlock[i][j] = blocks[i][j];
            }
        }
        transitionedBlocks.add(tBlock);
    }

    private int twoDTo1D(int r, int c) {
        return r * columns + c;
    }

    private int[] oneDTo2D(int x) {
        int[] twoD = new int[2];
        twoD[0] = x / columns; // row
        twoD[1] = x % columns; // column
        return twoD;
    }

    private boolean areVAlidCoordinates(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < columns;
    }

    public Forest(String filePath) throws IOException, IllegalArgumentException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String[] inputFilePath = filePath.split("/");
        inputFileName = inputFilePath[1];

        // set the rows and columns properties
        String line = br.readLine().trim();
        String[] dimensions = line.split(" ");
        if (!dimensions[0].matches(ROW_AND_COLUMN_VALUE_REGEX) || !dimensions[1].matches(ROW_AND_COLUMN_VALUE_REGEX)) {
            throw new IllegalArgumentException("Les nombres de lignes et colonnes doivent etre comprise entre 1 et 9999 (inclus).");
        }

        rows = Integer.parseInt(dimensions[0]);
        columns = Integer.parseInt(dimensions[1]);

        blocks = new Boolean[rows][columns];
        originalBlocks = new Boolean[rows][columns];

        // set the probability of forest fire propagation
        line = br.readLine().trim();
        if (!line.matches(PROBABILITY_REGEX)) {
            throw new IllegalArgumentException("La probabilité de propagation de feu n'est pas correcte.");
        }
        fireSpreadChance = Double.parseDouble(line);

        // initially, no block fire has been put out
        putOutBlocks = new boolean[rows * columns];

        // set the forest blocks
        blocksOnFire = new HashSet<>();
        int i = 0;
        while (i < rows) {
            line = br.readLine().trim();
            if (!line.matches(FOREST_ROW_REGEX)) {
                throw new IllegalArgumentException("La grille ne peut etre representé que par des '.' et 'x'.");
            }

            if (line.length() != columns) {
                throw new IllegalArgumentException("Le nombre de cases sur une ligne n'est pas correct.");
            }

            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == 'x') { // implies this block is on fire
                    blocks[i][j] = true;
                    originalBlocks[i][j] = true;
                    blocksOnFire.add(twoDTo1D(i, j));
                }
                else {
                    blocks[i][j] = false;
                    originalBlocks[i][j] = false;
                }
            }
            i++;
        }

        br.close();

        transitionedBlocks = new ArrayList<>();
        transitionedBlocks.add(originalBlocks); // the initial blocks state
    }

    /**
     *  Cette méthode:
     *      - éteint chaque case en feu une fois pour tout
     *      - et propage le feu vers chaque case adjacente selon la probabilité
     */
    public void transitionBlocks() {
        Set<Integer> nextBlocksOnFire = new HashSet<>();
        for (Integer blockOnFire1D : blocksOnFire) {
            int[] blockOnFire2D = oneDTo2D(blockOnFire1D);
            int r = blockOnFire2D[0], c = blockOnFire2D[1];
            blocks[r][c] = false;
            putOutBlocks[blockOnFire1D] = true;

            if (fireSpreadChance < 0.5) {
                continue;
            }

            int maybeNextBlockOnFire1D;
            if (areVAlidCoordinates(r-1, c)) {
                if (!blocks[r-1][c]) {
                    maybeNextBlockOnFire1D = twoDTo1D(r-1, c);
                    if (!putOutBlocks[maybeNextBlockOnFire1D]) {
                        nextBlocksOnFire.add(maybeNextBlockOnFire1D);
                        blocks[r-1][c] = true;
                    }
                }
            }

            if (areVAlidCoordinates(r+1, c)) {
                if (!blocks[r+1][c]) {
                    maybeNextBlockOnFire1D = twoDTo1D(r+1, c);
                    if (!putOutBlocks[maybeNextBlockOnFire1D]) {
                        nextBlocksOnFire.add(maybeNextBlockOnFire1D);
                        blocks[r+1][c] = true;
                    }
                }
            }

            if (areVAlidCoordinates(r, c-1)) {
                if (!blocks[r][c-1]) {
                    maybeNextBlockOnFire1D = twoDTo1D(r, c-1);
                    if (!putOutBlocks[maybeNextBlockOnFire1D]) {
                        nextBlocksOnFire.add(maybeNextBlockOnFire1D);
                        blocks[r][c-1] = true;
                    }
                }
            }

            if (areVAlidCoordinates(r, c+1)) {
                if (!blocks[r][c+1]) {
                    maybeNextBlockOnFire1D = twoDTo1D(r, c+1);
                    if (!putOutBlocks[maybeNextBlockOnFire1D]) {
                        nextBlocksOnFire.add(maybeNextBlockOnFire1D);
                        blocks[r][c+1] = true;
                    }
                }
            }
        }
        blocksOnFire = nextBlocksOnFire;
        addTransitionBlock();
    }

    public Boolean[][] getBlocks() {
        return blocks;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public double getFireSpreadChance() {
        return fireSpreadChance;
    }

    public List<Character[][]> getTransitionedBlocks() {
        List<Character[][]> transitionedBlocksList = new ArrayList<>();

        for (Boolean[][] tb : transitionedBlocks) {
            Character[][] transitionedBlocksChar = new Character[rows][columns];

            for (int i = 0; i < tb.length; i++) {
                for (int j = 0; j < tb[0].length; j++) {
                    transitionedBlocksChar[i][j] = (tb[i][j]) ? 'x' : '.';
                }
            }

            transitionedBlocksList.add(transitionedBlocksChar);
        }
        return transitionedBlocksList;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    /**
     * Un forêt est en feu s'il y a au moins une case en feu.
     * @return true si le foret est en feu et non sinon
     */
    public boolean isOnFire() {
        return !blocksOnFire.isEmpty();
    }
}
