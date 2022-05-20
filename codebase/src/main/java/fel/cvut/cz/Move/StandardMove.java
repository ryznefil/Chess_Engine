/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Move;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Chessman.GeneralPiece;
import fel.cvut.cz.PortableGameNotation.utilitiesPGN;
import fel.cvut.cz.Utilities.Position;

import java.util.logging.Logger;

/**
 * Generates an instance of a standard move, e.g. a move that does not include
 * taking opponents piece.
 *
 * @author Ryzny
 */
public class StandardMove extends Move {

    private static final Logger LOG = Logger.getLogger(StandardMove.class.getName());

    /**
     * Creates an instance of the standard move.
     *
     * @param originalBoard
     * @param MoveDestinationGiven
     * @param pieceMoved
     */
    public StandardMove(Board originalBoard, Position MoveDestinationGiven, GeneralPiece pieceMoved) {
        super(originalBoard, MoveDestinationGiven, pieceMoved, MoveType.STANDARD_MOVE);
    }

    /**
     * PGN format representing printing method
     * @return 
     */
    @Override
    public String toString() {
        return this.MovedPiece.getPieceType().toString() + disambiguationFunctionForPGN() + utilitiesPGN.getPositionAtCoordinate(this.MoveDestination);
    }


}
