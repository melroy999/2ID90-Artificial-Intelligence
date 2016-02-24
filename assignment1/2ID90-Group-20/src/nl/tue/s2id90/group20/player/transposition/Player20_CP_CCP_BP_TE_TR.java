/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.player.transposition;

import nl.tue.s2id90.group20.player.players.*;
import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import nl.tue.s2id90.group20.evaluation.BorderPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountCrownPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.TandemEvaluation;
import nl.tue.s2id90.group20.player.Player20Base;
import nl.tue.s2id90.group20.player.Player20TranspositionBase;

/**
 *
 * @author Melroy
 */
public class Player20_CP_CCP_BP_TE_TR extends Player20TranspositionBase {
    private final AbstractEvaluation[] evaluators;//The evaluation method used by the player

    public Player20_CP_CCP_BP_TE_TR() {
        super("Player20_CP_CCP_BP_TE");
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
}
