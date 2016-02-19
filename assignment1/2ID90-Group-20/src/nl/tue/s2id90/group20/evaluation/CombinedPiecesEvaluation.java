package nl.tue.s2id90.group20.evaluation;

public class CombinedPiecesEvaluation extends AbstractEvaluation {

    AbstractEvaluation cpe = new CountPiecesEvaluation();
    AbstractEvaluation ccpe = new CountCrownPiecesEvaluation();
    AbstractEvaluation bpe = new BorderPiecesEvaluation();
    
    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer) {
        int result = cpe.evaluate(pieces, isWhitePlayer);
        result += ccpe.evaluate(pieces, isWhitePlayer);
        result += bpe.evaluate(pieces, isWhitePlayer);
        return result;
    }
    
}
