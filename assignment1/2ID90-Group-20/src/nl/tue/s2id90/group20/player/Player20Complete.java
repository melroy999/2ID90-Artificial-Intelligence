package nl.tue.s2id90.group20.player;

import java.util.List;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import nl.tue.s2id90.group20.AIStoppedException;
import nl.tue.s2id90.group20.GameNode;
import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import nl.tue.s2id90.group20.evaluation.BorderPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountCrownPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountPiecesEvaluation;
import org10x10.dam.game.Move;

/**
 * The player represented by group 20.
 */
public class Player20Complete extends DraughtsPlayer {

    private boolean stopped = false;
    private int value = 0;
    private boolean isWhite = false;
    private final AbstractEvaluation[] evaluators;//The evaluation method used by the player

    public Player20Complete() {
        this.evaluators = new AbstractEvaluation[] {
            new CountPiecesEvaluation(),
            new CountCrownPiecesEvaluation(),
            new BorderPiecesEvaluation()
        };
    }

    @Override
    public Move getMove(DraughtsState state) {
        isWhite = state.isWhiteToMove();
        GameNode node = new GameNode(state);
        try {
            //Do iterative deepening.
            for (int maxDepth = 1; maxDepth < Integer.MAX_VALUE; maxDepth++) {
                value = alphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth, true);
            }
        } catch (AIStoppedException ex) {
            //Stop iterative deepening when exception is thrown.
        }
        return node.getBestMove();
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void stop() {
        stopped = true;
    }

    /**
     * Search function based on the alpha-beta algorithm.
     *
     * @param node: A node containing information about the current state of the
     * game.
     * @param a: The current alpha value.
     * @param b: The current beta value.
     * @param depthToGo: how much more recursive calls we are allowed to make.
     * @param maximize: Whether we maximize or minimize in this iteration.
     * @return Best evaluative value found during search.
     * @throws AIStoppedException
     */
    private int alphaBeta(GameNode node, int a, int b, int depthToGo, boolean maximize) throws AIStoppedException {
        if (stopped) {
            //if the stop sign has been given, throw an AI stopped exception.
            stopped = false;
            throw new AIStoppedException();
        }

        DraughtsState state = node.getGameState();

        if (depthToGo == 0) {
            //We reached the depth limit set for this search round.
            return evaluate(state);
        }

        if (state.isEndState()) {
            //No more moves exist, one player has won.
            return evaluate(state);
        }

        List<Move> moves = state.getMoves();

        Move bestMove = moves.get(0);

        for (Move move : moves) {
            state.doMove(move);

            //recursive boogaloo
            GameNode newNode = new GameNode(state);
            int childResult = alphaBeta(newNode, a, b, depthToGo - 1, !maximize);

            state.undoMove(move);

            if (maximize) {
                if (childResult > a) {
                    a = childResult;
                    bestMove = move;
                }
            } else if (childResult < b) {
                b = childResult;
                bestMove = move;
            }

            if (a >= b) {
                return maximize ? b : a;
            }
        }

        node.setBestMove(bestMove);
        return maximize ? a : b;
    }

    public AbstractEvaluation[] getEvaluators() {
        return evaluators;
    }

    /**
     * Evaluate the state of the board.
     *
     * @param state: The state that has to be evaluated.
     * @return An integer value denoting an evaluation.
     */
    int evaluate(DraughtsState state) {
        int[] pieces = state.getPieces();
        int result = 0;
        for(AbstractEvaluation evaluator : getEvaluators()){
            result += evaluator.evaluate(pieces, isWhite);
        }
        return result;
    }
}
