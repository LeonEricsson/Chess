package schack.pieces;


import schack.board.Board;
import schack.board.Tile;

import java.awt.*;
import java.util.List;
/**
 * This abstract class is used
 * to extract the common calculate
 * moves function for the Queen,
 * Bishop and Rook class in an
 * attempt to reduce duplicate code.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */

abstract class IterativeMove extends Piece {
	IterativeMove(Team team, boolean hasMoved){
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
	    int firstPosRow = pos.x;
	    int firstPosCol = pos.y;
	    int height = Math.max(Math.abs(0-tile.getTileRow()), Math.abs(7-tile.getTileRow()));
	    int length = Math.max(Math.abs(0-tile.getTileCol()), Math.abs(7-tile.getTileCol()));
	    for (int i = 0; i < Math.max(height, length) ; i++) {
	        int destRow = tile.getTileRow() + pos.x;
	        int destCol = tile.getTileCol() + pos.y;
		if (destRow < 8 && destRow >= 0 && destCol < 8 && destCol >= 0) {
		    Tile destTile = board.getTile(destRow, destCol);
		    board.addToMoveList(tile, destRow, destCol);
	        if(destTile.getPiece() != null){
	            break; }
	        pos.x += firstPosRow;
	        pos.y += firstPosCol;
	    	}
	    }
	}
    }
}
