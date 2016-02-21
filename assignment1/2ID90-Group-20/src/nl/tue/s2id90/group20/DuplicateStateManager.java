/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20;

import java.util.HashMap;
import nl.tue.s2id90.draughts.DraughtsState;

/**
 *
 * @author Melroy
 */
public class DuplicateStateManager {
    private final HashMap<Long, HashMap<Long, HashMap<Long,GameNode>>> processedNodes;

    public DuplicateStateManager() {
        processedNodes = new HashMap<>();
    }
    
    public static long[] convertBoardState(DraughtsState bs) {
        long[] longs = new long[3];
        int[] pieces = bs.getPieces();
        int indexL = 0;
        int indexA = 0;
        for (int piece = 0; piece < pieces.length; piece++) {
            longs[indexA] |= ((long) piece) << indexL;
            indexL += 3;
            if (indexL > 60) {
                indexL = 0;
                indexA++;
            }
        }
        
        longs[indexA] |= ((long) (bs.isWhiteToMove() ? 1 : 0)) << indexL;
        
        return longs;
    }
    
    /**
     * Store a node in the duplicate list. 
     * 
     * @param node: node to store. 
     */
    public void storeGameNode(GameNode node){
        long[] key = convertBoardState(node.getGameState());
        
        HashMap<Long, HashMap<Long,GameNode>> intermediate = processedNodes.get(key[0]);
        if(intermediate == null){
            intermediate = new HashMap<>();
        }
        
        HashMap<Long,GameNode> intermediate2 = intermediate.get(key[1]);
        if(intermediate2 == null){
            intermediate2 = new HashMap<>();
        }
        
        intermediate2.put(key[2], node);
        intermediate.put(key[1], intermediate2);
        processedNodes.put(key[0], intermediate);
    }
    
    /**
     * Check if a node with the same properties is present in the duplicate list.
     * 
     * @param node: GameNode for which you want to find a duplicate.
     * @return if the node has a deeper search, it returns the node. Else it returns null.
     */
    public GameNode getGameNode(GameNode node){
        long[] key = convertBoardState(node.getGameState());
        
        HashMap<Long, HashMap<Long,GameNode>> intermediate = processedNodes.get(key[0]);
        if(intermediate != null){
            HashMap<Long,GameNode> intermediate2 = intermediate.get(key[1]);
            if(intermediate2 != null){
                GameNode foundNode = intermediate2.get(key[2]);
                if(foundNode != null && foundNode.getDepth() >= node.getDepth()){
                    // only return a node if you reach the max depth with the subtree
                    // that the node creates. As getDepth returns the remaining depth 
                    // that has to be traversed, only return the foundNode if 
                    // its remaining depth is larger as the given node, as it would
                    // go deeper than the maximum depth.
                    return foundNode;
                }
            } 
        }
        return null;
    }
}
