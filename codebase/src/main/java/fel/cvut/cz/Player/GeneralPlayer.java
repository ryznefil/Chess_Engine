/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Player;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Board.Tile;
import fel.cvut.cz.Chessman.GeneralPiece;
import fel.cvut.cz.Chessman.King;
import fel.cvut.cz.Chessman.Rook;
import fel.cvut.cz.GUI.Baners;
import fel.cvut.cz.Move.*;
import fel.cvut.cz.PortableGameNotation.PieceType;
import fel.cvut.cz.Utilities.Position;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

/**
 * Abstract player class that serves as a foundation for the players of chess game.
 * Human player and computer player classes inherit from it.
 * Contains key game methods implementation
 *
 * @author Filip Ryzner
 */
public abstract class GeneralPlayer implements Serializable {

    private static final Logger LOG = Logger.getLogger(GeneralPlayer.class.getName());


    protected final Board PlayerGivenBoard;
    protected King PlayerKing = null;
    protected final Collection<Move> PlayerLegalMoves;
    protected final Alliance PlayerAlliance;
    private boolean inCheck = false;

    /**
     * Constructor for the instance of the general player class.
     *
     * @param board - current chess board state
     * @param playerAlliance - White or Black
     * @param OpponentMoveOpportunities - all moves that the opponent can do
     * @param MyMoveOpportunities - all moves that the player can do
     */
    public GeneralPlayer(final Board board, Alliance playerAlliance, final Collection<Move> MyMoveOpportunities, final Collection<Move> OpponentMoveOpportunities) {
        this.PlayerGivenBoard = board;
        this.PlayerLegalMoves = MyMoveOpportunities;
        this.PlayerAlliance = playerAlliance;

        try {
            this.PlayerKing = getMyKing();
            this.inCheck = !GeneralPlayer.getAttacksOnTile(this.PlayerKing.getPiecePosition(), OpponentMoveOpportunities).isEmpty();
            this.PlayerLegalMoves.addAll(calculateKingCastles(PlayerLegalMoves, OpponentMoveOpportunities));
        }catch (RuntimeException Ex){
            LOG.log(Level.SEVERE, "Board set up without the king piece!" + "\n Please restart the game", Ex);

        }

    }

    /**
     * Method calculates all possible attacking moves that an opponent could play on the given tile.
     * It is used to calculate whether the king is in check / check mate.
     *
     * @param piecePosition - position of the chess piece in question
     * @param opponentMoves - all moves that the opponent can do
     * @return ArrayList of all moves that attack the tile
     */
    public static Collection<Move> getAttacksOnTile(final Position piecePosition, final Collection<Move> opponentMoves) {
        final List<Move> attackingMoves = new ArrayList<>();

        for (Move move : opponentMoves) {
            if (piecePosition.positionsEqual(move.getMoveDestination())) {

                attackingMoves.add(move);

            }
        }
        return attackingMoves;
    }

    /**
     * Getter for the playerType variable.
     *
     * @return PlayerType - Human/Computer
     */
    public abstract PlayerType getPlayerType();

    /**
     * Method checks whether a proposed move is legal. This is done by checking whether the move is
     * contained in the players legal moves list that was given to a player at creating, which is done in every
     * round of the game.
     *
     * @param suggestedMove - move in question
     * @return boolean - true - move is lega / false - move is not
     */
    public boolean isLegalMove(final Move suggestedMove) {
        return this.PlayerLegalMoves.contains(suggestedMove);
    }


    /**
     *Method is used to decide whether a legal move can definitelly be made without breaking any rules such as check etc ...
     *
     * Method is given a move that a player proposed to make. It first check whether move is contained in players legal moves,
     * if not then move is marked as illegal and initial board is handed back, then the method checks whether after the move
     * there still would not be any attack on the king and hence a check, if there is it marks the move as ENDS_IN_CHECK,
     * lastly if all above is satisfied - move is marked as DONE and returned as an instance of the MoveTransition.
     *
     * @param suggestedMove - move suggested by the player to be made
     * @return MoveTransition - move transition instance containing the move
     */

