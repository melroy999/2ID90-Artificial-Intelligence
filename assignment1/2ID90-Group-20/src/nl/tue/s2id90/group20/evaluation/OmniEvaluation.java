package nl.tue.s2id90.group20.evaluation;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 *
 * @author Melroy
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
    
    private final int lonerPawnWeight;
    private final int lonerKingWeight;
    private final int holeWeight;

    public OmniEvaluation(int unoccupiedPromotionLineFieldsWeight,
            int pawnWeight, int kingWeight, int safePawnWeight,
            int safeKingWeight,
            int aggregatedDistanceOfPawnToPromotionLineWeight,
            int defenderPieceWeight, int attackPawnWeight, int centerPawnWeight,
            int centerKingWeight, int mainDiagonalPawnWeight,
            int mainDiagonalKingWeight, int doubleDiagonalPawnWeight,
            int doubleDiagonalKingWeight, int lonerPawnWeight,
            int lonerKingWeight, int holeWeight) {
        this.unoccupiedPromotionLineFieldsWeight = unoccupiedPromotionLineFieldsWeight;
        this.pawnWeight = pawnWeight;
        this.kingWeight = kingWeight;
        this.safePawnWeight = safePawnWeight;
        this.safeKingWeight = safeKingWeight;
        this.aggregatedDistanceOfPawnToPromotionLineWeight = aggregatedDistanceOfPawnToPromotionLineWeight;
        this.defenderPieceWeight = defenderPieceWeight;
        this.attackPawnWeight = attackPawnWeight;
        this.centerPawnWeight = centerPawnWeight;
        this.centerKingWeight = centerKingWeight;
        this.mainDiagonalPawnWeight = mainDiagonalPawnWeight;
        this.mainDiagonalKingWeight = mainDiagonalKingWeight;
        this.doubleDiagonalPawnWeight = doubleDiagonalPawnWeight;
        this.doubleDiagonalKingWeight = doubleDiagonalKingWeight;
        this.lonerPawnWeight = lonerPawnWeight;
        this.lonerKingWeight = lonerKingWeight;
        this.holeWeight = holeWeight;
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

    static {
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
        }
    }

    @Override
    public int evaluate(int[] pieces, boolean isWhitePlayer) {
        //all the following values are differences, with the positive side being white.
        int promotionLinesEmpty = 0;//
        int pawns = 0;//
        int kings = 0;//
        int safePawns = 0;//
        int safeKings = 0;//
        int pawnDistanceToPromotion = 0;//

        int defenders = 0;//
        int attackPawns = 0;//
        int centerPawns = 0;//
        int centerKings = 0;//
        int mainDiagonalPawns = 0;//
        int mainDiagonalKings = 0;//
        int doubleDiagonalPawns = 0;//
        int doubleDiagonalKings = 0;//     
        
        int stubbornKings = 0;
        
        int lonerPawns = 0;
        int lonerKings = 0;
        int holes = 0;

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
            } else {
                if ((piece & 1) == 1) {
                    //if piece id is even, it is either white.
                    if (piece == DraughtsState.WHITEPIECE) {
                        //if it is a white pawn.
                        pawns++;
                        if (isEdge[i]) {
                            //if we are at the edge of the board.
                            safePawns++;
                        }
                        pawnDistanceToPromotion -= whiteDistanceToPromotion[i];
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
                } else {
                    //as empty is not considered here, it has to be black.
                    if (piece == DraughtsState.BLACKPIECE) {
                        //if it is a black pawn.
                        pawns--;
                        if (isEdge[i]) {
                            //if we are at the edge of the board.
                            safePawns--;
                        }
                        //+ instead of -, as we desire the complete opposite effect.
                        pawnDistanceToPromotion += blackDistanceToPromotion[i];
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
                }
            }
        }

        int evaluation = 
                promotionLinesEmpty * unoccupiedPromotionLineFieldsWeight 
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
                + doubleDiagonalKings * doubleDiagonalKingWeight
                + lonerPawns * lonerPawnWeight
                + lonerKings * lonerKingWeight
                + holes * holeWeight;

        return isWhitePlayer ? evaluation : -evaluation;
    }

    @Override
    public String toString() {
        return "unoccupiedPromotionLineFieldsWeight=" + unoccupiedPromotionLineFieldsWeight + " pawnWeight=" + pawnWeight + " kingWeight=" + kingWeight + " safePawnWeight=" + safePawnWeight + " safeKingWeight=" + safeKingWeight + " aggregatedDistanceOfPawnToPromotionLineWeight=" + aggregatedDistanceOfPawnToPromotionLineWeight + " defenderPieceWeight=" + defenderPieceWeight + " attackPawnWeight=" + attackPawnWeight + " centerPawnWeight=" + centerPawnWeight + " centerKingWeight=" + centerKingWeight + " mainDiagonalPawnWeight=" + mainDiagonalPawnWeight + " mainDiagonalKingWeight=" + mainDiagonalKingWeight + " doubleDiagonalPawnWeight=" + doubleDiagonalPawnWeight + " doubleDiagonalKingWeight=" + doubleDiagonalKingWeight + " lonerPawnWeight=" + lonerPawnWeight + " lonerKingWeight=" + lonerKingWeight + " holeWeight=" + holeWeight + " ";
    }

    

}