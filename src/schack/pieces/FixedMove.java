package schack.pieces;

import schack.board.Board;
import schack.board.Tile;

import java.awt.*;
import java.util.List;
/**
 * This abstract class is used
 * to extract the common calculate
 * moves function for the King
 * and the Knight class in an
 * attempt to reduce duplicate code.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */
abstract class FixedMove extends Piece {

    FixedMove(Team team, boolean hasMoved){
        super(team, hasMoved);
    }
    /*
    Some pieces share similar movement patterns and therefor the calculations
    of these moves have been extracted into a common class which uses this method
    to calculate the possible moves for a specific piece.
     */
    @Override public void calculateMoves(final Tile tile, final Board board) {
        List<Point> pointList = possibleMoves();
	for (Point pos : pointList) {
	    int destRow = tile.getTileRow() + pos.x;
	    int destCol = tile.getTileCol() + pos.y;
	    board.addToMoveList(tile, destRow, destCol);
	}
    }
}
