package nl.tue.s2id90.group20.transposition;

/**
 * Entries used for the transposition table.
 */
public class TranspositionEntry {

    private final int depth;
    private final int value;
    private final int nodes;

    /**
     * Make a transposition table entry.
     *
     * @param depth: depth the entry is at.
     * @param value: value of the entry.
     * @param nodes: amount of nodes in the entry.
     */
    public TranspositionEntry(int depth, int value, int nodes) {
        /*this.type = type;*/
        this.depth = depth;
        this.value = value;
        this.nodes = nodes;
    }

    /**
     * Returns the depth attached to this entry.
     *
     * @return depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Returns the value attached to this entry.
     *
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the amount of nodes attached to this entry.
     *
     * @return nodes
     */
    public int getNodes() {
        return nodes;
    }
}
