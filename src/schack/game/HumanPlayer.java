package schack.game;

import schack.board.Tile;
import schack.pieces.Team;
/**
 * The HumanPlayer class is to hold
 * information about the player
 * playing the game and to make moves
 * that the player has requested.
 * It is a subclass to the PlayerEngine
 * as it is a type of player, one could
 * imagine AI players being another type.
 * The players are created with a team
 * and human players also hold a name.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */
class HumanPlayer extends PlayerEngine {
    private String name;

    HumanPlayer(Team team, String name, ChessGame game){
        super(team, game);
        this.name = name;
    }

    String getName() {
        return name;
    }

    @Override void makeMove(Tile selectedTile, PlayerEngine whoseTurn) {
        getGame().getBoard().tileCheck(selectedTile, whoseTurn);
    }

}
