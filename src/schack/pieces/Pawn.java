package schack.pieces;

import schack.board.Board;
import schack.board.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Pawn class handles everything
 * to do with the chess games Pawn
 * piece.This class is a subclass to
 * the Piece class and therefor it also
 * inherits and overrides a few methods
 * from there.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */
public class Pawn extends Piece {
    private List<Point> pawnMoves = new ArrayList<>();

    public Pawn(Team pieceTeam, boolean hasMoved) {
        super(pieceTeam, hasMoved);
    }

    private final static int[] ROW_MOVES = { -1, -1, -1, 1, 1, 1 };
    private final static int[] COL_MOVES = { 0, -1, 1, 0, -1, 1 };
    private final static Point[] POSSIBLE_TILE_POS = new Point[3];
    /*
    This method, just as in the other pieces returns a list of possible moves but
    the biggest difference here is that the moves depend a lot on the pawns surroundings and
    the pawns team so the possibleMoves and calculateMoves methods work together to make sure only the
    correct moves are sent.
     */
    @Override public List<Point> possibleMoves() {
        pawnMoves.clear();
        if(getPieceTeam() != Team.WHITE) {
            for (int i = 3; i < 6; i++) {
                POSSIBLE_TILE_POS[i - 3] = new Point(ROW_MOVES[i], COL_MOVES[i]);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                POSSIBLE_TILE_POS[i] = new Point(ROW_MOVES[i], COL_MOVES[i]);
            }

        }
        Collections.addAll(pawnMoves, POSSIBLE_TILE_POS);
        return pawnMoves;
    }

    @Override public void calculateMoves(Tile tile, Board board){
        final int tileRow = tile.getTileRow();
        final int tileCol = tile.getTileCol();
        List<Point> points = possibleMoves();
        for (int i = 0; i < points.size() ; i++) {
            int destRow = tileRow + points.get(i).x;
            int destCol = tileCol + points.get(i).y;
            if (destRow  < 8 && destRow  >= 0 && destCol < 8 && destCol >= 0){
                Tile destTile = board.getTile(destRow, destCol);
                if(i == 0){
                    if(destTile.getPiece() == null) {
                        board.addToMoveList(tile, destRow, destCol);
                    }
                }
                else {
                    if (destTile.getPiece() != null) {
                        if (destTile.getPiece().getPieceTeam() != tile.getPiece().getPieceTeam()) {
                            board.addToMoveList(tile, destRow, destCol);
                        }
                    }
                }
            }
        }
        if(tile.getPiece().getPieceTeam() == Team.WHITE && tileRow == 6 &&
            board.getTile(tileRow-1, tileCol).getPiece() == null &&
            board.getTile(tileRow-2, tileCol).getPiece() == null){
            board.addToMoveList(tile, 4, tileCol);
        }
        if(tile.getPiece().getPieceTeam() == Team.BLACK && tileRow == 1 &&
            board.getTile(tileRow+1,tileCol).getPiece() == null &&
            board.getTile(tileRow+2, tileCol).getPiece() == null){
            board.addToMoveList(tile, 3, tileCol);
        }
    }

    @Override public boolean isKing() {
        return false;
    }

    @Override public String getImageSuffix() {
            return "P.png";
        }

    @Override public boolean isPromotionAvailable(final Tile endTile) {
	return (endTile.getTileRow() == 0) || (endTile.getTileRow() == 8);
    }
}
