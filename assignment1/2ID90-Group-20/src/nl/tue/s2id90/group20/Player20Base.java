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

    //The evaluation method used by the player

    private final List<AbstractEvaluation> evaluators;

    protected boolean stopped = false;
    protected boolean isWhite = false;

    protected static long timestamp = -1;
    protected final File resultFile;

    protected int value = 0;

    protected long nodeCount = 0;
    protected int fetchCount = 0;
    protected int pruneCount = 0;
    protected Stack<Integer> values;
    protected int aspirationFails = 0;
    protected int maxDepth;

    protected final String name;

    //variables to keep track of taken names.
    private static HashSet<String> namesTaken = new HashSet<>();
    private static int duplicateNameCounter = 0;

    /**
     * Create a player using the mountain of parameters.
     */
    public Player20Base(int pieceWeight, int kingWeight, int sideWeight,
            int kingLaneWeight, int tandemWeight, int centerWeight,
            int endStateWeight) {
        this(pieceWeight, kingWeight, sideWeight, kingLaneWeight, tandemWeight,
                centerWeight, endStateWeight, false,
                Player20Base.class.getResource("resources/hourglass.png"));
    }

    /**
     * Create a basic player.
     *
     * @param icon: Icon of the player.
     */
    public Player20Base(URL icon) {
        super(icon);
        this.evaluators = new ArrayList<>();
        this.name = this.getClass().getSimpleName();
        this.resultFile = new File(
                "results/" + timestamp + "_" + this.getName() + ".csv");
    }

    /**
     * Create a basic player.
     */
    public Player20Base() {
        this(Player20Base.class.getResource("resources/hourglass.png"));
    }

    /**
     * Create a player using the mountain of parameters, and determine the name.
     */
    public Player20Base(int pieceWeight, int kingWeight, int sideWeight,
            int kingLaneWeight, int tandemWeight, int centerWeight,
            int endStateWeight, boolean isTransposition, URL url) {
        super(url);
        if (timestamp == -1) {
            timestamp = System.currentTimeMillis();
        }

        this.evaluators = new ArrayList<>();

        String temp = "Player20";

        //based on the parameters given, determine what evaluations are desired.
        if (pieceWeight != -1) {
            addEvaluator(new CountPiecesEvaluation(pieceWeight));
            temp += "#CPE";
        }
        if (kingWeight != -1) {
            addEvaluator(new CountCrownPiecesEvaluation(kingWeight));
            temp += "#CCPE";
        }
        if (sideWeight != -1 && kingLaneWeight != -1) {
            addEvaluator(new BorderPiecesEvaluation(sideWeight, kingLaneWeight));
            temp += "#BPE";
        }
        if (tandemWeight != -1) {
            addEvaluator(new TandemEvaluation(tandemWeight));
            temp += "#TE";
        }
        if (centerWeight != -1) {
            addEvaluator(new CenterEvaluation(centerWeight));
            temp += "#CE";
        }
        if (endStateWeight != -1) {
            addEvaluator(new PrioritiseEndstateEvaluation(endStateWeight));
            temp += "#PEE";
        }
        if (isTransposition) {
            temp += "#TR";
        }

        //if name is taken, add a number to it!
        if (namesTaken.contains(temp)) {
            temp += "#" + duplicateNameCounter++;
        }
        namesTaken.add(temp);

        //set the actual name.
        this.name = temp;

        //attach a result file, in which runtime data can be found.
        this.resultFile = new File(
                "results/" + timestamp + "_" + this.getName() + ".csv");
    }

    //variable that keeps track of time.
    protected long time;

    /**
     * Get the move this player desires.
     *
     * @param state: Current state.
     * @return best move as determined by this player.
     */
    @Override
    public Move getMove(DraughtsState state) {
        //initialize the logging file.
        initializeFile();

        //set current time.
        time = System.currentTimeMillis();

        //reset logging values.
        values = resetResultLoggingValues();

        //get the best move by iterative deepening.
        Move bestMove = iterativeDeepening(state);

        //set the time taken.
        time = System.currentTimeMillis() - time;

        //write the results and return best move.
        writeResultsToFile(state, values, bestMove, maxDepth);
        return bestMove;
    }

    /**
     * Iterative deepening algorithm.
     *
     * @param state: current state.
     * @return best move as determined by the iterative deepening algorithm.
     */
    protected Move iterativeDeepening(DraughtsState state) {
        //set whether this player is white.
        isWhite = state.isWhiteToMove();

        //create a gamenode.
        GameNode node = new GameNode(state.clone(), 0, -1);

        //set temporal move.
        node.setBestMove(state.getMoves().get(0));
        Move bestMove = node.getBestMove();

        try {
            //Do iterative deepening.
            for (maxDepth = 2; maxDepth < 40; maxDepth++) {
                //get best evaluation for given depth.
                value = alphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 1,
                        maxDepth, true);

                //as the starting gameNode is altered during runtime,
                //a half completed iteration could mess things up.
                bestMove = node.getBestMove();

                //push the value to a stack for logging purposes.
                values.push(value);
            }

            //report about iterative deepening.
            /*System.out.println(
                    this.getName() + " reached end state. Total of " + nodeCount + " nodes.");*/
        } catch (AIStoppedException ex) {
            //Stop iterative deepening when exception is thrown.

            //report about iterative deepening.
            /*System.out.println(
                    this.getName() + " reached depth " + maxDepth + " with " + nodeCount + " nodes.");*/
        }

        //return best move.
        return bestMove;
    }

    /**
     * Reset all logging variables.
     *
     * @return return empty stack of integers.
     */
    protected Stack<Integer> resetResultLoggingValues() {
        nodeCount = 0;
        pruneCount = 0;
        fetchCount = 0;
        aspirationFails = 0;
        Stack<Integer> values = new Stack<>();
        return values;
    }

    /**
     * Write information about the state and move.
     *
     * @param state: Current state of the game.
     * @param values: Values encountered.
     * @param bestMove: Move chosen.
     * @param maxDepth: Depth reached.
     */
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
            PrintWriter writer = new PrintWriter(
                    new FileWriter(resultFile, true));
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
                    + "[" + evaluationValues + "],"
                    + time
            );
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initialize the logging file.
     */
    protected void initializeFile() {
        if (!resultFile.exists()) {
            try {
                //create result file for this player.
                PrintWriter writer = new PrintWriter(new FileWriter(resultFile,
                        true));
                writer.println("sep=,");
                writer.println(this.toString());
                writer.println(
                        "PlayerSide,Move,TraversedNodes,FetchedNodes,SubtreesPruned,SearchDepth,AspirationFails,#WP,#BP,#WK,#BK,EvaluationValues");
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Get current expectation value of the player.
     *
     * @return the value.
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * Stop the player.
     */
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

        //count the amount of nodes encountered.
        nodeCount++;

        //get the current state.
        DraughtsState state = node.getGameState();

        //check if we are at a leaf or at max depth in the tree.
        if (depth > depthLimit || state.isEndState()) {
            return evaluate(state);
        }

        //get moves, and set temporal best one.
        List<Move> moves = state.getMoves();
        Move bestMove = moves.get(0);

        //iterate over all moves.
        for (Move move : moves) {
            //do the move.
            state.doMove(move);

            //make new node.
            GameNode newNode = new GameNode(state, depth, depthLimit);

            //recursively call alphaBeta.
            int childResult = alphaBeta(newNode, a, b, depth + 1, depthLimit,
                    !maximize);

            //undo the move.
            state.undoMove(move);

            //if our goal is to maximize.
            if (maximize) {
                //if alpha is better than previous value.
                if (childResult > a) {
                    //set alpha and bestMove.
                    a = childResult;
                    bestMove = move;
                }
            } else {
                if (childResult < b) {
                    //set beta and bestMove.
                    b = childResult;
                    bestMove = move;
                }
            }

            //if we are able to prune.
            if (a >= b) {
                //increment prune count.
                pruneCount++;
                
                //set new best move and return beta or alpha.
                node.setBestMove(bestMove);
                return maximize ? b : a;
            }
        }

        //set best move and return alpha or beta.
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
        
        //evaluate using all evaluation functions.
        for (AbstractEvaluation evaluator : evaluators) {
            result += evaluator.evaluate(state, isWhite);
        }
        return result;
    }

    /**
     * Converts this player to a string representation.
     * 
     * @return String having information about evaluation functions.
     */
    @Override
    public String toString() {
        String evaluationSettings = "";
        //generate string based on evaluation functions.
        for (AbstractEvaluation evaluator : evaluators) {
            evaluationSettings += evaluator.toString();
        }
        return evaluationSettings;
    }

    /**
     * Returns the name of this player.
     * 
     * @return name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Add evaluation function to evaluations.
     * 
     * @param a: desired evaluation functions.
     */
    public void addEvaluator(AbstractEvaluation a) {
        evaluators.add(a);
    }
}
