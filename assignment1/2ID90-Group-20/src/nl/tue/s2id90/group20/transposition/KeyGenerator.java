/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.transposition;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 *
 * @author Melroy
 */
public class KeyGenerator {
    private final static long[][] powerToValue = new long[27][5];
    static {
        for(int i = 0; i < powerToValue.length; i++){
            long powerBase = 1;
            for(int j = 1; j < 5; j++){
                powerToValue[i][j] = j * powerBase;
            }
            powerBase *= 5;
        }
    }
    
    public static long[] stateToKeypair(DraughtsState state){
        int[] pieces = state.getPieces();
        long[] keypair = new long[2];
        
        int key = 0;
        int arrayKey = 0;
        for(int piece : pieces) {
            keypair[arrayKey] ^= powerToValue[key][piece];
            key++;
            if(key > 26){
                arrayKey++;
                key = 0;
            }
        }
        
        keypair[arrayKey] ^= ((state.isWhiteToMove() ? 1L : 0L) << 63);
        
        return keypair;
    }
}
