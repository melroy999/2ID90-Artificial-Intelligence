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
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.group20.AIStoppedException;
import nl.tue.s2id90.group20.GameNode;
import nl.tue.s2id90.group20.evaluation.PrioritiseEndstateEvaluation;
import org10x10.dam.game.Move;

/**
 *
 * @author Melroy
 *
 * WARNING: HIGHLY EXPERIMENTAL
 */
public class Player20Blabbermouth extends Player20Complete {
    private final PrioritiseEndstateEvaluation extraEvaluator = new PrioritiseEndstateEvaluation();

    private HashMap<String, Integer> countMap;

    @Override
    public Move getMove(DraughtsState state) {
        isWhite = state.isWhiteToMove();
        GameNode node = new GameNode(state, 0);

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(new File("F:\\desktop windows8.1\\gitlab\\2ID90-Artificial-Intelligence\\assignment1\\blabbermouth.txt"), true));

            try {
                //Do iterative deepening.
                for (int maxDepth = 1; maxDepth < Integer.MAX_VALUE; maxDepth++) {
                    countMap = new HashMap<>();
                    countMap.put(state.toString(), 1);

                    value = alphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, maxDepth, true);

                    int i = 0;
                    int totalStateCount = 0;
                    int duplicateStateCount = 0;
                    for (String draughtsState : countMap.keySet()) {
                        int value = countMap.get(draughtsState);
                        totalStateCount += value;
                        if (value > 1) {
                            i++;
                            duplicateStateCount += value;
                        }
                    }
                    writer.println("Max depth for this iteration: " + maxDepth);

                    writer.println("Duplicate states: " + i);
                    writer.println("Total states: " + countMap.size());
                    writer.println("Percentile (states): " + ((double) i / (double) countMap.size()));

                    writer.println("Duplicate #states: " + duplicateStateCount);
                    writer.println("Total #states: " + totalStateCount);
                    writer.println("Percentile (#states): " + ((double) duplicateStateCount / (double) totalStateCount));
                    
                    writer.println("Evaluation in this state: " + value);

                    writer.println("");
                }
            } catch (AIStoppedException ex) {
                //Stop iterative deepening when exception is thrown.
                System.out.println(this.getClass().toString() + " reached depth " + ex.depth);
            }
            
            writer.println("--------------------------------");
            writer.println("");
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Player20Blabbermouth.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     * @param depthToGo: how much more recursive calls we are allowed to make.
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

        DraughtsState state = node.getGameState();

        if (!countMap.containsKey(state.toString())) {
            countMap.put(state.toString(), 0);
        }
        countMap.put(state.toString(), countMap.get(state.toString()) + 1);

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

            //recursive boogaloo
            GameNode newNode = new GameNode(state, depth);
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
                return maximize ? b : a;
            }
        }

        node.setBestMove(bestMove);
        return maximize ? a : b;
    }

    @Override
    int evaluate(DraughtsState state) {
        return extraEvaluator.evaluate(state, isWhite) + super.evaluate(state);
    }  
}
