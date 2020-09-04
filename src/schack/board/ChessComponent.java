package schack.board;


import javax.swing.*;
import java.awt.*;

/**
 * While the BoardViewer class allows you
 * to view the game it is the ChessComponent
 * class that paints everything in the correct way.
 * The pieces, tiles and the possible move circles
 * are all drawn from here. This class is also a
 * subclass to the boardListener interface.
 *
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */

public class ChessComponent extends JComponent implements BoardListener {
    private Board board;

    public void boardChanged(){
        repaint();
    }

    public ChessComponent(Board board) {
        this.board = board;
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(board.getWidth()*Tile.getTileSide(), board.getHeight()*Tile.getTileSide());
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Tile currentTile = board.getTile(i, j);
                currentTile.paintTile(g2d, board.getMoves(), i, j, this);
                }
            }
        }
    }


