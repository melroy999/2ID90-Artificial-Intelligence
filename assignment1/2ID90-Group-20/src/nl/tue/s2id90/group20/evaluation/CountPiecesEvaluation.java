package nl.tue.s2id90.group20.evaluation;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 * Holds the evaluation based on piece counting.
 */
public class CountPiecesEvaluation implements IEvaluation {

    /**
     * Evaluate the current state of the board by counting the amount of pieces
     * on the board.
     *
     * @param pieces: List of pieces on the board, in the order specified in the
     * problem description.
     * @param isWhite: Whether white makes the current move or not.
     * @return The evaluative value of your pieces minus the evaluative value of
     * the opponents pieces, where a normal piece evaluates to 1 and a king
     * piece evaluates to 3.
     */
    @Override
    public int evaluate(int[] pieces, boolean isWhite) {
        int computedValue = 0;
        for (int piece : pieces) {
            if (isWhite) {
                switch (piece) {
                    case DraughtsState.WHITEPIECE:
                        computedValue += 1;
                        break;
                    case DraughtsState.WHITEKING:
                        computedValue += 3;
                        break;
                    case DraughtsState.BLACKPIECE:
                        computedValue -= 1;
                        break;
                    case DraughtsState.BLACKKING:
                        computedValue -= 3;
                        break;
                }
            } else {
                switch (piece) {
                    case DraughtsState.BLACKPIECE:
                        computedValue += 1;
                        break;
                    case DraughtsState.BLACKKING:
                        computedValue += 3;
                        break;
                    case DraughtsState.WHITEPIECE:
                        computedValue -= 1;
                        break;
                    case DraughtsState.WHITEKING:
                        computedValue -= 3;
                        break;
                }
            }
        }

        return computedValue;
    }

}
