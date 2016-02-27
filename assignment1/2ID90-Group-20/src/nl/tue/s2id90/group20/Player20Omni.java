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
public class Player20Omni extends Player20Base {

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
         int lonerPawnWeight,
         int lonerKingWeight, 
         int holeWeight
         */
        OmniEvaluation evaluation = new OmniEvaluation(30, 40, 120, 5, 15, 3, 5, 10, 10,
                15, 10, 15, 10, 15, 0, 0, 0);
        this.addEvaluator(evaluation);
    }
   
}
