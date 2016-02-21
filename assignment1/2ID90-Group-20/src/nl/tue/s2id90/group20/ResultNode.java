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
    private int value = 0;
    private final int subTreeDepth;

    public ResultNode(int subTreeDepth) {
        this.subTreeDepth = subTreeDepth;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSubTreeDepth() {
        return subTreeDepth;
    }
    
    public ResultNode clone(){
        ResultNode clone = new ResultNode(subTreeDepth);
        clone.setValue(value);
        return clone;
    }

    @Override
    public String toString() {
        return "ResultNode{" + "value=" + value + ", subTreeDepth=" + subTreeDepth + '}';
    }
}
