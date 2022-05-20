package fel.cvut.cz.Move;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Chessman.GeneralPiece;
import fel.cvut.cz.PortableGameNotation.utilitiesPGN;
import fel.cvut.cz.Utilities.Position;

import java.util.logging.Logger;

/**
 * Class representing pawn attacking move, contains the underlying PGN notation printing method.
 *
 * @author Ryzner
 */
public class PawnAttackMove extends AttackMove {

    private static final Logger LOG = Logger.getLogger(PawnAttackMove.class.getName());

    /**
     * Fabricates an instance of the pawn attack move
     *
     * @param board current game board
     * @param desiredPosition destination of the move
     * @param pieceMoved piece on the move
     * @param pieceAttacked piece attacked by the move
     */
    public PawnAttackMove(final Board board, final Position desiredPosition, final GeneralPiece pieceMoved, GeneralPiece pieceAttacked) {
        super(board, desiredPosition, pieceMoved, pieceAttacked);
        this.moveType = MoveType.PAWN_ATTACK_MOVE;
    }

    /**
     * PGN format representing printing method
     * @return 
     */
    @Override
    public String toString() {
        return utilitiesPGN.getPositionAtCoordinate(this.getMovedPiece().getPiecePosition()).substring(0, 1) + "x" + utilitiesPGN.getPositionAtCoordinate(this.MoveDestination);
    }


}
