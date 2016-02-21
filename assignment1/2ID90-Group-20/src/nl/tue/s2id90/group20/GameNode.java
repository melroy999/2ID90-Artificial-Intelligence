package nl.tue.s2id90.group20;

import nl.tue.s2id90.draughts.DraughtsState;
import org10x10.dam.game.Move;

/**
 * Node used in the search tree to store the state of the game and other useful
 * information.
 */
public class GameNode {
    private final DraughtsState gameState;//state of the game in the current configuration.
    private Move bestMove;//best move to make.
    private final ResultNode resultParameters;
    private final boolean isWhitePlaying;
    private final int depth;

    public GameNode(DraughtsState gameState, int depth, int maxDepth) {
        this.gameState = gameState;
        resultParameters = new ResultNode(maxDepth - depth);
        isWhitePlaying = gameState.isWhiteToMove();
        this.depth = depth;
    }

    public ResultNode getResultParameters() {
        return resultParameters;
    }

    public int getValue() {
        return resultParameters.getValue();
    }

    public void setValue(int value) {
        this.resultParameters.setValue(value);
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
        return resultParameters.getSubTreeDepth();
    }

    public int getDepth() {
        return depth;
    }

    public boolean isWhitePlaying() {
        return isWhitePlaying;
    }
}
