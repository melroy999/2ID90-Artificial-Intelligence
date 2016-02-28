package nl.tue.s2id90.group20.evaluation;

public class BorderPiecesEvaluation extends AbstractEvaluation {

    private final int sideWeight;
    private final int kingLaneWeight;

    /**
     * Create a border piece evaluation.
     * 
     * @param sideWeight: Weight of pieces on the side.
     * @param kingLaneWeight: Weight of pieces on the promotion lane.
     */
    public BorderPiecesEvaluation(int sideWeight, int kingLaneWeight) {
        this.sideWeight = sideWeight;
        this.kingLaneWeight = kingLaneWeight;
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

        //top and bottom row of the playing board.
        for (int i = 1; i <= 5; i++) {
            count += evaluateSide(pieces[i], isWhitePlayer, kingLaneWeight);
            count += evaluateSide(pieces[45 + i], isWhitePlayer, kingLaneWeight);
        }

        //other borders of the playing board.
        for (int i = 1; i <= 4; i++) {
            count += evaluateSide(pieces[15 + 10 * (i - 1)], isWhitePlayer, sideWeight);
            count += evaluateSide(pieces[6 + 10 * (i - 1)], isWhitePlayer, sideWeight);
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
        return "sideWeight=" + sideWeight + " kingLaneWeight=" + kingLaneWeight + " ";
    }
}
