package nl.tue.s2id90.group20.evaluation;

/**
 * @Deprecated Not used in final solution, this is an intermediary step to final
 * solution.
 */
public class CenterEvaluation extends AbstractEvaluation {

    private final int centerWeight;

    /**
     * Make a center evaluator.
     *
     * @param centerWeight: Weight of a piece in the center.
     */
    public CenterEvaluation(int centerWeight) {
        this.centerWeight = centerWeight;
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
        int value = 0;

        value += evaluateSide(pieces[22], isWhitePlayer, centerWeight);
        value += evaluateSide(pieces[23], isWhitePlayer, centerWeight);
        value += evaluateSide(pieces[24], isWhitePlayer, centerWeight);
        value += evaluateSide(pieces[27], isWhitePlayer, centerWeight);
        value += evaluateSide(pieces[28], isWhitePlayer, centerWeight);
        value += evaluateSide(pieces[29], isWhitePlayer, centerWeight);

        return value;
    }

    /**
     * Name of the evaluation function.
     *
     * @return String with evaluation settings.
     */
    @Override
    public String toString() {
        return "centerWeight=" + centerWeight + " ";
    }

}
