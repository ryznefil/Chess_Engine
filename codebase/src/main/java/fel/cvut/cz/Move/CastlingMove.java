/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Move;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Chessman.GeneralPiece;
import fel.cvut.cz.Chessman.Rook;
import fel.cvut.cz.Utilities.Position;

import java.util.logging.Logger;

/**
 * Class representation of the castling move. Contains important uderlying method for the
 * execution of both king and queen side castling moves.
 *
 * @author Ryzner
 */
public abstract class CastlingMove extends Move {

    private static final Logger LOG = Logger.getLogger(CastlingMove.class.getName());

    /**
     *
     */
    protected Rook castledRook;

    /**
     *
     */
    protected Position RookPosition;

    /**
     *
     */
    protected Position RookDestination;

    /**
     * General constructor for the castling move
     *
     * @param originalBoard - current state of the game chess board
     * @param MoveDestinationGiven - position to move to
     * @param king - king initiating the castling move
     * @param rookInCastle - rook participating on the castling move
     * @param kingPosition - current king position
     * @param rookDestination - position where the rook will be after the castling
     */
    public CastlingMove(Board originalBoard, Position MoveDestinationGiven, GeneralPiece king, Rook rookInCastle, Position kingPosition, Position rookDestination) {
        super(originalBoard, MoveDestinationGiven, king, MoveType.CASTLING_MOVE);
        this.castledRook = rookInCastle;
        this.RookPosition = kingPosition;
        this.RookDestination = rookDestination;

    }
    /**
     *Identificator of the castling move
     *
     * @return boolean true
     */
    @Override
    public boolean isCastlingMove() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public Board executeMove() {
        Board clonedBoard = originalGameBoard.copyAndSetUpBoard();

        Position startingPiecePosition = this.MovedPiece.getPiecePosition();

        //move the king that has to initialize the castling and also the rook
        clonedBoard.getTile(MoveDestination).setPiece(this.MovedPiece.makeMove(this));
        clonedBoard.getTile(RookDestination).setPiece(new Rook(RookDestination, castledRook.getPieceAlliance(), false));

        //Delete initial king and the initial rook
        clonedBoard.getTile(startingPiecePosition).deletePiece();
        clonedBoard.getTile(RookPosition).deletePiece();
        clonedBoard.copiedBoardSetup();

        clonedBoard.setCurrentPlayer(clonedBoard.getCurrentPlayer().getOpponent());

        return clonedBoard;
    }

    /**
     *
     */
    public static final class KingSideCastleMove extends CastlingMove {

        /**
         * Constructor for the king side castling move
         *
         * @param originalBoard - current state of the game chess board
         * @param MoveDestinationGiven - position to move to
         * @param king - king initiating the castling move
         * @param rookInCastle - rook participating on the castling move
         * @param kingPosition - current king position
         * @param rookDestination - position where the rook will be after the castling
         */
        public KingSideCastleMove(Board originalBoard, Position MoveDestinationGiven, GeneralPiece king, Rook rookInCastle, Position kingPosition, Position rookDestination) {
            super(originalBoard, MoveDestinationGiven, king, rookInCastle, kingPosition, rookDestination);
        }

        /**
         *PGN print method
         * @return 
         */
        @Override
        public String toString() {
            return "O-O";
        }

    }

    /**
     *
     */
    public static final class QueenSideCastleMove extends CastlingMove {

        /**
         * Constructor for the queen side castling move
         *
         * @param originalBoard - current state of the game chess board
         * @param MoveDestinationGiven - position to move to
         * @param king - king initiating the castling move
         * @param rookInCastle - rook participating on the castling move
         * @param kingPosition - current king position
         * @param rookDestination - position where the rook will be after the castling
         */
        public QueenSideCastleMove(Board originalBoard, Position MoveDestinationGiven, GeneralPiece king, Rook rookInCastle, Position kingPosition, Position rookDestination) {
            super(originalBoard, MoveDestinationGiven, king, rookInCastle, kingPosition, rookDestination);
        }

        /**
         *PGN print method
         * @return 
         */
        @Override
        public String toString() {
            return "O-O-O";
        }

    }

}
