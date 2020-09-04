package schack.board;
/**
 * This class acts as a placeholder
 * for all the moves that are possible
 * for the pieces. The class in itself
 * has no effect on the board but it
 * is used to structure the project.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */
public class Move {
    private Tile startTile;
    private Tile endTile;

    public Move(Tile start, Tile end){
        this.startTile = start;
        this.endTile = end;
    }


    Tile getStartTile() {
        return startTile;
    }

    Tile getEndTile() {
        return endTile;
    }
}
