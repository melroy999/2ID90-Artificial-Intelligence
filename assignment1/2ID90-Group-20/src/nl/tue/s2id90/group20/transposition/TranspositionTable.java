package nl.tue.s2id90.group20.transposition;

import java.util.HashMap;
import nl.tue.s2id90.draughts.DraughtsState;
import org10x10.dam.game.Move;

/**
 * Class containing already found solutions for states.
 */
public class TranspositionTable {

    /**
     * Object that generates the keys connected to DraughtsStates.
     */
    private static final ZobristKey keyManager = ZobristKey.getInstance();

    /**
     * Table holding already analysed nodes.
     */
    private static final HashMap<Keypair, TranspositionEntry> transpositionTable = new HashMap<>(1000000);

    /**
     * Table holding leaf results.
     */
    private static final HashMap<Keypair, TranspositionEntry> leafTranspositionTable = new HashMap<>(1000000);
    
    /**
     * Returns a zobrist key.
     *
     * @param state: State you want a key for.
     * @return highly probable unique keypair for the given state.
     */    
    public static Keypair getZobristKeypair(DraughtsState state) {
        return keyManager.getZobristKeypair(state);
    }

    /**
     * Returns the key corresponding to the state in which the move was made.
     *
     * @param keypair: The keypair of the state where the move was not made yet.
     * @param move: The move made.
     * @return The key of the state where the move was made.
     */
    public static Keypair doMove(Keypair keypair, Move move) {
        return keyManager.doMove(keypair, move);
    }

    /**
     * Clears the transposition table and leaf table.
     */
    public void clear() {
        transpositionTable.clear();
        leafTranspositionTable.clear();
    }
    
    /**
     * Clears the leaf table.
     */
    public void clearLeafTable() {
        leafTranspositionTable.clear();
    }

    /**
     * Fetch an entry from the transpotation table.
     *
     * @param keypair: The keypair of the state you want to fetch.
     * @return The corresponding entry, if it exists. Null otherwise.
     */
    public TranspositionEntry fetchEntry(Keypair keypair) {
        return transpositionTable.get(keypair);
    }

    /**
     * Store an entry from the transposition table.
     *
     * @param keypair: The keypair of the state you want to store.
     * @param entry: Entry you want to store.
     */
    public void storeEntry(Keypair keypair, TranspositionEntry entry) {
        transpositionTable.put(keypair, entry);
    }
    
    /**
     * Fetch a leaf entry from the transpotation table.
     *
     * @param keypair: The keypair of the state you want to fetch.
     * @return The corresponding entry, if it exists. Null otherwise.
     */
    public TranspositionEntry fetchLeafEntry(Keypair keypair) {
        return leafTranspositionTable.get(keypair);
    }

    /**
     * Store a leaf entry from the transposition table.
     *
     * @param keypair: The keypair of the state you want to store.
     * @param entry: Entry you want to store.
     */
    public void storeLeafEntry(Keypair keypair, TranspositionEntry entry) {
        leafTranspositionTable.put(keypair, entry);
    }
}
