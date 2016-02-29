/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20;

import nl.tue.s2id90.group20.evaluation.OmniEvaluation;

/**
 *
 * @author Melroy
 */
public class Player20TranspositionOmni extends Player20TranspositionBase {

    /**
     * Create a player using the omniEvaluation.
     */
    public Player20TranspositionOmni() {
        super(11, 20000, Player20TranspositionBase.class.getResource("resources/gc.png"));

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
         int winWeight
         */
        OmniEvaluation evaluation = new OmniEvaluation(25, 50, 200, 5, 10, 1, 2, 3, 4,
                10, 2, 15, 2, 12, 0);
        this.addEvaluator(evaluation);
    }
}
