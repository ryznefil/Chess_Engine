/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Board;

import fel.cvut.cz.Chessman.*;
import fel.cvut.cz.Move.Move;
import fel.cvut.cz.Player.*;
import fel.cvut.cz.Utilities.Position;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * The Board class creates an instance of the chess board. Chess Board is
 * composed of 64 instances of the Tile class. Contains method for ASCII
 * printing of the board.
 *
 * @author Ryzner
 */
public class Board implements Serializable{

    private static final Logger LOG = Logger.getLogger(Board.class.getName());

    /**
     * Constant containing number of chess board columns.
     */
    public final static int BOARD_COLUMNS = 8;

    /**
     * Constant containing number of chess board rows.
     */
    public final static int BOARD_ROWS = 8;

    private Pawn EnPassantPawn;
    private GeneralPlayer whitePlayer;
    private GeneralPlayer blackPlayer;
    private GeneralPlayer currentPlayer;
    private Collection<GeneralPiece> whitePieces;
    private Collection<GeneralPiece> blackPieces;
    private Collection<Move> whitePiecesPossibleMoves;
    private Collection<Move> blackPiecesPossibleMoves;

    private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;
    private long whitePlayerTimer;
    private long blackPlayerTimer;

    private final Tile Board[][] = new Tile[BOARD_ROWS][BOARD_COLUMNS];

