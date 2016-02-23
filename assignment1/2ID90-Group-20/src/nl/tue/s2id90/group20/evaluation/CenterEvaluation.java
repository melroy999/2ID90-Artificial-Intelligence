/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.evaluation;

/**
 *
 * @author Melroy
 */
public class CenterEvaluation extends AbstractEvaluation {
    private final int centerWeight = 10;
    
    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer) {
        int value = 0;
        
        value += evaluateSide(pieces[22], isWhitePlayer, centerWeight);
        value += evaluateSide(pieces[23], isWhitePlayer, centerWeight);
        value += evaluateSide(pieces[24], isWhitePlayer, centerWeight);
        value += evaluateSide(pieces[27], isWhitePlayer, centerWeight);
        value += evaluateSide(pieces[28], isWhitePlayer, centerWeight);
        value += evaluateSide(pieces[29], isWhitePlayer, centerWeight);
        
        return value;
    }
    
}