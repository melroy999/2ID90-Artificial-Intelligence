package nl.tue.s2id90.group20;

import nl.tue.s2id90.group20.evaluation.OmniEvaluation;

public class Player20OmniMemTesterP2 extends Player20TranspositionBase {

    public Player20OmniMemTesterP2() {
        super(20000, 20000, Player20TranspositionBase.class.getResource(
                "resources/gc.png"));

        /*
         int unoccupiedPromotionLineFieldsWeight,
         int pawnWeight, 
         int kingWeight, 
         int safePawnWeight,
         int safeKingWeight,
         int aggregatedDistanceOfPawnToPromotionLineWeight,
         int defenderPieceWeight, 
         int attackPawnWeight, 
         int centerPawnWeight,
         int centerKingWeight, 
         int mainDiagonalPawnWeight,
         int mainDiagonalKingWeight, 
         int doubleDiagonalPawnWeight,
         int doubleDiagonalKingWeight, 
         */
        OmniEvaluation evaluation = new OmniEvaluation(20, 50, 150, 0, 0, 0 /*not useful*/,
                0, 0, 0,
                0, 0, 0, 0, 0);
        this.addEvaluator(evaluation);
    }

}
