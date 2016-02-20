package nl.tue.s2id90.group20.player;

import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import nl.tue.s2id90.group20.evaluation.BorderPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountCrownPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.PrioritiseEndstateEvaluation;
import org10x10.dam.game.Move;

/**
 * The player represented by group 20.
 */
public class Player20Complete extends Player20Base {
    private final PrioritiseEndstateEvaluation extraEvaluator = new PrioritiseEndstateEvaluation();
    private final AbstractEvaluation[] evaluators;//The evaluation method used by the player

    public Player20Complete() {
        this.evaluators = new AbstractEvaluation[]{
            new CountPiecesEvaluation(),
            new CountCrownPiecesEvaluation(),
            new BorderPiecesEvaluation()
        };
    }

    @Override
    public AbstractEvaluation[] getEvaluators() {
        return evaluators;
    }

    @Override
    int evaluate(DraughtsState state) {
        return extraEvaluator.evaluate(state, isWhite) + super.evaluate(state);
    }
    
}
