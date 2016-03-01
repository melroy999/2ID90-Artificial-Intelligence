package nl.tue.s2id90.group20.evaluation;

import java.util.ArrayList;
import nl.tue.s2id90.draughts.DraughtsState;

/**
 * Main class used for evaluation of states.
 */
public class OmniEvaluation extends AbstractEvaluation {

    private final int unoccupiedPromotionLineFieldsWeight;
    private final int pawnWeight;
    private final int kingWeight;
    private final int safePawnWeight;
    private final int safeKingWeight;
    private final int aggregatedDistanceOfPawnToPromotionLineWeight;

    private final int defenderPieceWeight;
    private final int attackPawnWeight;
    private final int centerPawnWeight;
    private final int centerKingWeight;
    private final int mainDiagonalPawnWeight;
    private final int mainDiagonalKingWeight;
    private final int doubleDiagonalPawnWeight;
    private final int doubleDiagonalKingWeight;

    /**
     * Create an omni evaluator. With a lot of parameters...
     *
     * @param unoccupiedPromotionLineFieldsWeight
     * @param pawnWeight
     * @param kingWeight
     * @param safePawnWeight
     * @param safeKingWeight
     * @param aggregatedDistanceOfPawnToPromotionLineWeight
     * @param defenderPieceWeight
     * @param attackPawnWeight
     * @param centerPawnWeight
     * @param centerKingWeight
     * @param mainDiagonalPawnWeight
     * @param mainDiagonalKingWeight
     * @param doubleDiagonalPawnWeight
     * @param doubleDiagonalKingWeight
     */
    public OmniEvaluation(int unoccupiedPromotionLineFieldsWeight,
            int pawnWeight, int kingWeight, int safePawnWeight,
            int safeKingWeight,
            int aggregatedDistanceOfPawnToPromotionLineWeight,
            int defenderPieceWeight, int attackPawnWeight, int centerPawnWeight,
            int centerKingWeight, int mainDiagonalPawnWeight,
            int mainDiagonalKingWeight, int doubleDiagonalPawnWeight,
            int doubleDiagonalKingWeight) {

        //promotionLinesEmpty 0 - 5
        //pawns 0 - 20
        //kings 0 - 20
        //safe 0 - 18
        //pawnDistance (0 - 5 * 9 + 5 * 8 + 5 * 7 + 5 * 6) / pieces (self) do seperate max 9
        //defenders 0 - 15
        //attack 0 - 20
        //center 0 - 10
        //diagonal 0 - 10
        //diagonal2 0 - 18
        this.unoccupiedPromotionLineFieldsWeight = unoccupiedPromotionLineFieldsWeight * 4;
        this.pawnWeight = pawnWeight * 1;
        this.kingWeight = kingWeight * 1;
        this.safePawnWeight = safePawnWeight * 1;
        this.safeKingWeight = safeKingWeight * 2;
        this.aggregatedDistanceOfPawnToPromotionLineWeight = aggregatedDistanceOfPawnToPromotionLineWeight * 2;
        this.defenderPieceWeight = defenderPieceWeight * 1;//
        this.attackPawnWeight = attackPawnWeight * 1;
        this.centerPawnWeight = centerPawnWeight * 2;
        this.centerKingWeight = centerKingWeight * 2;
        this.mainDiagonalPawnWeight = mainDiagonalPawnWeight * 2;
        this.mainDiagonalKingWeight = mainDiagonalKingWeight * 2;
        this.doubleDiagonalPawnWeight = doubleDiagonalPawnWeight;
        this.doubleDiagonalKingWeight = doubleDiagonalKingWeight;
    }

    private final static boolean[] isEdge = new boolean[51];
    private final static int[] whiteDistanceToPromotion = new int[51];
    private final static int[] blackDistanceToPromotion = new int[51];
    private final static boolean[] isWhiteDefender = new boolean[51];
    private final static boolean[] isBlackDefender = new boolean[51];
    private final static boolean[] isWhitePawnAttacker = new boolean[51];
    private final static boolean[] isBlackPawnAttacker = new boolean[51];
    private final static boolean[] isCenterPosition = new boolean[51];
    private final static boolean[] isOnMainDiagonal = new boolean[51];
    private final static boolean[] isOnDoubleDiagonal = new boolean[51];
    private final static ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>(
            51);

