/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.extensiveTesters;

import nl.tue.s2id90.group20.Player20TranspositionBase;
import nl.tue.s2id90.group20.evaluation.OmniEvaluation;

/**
 *
 * @author Melroy
 */
public class Player20Test8 extends Player20TranspositionBase {
    public Player20Test8() {
        super(20, 20000, Player20TranspositionBase.class.getResource("resources/gc.png"));

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
        OmniEvaluation evaluation = new OmniEvaluation(50, 50, 200, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        this.addEvaluator(evaluation);
    }
}
