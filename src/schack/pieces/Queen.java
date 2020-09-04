package schack.pieces;


import schack.board.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Queen class handles everything
 * to do with the chess games Queen
 * piece.This class is a subclass to
 * the Piece class and therefor it also
 * inherits and overrides a few methods
 * from there.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */
public class Queen extends IterativeMove {
    private List<Point> queenMoves = new ArrayList<>();

    public Queen(Team pieceTeam, boolean hasMoved) {
            super(pieceTeam, hasMoved);
        }

    private final static int[] ROW_MOVES = {-1, 0, 1, -1, 1, -1, 0, 1};
    private final static int[] COL_MOVES = {-1, -1, -1, 0, 0, 1, 1, 1};
    private final static Point[] POSSIBLE_TILE_POS = new Point[8];

    @Override public boolean hasMoved() {
        return false;
    }

    @Override public List<Point> possibleMoves() {
        return getMoves(POSSIBLE_TILE_POS, ROW_MOVES, COL_MOVES, queenMoves, ROW_MOVES.length);

    }

    @Override public boolean isKing() {
        return false;
    }

    @Override public String getImageSuffix() {
            return "Q.png";
        }

    @Override public boolean isPromotionAvailable(final Tile endTile) {
        return false;
    }
}
