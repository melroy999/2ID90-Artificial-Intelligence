package nl.tue.s2id90.group20;

/**
 * Exception thrown when we want to stop searching in the tree.
 */
public class AIStoppedException extends Exception {
    public final int depth;
    
    public AIStoppedException(int depth) {
        this.depth = depth;
    }
}
