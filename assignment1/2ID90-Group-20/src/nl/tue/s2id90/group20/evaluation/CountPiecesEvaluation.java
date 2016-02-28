package nl.tue.s2id90.group20.evaluation;

/**
 * Holds the evaluation based on piece counting.
 */
public class CountPiecesEvaluation extends AbstractEvaluation {

    private final int pieceWeight;

    /**
     * Count the amount of pieces on the board.
     * 
     * @param pieceWeight: Weight of a piece.
     */
    public CountPiecesEvaluation(int pieceWeight) {
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
        int count = 0;

        for (int piece : pieces) {
            count += evaluateSide(piece, isWhitePlayer, pieceWeight);
        }

        return count;
    }

    /**
     * Name of the evaluation function.
     * 
     * @return String with evaluation settings.
     */
    @Override
    public String toString() {
        return "pieceWeight=" + pieceWeight + " ";
    }

}
