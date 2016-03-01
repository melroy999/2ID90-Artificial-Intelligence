package nl.tue.s2id90.group20;

import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.group20.transposition.Keypair;
import org10x10.dam.game.Move;

/**
 * Node used in the search tree to store the state of the game and other useful
 * information.
 */
public class GameNode {

    private final DraughtsState gameState;//state of the game in the current configuration.
    private Move bestMove;//best move to make.
    private int value = 0;//best value found.
    private final int subTreeDepth;//depth of the subtree.
    private final boolean isWhitePlaying;//if white is playing this turn.
    private final int depth;//depth the node is at.
    private final Keypair keypair;//keypair of the state.

    /**
     * Create a gamenode without a keypair.
     *
     * @param gameState: current gamestate.
     * @param depth: depth the node is at.
     * @param maxDepth: maximum depth of the tree.
     */
    public GameNode(DraughtsState gameState, int depth, int maxDepth) {
        this.gameState = gameState;
        this.subTreeDepth = maxDepth - depth;
        this.isWhitePlaying = gameState.isWhiteToMove();
        this.depth = depth;
        this.keypair = null;
    }

    /**
     * Create a gamenode with a keypair.
     *
     * @param gameState: current gamestate.
     * @param depth: depth the node is at.
     * @param maxDepth: maximum depth of the tree.
     * @param keypair: keypair of the state.
     */
    public GameNode(DraughtsState gameState, int depth, int maxDepth,
            Keypair keypair) {
        this.gameState = gameState;
        this.subTreeDepth = maxDepth - depth;
        this.isWhitePlaying = gameState.isWhiteToMove();
        this.depth = depth;
        this.keypair = keypair;
    }

    /**
     * Return the value of this state.
     *
     * @return value;
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value of this state.
     *
     * @param value: new value.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Return the GameState.
     *
     * @return gameState.
     */
    public DraughtsState getGameState() {
        return gameState;
    }

    /**
     * Set new best move.
     *
     * @param move: new best move.
     */
    public void setBestMove(Move move) {
        this.bestMove = move;
    }

    /**
     * Returns the current best move.
     *
     * @return bestMove.
     */
    public Move getBestMove() {
        return bestMove;
    }

    /**
     * Returns the subtree depth.
     *
     * @return subTreeDepth.
     */
    public int getSubTreeDepth() {
        return subTreeDepth;
    }

    /**
     * Returns the depth this node is at.
     *
     * @return depth.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Returns whether white is playing.
     *
     * @return isWhitePlaying.
     */
    public boolean isWhitePlaying() {
        return isWhitePlaying;
    }

    /**
     * Return the keypair connected to this node.
     *
     * @return keypair.
     */
    public Keypair getKeypair() {
        return keypair;
    }
}
