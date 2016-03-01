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
 * @author s139037
 */
public class OmniTesterBase extends Player20TranspositionBase {
    private final String name;

    public OmniTesterBase(int pruningWindow, int bounds, int unoccupiedPromotionLineFieldsWeight,
            int pawnWeight, int kingWeight, int safePawnWeight,
            int safeKingWeight,
            int aggregatedDistanceOfPawnToPromotionLineWeight,
            int defenderPieceWeight, int attackPawnWeight, int centerPawnWeight,
            int centerKingWeight, int mainDiagonalPawnWeight,
            int mainDiagonalKingWeight, int doubleDiagonalPawnWeight,
            int doubleDiagonalKingWeight, String name) {
        super(pruningWindow, bounds, Player20TranspositionBase.class.getResource("resources/gc.png"));

        OmniEvaluation evaluation = new OmniEvaluation(
                unoccupiedPromotionLineFieldsWeight, 
                pawnWeight, 
                kingWeight, 
                safePawnWeight, 
                safeKingWeight, 
                aggregatedDistanceOfPawnToPromotionLineWeight, 
                defenderPieceWeight, 
                attackPawnWeight,
                centerPawnWeight,
                centerKingWeight, 
                mainDiagonalPawnWeight, 
                mainDiagonalKingWeight, 
                doubleDiagonalPawnWeight, 
                doubleDiagonalKingWeight);
        this.name = name;
        this.addEvaluator(evaluation);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getName() {
        return name; //To change body of generated methods, choose Tools | Templates.
    }

    
}
