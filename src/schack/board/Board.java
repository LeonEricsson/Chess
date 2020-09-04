package schack.board;

import schack.game.ChessGame;
import schack.game.PlayerEngine;
import schack.pieces.Bishop;
import schack.pieces.Knight;
import schack.pieces.King;
import schack.pieces.Pawn;
import schack.pieces.Piece;
import schack.pieces.Queen;
import schack.pieces.Rook;
import schack.pieces.Team;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;


/**
 * The board is the central part of a chess game therefor this
 * class has been constructed to handle everything that concerns
 * the board, from creation to piece movement. The class contains
 * a constructor which is initialized by the ChessGame class along
 * with several getters, used by other classes to collect vital
 * information from the board, and methods to handle player turns,
 * movement and notifying ChessComponent when to repaint. The board
 * has no direct relation to any other class.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */

public class Board {
    private ChessGame game;
    private Tile[][] tiles;
    private List<BoardListener> boardListeners;
    private final static int HEIGHT = 8;
    private final static int WIDTH = 8;
    private List<Piece> capturedPieces;
    private List<Move> moves;
    private List<Move> totalMoves;
    private int gameMoveCounter;
    private boolean anyTileSelected;
    private boolean isCheck;
    private BoardToTextConverter converter;
    private Piece piece;

    public Board(ChessGame game) {
        this.game = game;
	this.tiles = new Tile[WIDTH][HEIGHT];
	this.boardListeners = new ArrayList<>();
	this.moves = new ArrayList<>();
	this.capturedPieces = new ArrayList<>();
	this.converter = new BoardToTextConverter();
	this.totalMoves = new ArrayList<>();
	createStartBoard();
    }

    static int getHeight() {
        return HEIGHT;
    }

    static int getWidth() {
        return WIDTH;
    }

    public Tile getTile(int tileRow, int tileCol) throws ArrayIndexOutOfBoundsException{
        return tiles[tileRow][tileCol];
    }

    public int getTotalMovesSize(){
        return totalMoves.size();
    }

    List<Move> getMoves() {
	        return moves;
    }

    public int getGameMoveCounter(){
        return gameMoveCounter;
    }

   public List<Piece> getCapturedPieces(){
        return capturedPieces;
   }

    public BoardToTextConverter getConverter() {
	return converter;
    }

    public void addBoardListener(BoardListener bl){
            boardListeners.add(bl);
        }

    public void notifyListeners(){
        for (BoardListener boardListener : boardListeners){
            boardListener.boardChanged(); }
        }

        /*
        Once a tile has been selected it is this methods job to determine at what stage of the movement
        process the game is in and in turn either select a tile, unselect a tile or make a move. The method
        begins by checking if a tile is currently selected or not, if a tile is currently not selected
        the method will attempt to select the desired one. If a tile is already selected it will check
        if the player wanted to make a move and if so make one.

         */
    public void tileCheck(Tile selectedTile, PlayerEngine whoseTurn){
        if(!anyTileSelected) {
	    if ((selectedTile.getPiece() != null) && (selectedTile.getPiece().getPieceTeam() == whoseTurn.getTeam())) {
	        possibleBoardMoves(selectedTile, false);
		anyTileSelected = true;
	    }
        } else {
	    if (selectedTile.isHighlighted()) {
	        for (Move move : moves) {
	            if (move.getEndTile().equals(selectedTile)) {
	                doMove(move);
	                break; }
	        }
	    } else {
	        resetHighlighted();
	    }
	    anyTileSelected = false;
        }
    }

