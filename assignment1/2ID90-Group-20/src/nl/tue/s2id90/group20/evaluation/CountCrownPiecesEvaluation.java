package nl.tue.s2id90.group20.evaluation;

public class CountCrownPiecesEvaluation extends AbstractEvaluation {

    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer) {
        int count = 0;
        
        for (int piece : pieces) {
            if(isKing(piece)){
                count += evaluateSide(piece, isWhitePlayer, 100);
            }
        }
       
        return count;
    }
    
}
