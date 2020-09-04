package schack.board;

import schack.pieces.Piece;
import schack.pieces.Team;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * This class is the boards building
 * stone, it is what holds all the
 * individual pieces of the game.
 * The class is initialized in the
 * boardBuilder and is then used
 * in the movement of each piece
 * around the game board. It is
 * also used to display the possible
 * moves of each piece once highlighted.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */

public class Tile{
    private int tileRow;
    private int tileCol;
    private Color color;
    private Piece piece;
    private boolean highlighted = false;
    private final static int TILE_SIDE = 60;
    private final static float CIRCLE_FLOAT = 0.65f;


   Tile(int tileRow, int tileCol, Piece piece, Color color){
        this.tileRow = tileRow;
        this.tileCol = tileCol;
        this.piece = piece;
        this.color = color;
    }

    void setPiece(Piece piece) { this.piece = piece; }
    void setHighlighted(boolean set){
        this.highlighted = set;
    }
    boolean isHighlighted(){
        return highlighted;
    }
    public static int getTileSide() {
        return TILE_SIDE;
    }
    public Piece getPiece() {return piece;}
    public int getTileRow(){ return tileRow; }
    public int getTileCol(){ return tileCol; }

    //Magic number warnings for RGB is meager and constant would be more confusing
    void paintTile(Graphics2D g2d, List<Move> moveList, int i, int j, ChessComponent component) {
        final int tileSideX = this.tileCol * TILE_SIDE;
        final int tileSideY = this.tileRow * TILE_SIDE;
        Composite normal = g2d.getComposite();
        if (moveList.isEmpty()) {
            g2d.setColor(this.color);
            g2d.fillRect(tileSideX, tileSideY, TILE_SIDE, TILE_SIDE);
        } else {
            for (Move move : moveList) {
                if (move.getEndTile().equals(this) && this.highlighted) {
                    g2d.setColor(this.color);
                    g2d.fillRect(tileSideX, tileSideY, TILE_SIDE, TILE_SIDE);
                    if (piece != null) {
                        if (move.getStartTile().piece.getPieceTeam() != piece.getPieceTeam()) {
                            g2d.setColor(new Color(221, 8, 3));
                        }
                    } else {
                        g2d.setColor(new Color(92, 92, 92));
                    }
                    AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, CIRCLE_FLOAT);
                    g2d.setComposite(alcom);
                    g2d.fillOval(tileSideX + TILE_SIDE / 4, tileSideY + TILE_SIDE / 4,
                                 TILE_SIDE / 2, TILE_SIDE / 2);
                    break;
                } else {
                    g2d.setColor(this.color);
                    g2d.fillRect(tileSideX, tileSideY, TILE_SIDE, TILE_SIDE);
                }
            }
        }
        g2d.setComposite(normal);
        if (piece != null) {
            ImageIcon icon = getPieceImage();
            icon.paintIcon(component, g2d, j * TILE_SIDE + 5, i * TILE_SIDE + 5);
        }
    }
    private ImageIcon getPieceImage() {
       String fileSuffix = piece.getImageSuffix();
       if (piece.getPieceTeam() == Team.WHITE) {
           fileSuffix = "W" + fileSuffix;
       } else {
           fileSuffix = "B" + fileSuffix;
       }
        return new ImageIcon(ClassLoader.getSystemResource(fileSuffix));
    }
}


