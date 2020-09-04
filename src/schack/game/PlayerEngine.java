package schack.game;

import schack.board.Tile;
import schack.pieces.Team;
/**
 * PlayerEngine is a abstract class
 * that defines all the types
 * of players you can see play
 * the game such as Humans or AI.
 * Currently the game only supports
 * human player but for development
 * purposes this abstract class was
 * created.
 *
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */
public abstract class PlayerEngine {
    private Team team;
    private ChessGame game;

    PlayerEngine(Team team, ChessGame game){
            this.game = game;
            this.team = team;
    }

    public Team getTeam() {
	return team;
    }

    ChessGame getGame() {
	return game;
    }

    abstract void makeMove(Tile selectedTile, PlayerEngine whoseTurn);
}
