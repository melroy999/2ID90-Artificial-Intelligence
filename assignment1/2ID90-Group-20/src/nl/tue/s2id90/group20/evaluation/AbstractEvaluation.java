package nl.tue.s2id90.group20.evaluation;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 * Interface used to give an evaluation of a state of the board.
 */
public abstract class AbstractEvaluation {

    /**
     * Evaluate the state of the board by using the pieces on the field, and the
     * side that makes the current move.
     *
     * @param pieces: List of pieces on the board, in the order specified in the
     * problem description.
     * @param isWhitePlayer: Whether white makes the current move or not.
     * @return Evaluative value for the current state of the board.
     */
    public abstract int evaluate(int[] pieces, boolean isWhitePlayer);
    
    public int evaluate(DraughtsState state, boolean isWhitePlayer) {
        return this.evaluate(state.getPieces(), isWhitePlayer);
    }

    public static boolean isWhitePiece(int piece) {
        return piece == DraughtsState.WHITEPIECE;
    }

    public static boolean isWhiteKing(int piece) {
        return piece == DraughtsState.WHITEKING;
    }

    public static boolean isWhite(int piece) {
        return DraughtsState.EMPTY != piece && (isWhitePiece(piece) || isWhiteKing(piece));
    }

    public static boolean isBlackPiece(int piece) {
        return piece == DraughtsState.BLACKPIECE;
    }

    public static boolean isBlackKing(int piece) {
        return piece == DraughtsState.BLACKKING;
    }

    public static boolean isBlack(int piece) {
        return DraughtsState.EMPTY != piece && (isBlackPiece(piece) || isBlackKing(piece));
    }

    public static boolean isKing(int piece) {
        return isWhiteKing(piece) || isBlackKing(piece);
    }

    public static int evaluateSide(int piece, boolean isWhitePlayer) {
        return evaluateSide(piece, isWhitePlayer, 1);
    }

    public static int evaluateSide(int piece, boolean isWhitePlayer, int score) {
        if (isWhite(piece)) {
            return isWhitePlayer ? score : -score;
        } else if (isBlack(piece)) {
            return isWhitePlayer ? -score : score;
        }
        return 0;
    }

    @Override
    public abstract String toString();
}
