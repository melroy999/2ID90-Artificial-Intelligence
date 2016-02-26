/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.evaluation;

/**
 *
 * @author Melroy
 */
public class CountPiecesExponentialEvaluation extends AbstractEvaluation {

    protected final int pieceWeight;

    public CountPiecesExponentialEvaluation(int pieceWeight) {
        this.pieceWeight = pieceWeight;
    }

    /**
     * Evaluate the current state of the board by counting the amount of pieces
     * on the board.
     *
     * @param pieces: List of pieces on the board, in the order specified in the
     * problem description.
     * @param isWhitePlayer: Whether white makes the current move or not.
     * @return The evaluative value of your pieces minus the evaluative value of
     * the opponents pieces, where a normal piece evaluates to 1 and a king
     * piece evaluates to 3.
     */
    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer) {
        int black = 0;
        int white = 0;

        for (int piece : pieces) {
            if (isWhite(piece)) {
                white++;
            } else if (isBlack(piece)) {
                black++;
            }
        }

        black = (int) Math.pow(pieceWeight, black);
        white = (int) Math.pow(pieceWeight, white);

        return isWhitePlayer ? white - black : black - white;
    }

    @Override
    public String toString() {
        return "pieceWeight=" + pieceWeight + " ";
    }
}
