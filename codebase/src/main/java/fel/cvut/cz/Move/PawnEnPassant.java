package fel.cvut.cz.Move;


import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Chessman.GeneralPiece;
import fel.cvut.cz.Utilities.Position;

import java.util.logging.Logger;

/**
 * Subclass representing pawn EnPAssant attack, contains PGN print method and move execution method.
 *
 * @author Ryzny
 */
public class PawnEnPassant extends PawnAttackMove {

    private static final Logger LOG = Logger.getLogger(PawnEnPassant.class.getName());

    /**
     * Creates the enPassant move instance
     *
     * @param board current game board
     * @param desiredPosition destination of the move
     * @param pieceMoved piece on the move
     * @param pieceAttacked piece attacked by the move
     */
    public PawnEnPassant(final Board board, final Position desiredPosition, final GeneralPiece pieceMoved, GeneralPiece pieceAttacked) {
        super(board, desiredPosition, pieceMoved, pieceAttacked);
        this.moveType = MoveType.PAWN_EN_PASSANT;
    }

    /**
     * Method does the EnPassant move required by the move object and creates a copy of the board which it returns.
     *
     * @return Board - deep copy of the board after the move is done
     */
    @Override
    public Board executeMove() {
        Board clonedBoard = originalGameBoard.copyAndSetUpBoard();

        Position startingPiecePosition = this.MovedPiece.getPiecePosition();
        Position takenPiecePosition = this.getAttackedPiece().getPiecePosition();

        clonedBoard.getTile(startingPiecePosition).deletePiece();
        clonedBoard.getTile(takenPiecePosition).deletePiece();
//
        clonedBoard.getTile(MoveDestination).setPiece(this.MovedPiece.makeMove(this));

        clonedBoard.copiedBoardSetup();

        clonedBoard.setCurrentPlayer(clonedBoard.getCurrentPlayer().getOpponent());
        return clonedBoard;
    }
}
