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
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.group20.AIStoppedException;
import nl.tue.s2id90.group20.GameNode;
import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import nl.tue.s2id90.group20.evaluation.BorderPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CenterEvaluation;
import nl.tue.s2id90.group20.evaluation.CountCrownPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.PrioritiseEndstateEvaluation;
import nl.tue.s2id90.group20.evaluation.TandemEvaluation;
import nl.tue.s2id90.group20.player.Player20Base;
import nl.tue.s2id90.group20.transposition.TranspositionEntry;
import nl.tue.s2id90.group20.transposition.TranspositionTable;
import org10x10.dam.game.Move;

/**
 *
 * @author Melroy
 */
public abstract class Player20TranspositionBase extends Player20Base {

    /**
     * Table holding already analyzed nodes.
     */
    private final TranspositionTable transpositionTable = new TranspositionTable();
    
    int pruningWindow = 10;

    public Player20TranspositionBase(String name) {
        super(name + "_TR");
    }

    @Override
    public Move getMove(DraughtsState state) {
        Move bestMove = super.getMove(state);

        //clear the table, so that we cannot cheat by using previous results in next iteration.
        transpositionTable.clear();

        return bestMove;
    }
    
    @Override
    protected Move iterativeDeepening(DraughtsState state) {
        long key = TranspositionTable.getZobristKey(state);
        isWhite = state.isWhiteToMove();
        GameNode node = new GameNode(state.clone(), 0, Integer.MAX_VALUE, key);
        node.setBestMove(state.getMoves().get(0));
        Move bestMove = node.getBestMove();

        maxDepth = 2;
        try {
            //Do iterative deepening.
            //we use aspiration techniques to determine the starting alpha and beta.

            int alpha = -20000;
            int beta = 20000;

            while (maxDepth < 100) {
                value = alphaBeta(node, alpha, beta, 1, maxDepth, true);

                //if the evaluation is not between alpha and beta.
                if (value <= alpha || value >= beta) {
                    //set to starting values for alpha and beta, and proceed to
                    //the next iteration of the while loop without incrementing
                    //the maximum depth.
                    if (value <= alpha) {
                        alpha = -20000;
                    } else if (value >= beta) {
                        beta = 20000;
                    }

                    System.out.println("Failed to aspirate. Setting a=" + alpha + ", b=" + beta);
                    System.out.println("Pruned: " + pruneCount);
                    continue;
                }

                values.push(value);

                //we are within the specified window. set the new window
                //for the next iteration.
                alpha = value - pruningWindow;
                beta = value + pruningWindow;
                maxDepth++;
                System.out.println("Setting a=" + alpha + ", b=" + beta);
                System.out.println("Pruned: " + pruneCount);

                //as the starting gameNode is altered during runtime,
                //a half completed iteration could mess things up.
                bestMove = node.getBestMove();
            }

            System.out.println(this.getClass().toString() + " reached end state. Total of " + nodeCount + " nodes, of which we fetched " + fetchCount + ".");
        } catch (AIStoppedException ex) {
            //Stop iterative deepening when exception is thrown.
            System.out.println(this.getClass().toString() + " reached depth " + maxDepth + " with " + nodeCount + " nodes, of which we fetched " + fetchCount + ".");
        }
        
        return bestMove;
    }

    @Override
    protected int alphaBeta(GameNode node, int a, int b, int depth, int depthLimit, boolean maximize) throws AIStoppedException {
        if (stopped) {
            //if the stop sign has been given, throw an AI stopped exception.
            stopped = false;
            throw new AIStoppedException();
        }

        //get the entry that is stored in the transposition table.
        long key = node.getKey();
        TranspositionEntry entry = transpositionTable.fetchEntry(key);

        //if the entry is not null.
        //if the depth remaining is within reach for the entry.
        if (entry != null && entry.getDepth() >= depthLimit - depth) {
            fetchCount++;
            nodeCount += entry.getNodes();
            return entry.getValue();
        }

        nodeCount++;

        DraughtsState state = node.getGameState();

        if (depth == depthLimit || state.isEndState()) {
            int result = evaluate(state);
            TranspositionEntry resultEntry = new TranspositionEntry(depthLimit - depth, result, 1);
            transpositionTable.storeEntry(key, resultEntry);
            return result;
        }

        int subTreeNodeCountStart = nodeCount;
        List<Move> moves = state.getMoves();
        Move bestMove = moves.get(0);
        for (Move move : moves) {
            key = doMove(state, move, key);

            //recursive boogaloo
            GameNode newNode = new GameNode(state, depth, depthLimit, key);
            int childResult = alphaBeta(newNode, a, b, depth + 1, depthLimit, !maximize);

            key = undoMove(state, move, key);

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
                int subTreeDepth = nodeCount - subTreeNodeCountStart;
                //is calling best move here correct?
                node.setBestMove(bestMove);
                TranspositionEntry resultEntry = new TranspositionEntry(depthLimit - depth, maximize ? b : a, subTreeDepth);
                transpositionTable.storeEntry(key, resultEntry);
                pruneCount++;
                return maximize ? b : a;
            }
        }

        int subTreeDepth = nodeCount - subTreeNodeCountStart;
        node.setBestMove(bestMove);
        TranspositionEntry resultEntry = new TranspositionEntry(depthLimit - depth, maximize ? a : b, subTreeDepth);
        transpositionTable.storeEntry(key, resultEntry);
        return maximize ? a : b;
    }

    public long doMove(DraughtsState state, Move move, long key) {
        state.doMove(move);
        return TranspositionTable.doMove(key, move, state.isWhiteToMove());
    }

    public long undoMove(DraughtsState state, Move move, long key) {
        state.undoMove(move);
        return TranspositionTable.undoMove(key, move, state.isWhiteToMove());
    }
}
