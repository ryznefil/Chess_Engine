/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Move;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Chessman.GeneralPiece;
import fel.cvut.cz.Utilities.Position;

import java.io.Serializable;
import java.util.logging.Logger;

import static fel.cvut.cz.PortableGameNotation.utilitiesPGN.getPositionAtCoordinate;

/**
 * Abstract Move class serves as a foundation for underlying inherited classes
 * which support the move making in the game.
 *
 * @author Ryzny
 */
public abstract class Move implements Serializable {

    private static final Logger LOG = Logger.getLogger(Move.class.getName());

    /**
     *
     */
    final protected Position MoveDestination;

    /**
     *
     */
    final protected Board originalGameBoard;

    /**
     *
     */
    final protected GeneralPiece MovedPiece;

    /**
     *
     */
    final protected boolean isFirstMove;

    /**
     *
     */
    protected MoveType moveType;

    /**
     * Constructor for the move class
     *
     * @param board - current game board
     * @param MoveDestinationGiven
     * @param pieceMoved - piece moving
     * @param moveType
     */
    public Move(final Board board, final Position MoveDestinationGiven, final GeneralPiece pieceMoved, MoveType moveType) {
        this.originalGameBoard = board;
        this.MoveDestination = MoveDestinationGiven;
        this.MovedPiece = pieceMoved;
        this.isFirstMove = pieceMoved.isFirstMove();
        this.moveType = moveType;
    }

    /**
     * Constructor for the move class without moving piece given.
     *
     * @param board current game board
     * @param MoveDestinationGiven
     */
    public Move(final Board board, final Position MoveDestinationGiven) {
        this.originalGameBoard = board;
        this.MoveDestination = MoveDestinationGiven;
        this.MovedPiece = null;
        this.isFirstMove = false;
        this.moveType = null;
    }

    /**
     * Getter for the initial game board
     *
     * @return Board
     */
    public Board getOriginalGameBoard() {
        return originalGameBoard;
    }


    /**
     * getter for the moving piece current position
     *
     * @return position of the piece
     */
    public Position getCurrentPosition() {
        return this.MovedPiece.getPiecePosition();
    }

    /**
     * getter for the move destination position
     *
     * @return position
     */
    public Position getMoveDestination() {
        return MoveDestination;
    }

    /**
     * getter for the moving piece
     *
     * @return GeneralPiece - piece moving
     */
    public GeneralPiece getMovedPiece() {
        return MovedPiece;
    }


    /**
     * Method to decide whether move is an attacking move
     *
     * @return boolean
     */
    public boolean isAttack() {
        return false;
    }

    /**
     * Method to decide whether move is a castling move
     *
     * @return boolean
     */
    public boolean isCastlingMove() {
        return false;
    }

    /**
     *
     * @return
     */
    public MoveType getMoveType() {
        return moveType;
    }

    /**
     * Getter for attacked piece
     *
     * @return General piece
     */
    public GeneralPiece getAttackedPiece() {
        return null;
    }

    /**
     * Method executing the desired move. Creates a copy of the game board and executes the move, if everything is valid,
     * it will return the copy of the board after the move was made.
     *
     * @return Board - state after the move
     */
    public Board executeMove() {
        Board clonedBoard = originalGameBoard.copyAndSetUpBoard();

        Position startingPiecePosition = this.MovedPiece.getPiecePosition();
        clonedBoard.getTile(startingPiecePosition).deletePiece();
        clonedBoard.getTile(MoveDestination).setPiece(this.MovedPiece.makeMove(this));

        clonedBoard.copiedBoardSetup();

        clonedBoard.setCurrentPlayer(clonedBoard.getCurrentPlayer().getOpponent());
        return clonedBoard;
    }

    public Board undoMove(){
        Board clonedBoard = originalGameBoard.copyAndSetUpBoard();

        clonedBoard.setCurrentPlayer(originalGameBoard.getCurrentPlayer());

        return clonedBoard;
    }


    @Override
    public String toString() {
        return "Move[" + "MoveDestination=" + MoveDestination + " MovedPiece=" + MovedPiece + ']' + "\n";
    }


    /**
     *Move factory is used for matching player desired move with the legal moves of the player
     * and checking legality of the move. It takes the move desired by player and cross validate it to the List of legal moves
     * that can be done by the piece. If a match is found, it is returned.
     *
     */
    public static class MoveFactory {

        private static final Move NULL_MOVE = new EmptyMove();

        /**
         * @param board - current game board
         * @param currentPosition - current piece position
         * @param destinationPosition - destination position of the piece
         * @return return the desired move, if it is located in the player legal moves
         */
        public static Move createMove(final Board board, final Position currentPosition, final Position destinationPosition) {
            for (final Move move : board.getCurrentPlayer().getPlayerPossibleMoves()) {
                if ((move.getCurrentPosition().positionsEqual(currentPosition)) && (move.getMoveDestination().positionsEqual(destinationPosition))) {
                    return move;
                }
            }

            return NULL_MOVE;
        }
    }

    /**
     * Method used in PGN format printing. Used to add extra specification in case that two pieces of the same type
     * could move to the same position. All according to the PGN format standards.
     * @return 
     */
    public String disambiguationFunctionForPGN() {
        for (final Move move : this.originalGameBoard.getCurrentPlayer().getPlayerPossibleMoves()) {
            if (move.getMoveDestination().positionsEqual(this.MoveDestination) &&
                    !(this.MovedPiece.getPiecePosition().positionsEqual(move.getMovedPiece().getPiecePosition())) &&
                    (this.MovedPiece.getPieceType() == move.getMovedPiece().getPieceType())) {
                return getPositionAtCoordinate(this.MovedPiece.getPiecePosition()).substring(0, 1);
            }
        }
        return "";
    }



}
