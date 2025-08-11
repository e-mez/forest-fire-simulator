package group.ciril.emez;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ForestTest {

    @Test
    void isOnFireAsExpected() throws IOException, IllegalArgumentException {
        Forest forest = new Forest("input/2x1onfire.txt");
        Assertions.assertTrue(forest.isOnFire());
    }

    @Test
    void isNotOnFireAsExpected() throws IOException, IllegalArgumentException {
        Forest forest = new Forest("input/2x1-not-on-fire.txt");
        assertFalse(forest.isOnFire());
    }

    @Test
    void fireDoesNotSpreadAsExpectedOn2x1Forest() throws IOException, IllegalArgumentException {
        boolean[][] expectedBlocks = new boolean[][] {{false}, {false}};

        Forest forest = new Forest("input/2x1onfire.txt");
        forest.transitionBlocks();
        boolean[][] blocks = forest.getBlocks();

        assertEquals(expectedBlocks.length, blocks.length);
        assertEquals(expectedBlocks[0].length, blocks[0].length);
        assertTrue(allBlocksInSameFireState(expectedBlocks, blocks));
        assertFalse(forest.isOnFire());
    }

    @Test
    void fireSpreadsAsExpectedOn1x2Forest() throws IOException, IllegalArgumentException {
        boolean[][] expectedBlocks = new boolean[][] {{false, true}};

        Forest forest = new Forest("input/1x2.txt");
        forest.transitionBlocks();
        boolean[][] blocks = forest.getBlocks();

        assertEquals(expectedBlocks.length, blocks.length);
        assertEquals(expectedBlocks[0].length, blocks[0].length);
        assertTrue(allBlocksInSameFireState(expectedBlocks, blocks));
        assertTrue(forest.isOnFire());
    }

    @Test
    void fireSpreadsAsExpectedOn4x3Forest() throws IOException, IllegalArgumentException {
        boolean[][] expectedBlocks = new boolean[][] {
                {true, true, false},
                {false, true, false},
                {false, false, false},
                {false, true, true}
        };

        Forest forest = new Forest("input/4x3.txt");
        forest.transitionBlocks();
        boolean[][] blocks = forest.getBlocks();

        assertEquals(expectedBlocks.length, blocks.length);
        assertEquals(expectedBlocks[0].length, blocks[0].length);
        assertTrue(allBlocksInSameFireState(expectedBlocks, blocks));
        assertTrue(forest.isOnFire());
    }

    private boolean allBlocksInSameFireState(boolean[][] block1, boolean[][] block2) {
        for (int i = 0; i < block1.length; i++) {
            for (int j = 0; j < block1[0].length; j++) {
                if (block1[i][j] != block2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}