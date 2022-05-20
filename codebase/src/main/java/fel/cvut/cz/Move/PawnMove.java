package fel.cvut.cz.Move;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Chessman.GeneralPiece;
import fel.cvut.cz.PortableGameNotation.utilitiesPGN;
import fel.cvut.cz.Utilities.Position;

import java.util.logging.Logger;

/**
 * Class representing pawn standard move
 *
 * @author Ryzny
 */
public class PawnMove extends StandardMove {

    private static final Logger LOG = Logger.getLogger(PawnMove.class.getName());

    /**
     * Creates pawn standard move
     *
     * @param board - current game board
     * @param desiredPosition - target position
     * @param pieceMoved
     */
    public PawnMove(final Board board, final Position desiredPosition, final GeneralPiece pieceMoved) {
        super(board, desiredPosition, pieceMoved);
        this.moveType = MoveType.PAWN_MOVE;
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
