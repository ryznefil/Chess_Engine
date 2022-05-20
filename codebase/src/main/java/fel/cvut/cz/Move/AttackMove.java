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
 * Creates an instance of Attacking move, e.g. a move that includes taking
 * opponents piece
 *
 * @author Ryzny
 */
public class AttackMove extends Move {

    private static final Logger LOG = Logger.getLogger(AttackMove.class.getName());

    final private GeneralPiece AttackedPiece;

    /**
     * Constructor of the attacking move instance.
     *
     * @param board - current state of the chess board
     * @param MoveDestinationGiven - destination position of the move
     * @param pieceMoved - piece on move
     * @param pieceAttacked - piece attacked by the piece on move
     */
    public AttackMove(Board board, Position MoveDestinationGiven, GeneralPiece pieceMoved, GeneralPiece pieceAttacked) {
        super(board, MoveDestinationGiven, pieceMoved, MoveType.ATTACK_MOVE);
        this.AttackedPiece = pieceAttacked;
    }

    /**
     * Method returns whether move is an attacking move
     *
     *
     * @return boolean true for attack move
     */
    @Override
    public boolean isAttack() {
        return true;
    }

    /**
     * Getter for a piece attacked by the attacking move
     *
     * @return GeneralPiece - piece to be taken
     */
    @Override
    public GeneralPiece getAttackedPiece() {
        return AttackedPiece;
    }

    @Override
    public String toString() {
        return this.MovedPiece.getPieceType().toString() + disambiguationFunctionForPGN() + "x" + utilitiesPGN.getPositionAtCoordinate(this.MoveDestination);
    }

}
