package schack.game;


import labbP.Player;
import schack.board.Board;
import schack.board.Tile;
import schack.pieces.Piece;
import schack.pieces.Team;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ChessGame oversees the game process,
 * it is this class that calls all the
 * wanted moves, knows when the game is
 * over and calculates whose turn it
 * is. This class is initialized once
 * the game is opened and from there
 * it creates the board and the
 * game viewer. This class has no direct
 * relation with a class but it works
 * closely with the viewer and the board
 * to keep the game flowing.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */
public class ChessGame {
    private EnumMap<Team, HumanPlayer> players = new EnumMap<>(Team.class);
    private Board board;
    private ChessViewer viewer;
    private PlayerEngine winner = null;
    private boolean gameOver = false;
    private static final Logger LOGGER = Logger.getLogger(ChessGame.class.getName());


    public ChessGame(){
	this.board = new Board(this);
	this.viewer = new ChessViewer(board, this);
	players.put(Team.WHITE, viewer.whitePlayerName());
	players.put(Team.BLACK, viewer.blackPlayerName());
    }

    PlayerEngine getWinner(){
        return winner;
    }
    public HumanPlayer getPlayer(Team team){
        return players.get(team);
    }
    boolean isGameOver(){
        return gameOver;
    }
    public Board getBoard() {
	return board;
    }
    public ChessViewer getViewer(){ return viewer; }
    void setGameOver(){
            this.gameOver = false;
    }

    /*
    This method acts as a middleman between the viewer and the board,
    because this class holds information regarding whose turn it is
    the move needs to be called from here before it can move forward.
    This is also where the game is flipped into game over when the
    opposite player is in checkmate.
     */
    void addMove(Tile selectedTile){
        whoseTurn().makeMove(selectedTile, whoseTurn());
        if(board.getGameMoveCounter() > 2) {
	    if (board.getTotalMovesSize() == 0) {
		gameOver = true;
		winner = whoseTurn();
	    }
	}
    }

    public PlayerEngine whoseTurn(){
        if(board.getGameMoveCounter()%2 == 0){
            return players.get(Team.WHITE);
        }
        else{
            return players.get(Team.BLACK);
        }
    }


    void saveGame(){
            File file = new File(System.getProperty("user.home") + File.separator +"LastBoard.txt");
            try(PrintWriter writer = new PrintWriter(file)){
                writer.write(board.getConverter().convertToText(board));
        	} catch (FileNotFoundException e){
                    e.printStackTrace();
                    LOGGER.log(Level.WARNING, "Failed to save game file", e);
                    if(ChessViewer.askUserYesNo("Failed to save game \n Do you want to try again?" )){
                        saveGame();
    		}

        	}
        }

        //Assignment issue is not complicated in this instance, assignment is understandable
        void loadStartBoard(){
            try(BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + File.separator +"LastBoard.txt"))){
                int row = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] cols = line.split("");
                    board.checkWhichPiece(cols, row);
                    row += 1;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                askIfPlayerWantsRegularBoard("No saved board was found");
    	    LOGGER.log(Level.INFO, "No saved board was found", e);
            } catch (IOException ex) {
                ex.printStackTrace();
                askIfPlayerWantsRegularBoard("Unable to load board");
    	    LOGGER.log(Level.WARNING, "Failed to load board from file", ex);

            }
        }


        private void askIfPlayerWantsRegularBoard(String info){
            ChessViewer.giveInfo(info);
    	if(ChessViewer.askUserYesNo("Do you want to play with regular start board?")) {
    	    board.createStartBoard();
    	}
    }
}

