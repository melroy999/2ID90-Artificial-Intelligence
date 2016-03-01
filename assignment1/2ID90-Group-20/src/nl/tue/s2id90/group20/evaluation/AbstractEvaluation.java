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

    /**
     * Evaluate a state.
     *
     * @param state: State we want to evaluate.
     * @param isWhitePlayer: Whether we are the white player or not.
     * @return Evaluation of the state.
     */
    public int evaluate(DraughtsState state, boolean isWhitePlayer) {
        return this.evaluate(state.getPieces(), isWhitePlayer);
    }

    /**
     * Whether the piece is a white normal piece.
     *
     * @param piece: Piece type id.
     * @return piece == DraughtsState.WHITEPIECE
     */
    public static boolean isWhitePiece(int piece) {
        return piece == DraughtsState.WHITEPIECE;
    }

    /**
     * Whether the piece is a white king piece.
     *
     * @param piece: Piece type id.
     * @return piece == DraughtsState.WHITEKING
     */
    public static boolean isWhiteKing(int piece) {
        return piece == DraughtsState.WHITEKING;
    }

    /**
     * Whether the piece is a white piece.
     *
     * @param piece: Piece type id.
     * @return Whether piece is white or not.
     */
    public static boolean isWhite(int piece) {
        return DraughtsState.EMPTY != piece && (isWhitePiece(piece) || isWhiteKing(
                piece));
    }

    /**
     * Whether the piece is a black normal piece.
     *
     * @param piece: piece type id.
     * @return piece == DraughtsState.BLACKPIECE
     */
    public static boolean isBlackPiece(int piece) {
        return piece == DraughtsState.BLACKPIECE;
    }

    /**
     * Whether the piece is a black king piece.
     *
     * @param piece: Piece type id.
     * @return piece == DraughtsState.BLACKKING
     */
    public static boolean isBlackKing(int piece) {
        return piece == DraughtsState.BLACKKING;
    }

    /**
     * Whether the piece is a black piece.
     *
     * @param piece: Piece type id.
     * @return Whether this is a black piece.
     */
    public static boolean isBlack(int piece) {
        return DraughtsState.EMPTY != piece && (isBlackPiece(piece) || isBlackKing(
                piece));
    }

    /**
     * Whether the piece is a king piece.
     *
     * @param piece: Piece type id.
     * @return Whether the piece is a king.
     */
    public static boolean isKing(int piece) {
        return isWhiteKing(piece) || isBlackKing(piece);
    }

    /**
     * Convert score to correct side, with base score 1.
     *
     * @param piece: Piece id.
     * @param isWhitePlayer: Whether the player is the white player or not.
     * @return Appropriate evaluation.
     */
    public static int evaluateSide(int piece, boolean isWhitePlayer) {
        return evaluateSide(piece, isWhitePlayer, 1);
    }

    /**
     * Convert score to correct side.
     *
     * @param piece: Piece id.
     * @param isWhitePlayer: Whether the player is the white player or not.
     * @param score: Score we want to convert.
     * @return Appropriate evaluation.
     */
    public static int evaluateSide(int piece, boolean isWhitePlayer, int score) {
        if (isWhite(piece)) {
            return isWhitePlayer ? score : -score;
        } else if (isBlack(piece)) {
            return isWhitePlayer ? -score : score;
        }
        return 0;
    }

    /**
     * We require name for evaluation naming.
     *
     * @return Information about this evaluation.
     */
    @Override
    public abstract String toString();
}
