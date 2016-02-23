/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.player.players;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.group20.AIStoppedException;
import nl.tue.s2id90.group20.GameNode;
import nl.tue.s2id90.group20.transposition.TranspositionEntry;
import nl.tue.s2id90.group20.transposition.TranspositionTable;
import org10x10.dam.game.Move;

/**
 *
 * @author Melroy
 */
public class Player20_CP_CCP_BP_TE_CE_PE_TR extends Player20_CP_CCP_BP_TE_CE_PE {
    private int callCount = 0;
    private int fetchCount = 0;
    private int storeCount = 0;
    
    /**
     * Table holding already analysed nodes.
     */
    private final TranspositionTable transpositionTable = new TranspositionTable();

    @Override
    public Move getMove(DraughtsState state) {
        isWhite = state.isWhiteToMove();
        callCount = 0;
        fetchCount = 0;
        storeCount = 0;

        long key = TranspositionTable.getZobristKey(state);

        GameNode node = new GameNode(state, 0, Integer.MAX_VALUE, key);
        node.setBestMove(state.getMoves().get(0));
        Move bestMove = node.getBestMove();
        
        int maxDepth = 2;
        try {
            //Do iterative deepening.
            for (maxDepth = 2; maxDepth < 100; maxDepth++) {
                value = alphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, maxDepth, true);
                
                //as the starting gameNode is altered during runtime,
                //a half completed iteration could mess things up.
                bestMove = node.getBestMove();
            }
            System.out.println(this.getClass().toString() + " reached end state. Total of " + callCount + " nodes, of which we fetched " + fetchCount + " and stored " + storeCount + ".");
        } catch (AIStoppedException ex) {
            //Stop iterative deepening when exception is thrown.
            System.out.println(this.getClass().toString() + " reached depth " + maxDepth + " with " + callCount + " nodes, of which we fetched " + fetchCount + " and stored " + storeCount + ".");
        }
        
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(new File("F:\\desktop windows8.1\\gitlab\\2ID90-Artificial-Intelligence\\assignment1\\player20_transposition_callCount_logs.csv"), true));
            writer.println(callCount + ";" + fetchCount + ";" + maxDepth);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Player20_CP_CCP_BP_TE_CE_PE_TR.class.getName()).log(Level.SEVERE, null, ex);
        }

        //clear the table, so that we cannot cheat by using previous results in next iteration.
        transpositionTable.clear();

        return bestMove;
    }

    @Override
    protected int alphaBeta(GameNode node, int a, int b, int depth, int depthLimit, boolean maximize) throws AIStoppedException {
        if (stopped) {
            //if the stop sign has been given, throw an AI stopped exception.
            stopped = false;
            throw new AIStoppedException();
        }
        
        callCount++;
        
        //get the entry that is stored in the transposition table.
        long key = node.getKey();
        TranspositionEntry entry = transpositionTable.fetchEntry(key);

        //if the entry is not null.
        //if the depth remaining is within reach for the entry.
        if (entry != null && entry.getDepth() >= depthLimit - depth) {
            fetchCount++;
            return entry.getValue();
        }

        DraughtsState state = node.getGameState();

        if (depth == depthLimit || state.isEndState()) {
            int result = evaluate(state);
            TranspositionEntry resultEntry = new TranspositionEntry(depthLimit - depth, result);
            transpositionTable.storeEntry(key, resultEntry);
            storeCount++;
            return result;
        }

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
                //is calling best move here correct?
                node.setBestMove(bestMove);
                TranspositionEntry resultEntry = new TranspositionEntry(depthLimit - depth, maximize ? b : a);
                transpositionTable.storeEntry(key, resultEntry);
                storeCount++;
                return maximize ? b : a;
            }
        }

        node.setBestMove(bestMove);
        TranspositionEntry resultEntry = new TranspositionEntry(depthLimit - depth, maximize ? a : b);
        transpositionTable.storeEntry(key, resultEntry);
        storeCount++;
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
