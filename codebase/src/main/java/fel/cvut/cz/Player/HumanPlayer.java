/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Player;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Move.Move;

import java.util.Collection;
import java.util.logging.Logger;

import static fel.cvut.cz.Player.PlayerType.HUMAN;

/**
 * Representation of the human player in the game.
 * Human player extends the General player class and specifies only the human player type.
 *
 * @author Ryzner
 */
public class HumanPlayer extends GeneralPlayer {

    private static final Logger LOG = Logger.getLogger(HumanPlayer.class.getName());

    private PlayerType PlayerType;

    /**
     * Constructor for the human player class. Creates an instance of the human player.
     *
     * @param board - current chess board state
     * @param playerAlliance - White or Black
     * @param OpponentLegalMoves - all moves that the opponent can do
     * @param MyLegalMoves - all moves that the player can do
     */
    public HumanPlayer(Board board, Alliance playerAlliance, Collection<Move> MyLegalMoves, Collection<Move> OpponentLegalMoves) {
        super(board, playerAlliance, MyLegalMoves, OpponentLegalMoves);

        this.PlayerType = HUMAN;
    }

    /**
     * Getter for the player type of the human player.
     *
     * @return PlayerType type of the player - Human
     */
    @Override
    public PlayerType getPlayerType() {
        return this.PlayerType;
    }


}
