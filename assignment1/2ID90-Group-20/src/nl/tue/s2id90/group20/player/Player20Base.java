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
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import nl.tue.s2id90.group20.AIStoppedException;
import nl.tue.s2id90.group20.GameNode;
import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import nl.tue.s2id90.group20.evaluation.PrioritiseEndstateEvaluation;
import org10x10.dam.game.Move;

/**
 *
 * @author Melroy
 */
public abstract class Player20Base extends DraughtsPlayer {
    protected PrioritiseEndstateEvaluation extraEvaluator;
    
    protected boolean stopped = false;
    protected boolean isWhite = false;

    protected static long timestamp = -1;
    protected final File resultFile;

    protected int value = 0;

    protected int nodeCount = 0;
    protected int fetchCount = 0;
    protected int pruneCount = 0;
    protected Stack<Integer> values;
    protected int maxDepth;

    public Player20Base(String name) {
        if (timestamp == -1) {
            timestamp = System.currentTimeMillis();
        }
        resultFile = new File("results/" + timestamp + "_" + name + ".csv");
    }

    @Override
    public Move getMove(DraughtsState state) {
        initializeFile();

        values = resetResultLoggingValues();

        Move bestMove = iterativeDeepening(state);

        writeResultsToFile(state, values, bestMove, maxDepth);
        return bestMove;
    }

    protected Move iterativeDeepening(DraughtsState state) {
        isWhite = state.isWhiteToMove();
        GameNode node = new GameNode(state.clone(), 0, -1);
        node.setBestMove(state.getMoves().get(0));
        Move bestMove = node.getBestMove();

        try {
            //Do iterative deepening.
            for (maxDepth = 2; maxDepth < 100; maxDepth++) {
                value = alphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, maxDepth, true);

                //as the starting gameNode is altered during runtime,
                //a half completed iteration could mess things up.
                bestMove = node.getBestMove();
                values.push(value);
            }
            System.out.println(this.getClass().toString() + " reached end state. Total of " + nodeCount + " nodes.");
        } catch (AIStoppedException ex) {
            //Stop iterative deepening when exception is thrown.
            System.out.println(this.getClass().toString() + " reached depth " + maxDepth + " with " + nodeCount + " nodes.");
        }
        
        return bestMove;
    }

    protected Stack<Integer> resetResultLoggingValues() {
        nodeCount = 0;
        pruneCount = 0;
        fetchCount = 0;
        Stack<Integer> values = new Stack<>();
        return values;
    }

    protected void writeResultsToFile(DraughtsState state, Stack<Integer> values, Move bestMove, int maxDepth) {
        String playerSide = isWhite ? "White" : "Black";

        int countWhite = 0;
        int countBlack = 0;
        int countWhiteKing = 0;
        int countBlackKing = 0;

        for (int piece : state.getPieces()) {
            if (AbstractEvaluation.isWhite(piece)) {
                countWhite++;
                if (AbstractEvaluation.isWhiteKing(piece)) {
                    countWhiteKing++;
                }
            } else if (AbstractEvaluation.isBlack(piece)) {
                countBlack++;
                if (AbstractEvaluation.isBlackKing(piece)) {
                    countBlackKing++;
                }
            }
        }

        //get the evaluation results
        String evaluationValues = "";
        while (!values.empty()) {
            evaluationValues = values.pop() + " " + evaluationValues;
        }

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(resultFile, true));
            //PlayerSide, Move, Traversed Nodes, Fetched Nodes, Subtrees Pruned, Search Depth, #WP, #BP, #WK, #BK, Evaluation Values
            writer.println(
                    playerSide + ","
                    + bestMove.toString() + ","
                    + nodeCount + ","
                    + fetchCount + "," //we do not store in ordinary version.
                    + pruneCount + ","
                    + (maxDepth - 1) + ","
                    + countWhite + ","
                    + countBlack + ","
                    + countWhiteKing + ","
                    + countBlackKing + ","
                    + "[" + evaluationValues + "]"
            );
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Player20Base.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void initializeFile() {
        if (!resultFile.exists()) {
            try {
                //create result file for this player.
                PrintWriter writer = new PrintWriter(new FileWriter(resultFile, true));
                writer.println("sep=,");
                writer.println(this.toString());
                writer.println("PlayerSide,Move,TraversedNodes,FetchedNodes,SubtreesPruned,SearchDepth,#WP,#BP,#WK,#BK,EvaluationValues");
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Player20Base.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
            throw new AIStoppedException();
        }
        nodeCount++;

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
                pruneCount++;
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

    @Override
    public String toString() {
        String evaluationSettings = "";
        for(AbstractEvaluation evaluator : getEvaluators()){
            evaluationSettings += evaluator.toString();
        }
        String endStateEvaluation = extraEvaluator == null ? "" : extraEvaluator.toString() + " ";
        return evaluationSettings + endStateEvaluation;
    }
}
