package nl.tue.s2id90.group20.evaluation;

public class CountCrownPiecesEvaluation extends AbstractEvaluation {

    private final int crownWeight;

    /**
     * Count the amount of kings on the board.
     * 
     * @param crownWeight: Weight of a king.
     */
    public CountCrownPiecesEvaluation(int crownWeight) {
        this.crownWeight = crownWeight;
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
        int count = 0;

        for (int piece : pieces) {
            if (isKing(piece)) {
                count += evaluateSide(piece, isWhitePlayer, crownWeight);
            }
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
        return "crownWeight=" + crownWeight + " ";
    }

}
