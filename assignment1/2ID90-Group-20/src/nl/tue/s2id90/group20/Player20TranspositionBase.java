/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20;

import java.net.URL;
import java.util.List;
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
public class Player20TranspositionBase extends Player20Base {

    /**
     * Table holding already analyzed nodes.
     */
    private final TranspositionTable transpositionTable = new TranspositionTable();

    protected final int pruningWindow;
    protected final int bounds;
    protected int[][] historyHeuristic;
    //protected int[][][] killHeuristic;

    public Player20TranspositionBase(int pruningWindow, int bounds,
            int pieceWeight, int kingWeight, int sideWeight,
            int kingLaneWeight, int tandemWeight, int centerWeight,
            int endStateWeight) {
        super(pieceWeight, kingWeight, sideWeight, kingLaneWeight, tandemWeight, centerWeight, endStateWeight, true, Player20TranspositionBase.class.getResource("resources/book.png"));
        this.pruningWindow = pruningWindow;
        this.bounds = bounds;
    }

    public Player20TranspositionBase(int pruningWindow, int bounds, URL icon) {
        super(icon);
        this.pruningWindow = pruningWindow;
        this.bounds = bounds;
    }

    public Player20TranspositionBase(int pruningWindow, int bounds) {
        this.pruningWindow = pruningWindow;
        this.bounds = bounds;
    }

    @Override
    public Move getMove(DraughtsState state) {
        //clear the table, so that we cannot cheat by using previous results in next iteration.
        historyHeuristic = new int[52][52];
        //killHeuristic = new int[40][52][52];
        transpositionTable.clear();
        
        Move bestMove = super.getMove(state);

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

            int alpha = -bounds;
            int beta = bounds;

            while (maxDepth < 40) {
                value = alphaBeta(node, alpha, beta, 1, maxDepth, true);

                //if the evaluation is not between alpha and beta.
                if (value <= alpha || value >= beta) {
                    //set to starting values for alpha and beta, and proceed to
                    //the next iteration of the while loop without incrementing
                    //the maximum depth.
                    if (value <= alpha) {
                        alpha = -bounds;
                    } else if (value >= beta) {
                        beta = bounds;
                    }

                    aspirationFails++;

                    /*System.out.println("Failed to aspirate. Setting a=" + alpha + ", b=" + beta);
                     System.out.println("Pruned: " + pruneCount);*/
                    continue;
                }

                values.push(value);

                //we are within the specified window. set the new window
                //for the next iteration.
                alpha = value - pruningWindow;
                beta = value + pruningWindow;
                maxDepth++;
                /*System.out.println("Setting a=" + alpha + ", b=" + beta);
                 System.out.println("Pruned: " + pruneCount);*/

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
    protected int alphaBeta(GameNode node, int a, int b, int depth,
            int depthLimit, boolean maximize) throws AIStoppedException {
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
        /*List<Move> unorderedMoves = state.getMoves();*/

        Move bestMove;

        //no ordering
        List<Move> moves = state.getMoves();
        bestMove = moves.get(0);
        //complete ordering
        /*List<Move> unorderedMoves = state.getMoves();
         Move[] moves = MoveMergeSort.sort(unorderedMoves, historyHeuristic);
         bestMove = moves[0];*/
        //first two nodes ordering
        /*List<Move> unorderedMoves = state.getMoves();
         Move[] moves = unorderedMoves.toArray(new Move[unorderedMoves.size()]);
         bestMove = moves[0];*/

        /*if (moves.length > 1) {
         int eval0 = killHeuristic[depth][moves[0].getBeginField()][moves[0].getEndField()];
         for (int i = 1; i < unorderedMoves.size(); i++) {
         int evali = killHeuristic[depth][moves[i].getBeginField()][moves[i].getEndField()];
         if (eval0 < evali) {
         eval0 = evali;
         Move temp = moves[i];
         moves[i] = moves[0];
         moves[0] = temp;
         }
         }

         int eval1 = killHeuristic[depth][moves[1].getBeginField()][moves[1].getEndField()];
         //get second best local move
         for (int i = 2; i < unorderedMoves.size(); i++) {
         int evali = killHeuristic[depth][moves[i].getBeginField()][moves[i].getEndField()];
         if (eval1 < evali) {
         eval1 = evali;
         Move temp = moves[i];
         moves[i] = moves[1];
         moves[1] = temp;
         }
         }
         }*/
        //get best local move
        for (Move move : moves) {
            long childKey = doMove(state, move, key);

            //recursive boogaloo
            GameNode newNode = new GameNode(state, depth, depthLimit, childKey);
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
                int subTreeDepth = nodeCount - subTreeNodeCountStart;
                //is calling best move here correct?
                node.setBestMove(bestMove);
                TranspositionEntry resultEntry = new TranspositionEntry(depthLimit - depth, maximize ? b : a, subTreeDepth);
                transpositionTable.storeEntry(key, resultEntry);

                historyHeuristic[move.getBeginPiece()][move.getEndField()]++;
                //killHeuristic[depth][move.getBeginPiece()][move.getEndField()]++;

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
        for(int i = 0; i < move.getCaptureCount(); i++) {
            int piece = move.getCapturedPiece(i);
            int field = move.getCapturedField(i);
            transpositionTable.removePiece(key, field, piece);
        }
        return TranspositionTable.doMove(key, move);
    }

    private static class MoveMergeSort {

        private static Move[] moves;
        private static Move[] helper;
        private static int[][] heuristic;

        private static int size;

        public static Move[] sort(List<Move> moveList, int[][] historyHeuristic) {
            heuristic = historyHeuristic;
            size = moveList.size();
            moves = moveList.toArray(new Move[size]);
            helper = new Move[size];
            mergeSort(0, size - 1);
            return moves;
        }

        private static void mergeSort(int low, int high) {
            if (low < high) {
                int middle = low + (high - low) / 2;
                mergeSort(low, middle);
                mergeSort(middle + 1, high);
                merge(low, middle, high);
            }
        }

        private static void merge(int low, int middle, int high) {
            //copy to helper array.
            for (int i = low; i <= high; i++) {
                helper[i] = moves[i];
            }

            int i = low;
            int j = middle + 1;
            int k = low;

            //copy smallest values from the left or the right side to the original array.
            while (i <= middle && j <= high) {
                Move mi = moves[i];
                Move mj = moves[j];
                if (heuristic[mi.getBeginField()][mi.getEndField()] <= heuristic[mj.getBeginField()][mj.getEndField()]) {
                    moves[k] = helper[i];
                    i++;
                } else {
                    moves[k] = helper[j];
                    j++;
                }
                k++;
            }

            //copy remaining part
            while (i <= middle) {
                moves[k] = helper[i];
                k++;
                i++;
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + "pruningWindow=" + pruningWindow + " bounds=" + bounds;
    }
}
