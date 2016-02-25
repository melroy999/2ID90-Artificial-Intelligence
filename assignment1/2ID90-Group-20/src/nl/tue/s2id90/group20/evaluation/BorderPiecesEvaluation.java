package nl.tue.s2id90.group20.evaluation;

public class BorderPiecesEvaluation extends AbstractEvaluation {
    private final int sideWeight = 25;
    private final int kingLaneWeight = 75;
    
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

    @Override
    public String toString() {
        return "sideWeight=" + sideWeight + " kingLaneWeight=" + kingLaneWeight + " ";
    }
}
