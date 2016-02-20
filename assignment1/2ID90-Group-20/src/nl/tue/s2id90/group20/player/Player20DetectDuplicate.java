/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.player;

import java.util.List;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.group20.AIStoppedException;
import nl.tue.s2id90.group20.DuplicateStateManager;
import nl.tue.s2id90.group20.GameNode;
import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import nl.tue.s2id90.group20.evaluation.BorderPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountCrownPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.PrioritiseEndstateEvaluation;
import org10x10.dam.game.Move;

/**
 *
 * @author Melroy
 */
public class Player20DetectDuplicate extends Player20Base {
    private final PrioritiseEndstateEvaluation extraEvaluator = new PrioritiseEndstateEvaluation();
    private final AbstractEvaluation[] evaluators;//The evaluation method used by the player
    
    public Player20DetectDuplicate() {
        this.evaluators = new AbstractEvaluation[]{
            new CountPiecesEvaluation(),
            new CountCrownPiecesEvaluation(),
            new BorderPiecesEvaluation()
        };
    }
    
    @Override
    public Move getMove(DraughtsState state) {
        isWhite = state.isWhiteToMove();
        GameNode node = new GameNode(state, 0);

        try {
            //Do iterative deepening.
            for (int maxDepth = 1; maxDepth < Integer.MAX_VALUE; maxDepth++) {
                value = alphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, maxDepth, true);
            }
        } catch (AIStoppedException ex) {
            //Stop iterative deepening when exception is thrown.
            System.out.println(this.getClass().toString() + " reached depth " + ex.depth);
        }

        return node.getBestMove();
    }

    @Override
    protected int alphaBeta(GameNode node, int a, int b, int depth, int depthLimit, boolean maximize) throws AIStoppedException {
        if (stopped) {
            //if the stop sign has been given, throw an AI stopped exception.
            stopped = false;
            throw new AIStoppedException(depth);
        }

        DraughtsState state = node.getGameState();

        if (depth > depthLimit) {
            //We reached the depth limit set for this search round.
            return evaluate(state);
        }

        if (state.isEndState()) {
            //No more moves exist, one player has won.
            return evaluate(state);
        }

        List<Move> moves = state.getMoves();

        Move bestMove = moves.get(0);

        for (Move move : moves) {
            state.doMove(move);

            GameNode newNode = new GameNode(state, depth);
            int childResult = alphaBeta(newNode, a, b, depth + 1, depthLimit, !maximize);
            
            state.undoMove(move);

            if (maximize) {
                if (childResult > a) {
                    a = childResult;
                    newNode.setValue(a);
                    bestMove = move;
                }
            } else if (childResult < b) {
                b = childResult;
                newNode.setValue(b);
                bestMove = move;
            }

            if (a >= b) {
                return maximize ? b : a;
            }
        }

        node.setBestMove(bestMove);
        return maximize ? a : b;
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
