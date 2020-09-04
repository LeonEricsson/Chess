package schack.board;

/**
 * As a way to notify JComponent classes when the
 * board has changed and therefor needs to be repainted
 * this interface was created. The board calls all
 * board listeners whenever the board has changed and that
 * way they know when they need to repaint their
 * components. This interface currently has one
 * subclass, ChessComponent.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */

public interface BoardListener {
    void boardChanged();
}
