/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.evaluation;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 *
 * @author Melroy
 */
public class CountPiecesEvaluation implements IEvaluation {
    @Override
    public int evaluate(int[] pieces, boolean isWhite) {
        int computedValue = 0;
        for(int piece : pieces){
            if(isWhite){
                switch(piece) {
                    case DraughtsState.WHITEPIECE: computedValue += 1; break;
                    case DraughtsState.WHITEKING: computedValue += 3; break;
                    case DraughtsState.BLACKPIECE: computedValue -= 1; break;
                    case DraughtsState.BLACKKING: computedValue -= 3; break;
                } 
            } else {
                switch(piece) {
                    case DraughtsState.BLACKPIECE: computedValue += 1; break;
                    case DraughtsState.BLACKKING: computedValue += 3; break;
                    case DraughtsState.WHITEPIECE: computedValue -= 1; break;
                    case DraughtsState.WHITEKING: computedValue -= 3; break;
                }
            }
        }
        
        return computedValue;
    }
    
}
