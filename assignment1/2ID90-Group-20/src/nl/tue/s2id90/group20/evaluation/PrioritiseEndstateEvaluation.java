/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.evaluation;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 *
 * @author Melroy
 */
public class PrioritiseEndstateEvaluation extends AbstractEvaluation {

    private final int winWeight;

    /**
     * Create a prioritise endstate evaluation.
     * 
     * @param winWeight: Weight of a win. 
     */
    public PrioritiseEndstateEvaluation(int winWeight) {
        this.winWeight = winWeight;
    }

    /**
     * Evaluate the state of the board by using the pieces on the field, and the
     * side that makes the current move.
     *
     * @param pieces: List of pieces on the board, in the order specified in the
     * problem description.
     * @param isWhitePlayer: Whether white makes the current move or not.
     * @return Evaluative value for the current state of the board.
     */
    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Evaluate a state.
     * 
     * @param state: State we want to evaluate.
     * @param isWhitePlayer: Whether we are the white player or not.
     * @return Evaluation of the state.
     */
    @Override
    public int evaluate(DraughtsState state, boolean isWhitePlayer) {
        if (state.isEndState()) {
            if (state.isWhiteToMove() == isWhitePlayer) {
                return -winWeight;
            } else {
                return winWeight;
            }
        }
        return 0;
    }

    /**
     * Name of the evaluation function.
     * 
     * @return String with evaluation settings.
     */
    @Override
    public String toString() {
        return "winWeight=" + winWeight + " ";
    }

}
