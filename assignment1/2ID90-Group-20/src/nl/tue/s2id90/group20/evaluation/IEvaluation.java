package nl.tue.s2id90.group20.evaluation;

/**
 * Interface used to give an evaluation of a state of the board.
 */
public interface IEvaluation {

    /**
     * Evaluate the state of the board by using the pieces on the field, and the
     * side that makes the current move.
     *
     * @param pieces: List of pieces on the board, in the order specified in the
     * problem description.
     * @param isWhite: Whether white makes the current move or not.
     * @return Evaluative value for the current state of the board.
     */
    public int evaluate(int[] pieces, boolean isWhite);
}
