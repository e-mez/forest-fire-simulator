package group.ciril.emez;

public class FireSimulator {
    private final Forest forest;

    public FireSimulator(Forest forest) {
        this.forest = forest;
    }

    public void run() {
        while (forest.isOnFire()) {
            forest.transitionBlocks();
        }
    }

    public Forest getForest() {
        return forest;
    }
}
