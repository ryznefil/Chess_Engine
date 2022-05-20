/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Player;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Move.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static fel.cvut.cz.Player.PlayerType.COMPUTER;

/**
 * Preparation for the AI player in the game.
 * <p>
 * At this point the AI player can only make random moves, which he chooses from the legal moves of his pieces.
 * In case that the move would end in "self-check" another move is selected.
 * In case of piece promotion, the AI selects queen every time.
 *
 * @author Ryzner
 */
public class ComputerPlayer extends GeneralPlayer {

    /**
     *Player type
     */
    protected final PlayerType PlayerType;

    /**
     * Constructor for the instance of the Computer player
     * 
     * @param board - current chess board state
     * @param playerAlliance - White or Black
     * @param OpponentLegalMoves - all moves that the opponent can do
     * @param MyLegalMoves - all moves that the player can do
     */
    public ComputerPlayer(Board board, Alliance playerAlliance, Collection<Move> MyLegalMoves, Collection<Move> OpponentLegalMoves) {
        super(board, playerAlliance, MyLegalMoves, OpponentLegalMoves);

        this.PlayerType = COMPUTER;
    }

    @Override
    public PlayerType getPlayerType() {
        return this.PlayerType;
    }


    /**
     * Method selects a random move out of all the possible moves by the player.
     * The computer will make that move.
     *
     * @return Move - move to be made by the AI
     */
    public Move getAI_Move() {
        Random randomizer = new Random();
        List<Move> MovesList = new ArrayList<>();
        MovesList.addAll(this.PlayerLegalMoves);

        Move selectedMove = MovesList.get(randomizer.nextInt(MovesList.size()));

        return selectedMove;
    }

}
