/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Chessman;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Move.Move;
import fel.cvut.cz.Player.Alliance;
import fel.cvut.cz.PortableGameNotation.PieceType;
import fel.cvut.cz.Utilities.Position;

import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Abstract class providing a foundation for representation of the chess piece.
 * Abstract methods outline checking legality of the piece moves, creating its deep copy and implementing of printing.
 *
 * @author Ryzner
 */
public abstract class GeneralPiece implements Serializable {

    private static final Logger LOG = Logger.getLogger(GeneralPiece.class.getName());

    /**
     *
     */
    protected final PieceType pieceType;

    /**
     *
     */
    protected final Alliance pieceAlliance;

    /**
     *
     */
    protected final Position piecePosition;

    /**
     *
     */
    protected final boolean isFirstMove;

    /**
     * Create a new chess piece instance. First move variable is set to according to the parameter.
     *
     * @param pieceType - specifies chess piece type to be created
     * @param piecePosition - the position of the piece on the chess board
     * @param alliance  - piece alliance according to the player alliance
     * @param isFirstMove - defines whether the piece has not moved yet (true - has not moved)
     */
    public GeneralPiece(final PieceType pieceType, final Position piecePosition, final Alliance alliance, boolean isFirstMove) {
        this.pieceAlliance = alliance;
        this.piecePosition = piecePosition;
        this.pieceType = pieceType;
        this.isFirstMove = isFirstMove;
    }

    /**
     * Abstract preparation for method calculating possible move position based on the position, current board
     * and chess piece type.
     *
     * @param board - current state of the chess board
     * @return ArrayList list of all possible moves by the chess piece.
     */

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    /**
     * Abstract preparation for a method used by a piece to make a move.
     *
     * @param move - desired move to be made by the chess piece
     * @return GeneralPiece new instance of the piece placed on the move destination position
     */
    public abstract GeneralPiece makeMove(final Move move);

    /**
     * Getter for piece type
     *
     * @return PieceType - piece type
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Getter for piece alliance
     *
     * @return Alliance - piece alliance
     */
    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    /**
     * Getter for piece position xCoordinate, representing row of the chessboard.
     *
     * @return int, xcoordinate
     */
    public int getxCoordinate() {
        return this.piecePosition.getxCoordinate();
    }

    /**
     * Getter for piece position yCoordinate, representing columns of the chessboard.
     *
     * @return int, yCoordinate
     */
    public int getyCoordinate() {
        return this.piecePosition.getyCoordinate();
    }

    /**
     * Getter for the piece position on the chessboard
     *
     * @return Position - piece current position on the board
     */
    public Position getPiecePosition() {
        return piecePosition;
    }

    /**
     * Abstract outline for the deep copy function of each piece type
     *
     * @return GeneralPiece deep copy instance of the piece
     */
    public abstract GeneralPiece getCopy();

    /**
     *Getter for the isFirstMove variable
     *
     * @return boolean - true means that the piece has not moved yet
     */
    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    /**
     * Abstract method preparation. MEthod used for string representation of the piece info.
     *
     * @return String - standardised piece information for txt save.
     */
    public abstract String printToSave();


}
