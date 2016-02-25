/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.evaluation;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 *
 * @author Melroy
 */
public class TandemEvaluation extends AbstractEvaluation {

    private final int tandemWeight;

    public TandemEvaluation(int tandemWeight) {
        this.tandemWeight = tandemWeight;
    }

    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer) {
        //check neighbouring fields for friendly pieces.
        int value = 0;

        for (int i = 1; i < pieces.length; i++) {
            int piece = pieces[i];

            boolean isWhitePiece = isWhite(piece);

            if (piece != DraughtsState.EMPTY) {
                if ((i - 1) % 10 < 5) {
                    //rows 1 - 5, 11 - 15 etc

                    //always query bottom left.
                    value += evaluateNeighbour(pieces[i + 5], isWhitePiece, piece, isWhitePlayer);

                    //can always query top left, except for top row.
                    if (i > 5) {
                        value += evaluateNeighbour(pieces[i - 5], isWhitePiece, piece, isWhitePlayer);
                    }

                    //right may only be queried if i != 5,15,25,35,45
                    if (i % 10 != 5) {
                        //always query bottom right
                        value += evaluateNeighbour(pieces[i + 6], isWhitePiece, piece, isWhitePlayer);

                        //can always query top right, except for top row.
                        if (i > 5) {
                            value += evaluateNeighbour(pieces[i - 4], isWhitePiece, piece, isWhitePlayer);
                        }
                    }
                } else {
                    //rows 6 - 10, 16 - 20 etc

                    //always query top right
                    value += evaluateNeighbour(pieces[i - 5], isWhitePiece, piece, isWhitePlayer);

                    //can always query right, except for bottom row.
                    if (i < 46) {
                        value += evaluateNeighbour(pieces[i + 5], isWhitePiece, piece, isWhitePlayer);
                    }

                    //left may only be queried if i != 6,16,26,36,46
                    if (i % 10 != 6) {
                        //always query top left
                        value += evaluateNeighbour(pieces[i - 6], isWhitePiece, piece, isWhitePlayer);

                        //can always query right, except for bottom row.
                        if (i < 46) {
                            value += evaluateNeighbour(pieces[i + 4], isWhitePiece, piece, isWhitePlayer);
                        }
                    }
                }
            }
        }
        return value;
    }

    private int evaluateNeighbour(int neighbour, boolean isWhitePiece, int piece,
            boolean isWhitePlayer) {
        if (neighbour != DraughtsState.EMPTY && isWhitePiece == isWhite(neighbour)) {
            //if the piece is of the same color as the target.
            return evaluateSide(piece, isWhitePlayer, tandemWeight);
        }
        return 0;
    }

    @Override
    public String toString() {
        return "tandemWeight=" + tandemWeight + " ";
    }

}
