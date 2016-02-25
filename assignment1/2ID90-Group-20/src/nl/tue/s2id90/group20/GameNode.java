package nl.tue.s2id90.group20;

import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.group20.transposition.TranspositionEntry;
import org10x10.dam.game.Move;

/**
 * Node used in the search tree to store the state of the game and other useful
 * information.
 */
public class GameNode {

    private final DraughtsState gameState;//state of the game in the current configuration.
    private Move bestMove;//best move to make.
    private int value = 0;
    private final int subTreeDepth;
    private final boolean isWhitePlaying;
    private final int depth;
    private final long key;

    public GameNode(DraughtsState gameState, int depth, int maxDepth) {
        this.gameState = gameState;
        this.subTreeDepth = maxDepth - depth;
        this.isWhitePlaying = gameState.isWhiteToMove();
        this.depth = depth;
        this.key = 0;
    }

    public GameNode(DraughtsState gameState, int depth, int maxDepth, long key) {
        this.gameState = gameState;
        this.subTreeDepth = maxDepth - depth;
        this.isWhitePlaying = gameState.isWhiteToMove();
        this.depth = depth;
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public DraughtsState getGameState() {
        return gameState;
    }

    public void setBestMove(Move move) {
        this.bestMove = move;
    }

    public Move getBestMove() {
        return bestMove;
    }

    public int getSubTreeDepth() {
        return subTreeDepth;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isWhitePlaying() {
        return isWhitePlaying;
    }

    public long getKey() {
        return key;
    }
}
