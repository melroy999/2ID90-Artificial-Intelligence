/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.player.players;

import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import nl.tue.s2id90.group20.evaluation.CountCrownPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountPiecesEvaluation;
import nl.tue.s2id90.group20.player.Player20Base;

/**
 *
 * @author Melroy
 */
public class Player20_CP_CCP extends Player20Base {
    private final AbstractEvaluation[] evaluators;//The evaluation method used by the player

    public Player20_CP_CCP() {
        this.evaluators = new AbstractEvaluation[]{
            new CountPiecesEvaluation(),
            new CountCrownPiecesEvaluation()
        };
    }
    
    @Override
    public AbstractEvaluation[] getEvaluators() {
        return evaluators;
    }
}
