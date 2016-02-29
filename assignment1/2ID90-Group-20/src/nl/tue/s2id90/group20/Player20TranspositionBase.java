/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20;

import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import nl.tue.s2id90.group20.transposition.Keypair;
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
    private static final TranspositionTable transpositionTable = new TranspositionTable();

    protected final int pruningWindow;
    protected final int bounds;

    protected long estimatedTime = 0;
    protected int timeSamples = 0;

    /**
     * Not going to list all these parameters.
     */
    public Player20TranspositionBase(int pruningWindow, int bounds,
            int pieceWeight, int kingWeight, int sideWeight,
            int kingLaneWeight, int tandemWeight, int centerWeight,
            int endStateWeight) {
        super(pieceWeight, kingWeight, sideWeight, kingLaneWeight, tandemWeight,
                centerWeight, endStateWeight, true,
                Player20TranspositionBase.class.getResource("resources/book.png"));
        this.pruningWindow = pruningWindow;
        this.bounds = bounds;
    }

    /**
     * Creates a player using transposition.
     *
     * @param pruningWindow: aspiration window.
     * @param bounds: aspiration default bounds.
     * @param icon: icon used.
     */
    public Player20TranspositionBase(int pruningWindow, int bounds, URL icon) {
        super(icon);
        this.pruningWindow = pruningWindow;
        this.bounds = bounds;
    }

    /**
     * Creates a player using transposition.
     *
     * @param pruningWindow: aspiration window.
     * @param bounds: aspiration default bounds.
     */
    public Player20TranspositionBase(int pruningWindow, int bounds) {
        this.pruningWindow = pruningWindow;
        this.bounds = bounds;
    }

    @Override
    public Move getMove(DraughtsState state) {
        //reset stopped, just to be certain.
        stopped = false;

        //create a timer.
        Timer stopBeforeLimit = new Timer();

        //create a stop TimeTask.
        //StopPlayerTimer stopPlayer = new StopPlayerTimer(this);
        //if we have 'reliable' estimations of the timer.
        if (timeSamples > 1) {
            //create a new stop player timer.
            //stopPlayer = new StopPlayerTimer(this);

            //round the time to seconds.
            long stopTime = (long) ((((estimatedTime + 500) / 1000)) * 1000);
            System.out.println("Estimated turn time: " + stopTime);

            //time a garbage collection (just past) halfway our turn.
            stopBeforeLimit.schedule(new GarbageCollectionTimer(),
                    (long) (0.5 * stopTime));

            //previously used to make sure that we never go past the time limit.
            //this cannot be used anymore, as we need full time 
            //stopBeforeLimit.schedule(stopPlayer, stopTime);
        }

        //clear the table, so that we cannot cheat by using previous results in next iteration.
        transpositionTable.clear();

        //request a garbage collection at the start of the iteration.
        //so that garbage collection becomes more predictable.
        System.out.println("Requesting garbage collection at start of move.");
        System.gc();

        //call the getMove of parent, which will initiate the iterative deepening.
        Move bestMove = super.getMove(state);

        //stop the timer.
        stopBeforeLimit.cancel();

        return bestMove;
    }

    @Override
    protected Move iterativeDeepening(DraughtsState state) {
        //get the keypair for the starting state. 
        //only call this here! use move related methods during iteration.
        Keypair key = TranspositionTable.getZobristKeypair(state);

        //set whether are the white player.
        isWhite = state.isWhiteToMove();

        //make a new gamenode, containing information about this state.
        GameNode node = new GameNode(state.clone(), 0, Integer.MAX_VALUE, key);

        //set a temporary best move.
        node.setBestMove(state.getMoves().get(0));
        Move bestMove = node.getBestMove();

        //set the current time in milliseconds, used to determine timeframe.
        long tempTime = System.currentTimeMillis();

        //the starting search depth is 2.
        maxDepth = 2;
        try {
            //Do iterative deepening.
            //we use aspiration techniques to determine the starting alpha and beta.

            int alpha = -bounds;
            int beta = bounds;

            //we do not expect this algorithm to reach depth further than 40...
            //even 25 would be far fetched. But we like 40.
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
                    continue;
                }

                //add the value to the list of values encountered during
                //this iterative deepening.
                values.push(value);

                //we use aspiration techniques to prune more subtrees.
                //we are within the specified window. set the new window
                //for the next iteration.
                alpha = value - pruningWindow;
                beta = value + pruningWindow;

                //increase the depth for next iteration.
                maxDepth++;

                transpositionTable.clearLeafTable();

                //set the best move.
                bestMove = node.getBestMove();
            }

            //we do not estimate the time of the turn if we reach max depth!
            //the time given will be a wrong estimation of reality!
            //print information about the depth reached.
            System.out.println(
                    this.getClass().toString() + " reached end state. Total of " + nodeCount + " nodes, of which we fetched " + fetchCount + ".");
        } catch (AIStoppedException ex) {
            //Stop iterative deepening when exception is thrown.

            //print information about the depth reached.
            System.out.println(
                    this.getClass().toString() + " reached depth " + maxDepth + " with " + nodeCount + " nodes, of which we fetched " + fetchCount + ".");

            //estimate the time of the turn.
            if (estimatedTime == 0) {
                //if we have no data yet, use the first thing found.
                estimatedTime += System.currentTimeMillis() - tempTime;
            } else {
                //else, take the average of previous result, and new result.
                //we do not use the amount of time samples here,
                //as we want the time estimation to quickly adopt to time changes.
                estimatedTime = (estimatedTime + System.currentTimeMillis() - tempTime) / 2;
            }
            timeSamples++;
        }

        return bestMove;
    }

    @Override
    protected int alphaBeta(GameNode node, int a, int b, int depth,
            int depthLimit, boolean maximize) throws AIStoppedException {

        //check if stop has been called.
        if (stopped) {
            //if the stop sign has been given, throw an AI stopped exception.
            stopped = false;
            throw new AIStoppedException();
        }

        //get the entry that is stored in the transposition table.
        Keypair key = node.getKeypair();

        //fetch the entry connected to the keypair.
        TranspositionEntry entry = transpositionTable.fetchEntry(key);

        //check if there exists a result that can be used for this state.
        if (entry != null && entry.getDepth() >= depthLimit - depth) {
            //increase the amount of fetched nodes.
            fetchCount++;

            //increment the nodecount by the size of the subtree.
            nodeCount += entry.getNodes();

            //return the value connected to the state.
            return entry.getValue();
        }

        //increase the count of nodes encountered.
        nodeCount++;

        //get the state from the node.
        DraughtsState state = node.getGameState();

        //check if we reached a leaf, or reached our depth limit.
        if (depth == depthLimit || state.isEndState()) {
            entry = transpositionTable.fetchLeafEntry(key);
            if (entry != null) {
                return entry.getValue();
            } else {
                //evaluate the state.
                int result = evaluate(state);

                //convert this result to a entry that can be used later.
                TranspositionEntry resultEntry = new TranspositionEntry(
                        depthLimit - depth, result, 1);

                //store the entry.
                transpositionTable.storeLeafEntry(key, resultEntry);

                //return the evaluation of this state.
                return result;
            }
        }

        //keep track of where this subtree starts, countwise.
        long subTreeNodeCountStart = nodeCount;

        //get the list of moves, and set a temporary best move.
        List<Move> moves = state.getMoves();
        Move bestMove = moves.get(0);

        //get best move in this subtree
        for (Move move : moves) {
            //do a move.
            /*long childKey = doMove(state, move, key); //for a single key*/
            Keypair childKey = doMove(state, move, key); //for a keypair

            //make a new node, and call this recursively.
            GameNode newNode = new GameNode(state, depth, depthLimit, childKey);
            int childResult = alphaBeta(newNode, a, b, depth + 1, depthLimit,
                    !maximize);

            //undo the latest move.
            state.undoMove(move);

            //if our goal is to maximize at this depth:
            if (maximize) {
                //if the result of the child is better than alpha.
                if (childResult > a) {
                    //set new alpha and bestMove.
                    a = childResult;
                    bestMove = move;
                }
            } else {
                //if the result of the child is better than beta.
                if (childResult < b) {
                    //set new beta and bestMove.
                    b = childResult;
                    bestMove = move;
                }
            }

            //if we can alpha-beta prune.
            if (a >= b) {
                //get the size of the subtree.
                long subTreeDepth = nodeCount - subTreeNodeCountStart;

                //convert this result to a entry that can be used later.
                TranspositionEntry resultEntry = new TranspositionEntry(
                        depthLimit - depth, maximize ? b : a, (int) subTreeDepth);

                //store the entry.
                transpositionTable.storeEntry(key, resultEntry);

                //increase the amount of prunes detected.
                pruneCount++;

                //set the bestmove, and return beta or alpha.
                node.setBestMove(bestMove);
                return maximize ? b : a;
            }
        }

        //get the size of the subtree.
        long subTreeDepth = nodeCount - subTreeNodeCountStart;

        //convert this result to a entry that can be used later.
        TranspositionEntry resultEntry = new TranspositionEntry(
                depthLimit - depth, maximize ? a : b, (int) subTreeDepth);

        //store the entry.
        transpositionTable.storeEntry(key, resultEntry);

        //set the bestmove, and return alpha or beta.
        node.setBestMove(bestMove);
        return maximize ? a : b;
    }

    /**
     * Makes a move, and manages the key connected to the boardstate.
     *
     * @param state: Current state of the board.
     * @param move: Move you want to make.
     * @param key: Current keypair of the state.
     * @return Keypair corresponding to new state.
     */
    public Keypair doMove(DraughtsState state, Move move, Keypair key) {
        //do the move.
        state.doMove(move);
        //let the key do the move.
        return TranspositionTable.doMove(key, move);
    }

    /**
     * TimerTask to trigger stop for this player.
     */
    public class StopPlayerTimer extends TimerTask {

        private final DraughtsPlayer player;

        public StopPlayerTimer(DraughtsPlayer player) {
            this.player = player;
        }

        @Override
        public void run() {
            player.stop();
        }
    }

    /**
     * TimerTask to trigger garbage collection.
     */
    public class GarbageCollectionTimer extends TimerTask {

        @Override
        public void run() {
            System.out.println(
                    "Requesting garbage collection at 50% timemark of this turn.");
            System.gc();
        }
    }

    /**
     * Converts this object to a string.
     *
     * @return this as a string.
     */
    @Override
    public String toString() {
        return super.toString() + "pruningWindow=" + pruningWindow + " bounds=" + bounds;
    }

    /**
     * Returns the name of this player.
     *
     * @return name
     */
    @Override
    public String getName() {
        //we reset timing data in getName(), as it is called whenever
        //a match starts.
        estimatedTime = 0;
        timeSamples = 0;
        return super.getName();
    }

}
