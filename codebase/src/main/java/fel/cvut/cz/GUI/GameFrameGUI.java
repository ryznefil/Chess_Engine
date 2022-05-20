/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.GUI;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Board.BoardUtilities;
import fel.cvut.cz.Board.Tile;
import fel.cvut.cz.Chessman.GeneralPiece;
import fel.cvut.cz.Move.Move;
import fel.cvut.cz.Move.MoveStatus;
import fel.cvut.cz.Move.MoveTransition;
import fel.cvut.cz.Player.ComputerPlayer;
import fel.cvut.cz.Player.PlayerType;
import fel.cvut.cz.PortableGameNotation.MoveLog;
import fel.cvut.cz.PortableGameNotation.PieceType;
import fel.cvut.cz.PortableGameNotation.utilitiesPGN;
import fel.cvut.cz.Utilities.Position;
import fel.cvut.cz.Utilities.utilsGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import static fel.cvut.cz.Board.Board.BOARD_COLUMNS;
import static fel.cvut.cz.Board.Board.BOARD_ROWS;
import static javax.swing.JOptionPane.*;

/**
 * Major part of the GUI of the game. Excluded is only the move history panel and the pawn promotion window.
 * Contains all other methods and class - creates JFrame for the game, chess board with tiles and piece, option menus
 * and the timer.
 *
 * @author Ryzner
 */
public class GameFrameGUI {

    private static final Logger LOG = Logger.getLogger(GameFrameGUI.class.getName());

    private Tile inputTile;
    private Tile destinationTile;
    private JFrame gameFrame;
    private GameHistoryPanel gameHistoryPanel;
    private ChessBoardGUI BoardVisual;
    private Board ChessBoard;
    private Board lastRoundBoard;
    private ChessClock chessClock;
    private boolean networkGame;

    private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;
    private long whitePlayerTime;
    private long blackPlayerTime;
    private long timeToSet;
    private final MoveLog moveLog;

    private int roundsPlayed;

    private int computerMoves;

    private final static Dimension OUTER_FRAME_DIM = new Dimension(1300, 800);
    private final static Dimension CHESS_BOARD_DIM = new Dimension(800, 800);
    private final static Dimension CHESS_TILE_DIM = new Dimension(100, 100);

    private GeneralPiece movingPiece;
    private boolean highlightLegalMoves;
    private ImageIcon GreenDot;


    /**
     * Constructor for the whole GUI of the game, creates the JFrame and all its components
     * Board, chess pieces, timer, move history panel and the option menu.
     */