    static {
        neighbours.add(new ArrayList<>());
        for (int i = 1; i < 51; i++) {
            isEdge[i] = i < 6 || i > 45 || i % 10 == 6 || i % 10 == 5;
            whiteDistanceToPromotion[i] = (50 - i) / 5;
            blackDistanceToPromotion[i] = (i - 1) / 5;
            isWhiteDefender[i] = i > 35;
            isBlackDefender[i] = i < 16;
            isWhitePawnAttacker[i] = i < 21;
            isBlackPawnAttacker[i] = i > 30;
            isCenterPosition[i] = i > 20 && i < 31;
            isOnMainDiagonal[i] = i == 46 || i == 41 || i == 37
                    || i == 32 || i == 28 || i == 23 || i == 19
                    || i == 14 || i == 10 || i == 5;
            isOnDoubleDiagonal[i] = i == 1 || i == 6 || i == 7 || i == 11
                    || i == 12 || i == 17 || i == 18 || i == 22
                    || i == 23 || i == 28 || i == 29 || i == 33
                    || i == 34 || i == 39 || i == 40 || i == 44
                    || i == 45 || i == 50;

            ArrayList<Integer> neighbour = new ArrayList<>(4);
            if ((i - 1) % 10 < 5) {
                //rows 1 - 5, 11 - 15 etc

                //always query bottom left.
                neighbour.add(i + 5);

                //can always query top left, except for top row.
                if (i > 5) {
                    neighbour.add(i - 5);
                }

                //right may only be queried if i != 5,15,25,35,45
                if (i % 10 != 5) {
                    //always query bottom right
                    neighbour.add(i + 6);

                    //can always query top right, except for top row.
                    if (i > 5) {
                        neighbour.add(i - 4);
                    }
                }
            } else {
                //rows 6 - 10, 16 - 20 etc

                //always query top right
                neighbour.add(i - 5);

                //can always query right, except for bottom row.
                if (i < 46) {
                    neighbour.add(i + 5);
                }

                //left may only be queried if i != 6,16,26,36,46
                if (i % 10 != 6) {
                    //always query top left
                    neighbour.add(i - 6);

                    //can always query right, except for bottom row.
                    if (i < 46) {
                        neighbour.add(i + 4);
                    }
                }
            }

            neighbours.add(neighbour);
        }
    }

