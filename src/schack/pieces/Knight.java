package schack.pieces;

import schack.board.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Knight class handles everything
 * to do with the chess games Knight
 * piece. This class is a subclass to
 * the Piece class and therefor it also
 * inherits and overrides a few methods
 * from there.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */
public class Knight extends FixedMove{
    private List<Point> knightMoves = new ArrayList<>();

    public Knight(Team pieceTeam, boolean hasMoved) {
            super(pieceTeam, hasMoved);}

    private final static int[] ROW_MOVES = { -2, -1, 1, 2, 2, 1, -1, -2 };
    private final static int[] COL_MOVES = { 1, 2, 2, 1, -1, -2, -2, -1 };
    private final static Point[] POSSIBLE_TILE_POS = new Point[8];


    @Override public List<Point> possibleMoves() {
        return getMoves(POSSIBLE_TILE_POS, ROW_MOVES, COL_MOVES, knightMoves, ROW_MOVES.length);
    }
    @Override public String getImageSuffix() {
            return "N.png";
        }

    @Override public boolean isKing() {
        return false;
    }

    @Override public boolean isPromotionAvailable(final Tile endTile) {
        return false;
    }
}


