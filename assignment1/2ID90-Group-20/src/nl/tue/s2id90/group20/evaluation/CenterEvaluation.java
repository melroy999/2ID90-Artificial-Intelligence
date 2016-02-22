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
    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer) {
        int value = 0;
        
        value += evaluateSide(pieces[22], isWhitePlayer, 5);
        value += evaluateSide(pieces[23], isWhitePlayer, 5);
        value += evaluateSide(pieces[24], isWhitePlayer, 5);
        value += evaluateSide(pieces[27], isWhitePlayer, 5);
        value += evaluateSide(pieces[28], isWhitePlayer, 5);
        value += evaluateSide(pieces[29], isWhitePlayer, 5);
        
        return value;
    }
    
}
