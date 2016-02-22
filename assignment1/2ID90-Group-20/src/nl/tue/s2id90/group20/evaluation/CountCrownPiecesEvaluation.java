package nl.tue.s2id90.group20.evaluation;

public class CountCrownPiecesEvaluation extends AbstractEvaluation {
    private final int crownWeight = 125;
    
    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer) {
        int count = 0;
        
        for (int piece : pieces) {
            if(isKing(piece)){
                count += evaluateSide(piece, isWhitePlayer, crownWeight);
            }
        }
       
        return count;
    }
    
}