    // Remove possible moves highlights from tiles
    private void resetHighlighted(){
	for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                getTile(row, col).setHighlighted(false);
	    }
	}
        moves.clear();
        notifyListeners();
    }
    /*
    Makes the desired move on the board and also checks if the game is in
    checkmate after the move has been made by going through all of the opponents
    possible moves.
     */
    private void doMove(Move move) {
        Piece endTilePiece = move.getEndTile().getPiece();
	if(move.getStartTile().getPiece().isPromotionAvailable(move.getEndTile())){
	    promotionPiece(move, game.getViewer().choosePromotion());
	}
        if(endTilePiece != null){
            capturedPieces.add(endTilePiece);
        }
        move.getEndTile().setPiece(move.getStartTile().getPiece());
        move.getEndTile().getPiece().moved(true);
	move.getStartTile().setPiece(null);

	this.totalMoves = new ArrayList<>();
	for (int row = 0; row < 8; row++) {
	    for (int col = 0; col < 8; col++) {
	        if(getTile(row, col).getPiece() != null) {
		    if (getTile(row, col).getPiece().getPieceTeam() != game.whoseTurn().getTeam()) {
			possibleBoardMoves(getTile(row, col), true);

		    }
		}
	    }
	}
	game.getViewer().removeCastlingButton();
        moves.clear();
        gameMoveCounter += 1;
        notifyListeners();


    }

    /*	This method is designed to check the current "check" state
    of the game, this information is then used by possibleBoardMoves()
    to determine the possible moves for requested piece / tile.
     */
    private void checkMove(){
	loop:
	for (int row = 0; row < 8; row++) {
	    for (int col = 0; col < 8; col++) {
		moves.clear();
		Tile tile = getTile(row, col);
		if (tile.getPiece() != null) {
		    if (tile.getPiece().getPieceTeam() != game.whoseTurn().getTeam()) {
			tile.getPiece().calculateMoves(tile, this);
			for (Move move : moves) {
			    if (move.getEndTile().getPiece() != null) {
				if (move.getEndTile().getPiece().getPieceTeam() != move.getStartTile().getPiece().getPieceTeam()) {
				    isCheck = move.getEndTile().getPiece().isKing();
				    if (isCheck) {
					moves.clear();
					break loop;
				    }
				}
			    }
			}
		    }
		}
	    }
	}
    }

    /* Iterates over possible end tiles and highlights them for the user to see,
     also makes sure that while the game is in check the player in check can only
     move as to avoid check and players can not make a move putting their king in
     check. This method is also used when calculating possible moves left for each
     player used to determine checkmate.
    */
    private void possibleBoardMoves(Tile tile, Boolean checkOver) {
        resetHighlighted();
        if(tile.getPiece().isKing() && !checkOver){
	   if(castlingAvailable(tile.getPiece()) != null){
		game.getViewer().showCastlingButton();
	   }
	}
	tile.getPiece().calculateMoves(tile, this);
	List<Move> movesCopy = new ArrayList<>(moves);
	for (Move move : movesCopy) {
	    Piece startPiece = move.getStartTile().getPiece();
	    Piece endPiece = move.getEndTile().getPiece();
	    move.getEndTile().setPiece(move.getStartTile().getPiece());
	    move.getStartTile().setPiece(null);
	    checkMove();
	    if (!isCheck) {
	        move.getEndTile().setHighlighted(true);
	        if(checkOver){
	            totalMoves.add(move);
		}
	    }
	    move.getStartTile().setPiece(startPiece);
	    move.getEndTile().setPiece(endPiece);
	}
	tile.getPiece().calculateMoves(tile, this);
	notifyListeners();

    }


    public void addToMoveList(final Tile startTile, final int destRow, final int destCol) {
	if (destRow < 8 && destRow >= 0 && destCol < 8 && destCol >= 0) {
            Tile destTile = getTile(destRow, destCol);
	    if (destTile.getPiece() == null || ((destTile.getPiece().getPieceTeam() != startTile.getPiece().getPieceTeam()))) {
	        this.moves.add(new Move(startTile, destTile));
	    }
	}
    }

    //This method promotes the pawn to the desired piece
    private void promotionPiece(Move move,int result){
        final Team team = move.getStartTile().getPiece().getPieceTeam();
        switch (result){
	    case 0:
	        move.getEndTile().setPiece(new Knight(team, true));
	        break;
	    case 1:
	        move.getEndTile().setPiece(new Rook(team, true));
	        break;
	    case 2:
	        move.getEndTile().setPiece(new Bishop(team, true));
	        break;
	    case 3:
	        move.getStartTile().setPiece(new Queen(team, true));
	        break;
	}

    }

    private Piece castlingAvailable(Piece king){
        Piece piece = null;
	if(gameMoveCounter % 2 == 0){
	    if((getTile(7,0).getPiece() != null || getTile(7,7).getPiece() != null) && !king.hasMoved()){
	        if((!getTile(7, 0).getPiece().hasMoved()) && (getTile(7, 1).getPiece() == null)
		   && (getTile(7,2).getPiece() == null) && (getTile(7,3).getPiece() == null)){
	            getTile(7,2).setPiece(king);
	            checkMove();
	            if(!isCheck){
	                piece = getTile(7,0).getPiece();
		    }
	            getTile(7,2).setPiece(null);
	            getTile(7,4).setPiece(king);

		}
	        else if((!getTile(7, 7).getPiece().hasMoved()) && (getTile(7, 6).getPiece() == null)
			&& (getTile(7,5).getPiece() == null)){
	            getTile(7,6).setPiece(king);
	            checkMove();
	            if(!isCheck){
	                piece = getTile(7,7).getPiece();
		    }
	            getTile(7,4).setPiece(king);
	            getTile(7,6).setPiece(null);
		}
	    }
	}
	else{
	    if((getTile(0,0).getPiece() != null || getTile(0,7).getPiece() != null) && !king.hasMoved()){
	        if((!getTile(0, 0).getPiece().hasMoved()) && (getTile(0, 1).getPiece() == null)
		   && (getTile(0,2).getPiece() == null) && (getTile(0,3).getPiece() == null)){
	            getTile(0,2).setPiece(king);
	            checkMove();
	            if(!isCheck){
	                piece = getTile(0,0).getPiece();
	            }
	            getTile(0,2).setPiece(null);
	            getTile(0,4).setPiece(king);
	    		}
	        else if((!getTile(0, 7).getPiece().hasMoved()) && (getTile(0, 6).getPiece() == null)
			&& (getTile(0,5).getPiece() == null)){
	           	 getTile(0,6).setPiece(king);
	            	checkMove();
	            	if(!isCheck){
	             	   piece = getTile(0,7).getPiece();
	            	}
	           	 getTile(0,4).setPiece(king);
	           	 getTile(0,6).setPiece(null);
	        }
	    }
	}
	return piece;
    }

    public void castle(){

    }

    /*
    This method is used to reset the board once necessary, it uses the FEN notation to
    determine where to place the pieces on the standard board, then together with the
    checkWhichPiece method it places them out on the board.
     */
    public void createStartBoard(){
	String startBoardString = "thbqkbht\npppppppp\n--------\n--------\n--------\n--------\nPPPPPPPP\nTHBQKBHT";
	String[] rows = startBoardString.split("\n");
	for (int row = 0; row < 8 ; row++) {
	    String[] cols = rows[row].split("");
	    checkWhichPiece(cols, row);

	}
	isCheck = false;
	capturedPieces.clear();
	this.gameMoveCounter = 0;
	notifyListeners();
    }

    //Magic number constant in RGB is not a problem
    private Color getTileColor(int row, int col){
                if(abs((row+col)%2) == 1){
                    return new Color(118 ,149, 191);
                }
                else{
                    return Color.WHITE;
                }
            }


    public void checkWhichPiece(String[] cols, int row){
	for (int col = 0; col < 8; col++) {
	    switch (cols[col]) {
		case "t":
		    this.piece = new Rook(Team.BLACK, false);
		    break;
		case "h":
		    this.piece = new Knight(Team.BLACK, false);
		    break;
		case "b":
		    this.piece = new Bishop(Team.BLACK, false);
		    break;
		case "q":
		    this.piece = new Queen(Team.BLACK, false);
		    break;
		case "k":
		    this.piece = new King(Team.BLACK, false);
		    break;
		case "p":
		    this.piece = new Pawn(Team.BLACK, false);
		    break;
		case "-":
		    this.piece = null;
		    break;
		case "P":
		    this.piece = new Pawn(Team.WHITE, false);
		    break;
		case "T":
		    this.piece = new Rook(Team.WHITE, false);
		    break;
		case "H":
		    this.piece = new Knight(Team.WHITE, false);
		    break;
		case "B":
		    this.piece = new Bishop(Team.WHITE, false);
		    break;
		case "Q":
		    this.piece = new Queen(Team.WHITE, false);
		    break;
		case "K":
		   this.piece = new King(Team.WHITE, false);
		    break;
	    }
	    if(piece == null){
	        tiles[row][col] = new Tile(row,col,null,getTileColor(row, col));
	    }
	    else {
		putInPiece(piece, row, col, piece.getPieceTeam());
	    }
	}
    }

    private void putInPiece(Piece piece, int row, int col, Team team){
            tiles[row][col] = new Tile(row, col, piece, getTileColor(row, col));
        }
}