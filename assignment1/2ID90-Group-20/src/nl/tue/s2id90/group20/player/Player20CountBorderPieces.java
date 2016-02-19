/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.player;

import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import nl.tue.s2id90.group20.evaluation.BorderPiecesEvaluation;

/**
 *
 * @author Melroy
 */
public class Player20CountBorderPieces extends Player20Complete {
    private AbstractEvaluation[] evaluators;

    public Player20CountBorderPieces() {
        evaluators = new AbstractEvaluation[] {
            new BorderPiecesEvaluation()
        };
    }
    
    @Override
    public AbstractEvaluation[] getEvaluators() {
        return evaluators;
    }
}
