/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.player.players;

import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import nl.tue.s2id90.group20.evaluation.BorderPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountCrownPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.PrioritiseEndstateEvaluation;
import nl.tue.s2id90.group20.evaluation.TandemEvaluation;
import nl.tue.s2id90.group20.player.Player20Base;

/**
 *
 * @author Melroy
 */
public class Player20_CP_CCP_BP_TE_PE extends Player20Base {
    private final PrioritiseEndstateEvaluation extraEvaluator = new PrioritiseEndstateEvaluation();
    private final AbstractEvaluation[] evaluators;//The evaluation method used by the player

    public Player20_CP_CCP_BP_TE_PE() {
        this.evaluators = new AbstractEvaluation[]{
            new CountPiecesEvaluation(),
            new CountCrownPiecesEvaluation(),
            new BorderPiecesEvaluation(),
            new TandemEvaluation()
        };
    }
    
    @Override
    public AbstractEvaluation[] getEvaluators() {
        return evaluators;
    }

    @Override
    public int evaluate(DraughtsState state) {
        return extraEvaluator.evaluate(state, isWhite) + super.evaluate(state);
    }
}