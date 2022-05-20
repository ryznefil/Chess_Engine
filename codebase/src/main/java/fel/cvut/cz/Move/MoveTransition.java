/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Move;

import fel.cvut.cz.Board.Board;

import java.util.logging.Logger;

/**
 * @author Ryzny
 */
public class MoveTransition {

    private static final Logger LOG = Logger.getLogger(MoveTransition.class.getName());

    private final Board sourceBoard;
    private final Board targetBoard;
    private final Move transitionMove;
    private final MoveStatus moveStatus;

    /**
     * Constructor for the move transition
     *
     * @param SourceBoard - intial board state
     * @param TargetBoard - board state after move
     * @param move - desired move
     * @param moveStatus - move status
     */
    public MoveTransition(Board SourceBoard, Board TargetBoard, Move move, MoveStatus moveStatus) {
        this.sourceBoard = SourceBoard;
        this.targetBoard = TargetBoard;
        this.transitionMove = move;
        this.moveStatus = moveStatus;
    }

    /**
     * Getter for the move status, representing whether move is possible.
     *
     * @return move status
     */
    public MoveStatus getMoveStatus() {
        return moveStatus;
    }

    /**
     * Getter for the board after move
     *
     * @return board
     */
    public Board getTargetBoard() {
        return this.targetBoard;
    }


}
