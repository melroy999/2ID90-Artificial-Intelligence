package nl.tue.s2id90.group20.transposition;

import java.util.HashSet;
import java.util.Random;
import nl.tue.s2id90.draughts.DraughtsState;
import org10x10.dam.game.Move;

/**
 * Class that generates keys for states.
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
    private static final Random randomGenerator = new Random(92387917698L);

    /**
     * A table of random numbers, used to generate a hash key.
     *
     * The table is two dimensional, as every position type combination needs to
     * have an unique key.
     */
    private static final long[][] zobristArray = new long[positions][types];
    private static final long[][] zobristArray2 = new long[positions][types];

    private static final long playerSideKey = randomGenerator.nextLong();
    private static final long playerSideKey2 = randomGenerator.nextLong();

    private static ZobristKey instance;

    /**
     * Constructor
     */
    private ZobristKey() {
        initializeRandomTable(zobristArray);
    }

    public static ZobristKey getInstance() {
        if (instance == null) {
            instance = new ZobristKey();
        }
        return instance;
    }

    /**
     * Fills the zobristArray with pseudorandom numbers.
     *
     * @param zobristArray: table to be filled.
     */
    public static void initializeRandomTable(long[][] zobristArray) {
        HashSet<Long> takenKeys = new HashSet<>();
        takenKeys.add(playerSideKey);
        long key;
        for (int i = 0; i < positions; i++) {
            for (int j = 0; j < types; j++) {
                key = randomGenerator.nextLong();

                //we do not want duplicate keys.
                while (takenKeys.contains(key)) {
                    key = randomGenerator.nextLong();
                }

                takenKeys.add(key);
                zobristArray[i][j] = key;

                key = randomGenerator.nextLong();

                //we do not want duplicate keys.
                while (takenKeys.contains(key)) {
                    key = randomGenerator.nextLong();
                }

                takenKeys.add(key);
                zobristArray2[i][j] = key;
            }
        }
    }

    public Keypair getZobristKeypair(DraughtsState state) {
        int[] pieces = state.getPieces();

        long[] keypair = new long[2];
        for (int i = 1; i < pieces.length; i++) {
            //the index starts at 1, according to the assignment description.
            int piece = pieces[i];
            if (piece != DraughtsState.EMPTY) {
                //here we decrement i by one. 
                //we also decrement piece by 1, as piece ids start at 1. 
                keypair[0] ^= zobristArray[i - 1][piece - 1];
                keypair[1] ^= zobristArray2[i - 1][piece - 1];
            }
        }

        //do it regardless of state. has to be applied ever time you go 
        //deeper into the tree anyways.
        keypair[0] ^= playerSideKey;
        keypair[1] ^= playerSideKey2;

        return new Keypair(keypair);
    }

    public Keypair doMove(Keypair keypair, Move move) {
        long[] keys = keypair.toArray();

        //undo source position
        int bi = move.getBeginField();
        int bPiece = move.getBeginPiece();
        keys = togglePiece(keys, bi, bPiece);

        for (int i = 0; i < move.getCaptureCount(); i++) {
            int piece = move.getCapturedPiece(i);
            int field = move.getCapturedField(i);
            keys = togglePiece(keys, field, piece);
        }

        //do target position
        int ti = move.getEndField();
        int tPiece = move.getEndPiece();
        keys = togglePiece(keys, ti, tPiece);

        keys[0] ^= playerSideKey;
        keys[1] ^= playerSideKey2;

        return new Keypair(keys);
    }

    private long[] togglePiece(long[] keypair, int field, int piece) {
        if (piece != DraughtsState.EMPTY) {
            keypair[0] ^= zobristArray[field - 1][piece - 1];
            keypair[1] ^= zobristArray2[field - 1][piece - 1];
        }
        return keypair;
    }
}