    /**
     * Construct Chess Board
     * <p>
     * Construct the chess board, constructs a board of 64 empty tiles To
     * fill figures "startingGameSetup" needs to be used.
     * @param WhitePlayerType
     * @param BlackPlayerType
     * @param whitePlayerTimer
     * @param blackPlayerTimer
     */
    public Board(PlayerType WhitePlayerType, PlayerType BlackPlayerType, long whitePlayerTimer, long blackPlayerTimer) {
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLUMNS; j++) {
                Board[i][j] = new Tile(i, j);
            }
        }

        this.whitePlayerType = WhitePlayerType;
        this.blackPlayerType = BlackPlayerType;

        this.currentPlayer = null;
        this.EnPassantPawn = null;
        this.whitePlayerTimer = whitePlayerTimer;
        this.blackPlayerTimer = blackPlayerTimer;

    }

    /**
     * Prepare pieces for the standard starting game
     * <p>
     * Method needs to be called on the board following its construction in
     * constructor to lay out the standard game setup.
     */
    public void startingGameSetup() {
        startingBoard();
        GeneralSetup();
        System.out.println("HELL");
        currentPlayer = whitePlayer;
        System.out.println("NOPE");
    }

    /**
     * Copy Chess board and place pieces on it
     * <p>
     * Method is used to copy the current board and subsequently sets it up, eg.
     * calculate legal moves, assigning players ...
     *
     * @return copy of the initial chess Board
     */
    public Board copyAndSetUpBoard() {
        Board clone = this.getCopy();
        clone.copiedBoardSetup();
        return clone;
    }

    /**
     * Set up all variables related to the copied board
     * <p>
     * Sets up the copied board, recalculates the legal moves for each player,
     * his/her pieces determines player's situation. Should be called after a
     * move is made to update all respective values, such as number of active
     * pieces on board etc ...
     */
    public void copiedBoardSetup() {
        GeneralSetup();
        if (this.currentPlayer.getAlliance() == Alliance.WHITE) {
            this.currentPlayer = this.whitePlayer;

        } else {
            this.currentPlayer = this.blackPlayer;
        }
    }

    private void GeneralSetup() {
        //Ensures the general setup - active pieces for players, legal moves for players, creates new instances of players
        this.whitePieces = calculateActivePieces(Board, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(Board, Alliance.BLACK);

        this.whitePiecesPossibleMoves = calculateLegalMoves(whitePieces);
        this.blackPiecesPossibleMoves = calculateLegalMoves(blackPieces);

        //Setting copied player types
        if (this.whitePlayerType == PlayerType.HUMAN) {
            this.whitePlayer = new HumanPlayer(this, Alliance.WHITE, whitePiecesPossibleMoves, blackPiecesPossibleMoves);
        } else {
            this.whitePlayer = new ComputerPlayer(this, Alliance.WHITE, whitePiecesPossibleMoves, blackPiecesPossibleMoves);
        }

        if (this.blackPlayerType == PlayerType.HUMAN) {
            this.blackPlayer = new HumanPlayer(this, Alliance.BLACK, blackPiecesPossibleMoves, whitePiecesPossibleMoves);
        } else {
            this.blackPlayer = new ComputerPlayer(this, Alliance.BLACK, blackPiecesPossibleMoves, whitePiecesPossibleMoves);
        }
    }


    //Create the starting setup for the pieces on the chessboard
    private void startingBoard() {
        //BLACK Player pieces spawning
        Board[0][0].setPiece(new Rook(new Position(0, 0), Alliance.BLACK));
        Board[0][7].setPiece(new Rook(new Position(0, 7), Alliance.BLACK));
        Board[0][1].setPiece(new Knight(new Position(0, 1), Alliance.BLACK));
        Board[0][6].setPiece(new Knight(new Position(0, 6), Alliance.BLACK));
        Board[0][2].setPiece(new Bishop(new Position(0, 2), Alliance.BLACK));
        Board[0][5].setPiece(new Bishop(new Position(0, 5), Alliance.BLACK));
        Board[0][3].setPiece(new Queen(new Position(0, 3), Alliance.BLACK));
        Board[0][4].setPiece(new King(new Position(0, 4), Alliance.BLACK));

        Board[1][0].setPiece(new Pawn(new Position(1, 0), Alliance.BLACK));
        Board[1][1].setPiece(new Pawn(new Position(1, 1), Alliance.BLACK));
        Board[1][2].setPiece(new Pawn(new Position(1, 2), Alliance.BLACK));
        Board[1][3].setPiece(new Pawn(new Position(1, 3), Alliance.BLACK));
        Board[1][4].setPiece(new Pawn(new Position(1, 4), Alliance.BLACK));
        Board[1][5].setPiece(new Pawn(new Position(1, 5), Alliance.BLACK));
        Board[1][6].setPiece(new Pawn(new Position(1, 6), Alliance.BLACK));
        Board[1][7].setPiece(new Pawn(new Position(1, 7), Alliance.BLACK));

        //WHITE Player pieces spawning
        Board[7][0].setPiece(new Rook(new Position(7, 0), Alliance.WHITE));
        Board[7][7].setPiece(new Rook(new Position(7, 7), Alliance.WHITE));
        Board[7][1].setPiece(new Knight(new Position(7, 1), Alliance.WHITE));
        Board[7][6].setPiece(new Knight(new Position(7, 6), Alliance.WHITE));
        Board[7][2].setPiece(new Bishop(new Position(7, 2), Alliance.WHITE));
        Board[7][5].setPiece(new Bishop(new Position(7, 5), Alliance.WHITE));
        Board[7][3].setPiece(new Queen(new Position(7, 3), Alliance.WHITE));
        Board[7][4].setPiece(new King(new Position(7, 4), Alliance.WHITE));

        Board[6][0].setPiece(new Pawn(new Position(6, 0), Alliance.WHITE));
        Board[6][1].setPiece(new Pawn(new Position(6, 1), Alliance.WHITE));
        Board[6][2].setPiece(new Pawn(new Position(6, 2), Alliance.WHITE));
        Board[6][3].setPiece(new Pawn(new Position(6, 3), Alliance.WHITE));
        Board[6][4].setPiece(new Pawn(new Position(6, 4), Alliance.WHITE));
        Board[6][5].setPiece(new Pawn(new Position(6, 5), Alliance.WHITE));
        Board[6][6].setPiece(new Pawn(new Position(6, 6), Alliance.WHITE));
        Board[6][7].setPiece(new Pawn(new Position(6, 7), Alliance.WHITE));

    }

    /**
     * Calculate Active pieces per player.
     * <p>
     * Method calculates the number of active pieces for the player of
     * respective alliance, eg. pieces that have not been taken yet.
     *
     * @param board          - chess board instance
     * @param playerAlliance - White or Black
     * @return collection of active pieces owned by the player
     */
    public Collection<GeneralPiece> calculateActivePieces(final Tile[][] board, final Alliance playerAlliance) {
        List<GeneralPiece> activePieces = new ArrayList<>();
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLUMNS; j++) {
                if (board[i][j].hasPiece()) {
                    GeneralPiece pieceOnTile = board[i][j].getPiece();
                    if (pieceOnTile.getPieceAlliance() == playerAlliance) {
                        activePieces.add(pieceOnTile);
                    }
                }
            }
        }
        return activePieces;
    }

    /**
     * Calculate legal moves per player.
     * <p>
     * Method takes in the list of currently active pieces owned by a player and
     * calculates all the possible moves that can be made with them.
     *
     * @param piecesOwned - not taken pieces owned by player
     * @return list of pieces owned by player
     */
    public Collection<Move> calculateLegalMoves(final Collection<GeneralPiece> piecesOwned) {
        List<Move> allPossibleMoves = new ArrayList<>();
        for (final GeneralPiece piece : piecesOwned) {
            allPossibleMoves.addAll(piece.calculateLegalMoves(this));
        }
        return allPossibleMoves;
    }

    //Manually create a deep copy of the chess board
    private Board getCopy() {
        Board copyBoard = new Board(this.whitePlayerType, this.blackPlayerType, this.whitePlayerTimer, this.blackPlayerTimer);
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLUMNS; j++) {
                if (Board[i][j].getPiece() != null) {
                    copyBoard.getTile(i, j).setPiece(Board[i][j].getPiece().getCopy());
                }
            }
            copyBoard.currentPlayer = this.currentPlayer;
        }
        return copyBoard;
    }

    /**
     * Check if last move was a double step by a pawn.
     * <p>
     * Determines whether the board has an PawnEnPassant move. Only true if the last
     * round opponents pawn did double step.
     *
     * @return true/false
     */
    public boolean hasEnPassantPawn() {
        return EnPassantPawn != null;
    }

    /**
     * Returns the pawn that played double move in the last round can be taken via EnPassant.
     *
     * @return Pawn the pawn that played double step
     */
    public Pawn getEnPassantPawn() {
        return EnPassantPawn;
    }

    /**
     * Get white player type, eg. Computer or Human
     *
     * @return white player type
     */
    public GeneralPlayer getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * Get black player type, eg. Computer or Human
     *
     * @return black player type
     */
    public GeneralPlayer getBlackPlayer() {
        return blackPlayer;
    }

    /**
     * Method used to get the current player - eg. player that is eligible to make move now
     *
     * @return current player on the move
     */
    public GeneralPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @param nextPlayer
     */
    public void setCurrentPlayer(final GeneralPlayer nextPlayer) {
        this.currentPlayer = nextPlayer;
    }

    /**
     * @param EnPassantPawn
     */
    public void setEnPassantPawn(Pawn EnPassantPawn) {
        this.EnPassantPawn = EnPassantPawn;
    }

    /**
     * Return the instance of tile on the respective coordinates.
     *
     * @param x row representing coordinate
     * @param y column representing coordinate
     * @return instance of the tile
     */
    public Tile getTile(final int x, final int y) {
        return Board[x][y];
    }

    /**
     * Sets tile on a given position to a given tile instance
     *
     * @param tilePosition the position of the tile on the chess board
     * @param tile the tile of the chess board
     */
    public void setTile(final Position tilePosition, final Tile tile) {
        Board[tilePosition.getxCoordinate()][tilePosition.getyCoordinate()] = tile;
    }

    /**
     * Returns the instance of a tile on a given position
     *
     * @param tilePosition position of the tile on the chess board
     * @return Tile the chess board tile on the position
     */
    public Tile getTile(final Position tilePosition) {
        return Board[tilePosition.getxCoordinate()][tilePosition.getyCoordinate()];
    }

    /**
     * Access the number of active, eg. not taken, pieces owned by the White player.
     *
     * @return all not taken pieces owned by the White player
     */
    public Collection<GeneralPiece> getWhitePieces() {
        return whitePieces;
    }

    /**
     * Access the number of active, eg. not taken, pieces owned by the Black player.
     *
     * @return all not taken pieces owned by the Black player
     */
    public Collection<GeneralPiece> getBlackPieces() {
        return blackPieces;
    }


    /**
     * Access the number of active, eg. not taken, pieces owned by the Black player.
     *
     * @return the initial time allocation given to the black player for the game
     */
    public long getWhitePlayerTimer() {
        return this.whitePlayerTimer;
    }


    /**
     * Access the number of active, eg. not taken, pieces owned by the Black player.
     *
     * @return the initial time allocation given to the black player for the game
     */
    public long getBlackPlayerTimer() {
        return this.blackPlayerTimer;
    }


    /**
     * Sets the current player of the game to a given white alliance. Used while loading saved game from PGN or txt.
     */
    public void setCurrentPlayerToWhite() {
        this.currentPlayer = this.whitePlayer;
    }

    /**
     * Sets the current player of the game to a given black alliance. Used while loading saved game from PGN or txt.
     */
    public void setCurrentPlayerToBlack() {
        this.currentPlayer = this.blackPlayer;
    }

    /**
     * Method used for creating string version of the data related to the current player. Used for saving the state of the game while saving it.
     *
     * @return String form of the current player alliance and type
     */
    public String printCurrentPlayer() {
        return ("#CURRENT_PLAYER# " + this.currentPlayer.getAlliance() + " #PLAYER_TYPE# " + this.currentPlayer.getPlayerType() + " #");
    }

    /**
     * Method used for creating string version of the data related to the opponent player. Used for saving the state of the game while saving it.
     *
     * @return String form of the opponent player alliance and type
     */
    public String printOpponentPlayer() {
        return ("#OPPOSITION_PLAYER# " + this.currentPlayer.getOpponent().getAlliance() + " #PLAYER_TYPE# " + this.currentPlayer.getOpponent().getPlayerType() + " #");
    }

    /**
     * Method is used for creating ASCII look of the board.
     * Each tile on the board is then filled using the prettyPrint method for tiles.
     *
     * @return ASCII look of the board
     */
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLUMNS; j++) {
                String tileText = prettyPrint(Board[i][j]);
                stringBuilder.append(String.format("%3s", tileText));
                if ((j % 7 == 0) && (j != 0)) {
                    stringBuilder.append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Method used for printing tiles of the chess board in the ASCII format.
     * If piece is located on the tile, letter representing the piece is added.
     * Black alliance pieces are represented by lower case letter, white alliance by upper case letters.
     *
     * @param tile given chess board tile
     * @return ASCII version of the tile
     */
    public static String prettyPrint(final Tile tile) {
        if (tile.hasPiece()) {
            if (tile.getPieceOnTile().getPieceAlliance() == Alliance.BLACK) {
                return tile.toString().toLowerCase();
            } else {
                return tile.toString();
            }
        }
        return tile.toString();
    }

}
