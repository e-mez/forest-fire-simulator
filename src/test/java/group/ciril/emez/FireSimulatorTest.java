package group.ciril.emez;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FireSimulatorTest {

    @Test
    void testRun() throws Exception {
        FireSimulator fireSimulator = new FireSimulator(new Forest("input/3x3.txt"));
        fireSimulator.run();
        assertFalse(fireSimulator.getForest().isOnFire());
    }
}