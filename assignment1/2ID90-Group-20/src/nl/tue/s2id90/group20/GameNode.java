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

    public GameNode(DraughtsState gameState) {
        this.gameState = gameState;
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
}