    /**
     * Evaluate the state of the board by using the pieces on the field, and the
     * side that makes the current move.
     *
     * @param pieces: List of pieces on the board, in the order specified in the
     * problem description.
     * @param isWhitePlayer: Whether white makes the current move or not.
     * @return Evaluative value for the current state of the board.
     */
    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer
    ) {
        //all the following values are differences, with the positive side being white.
        int promotionLinesEmpty = 0;//
        int pawns = 0;//
        int kings = 0;//
        int blackPieces = 0;
        int whitePieces = 0;
        int safePawns = 0;//
        int safeKings = 0;//
        int whitePawnDistanceToPromotion = 0;//
        int blackPawnDistanceToPromotion = 0;//

        int defenders = 0;//
        int attackPawns = 0;//
        int centerPawns = 0;//
        int centerKings = 0;//
        int mainDiagonalPawns = 0;//
        int mainDiagonalKings = 0;//
        int doubleDiagonalPawns = 0;//
        int doubleDiagonalKings = 0;//     

        for (int i = 1; i < pieces.length; i++) {
            int piece = pieces[i];
            if (piece == DraughtsState.EMPTY) {
                if (i < 6) {
                    //if black promotion line fields are empty.
                    promotionLinesEmpty++;
                }
                if (i > 45) {
                    //if white promotion line fields are empty.
                    promotionLinesEmpty--;
                }
            } else if ((piece & 1) == 1) {
                whitePieces++;
                //if piece id is even, it is either white.
                if (piece == DraughtsState.WHITEPIECE) {
                    //if it is a white pawn.
                    pawns++;
                    if (isEdge[i]) {
                        //if we are at the edge of the board.
                        safePawns++;
                    }
                    whitePawnDistanceToPromotion += whiteDistanceToPromotion[i];
                    if (isWhitePawnAttacker[i]) {
                        //if this pawn is in the starting territory of the opponent.
                        attackPawns++;
                    }
                    if (isCenterPosition[i]) {
                        //if this pawn is in the empty starting area.
                        centerPawns++;
                    }
                    if (isOnMainDiagonal[i]) {
                        //if this piece is on the main diagonal.
                        mainDiagonalPawns++;
                    }
                    if (isOnDoubleDiagonal[i]) {
                        //if this piece is on the double diagonal.
                        doubleDiagonalPawns++;
                    }
                } else {
                    //if it is a white king.
                    kings++;
                    if (isEdge[i]) {
                        //if we are at the edge of the board.
                        safeKings++;
                    }
                    if (isCenterPosition[i]) {
                        //if this king is in the empty starting area.
                        centerKings++;
                    }
                    if (isOnMainDiagonal[i]) {
                        //if this piece is on the main diagonal.
                        mainDiagonalKings++;
                    }
                    if (isOnDoubleDiagonal[i]) {
                        //if this piece is on the double diagonal.
                        doubleDiagonalKings++;
                    }
                }
                if (isWhiteDefender[i]) {
                    //if this piece is on the last 3 rows of his side.
                    defenders++;
                }
                if (i < 6) {
                    //if black promotion line fields are empty.
                    promotionLinesEmpty++;
                }
            } else {
                blackPieces++;
                //as empty is not considered here, it has to be black.
                if (piece == DraughtsState.BLACKPIECE) {
                    //if it is a black pawn.
                    pawns--;
                    if (isEdge[i]) {
                        //if we are at the edge of the board.
                        safePawns--;
                    }
                    //+ instead of -, as we desire the complete opposite effect.
                    blackPawnDistanceToPromotion += blackDistanceToPromotion[i];
                    if (isBlackPawnAttacker[i]) {
                        //if this pawn is in the starting territory of the opponent.
                        attackPawns--;
                    }
                    if (isCenterPosition[i]) {
                        //if this pawn is in the empty starting area.
                        centerPawns--;
                    }
                    if (isOnMainDiagonal[i]) {
                        //if this piece is on the main diagonal.
                        mainDiagonalPawns--;
                    }
                    if (isOnDoubleDiagonal[i]) {
                        //if this piece is on the double diagonal.
                        doubleDiagonalPawns--;
                    }
                } else {
                    //if it is a black king.
                    kings--;
                    if (isEdge[i]) {
                        //if we are at the edge of the board.
                        safeKings--;
                    }
                    if (isCenterPosition[i]) {
                        //if this king is in the empty starting area.
                        centerPawns--;
                    }
                    if (isOnMainDiagonal[i]) {
                        //if this piece is on the main diagonal.
                        mainDiagonalKings--;
                    }
                    if (isOnDoubleDiagonal[i]) {
                        //if this piece is on the double diagonal.
                        doubleDiagonalKings--;
                    }
                }
                if (isBlackDefender[i]) {
                    //if this piece is on the last 3 rows of his side.
                    defenders--;
                }
                if (i > 45) {
                    //if white promotion line fields are empty.
                    promotionLinesEmpty--;
                }
            }
        }

        int pawnDistanceToPromotion = blackPawnDistanceToPromotion / (blackPieces + 1) - whitePawnDistanceToPromotion / (whitePieces + 1);

        int evaluation
                = promotionLinesEmpty * unoccupiedPromotionLineFieldsWeight
                + pawns * pawnWeight
                + kings * kingWeight
                + safePawns * safePawnWeight
                + safeKings * safeKingWeight
                + pawnDistanceToPromotion * aggregatedDistanceOfPawnToPromotionLineWeight
                + defenders * defenderPieceWeight
                + attackPawns * attackPawnWeight
                + centerPawns * centerPawnWeight
                + centerKings * centerKingWeight
                + mainDiagonalPawns * mainDiagonalPawnWeight
                + mainDiagonalKings * mainDiagonalKingWeight
                + doubleDiagonalPawns * doubleDiagonalPawnWeight
                + doubleDiagonalKings * doubleDiagonalKingWeight;

        //promotionLinesEmpty 0 - 5
        //pawns 0 - 20
        //kings 0 - 20
        //safe 0 - 18
        //pawnDistance (0 - 5 * 9 + 5 * 8 + 5 * 7 + 5 * 6) / pieces (self) do seperate
        //defenders 0 - 15
        //attack 0 - 20
        //center 0 - 10
        //diagonal 0 - 10
        //diagonal2 0 - 18
        return isWhitePlayer ? evaluation : -evaluation;
    }

    /**
     * Name of the evaluation function.
     *
     * @return String with evaluation settings.
     */
    @Override
    public String toString() {
        return "unoccupiedPromotionLineFieldsWeight=" + unoccupiedPromotionLineFieldsWeight + " pawnWeight=" + pawnWeight + " kingWeight=" + kingWeight + " safePawnWeight=" + safePawnWeight + " safeKingWeight=" + safeKingWeight + " aggregatedDistanceOfPawnToPromotionLineWeight=" + aggregatedDistanceOfPawnToPromotionLineWeight + " defenderPieceWeight=" + defenderPieceWeight + " attackPawnWeight=" + attackPawnWeight + " centerPawnWeight=" + centerPawnWeight + " centerKingWeight=" + centerKingWeight + " mainDiagonalPawnWeight=" + mainDiagonalPawnWeight + " mainDiagonalKingWeight=" + mainDiagonalKingWeight + " doubleDiagonalPawnWeight=" + doubleDiagonalPawnWeight + " doubleDiagonalKingWeight=" + doubleDiagonalKingWeight + " ";
    }

}
