/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20;

/**
 *
 * @author Melroy
 */
public class ResultNode {
    private int evaluativeValue = 0;
    private final int subTreeDepth;
    private final boolean isWhiteTurn;

    public ResultNode(int subTreeDepth, boolean isWhiteTurn) {
        this.subTreeDepth = subTreeDepth;
        this.isWhiteTurn = isWhiteTurn;
    }

    public int getEvaluativeValue() {
        return evaluativeValue;
    }

    public void setEvaluativeValue(int evaluativeValue) {
        this.evaluativeValue = evaluativeValue;
    }

    public int getSubTreeDepth() {
        return subTreeDepth;
    }

    public boolean isIsWhiteTurn() {
        return isWhiteTurn;
    }
}
