package schack.pieces;


import schack.board.Board;
import schack.board.Tile;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
/**
 * The Piece class is the
 * abstract class that defines
 * all the chess games pieces.
 * It contains the methods of
 * each piece class as well
 * as some of its own.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */
public abstract class Piece {
    private final Team pieceTeam;
    private boolean hasMoved;

    Piece(Team pieceTeam, boolean hasMoved){
        this.pieceTeam = pieceTeam;
        this.hasMoved = hasMoved;
    }

    public Team getPieceTeam(){
        return pieceTeam;
    }

    public abstract boolean isKing();

    public void moved(boolean set){
        this.hasMoved = set;
    }

    public boolean hasMoved(){
        return hasMoved;
    }

    public abstract List<Point> possibleMoves();

    //To enforce encapsulation each Piece holds information regarding how it looks visually and this method extracts that information.
    public abstract String getImageSuffix();

    public abstract boolean isPromotionAvailable(Tile endTile);

    public abstract void calculateMoves(Tile tile, Board board);

    // This method is simply extracted out of each individual Piece to reduce repetitiveness.
    static List<Point> getMoves(Point[] possibleTilePos, int[] rowMoves, int[] colMoves, List<Point> pieceMoves, int length) {
        pieceMoves.clear();
        for (int i = 0; i < length; i++) {
            possibleTilePos[i] = new Point(rowMoves[i], colMoves[i]);

        }
        pieceMoves.addAll(Arrays.asList(possibleTilePos));
        return pieceMoves;
    }
}
