/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.transposition;

/**
 *
 * @author Melroy
 */
public class TranspositionEntry {
    /*private final TranspositionType type;*/
    private final int depth;
    private final int value;
    private final int nodes;

    public TranspositionEntry(int depth, int value, int nodes) {
        /*this.type = type;*/
        this.depth = depth;
        this.value = value;
        this.nodes = nodes;
    }

    /*public TranspositionType getType() {
        return type;
    }*/

    public int getDepth() {
        return depth;
    }

    public int getValue() {
        return value;
    }

    public int getNodes() {
        return nodes;
    }
}