    public MoveTransition makeMove(final Move suggestedMove) {

        //ILLEGAL MOVE
        if (!isLegalMove(suggestedMove)) {
            return new MoveTransition(this.PlayerGivenBoard, this.PlayerGivenBoard, suggestedMove, MoveStatus.ILLEGAL_MOVE);
        }

        //MOVE HIMSELF INTO CHECK
        Board TransitionBoard = suggestedMove.executeMove();
        final Collection<Move> kingAttacks = GeneralPlayer.getAttacksOnTile(TransitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                TransitionBoard.getCurrentPlayer().getPlayerPossibleMoves());
        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.PlayerGivenBoard, this.PlayerGivenBoard, suggestedMove, MoveStatus.ENDS_IN_CHECK);
        }

        //STANDARD MOVE
        return new MoveTransition(this.PlayerGivenBoard, TransitionBoard, suggestedMove, MoveStatus.DONE);
    }

    //Method is used to locate player's king on the chess board, it also checks for its existence and is used
    //when the game is being loaded from file to cross check that everything is set up correctly.
    private King getMyKing() throws RuntimeException  {
        //white alliance player
        if (this.PlayerAlliance == Alliance.WHITE) {
            for (final GeneralPiece piece : PlayerGivenBoard.getWhitePieces()) {
                if (piece.getPieceType() == PieceType.KING) {
                    return (King) piece;
                }
            }
            Baners.createErrorBanner("Game is set up without the white king! Restart the game via the Game menu");
            throw new RuntimeException("NO KING SET UP, INVALID BOARD");

            //black alliance player
        } else {
            for (final GeneralPiece piece : PlayerGivenBoard.getBlackPieces()) {
                if (piece.getPieceType() == PieceType.KING) {
                    return (King) piece;
                }
            }
            Baners.createErrorBanner("Game is set up without the white king! Restart the game via the Game menu");
            throw new RuntimeException("NO KING SET UP, INVALID BOARD");
        }
    }

    /**
     * Method return variable that boolean that represents whether player is currenly in check or not.
     *
     * @return boolean, true - player is in check
     */
    public boolean Check() {
        return this.inCheck;
    }

    /**
     * Method return variable that boolean that represents whether player is currenly in check mate or not.
     *
     * @return boolean, true - player is in check mate
     */
    public boolean CheckMate() {
        return this.inCheck && !CheckEscapePossible();
    }

    /**
     * Method return variable that boolean that represents whether the game is currenly in stalemate or not.
     *Stalemate occurs if player is not in check, but has no moves taht he could make.
     *
     * @return boolean, true - player is in stalemate
     */
    public boolean Stalemate() {
        return !this.inCheck && PlayerLegalMoves.isEmpty();
    }


    /**
     * Method checks whether a legal move that would not end in another check for the player, exists.
     *
     * @return boolean - true if player can escape from the current check he is facing
     */
    protected boolean CheckEscapePossible() {
        for (final Move move : PlayerLegalMoves) {
            if(move.getMoveType() != MoveType.PAWN_PROMOTION) {
                MoveTransition moveTransition = makeMove(move);
                if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                    return true;
                }
            }
        }
        return false;
    }

    public MoveTransition unMakeMove(final Move move) {
        return new MoveTransition(this.PlayerGivenBoard, move.undoMove(), move, MoveStatus.DONE);
    }

    public MoveTransition reconstructMove(final Move move) {
        return new MoveTransition(this.PlayerGivenBoard, move.executeMove(), move, MoveStatus.DONE);
    }


    /**
     * Getter for the alliance of the player
     *
     * @return Alliance player alliance
     */
    public Alliance getAlliance() {
        return this.PlayerAlliance;
    }

    /**
     * Getter for the player king piece.
     *
     * @return King - player's king
     */
    public King getPlayerKing() {
        return this.PlayerKing;
    }

    /**
     * Getter for the ArrayList handed to a player that represents all legal moves his pieces can make.
     * The list does not exclude moves resulting in check/check mate or stalemate for the player.
     *
     * @return Collection - all moves that player's pieces could possibly make
     */
    public Collection<Move> getPlayerPossibleMoves() {
        return PlayerLegalMoves;
    }

    /**
     * Methods get the opponent to the current player calling the method.
     * The opponent is extracted from the chess board that was given to the player based on the opposition alliance.
     *
     * @return GeneralPlayer Opponent to the current player
     */
    public GeneralPlayer getOpponent() {
            switch (this.PlayerAlliance) {
                case WHITE:
                    return this.PlayerGivenBoard.getBlackPlayer();
                case BLACK:
                    return this.PlayerGivenBoard.getWhitePlayer();
            }
            return null;
        }
    /**
     * Method checks whether a player has currently an opportunity to make a castling move,
     *
     * Method checks all underlying rules of castling and if they are all satisfied, then move is attached to player's
     * movement opportunities.
     *
     * @param playerLegalmovesMoves - current legal moves by the player
     * @param opponentLegalMoves - all opponents legal moves
     * @return ArrayList - contains all possibilities for castling if any exists
     */
    public Collection<Move> calculateKingCastles(Collection<Move> playerLegalmovesMoves, Collection<Move> opponentLegalMoves) {
        List<Move> kingCastles = new ArrayList<>();

        if (this.PlayerAlliance == Alliance.WHITE) {
            if (this.PlayerKing.isFirstMove() && !this.Check()) {
                //KING SIDE 
                if (!this.PlayerGivenBoard.getTile(7, 5).hasPiece() && !this.PlayerGivenBoard.getTile(7, 6).hasPiece()) {
                    final Tile rookTile = this.PlayerGivenBoard.getTile(7, 7);
                    final Position rookDestination = new Position(7, 5);
                    final Position kingDestination = new Position(7, 6);

                    if (rookTile.hasPiece() && rookTile.getPiece().isFirstMove()
                            && GeneralPlayer.getAttacksOnTile(new Position(7, 5), opponentLegalMoves).isEmpty()
                            && GeneralPlayer.getAttacksOnTile(new Position(7, 6), opponentLegalMoves).isEmpty()
                            && (rookTile.getPiece().getPieceType() == PieceType.ROOK)) {
                        kingCastles.add(new CastlingMove.KingSideCastleMove(PlayerGivenBoard, kingDestination, PlayerKing, (Rook) rookTile.getPiece(), rookTile.getTilePosition(), rookDestination));
                    }
                }
                //QUEEN SIDE
                if (!this.PlayerGivenBoard.getTile(7, 1).hasPiece() && !this.PlayerGivenBoard.getTile(7, 2).hasPiece()
                        && !this.PlayerGivenBoard.getTile(7, 3).hasPiece()) {
                    final Tile rookTile = this.PlayerGivenBoard.getTile(7, 0);
                    final Position rookDestination = new Position(7, 3);
                    final Position kingDestination = new Position(7, 2);

                    if (rookTile.hasPiece() && rookTile.getPiece().isFirstMove() && rookTile.getPiece().getPieceType() == PieceType.ROOK
                            && GeneralPlayer.getAttacksOnTile(new Position(7, 2), opponentLegalMoves).isEmpty()
                            && GeneralPlayer.getAttacksOnTile(new Position(7, 3), opponentLegalMoves).isEmpty()) {
                        kingCastles.add(new CastlingMove.QueenSideCastleMove.KingSideCastleMove(PlayerGivenBoard, kingDestination, PlayerKing, (Rook) rookTile.getPiece(), rookTile.getTilePosition(), rookDestination));
                    }
                }
            }
            return kingCastles;
        } else {
            if (this.PlayerKing.isFirstMove() && !this.Check()) {
                //KING SIDE
                if (!this.PlayerGivenBoard.getTile(0, 5).hasPiece() && !this.PlayerGivenBoard.getTile(0, 6).hasPiece()) {

                    final Tile rookTile = this.PlayerGivenBoard.getTile(0, 7);
                    final Position rookDestination = new Position(0, 5);
                    final Position kingDestination = new Position(0, 6);

                    if (rookTile.hasPiece() && rookTile.getPiece().isFirstMove()
                            && GeneralPlayer.getAttacksOnTile(new Position(0, 6), opponentLegalMoves).isEmpty()
                            && GeneralPlayer.getAttacksOnTile(new Position(0, 5), opponentLegalMoves).isEmpty()
                            && (rookTile.getPiece().getPieceType() == PieceType.ROOK)) {

                        kingCastles.add(new CastlingMove.KingSideCastleMove(PlayerGivenBoard, kingDestination, PlayerKing, (Rook) rookTile.getPiece(), rookTile.getTilePosition(), rookDestination));
                    }
                }
                //QUEEN SIDE
                if (!this.PlayerGivenBoard.getTile(0, 1).hasPiece() && !this.PlayerGivenBoard.getTile(0, 2).hasPiece()
                        && !this.PlayerGivenBoard.getTile(0, 3).hasPiece()) {
                    final Tile rookTile = this.PlayerGivenBoard.getTile(0, 0);
                    final Position rookDestination = new Position(0, 3);
                    final Position kingDestination = new Position(0, 2);

                    if (rookTile.hasPiece() && rookTile.getPiece().isFirstMove()
                            && GeneralPlayer.getAttacksOnTile(new Position(0, 2), opponentLegalMoves).isEmpty()
                            && GeneralPlayer.getAttacksOnTile(new Position(0, 3), opponentLegalMoves).isEmpty()
                            && (rookTile.getPiece().getPieceType() == PieceType.ROOK)) {

                        kingCastles.add(new CastlingMove.QueenSideCastleMove(PlayerGivenBoard, kingDestination, PlayerKing, (Rook) rookTile.getPiece(), rookTile.getTilePosition(), rookDestination));
                    }
                }
            }
            return kingCastles;
        }
    }
}
