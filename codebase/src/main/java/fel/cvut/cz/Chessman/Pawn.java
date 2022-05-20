/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Chessman;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Board.Tile;
import fel.cvut.cz.Move.*;
import fel.cvut.cz.Player.Alliance;
import fel.cvut.cz.PortableGameNotation.PieceType;
import fel.cvut.cz.Utilities.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;


/**
 * Object representation of the pawn chess piece, that can check legality of the piece moves,
 * create its deep copy and implements two styles of printing.
 *
 * @author Ryzner
 */
public class Pawn extends GeneralPiece {

    private static final Logger LOG = Logger.getLogger(Pawn.class.getName());

    private final Position MovementDirections;
    private final Position[] AttackDirections;
    private final Position DoubleStep;
    private final Position[] EnPassantLocation;
    private final int LastRow;

    /**
     * Create a new pawn instance. First move variable is automatically set to true, thus the pawn is acting as
     * it has not moved before.
     *
     * @param piecePosition - Position of the piece on the chess board
     * @param alliance - piece alliance according to the player alliance
     */
    public Pawn(Position piecePosition, Alliance alliance) {
        super(PieceType.PAWN, piecePosition, alliance, true);
        //Movement direction is based on alliance of the piece
        this.EnPassantLocation = new Position[]{new Position(0, -1), new Position(0, 1)};

        if (this.pieceAlliance == Alliance.BLACK) {
            this.MovementDirections = new Position(1, 0);
            this.AttackDirections = new Position[]{new Position(1, 1), new Position(1, -1)};
            this.DoubleStep = new Position(2, 0);
            this.LastRow = 7;
        } else {
            this.MovementDirections = new Position(-1, 0);
            this.AttackDirections = new Position[]{new Position(-1, -1), new Position(-1, 1)};
            this.DoubleStep = new Position(-2, 0);
            this.LastRow = 0;
        }
    }

    /**
     * Create a new pawn instance. First move variable is set to according to the parameter.
     *
     * @param piecePosition - the position of the piece on the chess board
     * @param alliance  - piece alliance according to the player alliance
     * @param isFirstMove - defines whether the piece has not moved yet (true - has not moved)
     */
    public Pawn(Position piecePosition, Alliance alliance, boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, alliance, isFirstMove);
        this.EnPassantLocation = new Position[]{new Position(0, -1), new Position(0, 1)};

