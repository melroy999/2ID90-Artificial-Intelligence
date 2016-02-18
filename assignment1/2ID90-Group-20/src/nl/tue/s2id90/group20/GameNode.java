/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20;

import nl.tue.s2id90.draughts.DraughtsState;
import org10x10.dam.game.Move;

/**
 *
 * @author Melroy
 */
public class GameNode {
    private final DraughtsState gameState;
    private Move bestMove;
    private int value;

    public GameNode(DraughtsState gameState) {
        this.gameState = gameState;
    }
    
    public DraughtsState getGameState() {
        return gameState; 
    }
    
    public void setBestMove(Move move){
        this.bestMove = move;
    }

    public Move getBestMove() {
        return bestMove;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
