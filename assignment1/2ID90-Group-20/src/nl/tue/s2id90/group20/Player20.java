/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.Move;

/**
 *
 * @author Melroy
 */
public class Player20 extends DraughtsPlayer {
    private static final Logger LOG = Logger.getLogger(DraughtsPlayer.class.getName());
    private boolean stopped = false;
    private int value = 0;
    private boolean isWhite = false;
    
    @Override
    public Move getMove(DraughtsState state) {
        isWhite = state.isWhiteToMove();
        GameNode node = new GameNode(state);
        try {
            for (int maxDepth = 1; maxDepth < Integer.MAX_VALUE; maxDepth++) {
                alphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth, true);
            }
        } catch (AIStoppedException ex) {
            
        }
        
        value = node.getValue();
        return node.getBestMove();
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public String getName() {
        return "Player 20";
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void stop() {
        stopped = true;
    }
    
    private int alphaBeta(GameNode node, int a, int b, int depthToGo, boolean maximize) throws AIStoppedException {
        if(stopped){
            stopped = false;
            throw new AIStoppedException();
        }
        
        DraughtsState state = node.getGameState();
        
        if(depthToGo == 0){
            return evaluate(state);
        }
        
        if(state.isEndState()){
            return evaluate(state);
        }
        
        
        List<Move> moves = state.getMoves();
        
        Move bestMove = moves.get(0);
        
        for(Move move : moves){
            state.doMove(move);
            
            //recursive boogaloo
            GameNode newNode = new GameNode(state);
            int childResult = alphaBeta(newNode, a, b, depthToGo - 1, !maximize);
            
            state.undoMove(move);
            
            if(maximize){
                if(childResult > a){
                    a = childResult;
                    node.setValue(a);
                    bestMove = move;
                }
            } else {
                if(childResult < b){
                    b = childResult;
                    node.setValue(b);
                    bestMove = move;
                }
            }
            
            if(a >= b){
                return maximize ? b : a;
            }
        }
        
        node.setBestMove(bestMove);
        return maximize ? a : b;
    }
    
    int evaluate(DraughtsState state) {
        int computedValue = 0;
        int[] pieces = state.getPieces();
        
        for(int piece : pieces){
            if(isWhite){
                switch(piece) {
                    case DraughtsState.WHITEPIECE: computedValue += 1; break;
                    case DraughtsState.WHITEKING: computedValue += 3; break;
                    case DraughtsState.BLACKPIECE: computedValue -= 1; break;
                    case DraughtsState.BLACKKING: computedValue -= 3; break;
                } 
            } else {
                switch(piece) {
                    case DraughtsState.BLACKPIECE: computedValue += 1; break;
                    case DraughtsState.BLACKKING: computedValue += 3; break;
                    case DraughtsState.WHITEPIECE: computedValue -= 1; break;
                    case DraughtsState.WHITEKING: computedValue -= 3; break;
                }
            }
        }
        
        return computedValue;
    }
}
