package nl.tue.s2id90.group20.evaluation;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 *
 * @author Melroy
 */
public interface IEvaluation {
    public int evaluate(int[] pieces, boolean isWhite);
}
