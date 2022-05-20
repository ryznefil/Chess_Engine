package fel.cvut.cz.Move;


import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Chessman.GeneralPiece;
import fel.cvut.cz.Chessman.Pawn;
import fel.cvut.cz.PortableGameNotation.utilitiesPGN;
import fel.cvut.cz.Utilities.Position;

import java.util.logging.Logger;

/**
 * Class representing the pawn double move in his first round of movement.
 *
 * @author Ryzner
 */
public class PawnFirstMove extends PawnMove {

    private static final Logger LOG = Logger.getLogger(PawnFirstMove.class.getName());

    /**
     * Constructor for the first move class
     *
     * @param board current game board
     * @param desiredPosition destination of the move
     * @param pieceMoved piece on the move
     */
    public PawnFirstMove(final Board board, final Position desiredPosition, final GeneralPiece pieceMoved) {
        super(board, desiredPosition, pieceMoved);
        this.moveType = MoveType.PAWN_FIRST_MOVE;
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

        clonedBoard.getTile(MoveDestination).setPiece(this.MovedPiece.makeMove(this));
        clonedBoard.getTile(startingPiecePosition).deletePiece();
        Pawn movedPawn = (Pawn) clonedBoard.getTile(MoveDestination).getPiece();

        //clonedBoard.copiedBoardSetup();
        clonedBoard.setEnPassantPawn(movedPawn);
        clonedBoard.copiedBoardSetup();
        clonedBoard.setCurrentPlayer(clonedBoard.getCurrentPlayer().getOpponent());

        return clonedBoard;
    }

    /**
     * PGN format representing printing method
     * @return 
     */
    @Override
    public String toString() {
        return utilitiesPGN.getPositionAtCoordinate(this.MoveDestination);
    }
}
