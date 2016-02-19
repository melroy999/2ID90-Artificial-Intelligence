package nl.tue.s2id90.group20.evaluation;

public class BorderPiecesEvaluation extends AbstractEvaluation {

    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer) {
        int count = 0;

        for (int i = 1; i <= 4; i++) {
            evaluatePiece(pieces[i], count, isWhitePlayer);
            evaluatePiece(pieces[46 + i], count, isWhitePlayer);
            evaluatePiece(pieces[5 + 10 * (i - 1)], count, isWhitePlayer);
            evaluatePiece(pieces[6 + 10 * (i - 1)], count, isWhitePlayer);
        }
        
        evaluatePiece(pieces[45], count, isWhitePlayer);
        evaluatePiece(pieces[46], count, isWhitePlayer);

        return count;
    }

    public void evaluatePiece(int piece, int count, boolean isWhitePlayer) {
            count += evaluateSide(piece, isWhitePlayer);
    }
}
