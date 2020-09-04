package schack.board;

import schack.pieces.Piece;
import schack.pieces.Team;

/**
 * BoardToTextConverter was first used
 * to test the system before the visual
 * implementation had been completed. Now
 * the class is used to convert the current
 * board into text format for saving. The user
 * is asked if he or she wants to save the
 * game before closing down and if so this class
 * helps translate the current board state
 * into a save-able format.
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */
public class BoardToTextConverter {
    public String convertToText(Board boardlook) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final Piece piece = boardlook.getTile(i, j).getPiece();
                if (piece == null) {
                    builder.append("-");

                } else {
                    if (piece.getPieceTeam() == Team.WHITE) {
                        switch (piece.getClass().getSimpleName()) {
                            case "Bishop":
                                builder.append("B");
                                break;
                            case "King":
                                builder.append("K");
                                break;
                            case "Pawn":
                                builder.append("P");
                                break;
                            case "Queen":
                                builder.append("Q");
                                break;
                            case "Rook":
                                builder.append("T");
                                break;
                            case "Knight":
                                builder.append("H");
                                break;
                        }
                    } else {
                        switch (piece.getClass().getSimpleName()) {
                            case "Bishop":
                                builder.append("b");
                                break;
                            case "King":
                                builder.append("k");
                                break;
                            case "Pawn":
                                builder.append("p");
                                break;
                            case "Queen":
                                builder.append("q");
                                break;
                            case "Rook":
                                builder.append("t");
                                break;
                            case "Knight":
                                builder.append("h");
                                break;
                        }
                    }
                }
            }

            builder.append("\n");

        }
        return builder.toString();
    }
}

