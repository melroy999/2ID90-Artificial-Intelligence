/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.transposition;

import java.util.HashSet;
import java.util.Random;
import nl.tue.s2id90.draughts.DraughtsState;
import org10x10.dam.game.Move;

/**
 *
 * @author Melroy
 */
public class ZobristKey {
    //TODO check if isWhite is really needed.

    /**
     * The board has 50 positions that are in use, with index 1 - 50.
     *
     * To property fetch a position, the index has to be decremented by one.
     */
    private static final int positions = 50;

    /**
     * Each position can be one of the following 4 states.
     *
     * WHITEPIECE=1, BLACKPIECE=2, WHITEKING=3 and BLACKKING=4.
     *
     * Empty states are not stored.
     */
    private static final int types = 4;

    /**
     * Random number generator, with a fixed seed.
     *
     * To make debugging possible, we use a fixed seed.
     */
    private static final Random randomGenerator = new Random(9238791733L);

    /**
     * A table of random numbers, used to generate a hash key.
     *
     * The table is two dimensional, as every position type combination needs to
     * have an unique key.
     */
    private final long[][][] zobristArray = new long[positions][types][2];

    /**
     * Constructor
     */
    public ZobristKey() {
        initializeRandomTable(zobristArray);
    }

    /**
     * Fills the zobristArray with pseudorandom numbers.
     *
     * @param zobristArray: table to be filled.
     */
    public static void initializeRandomTable(long[][][] zobristArray) {
        HashSet<Long> takenKeys = new HashSet<>();
        for (int i = 0; i < positions; i++) {
            for (int j = 0; j < types; j++) {
                for (int k = 0; k < 2; k++) {
                    long key = randomGenerator.nextLong();

                    //we do not want duplicate keys.
                    while (takenKeys.contains(key)) {
                        key = randomGenerator.nextLong();
                    }

                    takenKeys.add(key);
                    zobristArray[i][j][k] = key;
                }
            }
        }
    }

    /**
     * Returns a zobrist key.
     *
     * @param state: State you want a key for.
     * @return Highly probable unique key for the given state.
     */
    public long getZobristKey(DraughtsState state) {
        int[] pieces = state.getPieces();

        long key = 0;
        for (int i = 1; i < pieces.length; i++) {
            //the index starts at 1, according to the assignment description.
            int piece = pieces[i];
            if (piece != DraughtsState.EMPTY) {
                //here we decrement i by one. 
                //we also decrement piece by 1, as piece ids start at 1. 
                key ^= zobristArray[i - 1][piece - 1][state.isWhiteToMove() ? 1 : 0];
            }
        }

        return key;
    }

    /**
     * Returns the key corresponding to the state in which the move was made.
     *
     * @param key: The key of the state where the move was not made yet.
     * @param move: The move made.
     * @return The key of the state where the move was made.
     */
    public long doMove(long key, Move move, boolean whiteMove) {
        //undo source position
        int bi = move.getBeginField();
        int bPiece = move.getBeginPiece();
        key ^= zobristArray[bi - 1][bPiece - 1][whiteMove ? 1 : 0];

        //do target position
        int ti = move.getEndField();
        int tPiece = move.getEndPiece();
        key ^= zobristArray[ti - 1][tPiece - 1][whiteMove ? 1 : 0];

        return key;
    }

    /**
     * Returns the key corresponding to the state in which the move was made.
     *
     * @param key: The key of the state where the move was not made yet.
     * @param move: The move made.
     * @return The key of the state where the move was made.
     */
    public long undoMove(long key, Move move, boolean whiteMove) {
        //undoing a move is exactly the same as making a move, as the order
        //in which we do the XOR operations does not matter.
        return doMove(key, move, whiteMove);
    }
}
