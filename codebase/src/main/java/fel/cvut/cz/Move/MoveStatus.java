/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Move;

/**
 * The move enumerator enlists the status of the move, e.g. if it is legal and how it ends
 *
 * @author Ryzny
 */
public enum MoveStatus {

    /**
     *Done move
     */
    DONE,

    /**
     *Move is illegal under chess rules
     */
    ILLEGAL_MOVE,

    /**
     *Move would end the player's turn with his king in check
     */
    ENDS_IN_CHECK;
}
