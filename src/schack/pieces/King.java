package schack.pieces;

import schack.board.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The King class handles everything
 * to do with the chess games King
 * piece.This class is a subclass to
 * the Piece class and therefor it also
 * inherits and overrides a few methods
 * from there.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */
public class King extends FixedMove{
    private List<Point> kingMoves = new ArrayList<>();

    public King(Team pieceTeam, boolean hasMoved) {
        super(pieceTeam, hasMoved);
    }

    private final static int[] ROW_MOVES = {1, 0, -1, 1, -1, 1, 0, -1};
    private final static int[] COL_MOVES = {-1, -1, -1, 0, 0, 1, 1, 1};
    private final static Point[] POSSIBLE_TILE_POS = new Point[8];

    @Override public List<Point> possibleMoves() {
        return getMoves(POSSIBLE_TILE_POS, ROW_MOVES, COL_MOVES, kingMoves, ROW_MOVES.length);
    }

    @Override public String getImageSuffix() {
            return "K.png";
        }

    @Override public boolean isKing() {
        return true;
    }

    @Override public boolean isPromotionAvailable(final Tile endTile) {
        return false;
    }
}

