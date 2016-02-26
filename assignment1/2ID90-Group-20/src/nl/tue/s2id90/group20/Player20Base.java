/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20;

import nl.tue.s2id90.group20.UninformedPlayer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import nl.tue.s2id90.group20.AIStoppedException;
import nl.tue.s2id90.group20.GameNode;
import nl.tue.s2id90.group20.evaluation.AbstractEvaluation;
import nl.tue.s2id90.group20.evaluation.BorderPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CenterEvaluation;
import nl.tue.s2id90.group20.evaluation.CountCrownPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.CountPiecesEvaluation;
import nl.tue.s2id90.group20.evaluation.PrioritiseEndstateEvaluation;
import nl.tue.s2id90.group20.evaluation.TandemEvaluation;
import org10x10.dam.game.Move;

/**
 *
 * @author Melroy
 */
public class Player20Base extends DraughtsPlayer {

    protected final List<AbstractEvaluation> evaluators;//The evaluation method used by the player

    protected boolean stopped = false;
    protected boolean isWhite = false;

    protected static long timestamp = -1;
    protected final File resultFile;

    protected int value = 0;

    protected int nodeCount = 0;
    protected int fetchCount = 0;
    protected int pruneCount = 0;
    protected Stack<Integer> values;
    protected int aspirationFails = 0;
    protected int maxDepth;
    
    protected final String name;
    
    private static HashSet<String> namesTaken = new HashSet<>();
    private static int duplicateNameCounter = 0;

    public Player20Base(int pieceWeight, int kingWeight, int sideWeight,
            int kingLaneWeight, int tandemWeight, int centerWeight,
            int endStateWeight) {
        this(pieceWeight, kingWeight, sideWeight, kingLaneWeight, tandemWeight, centerWeight, endStateWeight, false, Player20Base.class.getResource("resources/hourglass.png"));
    }
    
    public Player20Base(int pieceWeight, int kingWeight, int sideWeight,
            int kingLaneWeight, int tandemWeight, int centerWeight,
            int endStateWeight, boolean isTransposition, URL url) {
        super(url);
        if (timestamp == -1) {
            timestamp = System.currentTimeMillis();
        }
        
        String temp = "Player20";
        
        evaluators = new ArrayList<>();
        if(pieceWeight != -1){
            evaluators.add(new CountPiecesEvaluation(pieceWeight));
            temp += "#CPE";
        }
        if(kingWeight != -1){
            evaluators.add(new CountCrownPiecesEvaluation(pieceWeight));
            temp += "#CCPE";
        }
        if(sideWeight != -1 && kingLaneWeight != -1){
            evaluators.add(new BorderPiecesEvaluation(sideWeight, kingLaneWeight));
            temp += "#BPE";
        }
        if(tandemWeight != -1){
            evaluators.add(new TandemEvaluation(tandemWeight));
            temp += "#TE";
        }
        if(centerWeight != -1){
            evaluators.add(new CenterEvaluation(centerWeight));
            temp += "#CE";
        }
        if(endStateWeight != -1){
            evaluators.add(new PrioritiseEndstateEvaluation(endStateWeight));
            temp += "#PEE";
        }
        if(isTransposition){
            temp += "#TR";
        }
        
        if(namesTaken.contains(temp)){
            temp += "#" + duplicateNameCounter++;
        }
        
        namesTaken.add(temp);
        
        this.name = temp;
        
        resultFile = new File("results/" + timestamp + "_" + this.getName() + ".csv");
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
            System.out.println(this.getName() + " reached end state. Total of " + nodeCount + " nodes.");
        } catch (AIStoppedException ex) {
            //Stop iterative deepening when exception is thrown.
            System.out.println(this.getName() + " reached depth " + maxDepth + " with " + nodeCount + " nodes.");
        }

        return bestMove;
    }

    protected Stack<Integer> resetResultLoggingValues() {
        nodeCount = 0;
        pruneCount = 0;
        fetchCount = 0;
        aspirationFails = 0;
        Stack<Integer> values = new Stack<>();
        return values;
    }

    protected void writeResultsToFile(DraughtsState state, Stack<Integer> values,
            Move bestMove, int maxDepth) {
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
                    + aspirationFails + ","
                    + countWhite + ","
                    + countBlack + ","
                    + countWhiteKing + ","
                    + countBlackKing + ","
                    + "[" + evaluationValues + "]"
            );
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void initializeFile() {
        if (!resultFile.exists()) {
            try {
                //create result file for this player.
                PrintWriter writer = new PrintWriter(new FileWriter(resultFile, true));
                writer.println("sep=,");
                writer.println(this.toString());
                writer.println("PlayerSide,Move,TraversedNodes,FetchedNodes,SubtreesPruned,SearchDepth,AspirationFails,#WP,#BP,#WK,#BK,EvaluationValues");
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
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
    protected int alphaBeta(GameNode node, int a, int b, int depth,
            int depthLimit, boolean maximize) throws AIStoppedException {
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

    /**
     * Evaluate the state of the board.
     *
     * @param state: The state that has to be evaluated.
     * @return An integer value denoting an evaluation.
     */
    public int evaluate(DraughtsState state) {
        int result = 0;
        for (AbstractEvaluation evaluator : evaluators) {
            result += evaluator.evaluate(state, isWhite);
        }
        return result;
    }

    @Override
    public String toString() {
        String evaluationSettings = "";
        for (AbstractEvaluation evaluator : evaluators) {
            evaluationSettings += evaluator.toString();
        }
        return evaluationSettings;
    }

    @Override
    public String getName() {
        return name;
    }
}