    public GameFrameGUI() {

        //setting up values of variable in constructor
        this.networkGame = false;
        this.timeToSet = 0;
        this.computerMoves = 0;
        this.roundsPlayed = 0;
        this.whitePlayerType = PlayerType.HUMAN;
        this.blackPlayerType = PlayerType.HUMAN;
        this.whitePlayerTime = 7200 * 1000; //seconds to milliseconds
        this.blackPlayerTime = 7200 * 1000; //seconds to milliseconds
        this.moveLog = new MoveLog();

        //create the Container
        this.gameFrame = new JFrame("Project Chess");
        this.gameFrame.setLayout(new BorderLayout());

        //Create and add the menu bar located above the chessBoard
        final JMenuBar tableMenuBar = new JMenuBar();
        createMenuBar(tableMenuBar);
        this.gameFrame.setJMenuBar(tableMenuBar);

        //create new chess board and set the last round board to empty (there is none at this point, no turn has been played)
        this.ChessBoard = new Board(whitePlayerType, blackPlayerType, whitePlayerTime * 1000, blackPlayerTime * 1000);
        ChessBoard.startingGameSetup();
        lastRoundBoard = null;

        //Create move history panel, chess board, start highlighting legal moves
        this.gameHistoryPanel = new GameHistoryPanel();
        this.BoardVisual = new ChessBoardGUI();
        this.highlightLegalMoves = true;
        this.gameFrame.add(this.BoardVisual, BorderLayout.CENTER);
        this.gameFrame.add(gameHistoryPanel, BorderLayout.EAST);

        //Create the chesss clock
        chessClock = new ChessClock(whitePlayerTime);
        chessClock.setBlackTime(blackPlayerTime);
        chessClock.start();

        //Finilize visuals of the frame
        this.gameFrame.setResizable(false);
        this.gameFrame.setSize(OUTER_FRAME_DIM);
        center(this.gameFrame);
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //GUI MENU BAR
    //------------------------------------------------------------------------------------------------------------------------------------------------------

    //Create the menu bar and its content
    private void createMenuBar(final JMenuBar tableMenuBar) {
        tableMenuBar.add(createGameMenu());
        tableMenuBar.add(createNetworkGameMenu());
        tableMenuBar.add(createCurrentMenu());
        tableMenuBar.add(createPreferencesMenu());
    }

    //MAIN MENU
    private JMenu createGameMenu() {
        final JMenu fileMenu = new JMenu("Game");

        //NEW GAME
        final JMenuItem newGame = new JMenuItem("New game");
        newGame.addActionListener((ActionEvent e) -> {
            NewGameMenu GUI = new NewGameMenu(gameFrame, true);
        });
        fileMenu.add(newGame);

        //RESTART GAME
        final JMenuItem restartGame = new JMenuItem("Restart game");
        restartGame.addActionListener((ActionEvent e) -> {
            resetRestartGame();
            JOptionPane.showMessageDialog(null, "Game successfully restarted!", "Restart Game", JOptionPane.INFORMATION_MESSAGE);

        });
        fileMenu.add(restartGame);

        //SAVE GAME
        final JMenuItem saveGame = new JMenuItem("Save game");
        saveGame.addActionListener((ActionEvent e) -> {
            String saveName = JOptionPane.showInputDialog("Enter the name of the save:");
            //check if something was actually written as a name
            if (saveName != null && !saveName.isEmpty()) {
                System.out.println(saveName);
                BoardUtilities saver = new BoardUtilities();
                saver.saveBoard(ChessBoard, saveName, chessClock.printTime());

            } else {
                Baners.createWarningBanner("Save game", "Saving failed! You must enter file name.");
            }

        });
        fileMenu.add(saveGame);

        //SAVE GAME TO PGN
        final JMenuItem SaveToPGN = new JMenuItem("Save to PGN");
        SaveToPGN.addActionListener((ActionEvent e) -> {
            //check if something was actually written as a name
            String saveName = JOptionPane.showInputDialog("Enter the name of the save:");
            if (saveName != null && !saveName.isEmpty()) {
                saveName = saveName + ".pgn";
                utilitiesPGN utils = new utilitiesPGN();
                utils.saveGameToPGNFile(saveName, this.moveLog);
            } else {
                Baners.createWarningBanner("Save game", "Saving failed! You must enter file name.");
            }

        });
        fileMenu.add(SaveToPGN);

        //LOAD GAME
        final JMenuItem loadGame = new JMenuItem("Load game");
        loadGame.addActionListener((ActionEvent e) -> {

            System.out.println("Loading game from file!");
            String loadFileName = JOptionPane.showInputDialog("Enter the name of the file to load from:");
            if (loadFileName != null && !loadFileName.isEmpty()) {
                BoardUtilities loader = new BoardUtilities();
                ChessBoard = loader.loadBoard(loadFileName, ChessBoard);
                BoardVisual.drawBoard(ChessBoard);
                chessClock.setWhiteTime(ChessBoard.getWhitePlayerTimer());
                chessClock.setBlackTime(ChessBoard.getBlackPlayerTimer());
                moveLog.clear();
                gameHistoryPanel.redo(ChessBoard, moveLog);
            } else {
                Baners.createWarningBanner("Load game", "Loading failed! You must enter file name.");
            }
        });
        fileMenu.add(loadGame);

        //EXIT THE GAME
        final JMenuItem Exit = new JMenuItem("Exit the game");
        Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(Exit);

        return fileMenu;
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    //PREFEREnCES MENU
    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");

        //Highligh lega moves on/off
        final JCheckBoxMenuItem LegalMoveHighlighter = new JCheckBoxMenuItem(
                "Highlight Legal Moves", true);

        LegalMoveHighlighter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                highlightLegalMoves = LegalMoveHighlighter.isSelected();
            }
        });

        preferencesMenu.add(LegalMoveHighlighter);

        return preferencesMenu;
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    //NETWORK GAME MENU
    private JMenu createNetworkGameMenu() {
        final JMenu networkgameMenu = new JMenu("Network Game");

        //Start the network game
        final JMenuItem createServer = new JMenuItem("Start network game");

        createServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetRestartGame();
                networkGame = true;
                //IMPLEMENT STARTING THE SERVER

            }
        });
        networkgameMenu.add(createServer);

        //Connect to existing one
        final JMenuItem joinNetwork = new JMenuItem("Join network game");

        joinNetwork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                networkGame = true;
                //CLIENT SIDE OF THE ONLINE GAME
            }
        });

        networkgameMenu.add(joinNetwork);

        return networkgameMenu;
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    private JMenu createCurrentMenu() {

        final JMenu currentMenu = new JMenu("Move");

        //Undo last move method
        final JMenuItem lastMove = new JMenuItem("Undo last move");

        lastMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (moveLog.size() > 0) {
                    undoLastMove();
                }
            }
        });

        final JMenuItem moveTrack = new JMenuItem("Review all steps");
        moveTrack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveReviewPanel panel =new moveReviewPanel(gameFrame,roundsPlayed, ChessBoard);
            }
        });

        currentMenu.add(lastMove);
        currentMenu.add(moveTrack);

        return currentMenu;
    }
    //-------------------------------------------------------------------------------------------------------------------------------------

    //Steps performed to reset the game
    private void resetForNewGame() {
        ChessBoard = new Board(whitePlayerType, blackPlayerType, timeToSet, timeToSet);
        ChessBoard.startingGameSetup();
        BoardVisual.drawBoard(ChessBoard);
        chessClock.setWhiteTime(timeToSet);
        chessClock.setBlackTime(timeToSet);
        moveLog.clear();
        gameHistoryPanel.redo(ChessBoard, moveLog);
        roundsPlayed = 0;
        //chessClock.start();


    }

    //Steps performed while restarting the game
    private void resetRestartGame() {
        this.ChessBoard = new Board(whitePlayerType, blackPlayerType, whitePlayerTime, blackPlayerTime);
        ChessBoard.startingGameSetup();
        BoardVisual.drawBoard(ChessBoard);
        chessClock.setWhiteTime(whitePlayerTime);
        chessClock.setBlackTime(whitePlayerTime);
        moveLog.clear();
        gameHistoryPanel.redo(ChessBoard, moveLog);
        roundsPlayed = 0;
        // chessClock.start();

    }


    private void undoLastMove() {
        Move lastMove = moveLog.removeMove(moveLog.size() - 1);
        this.ChessBoard = ChessBoard.getCurrentPlayer().unMakeMove(lastMove).getTargetBoard();
        moveLog.removeMove(lastMove);
        gameHistoryPanel.redo(ChessBoard, moveLog);
        BoardVisual.drawBoard(ChessBoard);
        roundsPlayed --;


    }

    //setting tile pointer to null, relevant in tile GUI part
    private void restartPointers() {
        inputTile = null;
        destinationTile = null;
        movingPiece = null;
    }

    //Method to center the JFrame
    private void center(final JFrame frame) {
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final int w = frame.getSize().width;
        final int h = frame.getSize().height;
        final int x = (dim.width - w) / 2;
        final int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }


    //GUI BOARD REPRESENTATION
    //-------------------------------------------------------------------------------------------------------------------------------------
    //Method creates JPanel containing 64 chess tiles
    private class ChessBoardGUI extends JPanel {

        final ChessTileGUI[][] chessBoardTiles = new ChessTileGUI[Board.BOARD_COLUMNS][Board.BOARD_ROWS];

        //Create the 64 tiles in 8x8 layout, use grid layout
        ChessBoardGUI() {
            super(new GridLayout(8, 8));

            for (int i = 0; i < BOARD_ROWS; i++) {
                for (int j = 0; j < BOARD_COLUMNS; j++) {
                    final ChessTileGUI tile = new ChessTileGUI(this, new Position(i, j));
                    this.chessBoardTiles[i][j] = tile;
                    add(tile);
                }
            }

            setPreferredSize(CHESS_BOARD_DIM);
            validate();
        }

        //Repaint the chess-board to represent the current state
        void drawBoard(final Board board) {
            removeAll();
            for (int i = 0; i < BOARD_ROWS; i++) {
                for (int j = 0; j < BOARD_COLUMNS; j++) {
                    chessBoardTiles[i][j].drawTile(board);
                    add(chessBoardTiles[i][j]);
                }
            }
            validate();
            repaint();
        }
    }

    //GUI TILE REPRESENTATION
    //-------------------------------------------------------------------------------------------------------------------------------------
    private class ChessTileGUI extends JPanel {

        private final Position tilePosition;
        private Color lightTileColor = Color.decode("#f5deb3");
        private Color darkTileColor = Color.decode("#a96342");
        private String iconLocationPath = "src/main/resources/fancy/";

        public ChessTileGUI(ChessBoardGUI ChessBoardVisual, final Position tilePosition) {
            super(new GridBagLayout());
            this.tilePosition = tilePosition;
            setPreferredSize(CHESS_TILE_DIM);
            assignTileColor();
            assignTileAnIcon(ChessBoard);

            addMouseListener(new MouseListener() {

                                 @Override
                                 public void mouseClicked(MouseEvent event) {
                                     if (SwingUtilities.isRightMouseButton(event)) {

                                         restartPointers();

                                     } else if (SwingUtilities.isLeftMouseButton(event)) {
                                         if (ChessBoard.getCurrentPlayer().getPlayerType() == PlayerType.HUMAN) {
                                             if (inputTile == null) {
                                                 inputTile = ChessBoard.getTile(tilePosition);
                                                 movingPiece = inputTile.getPiece();
                                                 if (movingPiece == null) {
                                                     inputTile = null;
                                                 }
                                             } else {
                                                 destinationTile = ChessBoard.getTile(tilePosition);
                                                 //Check whether move is in players legals, if yes take it if not return empty move
                                                 final Move move = Move.MoveFactory.createMove(ChessBoard, inputTile.getTilePosition(), destinationTile.getTilePosition());
                                                 final MoveTransition transition = ChessBoard.getCurrentPlayer().makeMove(move);
                                                 if (transition.getMoveStatus() == MoveStatus.DONE) {
                                                     lastRoundBoard = ChessBoard.copyAndSetUpBoard();
                                                     lastRoundBoard.copiedBoardSetup();
                                                     ChessBoard = transition.getTargetBoard();
                                                     moveLog.addMove(move);
                                                     roundsPlayed ++;
                                                     if (ChessBoard.getCurrentPlayer().Check()) {
                                                         JOptionPane.showMessageDialog(null, ChessBoard.getCurrentPlayer().getAlliance().toString() + " player is in Check", "Check!", WARNING_MESSAGE);
                                                     }
                                                 } else if (transition.getMoveStatus() == MoveStatus.ENDS_IN_CHECK) {
                                                     JOptionPane.showMessageDialog(null, "Move ends in Check", "Invalid move", WARNING_MESSAGE);

                                                 }
                                                 restartPointers();
                                             }

                                             gameHistoryPanel.redo(ChessBoard, moveLog);
                                             ChessBoardVisual.drawBoard(ChessBoard);

                                             if (BoardUtilities.isCheckMate(ChessBoard)) {
                                                 JOptionPane.showMessageDialog(null, "Check mate on " + ChessBoard.getCurrentPlayer().toString() + "!" + "\nRestart the game via the Game menu", "GAME OVER", ERROR_MESSAGE);
                                             }
                                             if (BoardUtilities.isStalemate(ChessBoard)) {
                                                 JOptionPane.showMessageDialog(null, "Stalemate!" + "\nRestart the game via the Game menu", "GAME OVER", ERROR_MESSAGE);
                                             }
                                         }
                                         while (ChessBoard.getCurrentPlayer().getPlayerType() == PlayerType.COMPUTER) {
                                             ComputerPlayer AI = (ComputerPlayer) ChessBoard.getCurrentPlayer();
                                             Move AIMove = AI.getAI_Move();
                                             inputTile = ChessBoard.getTile(AIMove.getMovedPiece().getPiecePosition());
                                             destinationTile = ChessBoard.getTile(AIMove.getMoveDestination());

                                             final Move move = Move.MoveFactory.createMove(ChessBoard, inputTile.getTilePosition(), destinationTile.getTilePosition());
                                             final MoveTransition transition = ChessBoard.getCurrentPlayer().makeMove(move);

                                             if (transition.getMoveStatus() == MoveStatus.DONE) {
                                                 lastRoundBoard = ChessBoard.copyAndSetUpBoard();
                                                 lastRoundBoard.copiedBoardSetup();
                                                 ChessBoard = transition.getTargetBoard();
                                                 moveLog.addMove(move);
                                                 roundsPlayed ++;
                                                 computerMoves++;
                                                 if (computerMoves == 50) {
                                                     computerMoves = 0;
                                                     JOptionPane message = new JOptionPane();
                                                     int response = message.showConfirmDialog(null, "The computer has played 50 rounds. Do you want to start a new game and try it yourself? :)", "Demonstration game", YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                                                     switch (response) {
                                                         case YES_OPTION:
                                                             NewGameMenu GUI = new NewGameMenu(gameFrame, true);
                                                             resetForNewGame();
                                                             break;
                                                         case NO_OPTION:
                                                             break;
                                                     }
                                                 }
                                             } else {
                                                 continue;
                                             }
                                             restartPointers();

                                             gameHistoryPanel.redo(ChessBoard, moveLog);
                                             ChessBoardVisual.drawBoard(ChessBoard);

                                             if (BoardUtilities.isCheckMate(ChessBoard)) {
                                                 JOptionPane.showMessageDialog(null, "Check mate on " + ChessBoard.getCurrentPlayer().getAlliance().toString() + " player !" + "\nRestart the game via the Game menu", "GAME OVER", ERROR_MESSAGE);
                                                 break;
                                             }
                                             if (BoardUtilities.isStalemate(ChessBoard)) {
                                                 JOptionPane.showMessageDialog(null, "Stalemate!" + "\nRestart the game via the Game menu", "GAME OVER", ERROR_MESSAGE);
                                                 break;
                                             }
                                         }

                                     }
                                 }

                                 @Override
                                 public void mousePressed(MouseEvent e) {
                                 }

                                 @Override
                                 public void mouseReleased(MouseEvent e) {
                                 }

                                 @Override
                                 public void mouseEntered(MouseEvent e) {
                                 }

                                 @Override
                                 public void mouseExited(MouseEvent e) {
                                 }
                             }
            );
            validate();
        }


        public void drawTile(final Board board) {
            assignTileColor();
            assignTileAnIcon(board);
            highlightTileBorder(board);
            highlightLegals(board);
            validate();
            repaint();
        }

        //Method draws tile border to the selected tile, makes all tiles raised to better visual
        private void highlightTileBorder(Board board) {
            if (movingPiece != null && movingPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance() && movingPiece.getPiecePosition().positionsEqual(this.tilePosition)) {
                setBorder(BorderFactory.createDashedBorder(Color.RED, 15, 0));
            } else {
                setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
            }
        }

        //Method ensures highlighting of legal moves
        private void highlightLegals(final Board board) {
            try {
                GreenDot = new ImageIcon(ImageIO.read(new File("src/main/resources/misc/green_dot.png")));
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "FAILED TO LOAD THE GREEN DOT INDICATING THE LEGAL MOVE", ex);
            }
            //if highlighting is switched on; go through all legal moves for the piece and add a green dot to the tiles
            if (highlightLegalMoves) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getMoveDestination().positionsEqual(this.tilePosition)) {
                        add(new JLabel(GreenDot));
                    }
                }
            }
        }

        //Paints tiles to the two colors
        private void assignTileColor() {
            int xCoord = this.tilePosition.getxCoordinate();
            int yCoord = this.tilePosition.getyCoordinate();

            if (xCoord % 2 == 0) { //rows, eg. 0,2,4,6
                if (yCoord % 2 != 0) {
                    setBackground(darkTileColor);
                } else {
                    setBackground(lightTileColor);
                }
            } else { //rows 1,3,5,7 - switched around
                if (yCoord % 2 != 0) {
                    setBackground(lightTileColor);
                } else {
                    setBackground(darkTileColor);
                }
            }
        }

        //Method assigns chess pieces to the tile if it has the piece in the engine
        private void assignTileAnIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tilePosition).hasPiece()) { //check if tile has a piece on it
                BufferedImage image = null;
                //read image to the buffer
                try {
                    image = ImageIO.read(new File(iconLocationPath //get the piece icon for subsequent pieces
                            + board.getTile(this.tilePosition).getPiece().getPieceAlliance().toString() //get the piece alliance
                            + board.getTile(this.tilePosition).getPiece().toString() //get the piece tipe
                            + ".gif")); //add .gif

                } catch (IOException ex) {
                    Logger.getLogger(ChessTileGUI.class
                            .getName()).log(Level.SEVERE, "FAILED TO LOAD THE CHESS FIGURES", ex);
                }
                add(new JLabel(new ImageIcon(image)));
            }
        }

        //Takes players and calculates legal moves for the selected piece
        private Collection<Move> pieceLegalMoves(final Board board) {
            if (movingPiece != null && movingPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {

                Collection<Move> pieceLegalMoves;
                pieceLegalMoves = movingPiece.calculateLegalMoves(board);

                if (movingPiece.getPieceType() == PieceType.KING) {
                    pieceLegalMoves.addAll(board.getCurrentPlayer().calculateKingCastles(board.getCurrentPlayer().getPlayerPossibleMoves(), board.getCurrentPlayer().getOpponent().getPlayerPossibleMoves()));
                }

                return pieceLegalMoves;

            }
            return Collections.emptyList();
        }
    }

    //NEW GAME MENU DIALOG - Pop for new game creation
    //-------------------------------------------------------------------------------------------------------------------------------------
    private class NewGameMenu extends JDialog {

        private static final String HUMAN_TEXT = "Human";
        private static final String COMPUTER_TEXT = "Computer";
        JSlider TimeChoice;
        JLabel time;

        public NewGameMenu(JFrame frame, boolean modal) {
            super(frame, modal);

            final JPanel myPanel = new JPanel(new GridLayout(0, 1));
            myPanel.setSize(300, 300);

            //Selection buttions
            final JRadioButton whiteHumanButton = new JRadioButton(HUMAN_TEXT);
            final JRadioButton whiteComputerButton = new JRadioButton(COMPUTER_TEXT);
            final JRadioButton blackHumanButton = new JRadioButton(HUMAN_TEXT);
            final JRadioButton blackComputerButton = new JRadioButton(COMPUTER_TEXT);

            //Labes and Slider to set time
            this.time = new JLabel("Game duration in minutes: " + "120");
            this.TimeChoice = new JSlider(SwingConstants.HORIZONTAL, 1, 180, 120);
            this.TimeChoice.setMajorTickSpacing(60);
            this.TimeChoice.setValueIsAdjusting(true);
            this.TimeChoice.setMinorTickSpacing(5);
            this.TimeChoice.setPaintTicks(true);

            //White player buttons
            whiteHumanButton.setActionCommand(HUMAN_TEXT);
            final ButtonGroup whiteGroup = new ButtonGroup();
            whiteGroup.add(whiteHumanButton);
            whiteGroup.add(whiteComputerButton);
            whiteHumanButton.setSelected(true);

            //Black player buttons
            final ButtonGroup blackGroup = new ButtonGroup();
            blackGroup.add(blackHumanButton);
            blackGroup.add(blackComputerButton);
            blackHumanButton.setSelected(true);

            //Composition
            getContentPane().add(myPanel);
            myPanel.add(new JLabel("White Player   "));
            myPanel.add(whiteHumanButton);
            myPanel.add(whiteComputerButton);
            myPanel.add(new JLabel("Black Player                                "));
            myPanel.add(blackHumanButton);
            myPanel.add(blackComputerButton);

            myPanel.add(time);
            myPanel.add(this.TimeChoice);
            sliderSlide e = new sliderSlide();
            this.TimeChoice.addChangeListener(e);

            final JButton cancelButton = new JButton("Cancel");
            final JButton okButton = new JButton("OK");

            //OK Button functions
            okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    whitePlayerType = whiteComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                    blackPlayerType = blackComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                    long minutes = TimeChoice.getValue();
                    timeToSet = minutes * 60 * 1000;
                    System.out.println("OK");
                    NewGameMenu.this.setVisible(false);
                    resetForNewGame();
                    JOptionPane.showMessageDialog(null, "Game successfully started!", "New Game", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            //cancel button functions
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Cancel");
                    NewGameMenu.this.setVisible(false);
                }
            });

            myPanel.add(cancelButton);
            myPanel.add(okButton);

            this.setSize(400, 400);
            this.setResizable(false);
            setLocationRelativeTo(null);
            pack();
            setVisible(true);
        }

        //Slider change listener
        private class sliderSlide implements ChangeListener {
            public void stateChanged(ChangeEvent e) {
                time.setText("Game duration in minutes: " + TimeChoice.getValue());
            }
        }

    }

    //MOVE REVIEW PANEL
    //-------------------------------------------------------------------------------------------------------------------------------------
    public class moveReviewPanel extends JDialog{

        private JButton previousMoveButton;
        private JButton nextMoveButton;
        private JButton continueGameButton;
        private JButton restartButton;

        private JLabel instruction;
        private JPanel panel;
        private int currentRound;
        private Board originalBoard;

        public moveReviewPanel(JFrame frame, int turns, Board originalGameBoard){
            super(frame, true);
            this.currentRound = turns;
            originalBoard = originalGameBoard;

            previousMoveButton = new JButton("PREVIOUS MOVE");
            nextMoveButton = new JButton("NEXT MOVE");
            continueGameButton = new JButton("Continue Game at the END");
            restartButton = new JButton("Continue at this point");

            instruction = new JLabel("      Use buttons to navigate through the history of the game");
            panel = new JPanel(new GridLayout(9,1));

            panel.setSize(400,200 );

            panel.add(instruction);
            panel.add(new JLabel(""));
            panel.add(previousMoveButton);
            panel.add(new JLabel(""));
            panel.add(nextMoveButton);
            panel.add(new JLabel(""));
            panel.add(restartButton);
            panel.add(new JLabel(""));
            panel.add(continueGameButton);

            previousMoveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(currentRound > 0) {
                        LOG.log(Level.INFO, "PREVIOUS MOVE");
                        Move previousMove = moveLog.getMove(currentRound - 1);
                        ChessBoard = ChessBoard.getCurrentPlayer().unMakeMove(previousMove).getTargetBoard();
                        BoardVisual.drawBoard(ChessBoard);
                        currentRound--;
                    }else{
                        LOG.log(Level.INFO, "End reached");
                        Baners.createInfoBanner("Previous Move", "Beggining of the game reached!");
                    }

                }
            });

            nextMoveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(currentRound < turns) {
                        LOG.log(Level.INFO, "NEXT MOVE");
                        Move nextMove = moveLog.getMove(currentRound);
                        ChessBoard = ChessBoard.getCurrentPlayer().reconstructMove(nextMove).getTargetBoard();
                        BoardVisual.drawBoard(ChessBoard);
                        currentRound++;
                    }else{
                        LOG.log(Level.INFO, "End reached");
                        Baners.createInfoBanner("Next Move", "No more moves after this move!");
                    }

                }
            });

            restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LOG.log(Level.INFO  , "COntinuing game at current position");

                    if(currentRound == 0){
                        moveLog.clear();
                    }else {
                        moveLog.setMoves(moveLog.trimMoveLog(currentRound));
                        roundsPlayed = currentRound;
                    }
                    gameHistoryPanel.redo(ChessBoard, moveLog);
                    BoardVisual.drawBoard(ChessBoard);

                    setVisible(false);


                }
            });


            continueGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChessBoard = originalBoard;
                    BoardVisual.drawBoard(ChessBoard);
                    setVisible(false);
                }
            });

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    ChessBoard = originalBoard;
                    BoardVisual.drawBoard(ChessBoard);

                }
            });

            this.add(panel);
            this.setSize(400,300);
            this.setVisible(true);


        }

    }
    //CHESS CLOCK PANEL
    //-------------------------------------------------------------------------------------------------------------------------------------
    //Chess clock containing JPanel

    //Creates the GUI of the chess clock
    private class ChessClockContainer extends JPanel {

        private JLabel whitePlayerTimer;
        private JLabel blackPlayerTimer;
        private JLabel blackPlayer;
        private JLabel whitePlayer;


        private ChessClockContainer() {

            this.setLayout(new GridLayout(5, 1));

            //CREATE LABELS for player and to represent time
            whitePlayer = new JLabel("WHITE PLAYER TIMER", JLabel.CENTER);
            blackPlayer = new JLabel("BLACK PLAYER TIMER", JLabel.CENTER);
            whitePlayerTimer = new JLabel("0:00:00", JLabel.CENTER);
            blackPlayerTimer = new JLabel("0:00:00", JLabel.CENTER);

            this.add(blackPlayer);
            this.add(blackPlayerTimer);
            this.add(whitePlayer);
            this.add(whitePlayerTimer);

            this.setBorder(new EmptyBorder(10, 40, 10, 40));
            this.setSize(300, 300);
            this.setVisible(true);
            gameFrame.add(this, BorderLayout.WEST);

        }

        /**
         * Chess clock time getter
         *
         * @return JLabel showing the time remaining for the white player
         */
        public JLabel getWhitePlayerTimer() {
            return whitePlayerTimer;
        }

        /**
         * Chess clock time getter
         *
         * @return JLabel showing the time remaining for the black player
         */
        public JLabel getBlackPlayerTimer() {
            return blackPlayerTimer;
        }

    }


    //CHESS CLOCK ENGINE - creates the underlying engine that calculates the time remaining
    //Uses system time comparison to make precise calculations
    //-------------------------------------------------------------------------------------------------------------------------------------
    private class ChessClock extends Thread {

        private long blackTime;
        private long whiteTime;

        private ChessClock(long chessTIme) {
            this.blackTime = chessTIme;
            this.whiteTime = chessTIme;
        }

        utilsGUI convertor = new utilsGUI();
        ChessClockContainer TIME = new ChessClockContainer();

        /**
         * Run the thread making the calculation
         * Checks every 100ms which player is playing and deducts the difference in system time from his timer
         */
        public void run() {

            TIME.getBlackPlayerTimer().setText(String.valueOf(convertor.timeMiliConvertor(blackTime)));
            TIME.getWhitePlayerTimer().setText(String.valueOf(convertor.timeMiliConvertor(whiteTime)));

            while (blackTime > 100 && whiteTime > 100) {
                try {
                    long entryTime = System.currentTimeMillis(); //get the system time when the game begins
                    Thread.sleep(100);
                    //WHITE PLAYER TIMER
                    if (ChessBoard.getCurrentPlayer() == ChessBoard.getWhitePlayer()) {

                        TIME.getBlackPlayerTimer().setForeground(Color.BLACK);
                        TIME.getWhitePlayerTimer().setForeground(Color.BLUE);

                        long exitTime = System.currentTimeMillis();
                        long timespent = exitTime - entryTime;
                        whiteTime = whiteTime - timespent;

                        TIME.getWhitePlayerTimer().setText(String.valueOf(convertor.timeMiliConvertor(whiteTime)));
                    }
                    //BLACK PLAYER TIMER
                    if (ChessBoard.getCurrentPlayer() == ChessBoard.getBlackPlayer()) {

                        TIME.getBlackPlayerTimer().setForeground(Color.BLUE);
                        TIME.getWhitePlayerTimer().setForeground(Color.BLACK);

                        long exitTime = System.currentTimeMillis();
                        long timespent = exitTime - entryTime;
                        blackTime = blackTime - timespent;

                        TIME.getBlackPlayerTimer().setText(String.valueOf(convertor.timeMiliConvertor(blackTime)));
                    }

                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "THREAD PROBLEM", ex);
                }
            }

            if (whiteTime <= 100) {
                TIME.getWhitePlayerTimer().setForeground(Color.RED);
                JOptionPane.showMessageDialog(null, "White player ran out of time!", "GAME OVER", ERROR_MESSAGE);
            }

            if (blackTime <= 100) {
                TIME.getBlackPlayerTimer().setForeground(Color.RED);
                JOptionPane.showMessageDialog(null, "Black player ran out of time!", "GAME OVER", ERROR_MESSAGE);
            }

        }

        /**
         * Setter for the black player time.
         * Converts the milliseconds remaineder into standard digital clock format so that it can be printed out
         *
         * @param miliseconds - remaining time in milliseconds
         */
        public void setBlackTime(long miliseconds) {
            this.blackTime = miliseconds;
            TIME.getBlackPlayerTimer().setText(String.valueOf(convertor.timeMiliConvertor(blackTime)));
        }

        /**
         * Setter for the white player time.
         * Converts the milliseconds remaineder into standard digital clock format so that it can be printed out
         *
         * @param miliseconds - remaining time in milliseconds
         */
        public void setWhiteTime(long miliseconds) {
            this.whiteTime = miliseconds;
            TIME.getWhitePlayerTimer().setText(String.valueOf(convertor.timeMiliConvertor(whiteTime)));
        }

        /**
         * Get the remained of the time in miliseconds
         *
         * @return long, miliseconds of the time remaining
         */
        public long getWhiteTime() {
            return this.whiteTime;
        }

        /**
         * Get the remained of the time in miliseconds
         *
         * @return long, miliseconds of the time remaining
         */
        public long getBlackTime() {
            return this.blackTime;
        }

        /**
         * Method for saving game to TXT and saving the time of both players
         *
         * @return String, printed format of both players times reminder
         */
        public String printTime() {
            return ("#WHITE_PLAYER_TIMER_SECONDS# " + this.getWhiteTime() + " #BLACK_PLAYER_TIMER_SECONDS# " + this.getBlackTime() + " #");
        }
    }
}

