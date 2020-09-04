package schack.game;

import schack.board.Board;
import schack.board.ChessComponent;
import schack.board.Tile;
import schack.pieces.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

/**
 * This class manages everything visual
 * about the game as well as registering
 * the users mouse clicks. The frame that
 * game is played in as well as all the popups
 * and menus are handled from here. The class is
 * initialized by the BoardTest class
 * and after that it acts as a informant
 * to the Board class, instructing it
 * of which tiles are pressed and moved.
 *
 *
 * @author Leon Ericsson
 * @author Adam Hjalmarsson
 */

public class ChessViewer
{
    private JFrame frame;
    private Board board;
    private ChessComponent chessComponent;
    private ChessGame game;
    private JMenuBar menubar;
    private JButton castlingButton;
    private static final int MENU_BAR_SIZE = 50;
    private static final Logger LOGGER = Logger.getLogger(ChessViewer.class.getName());

    ChessViewer(Board board,ChessGame game) {
        this.frame = new JFrame("Chess");
        this.board = board;
        this.game = game;
        this.chessComponent = new ChessComponent(board);
        addMenu();
        board.addBoardListener(chessComponent);
        frame.setLayout(new BorderLayout());
        frame.add(chessComponent, BorderLayout.CENTER);
        frame.addMouseListener(new MouseListener() {
            @Override
            /* This method is where the player movement stems from, together with
            getTileMouseClick this method tells the board which tile has been selected
            by the player and the board then decides what to do with that information.
            It is also this methods job to call the gameOverScreen once the game is over.
             */
            public void mouseClicked(MouseEvent mouseEvent) {
		int x = mouseEvent.getX();
		int y = mouseEvent.getY();
		handleMouseClick(x, y);
	    }

            @Override
            public void mousePressed(MouseEvent mouseEvent) { }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) { }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) { }

            @Override
            public void mouseExited(MouseEvent mouseEvent) { }
        });
        frame.pack();
        frame.setVisible(true);
    }
    private void handleMouseClick(int x, int y){
        int tileRow = Math.floorDiv(y - MENU_BAR_SIZE, Tile.getTileSide());
        int tileCol = Math.floorDiv(x, Tile.getTileSide());
        if ((tileRow >= 0 && tileRow < 8) && (tileCol >= 0 && tileCol < 8)) {
            Tile selectedTile = board.getTile(tileRow, tileCol);
            game.addMove(selectedTile);
            if (game.isGameOver()) {
                if (gameOverScreen(game.getWinner().getTeam().toString())) {
                    board.createStartBoard();
                    game.setGameOver();
                } else {
                    System.exit(0);
                }
            }
        }
    }


    HumanPlayer whitePlayerName(){
        return new HumanPlayer(Team.WHITE, askUserName("Player 1 Name:"), game);
    }

    HumanPlayer blackPlayerName(){
        return new HumanPlayer(Team.BLACK, askUserName("Player 2 Name:"), game);
    }

    public int choosePromotion(){
        Object[] options = { "Knight", "Rook", "Bishop", "Queen"};
        return JOptionPane.showOptionDialog(frame, "Promote pawn to?", "Pawn Promotion!",
                                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                            options, options[3]);
    }

    private boolean gameOverScreen(String winner){
        return JOptionPane.showConfirmDialog(frame, "Do you want to play again?", winner + " wins",
                                             JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION;
    }

    public void gameInCheck(){
        JFrame check = new JFrame("Check");
        check.setLayout(new BorderLayout());
        JLabel checkText = new JLabel("CHECK");
        checkText.setText("CHECK");
        check.add(checkText, BorderLayout.CENTER);
        check.setPreferredSize(new Dimension(300, 300));
        check.pack();
        check.setVisible(true);
        check.setLocationRelativeTo(chessComponent);
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        check.dispose();
    }

    public void showCastlingButton(){
        menubar.add(castlingButton);
    }

    public void removeCastlingButton(){
        menubar.remove(castlingButton);
    }

    private void addMenu() {
        this.menubar = new JMenuBar();
        final JMenu menu = new JMenu("Menu");
        final JMenuItem setNewGame = new JMenuItem("New Game", 'N');
        final JMenuItem quit = new JMenuItem("Quit", 'Q');
        final JMenuItem loadLastgame = new JMenuItem("Load Game", 'L');
        this.castlingButton = new JButton(castling);
        menu.add(setNewGame);
        setNewGame.addActionListener(newGame);
        menu.add(loadLastgame);
        loadLastgame.addActionListener(loadGame);
        menu.add(quit);
        quit.addActionListener(quitGame);
        menubar.add(menu);
        menubar.add(new JButton(teams));

        frame.setJMenuBar(menubar);
    }

    private final Action teams = new AbstractAction("Teams") {
        @Override public void actionPerformed(final ActionEvent e) {
            giveInfo("White player: " + game.getPlayer(Team.WHITE).getName()+ "\n"
                     + "Black player: " + game.getPlayer(Team.BLACK).getName());
        }
    };

    private final Action castling = new AbstractAction("Teams") {
            @Override public void actionPerformed(final ActionEvent e) {
                if(askUserLeftRight("Choose rook to castle") == 0){

                }
                else {

                }
            }
        };

    private final Action quitGame = new AbstractAction() {//I don't see this warning as a problem//
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (askUserYesNo("Do you really want to quit?")) {
                    if(askUserYesNo("Do you want to save game?")){
                        game.saveGame();
                    }
                    System.exit(0);
                }
            }
        };
    private final Action loadGame = new AbstractAction()
    {
        @Override public void actionPerformed(final ActionEvent e) {
            if(askUserYesNo("Do you want to load last game?")){
                game.loadStartBoard();
                board.notifyListeners();
            }
        }
    };
    private final Action newGame = new AbstractAction()
    {
        @Override public void actionPerformed(final ActionEvent e) {
            if(askUserYesNo("New Game?")){
                board.createStartBoard();
            }
        }
    };
    static boolean askUserYesNo(String question) {
                        return JOptionPane.showConfirmDialog(null, question,"",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                }
    private static String askUserName(String question){
        String name = "";
        while ("".equals(name) || (" ".equals(name)) || (name == null)){
            name = JOptionPane.showInputDialog(null, question, "Enter player names", JOptionPane.QUESTION_MESSAGE);
        }
        return name;
    }
    //If you want to use it later on
    static void giveInfo(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    static int askUserLeftRight(String question){
        Object[] options = { "Left", "Right"};
        return JOptionPane.showOptionDialog(null, question, "Castling",
                                                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                                    options, options[0]);
    }
}
