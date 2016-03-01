package nl.tue.s2id90.group20;

import nl.tue.s2id90.group20.evaluation.OmniEvaluation;

public class Player20Omni extends Player20Base {

    /**
     * Create a player using the omniEvaluation.
     */
    public Player20Omni() {
        super(Player20Omni.class.getResource("resources/owl.png"));
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
        OmniEvaluation evaluation = new OmniEvaluation(25, 50, 150, 5, 10, 1, 2,
                3, 4,
                10, 2, 15, 2, 12);
        this.addEvaluator(evaluation);
    }

}
