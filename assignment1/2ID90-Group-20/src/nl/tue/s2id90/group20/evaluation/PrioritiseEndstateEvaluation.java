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
public class PrioritiseEndstateEvaluation extends AbstractEvaluation {
    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int evaluate(DraughtsState state, boolean isWhitePlayer) {
        if(state.isEndState()){
            if(state.isWhiteToMove() == isWhitePlayer){
                return -1000;
            } else {
                return 1000;
            }
        }
        return 0;
    }
    
}
