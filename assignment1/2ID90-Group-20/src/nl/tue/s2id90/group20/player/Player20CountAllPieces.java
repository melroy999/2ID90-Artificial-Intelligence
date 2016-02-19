/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.player;

import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import nl.tue.s2id90.group20.evaluation.CountCrownPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountPiecesEvaluation;

/**
 *
 * @author Melroy
 */
public class Player20CountAllPieces extends Player20Complete {
    private final AbstractEvaluation[] evaluators;

    public Player20CountAllPieces() {
        evaluators = new AbstractEvaluation[] {
            new CountPiecesEvaluation(),
            new CountCrownPiecesEvaluation()
        };
    }
    
    @Override
    public AbstractEvaluation[] getEvaluators() {
        return evaluators;
    }
}