        if (this.pieceAlliance == Alliance.BLACK) {
            this.MovementDirections = new Position(1, 0);
            this.AttackDirections = new Position[]{new Position(1, 1), new Position(1, -1)};
            this.DoubleStep = new Position(2, 0);
            this.LastRow = 7;
        } else {
            this.MovementDirections = new Position(-1, 0);
            this.AttackDirections = new Position[]{new Position(-1, -1), new Position(-1, 1)};
            this.DoubleStep = new Position(-2, 0);
            this.LastRow = 0;
        }
    }

    /**
     * Method calculates all legal movements for the bishop.
     * It does not take into account any possibility of check, check mate etc. It only calculates all squares that the
     * piece could legaly move to. It differs between movement to an empty tile and a movement to a tile
     * occupied by opponent piece as well as EnPassant move, double move possible at first move and pawn promotion move.
     *
     * @param board - current state of the chess board
     * @return ArrayList of legal moves available to the chess piece
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        boolean Clear = false; //in case of two tile movement, the first tile must be clear

        //STANDARD MOVEMENT BY ONE TILE
        Position positionTochange = new Position(this.piecePosition.getxCoordinate(), this.piecePosition.getyCoordinate());
        Position MoveDestination = positionTochange.addPositionToPosition(MovementDirections);

        if (MoveDestination.isOnBoard()) { //standard move
            Tile tileConsidered = board.getTile(MoveDestination.getxCoordinate(), MoveDestination.getyCoordinate());
            if (!tileConsidered.hasPiece()) {
                if (MoveDestination.getxCoordinate() == this.LastRow) {
                    legalMoves.add(new PawnPromotion(new PawnMove(board, MoveDestination, this)));
                } else {
                    legalMoves.add(new PawnMove(board, MoveDestination, this));
                }
                Clear = true; //first tile is clear, double move is possible
            }
        }
        //POSSIBLE DOUBLE MOVE IF MOVING FOR THE FIRST TIME
        if (Clear && isFirstMove) { //first position was clear, investigate possible double move
            MoveDestination = this.piecePosition.addPositionToPosition(DoubleStep);
            if (MoveDestination.isOnBoard()) { //should not be needed but it is used as a sanity check
                Tile tileConsidered = board.getTile(MoveDestination.getxCoordinate(), MoveDestination.getyCoordinate());
                if (!tileConsidered.hasPiece()) {
                    if (MoveDestination.getxCoordinate() == this.LastRow) {
                        legalMoves.add(new PawnPromotion(new PawnFirstMove(board, MoveDestination, this)));
                    } else {
                        legalMoves.add(new PawnFirstMove(board, MoveDestination, this));
                    }
                }
            }
        }

        //STANDARD ATTACK MOVEMENT
        for (Position direction : AttackDirections) {
            positionTochange = new Position(this.piecePosition.getxCoordinate(), this.piecePosition.getyCoordinate());
            MoveDestination = positionTochange.addPositionToPosition(direction);
            if (MoveDestination.isOnBoard()) {
                Tile tileConsidered = board.getTile(MoveDestination.getxCoordinate(), MoveDestination.getyCoordinate());
                if (tileConsidered.hasPiece()) {
                    if (tileConsidered.getPiece().getPieceAlliance() != this.pieceAlliance) {
                        if (MoveDestination.getxCoordinate() == this.LastRow) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, MoveDestination, this, tileConsidered.getPiece())));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, MoveDestination, this, tileConsidered.getPiece()));
                        }
                    }
                }
            }
        }
        //EN PASSANT 
        if (board.hasEnPassantPawn() && (board.getEnPassantPawn().getPieceAlliance() != this.pieceAlliance)) {
            Pawn EnPassantPawn = board.getEnPassantPawn();
            for (Position directionMove : EnPassantLocation) {
                positionTochange = new Position(this.piecePosition.getxCoordinate(), this.piecePosition.getyCoordinate());
                Position PositionInvestigated = positionTochange.addPositionToPosition(directionMove);

                if (EnPassantPawn.getPiecePosition().positionsEqual(PositionInvestigated)) {
                    if (this.pieceAlliance == Alliance.WHITE) {
                        MoveDestination = EnPassantPawn.getPiecePosition().addPositionToPosition(new Position(-1, 0));
                        legalMoves.add(new PawnEnPassant(board, MoveDestination, this, EnPassantPawn));
                        break;
                    } else if (this.pieceAlliance == Alliance.BLACK) {
                        MoveDestination = EnPassantPawn.getPiecePosition().addPositionToPosition(new Position(1, 0));
                        legalMoves.add(new PawnEnPassant(board, MoveDestination, this, EnPassantPawn));
                        break;
                    }
                }
            }

        }
        //System.out.println(legalMoves);
        return legalMoves;

    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    /**
     * Method creates a new instance of pawn that is positioned on the move destination position.
     * Eg. method creates new pawn at the place the player wants it to move.
     *
     * @param move - desired move to be made by the chess piece
     * @return Pawn - a new instance of the chess piece located at the move destination
     */
    @Override
    public Pawn makeMove(Move move
    ) {
        return new Pawn(move.getMoveDestination(), move.getMovedPiece().getPieceAlliance(), false);
    }

    /**
     * Method creates a deep copy of the chess piece.
     * Best used when creating the copy of the chess board that is deep.
     *
     * @return Pawn - deep copy of the instance of pawn.
     */
    @Override
    public Pawn getCopy() {
        Pawn copyPiece = new Pawn(this.piecePosition, this.pieceAlliance, this.isFirstMove);
        return copyPiece;
    }

    /**
     * Print method used to represent the chess piece type when saving the game and creating the save game txt.
     *
     * @return String  - representation of the chess piece type
     */
    @Override
    public String printToSave() {
        return "pawn";
    }
}
