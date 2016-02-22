/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import nl.tue.s2id90.group20.AIStoppedException;
import nl.tue.s2id90.group20.GameNode;
import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import org10x10.dam.game.Move;

/**
 *
 * @author Melroy
 */
public abstract class Player20Base extends DraughtsPlayer {
    protected boolean stopped = false;
    protected int value = 0;
    protected boolean isWhite = false;
    int callCount = 0;

    public Player20Base() {

    }

    @Override
    public Move getMove(DraughtsState state) {
        isWhite = state.isWhiteToMove();
        GameNode node = new GameNode(state, 0, -1);
        node.setBestMove(state.getMoves().get(0));
        
        callCount = 0;

        try {
            //Do iterative deepening.
            for (int maxDepth = 1; maxDepth < 100; maxDepth++) {
                value = alphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, maxDepth, true);
            }
        } catch (AIStoppedException ex) {
            //Stop iterative deepening when exception is thrown.
            System.out.println(this.getClass().toString() + " reached depth " + ex.depth);
        }
        
        /*try {
            PrintWriter writer = new PrintWriter(new FileWriter(new File("F:\\desktop windows8.1\\gitlab\\2ID90-Artificial-Intelligence\\assignment1\\player20_complete_callCount_logs.csv"), true));
            writer.println(callCount);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Player20Base.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        /*try {
            PrintWriter writer = new PrintWriter(new FileWriter(new File("F:\\desktop windows8.1\\gitlab\\2ID90-Artificial-Intelligence\\assignment1\\value_log.txt"), true));
            writer.println(value);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Player20DetectDuplicate.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        return node.getBestMove();
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void stop() {
        stopped = true;
    }

    /**
     * Search function based on the alpha-beta algorithm.
     *
     * @param node: A node containing information about the current state of the
     * game.
     * @param a: The current alpha value.
     * @param b: The current beta value.
     * @param depth: The current depth.
     * @param depthLimit: The depth limit.
     * @param maximize: Whether we maximize or minimize in this iteration.
     * @return Best evaluative value found during search.
     * @throws AIStoppedException
     */
    protected int alphaBeta(GameNode node, int a, int b, int depth, int depthLimit, boolean maximize) throws AIStoppedException {
        if (stopped) {
            //if the stop sign has been given, throw an AI stopped exception.
            stopped = false;
            throw new AIStoppedException(depth);
        }
        callCount++;

        DraughtsState state = node.getGameState();

        if (depth > depthLimit || state.isEndState()) {
            //No more moves exist, one player has won.
            return evaluate(state);
        }

        List<Move> moves = state.getMoves();
        Move bestMove = moves.get(0);

        for (Move move : moves) {
            state.doMove(move);

            //recursive boogaloo
            GameNode newNode = new GameNode(state, depth, depthLimit);
            int childResult = alphaBeta(newNode, a, b, depth + 1, depthLimit, !maximize);

            state.undoMove(move);

            if (maximize) {
                if (childResult > a) {
                    a = childResult;
                    bestMove = move;
                }
            } else if (childResult < b) {
                b = childResult;
                bestMove = move;
            }

            if (a >= b) {
                //is calling best move here correct?
                node.setBestMove(bestMove);
                return maximize ? b : a;
            }
        }

        node.setBestMove(bestMove);
        return maximize ? a : b;
    }

    public abstract AbstractEvaluation[] getEvaluators();

    /**
     * Evaluate the state of the board.
     *
     * @param state: The state that has to be evaluated.
     * @return An integer value denoting an evaluation.
     */
    public int evaluate(DraughtsState state) {
        int[] pieces = state.getPieces();
        int result = 0;
        for (AbstractEvaluation evaluator : getEvaluators()) {
            result += evaluator.evaluate(pieces, isWhite);
        }
        return result;
    }
}
