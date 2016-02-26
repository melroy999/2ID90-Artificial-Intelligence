/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.transposition;

import java.util.HashMap;
import nl.tue.s2id90.draughts.DraughtsState;
import org10x10.dam.game.Move;

/**
 *
 * @author Melroy
 */
public class TranspositionTable {

    /**
     * Object that generates the keys connected to DraughtsStates.
     */
    private static final ZobristKey keyManager = new ZobristKey();

    /**
     * Table holding already analysed nodes.
     */
    private static final HashMap<Long, TranspositionEntry> transpositionTable = new HashMap<>(2000000);

    /**
     * Returns a zobrist key.
     *
     * @param state: State you want a key for.
     * @return highly probable unique key for the given state.
     */
    public static long getZobristKey(DraughtsState state) {
        return keyManager.getZobristKey(state);
    }

    /**
     * Returns the key corresponding to the state in which the move was made.
     *
     * @param key: The key of the state where the move was not made yet.
     * @param move: The move made.
     * @return The key of the state where the move was made.
     */
    public static long doMove(long key, Move move, boolean isWhiteMove) {
        return keyManager.doMove(key, move, isWhiteMove);
    }

    /**
     * Returns the key corresponding to the state in which the move was made.
     *
     * @param key: The key of the state where the move was not made yet.
     * @param move: The move made.
     * @return The key of the state where the move was made.
     */
    public static long undoMove(long key, Move move, boolean isWhiteMove) {
        return keyManager.undoMove(key, move, isWhiteMove);
    }

    /**
     * Clears the transposition table.
     */
    public void clear() {
        transpositionTable.clear();
    }

    /**
     * Fetch an entry from the transpotation table.
     *
     * @param key: The key of the state you want to fetch.
     * @return The corresponding entry, if it exists. Null otherwise.
     */
    public TranspositionEntry fetchEntry(long key) {
        return transpositionTable.get(key);
    }

    /**
     * Store an entry from the transposition table.
     *
     * @param key: The key of the state you want to store.
     * @param entry: Entry you want to store.
     */
    public void storeEntry(long key, TranspositionEntry entry) {
        transpositionTable.put(key, entry);
    }
}
