package schack.board;

import schack.game.ChessGame;

/**
 * To initialize the game and to test it while
 * working on it this class is used. It has no
 * relation to any class.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */

//Private constructor is redundant and result of new ChessGame is irrelevant
public final class BoardTest {
    private BoardTest() {
    }

    public static void main(String[] args) {
        new ChessGame();
    }
}
