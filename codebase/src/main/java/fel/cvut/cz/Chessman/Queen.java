/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Chessman;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Board.Tile;
import fel.cvut.cz.Move.AttackMove;
import fel.cvut.cz.Move.Move;
import fel.cvut.cz.Move.StandardMove;
import fel.cvut.cz.Player.Alliance;
import fel.cvut.cz.PortableGameNotation.PieceType;
import fel.cvut.cz.Utilities.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Object representation of the queen chess piece, that can check legality of the piece moves,
 * create its deep copy and implements two styles of printing.
 *
 * @author Ryzner
 */
public class Queen extends GeneralPiece {

    private static final Logger LOG = Logger.getLogger(Queen.class.getName());

    //Array storing possible offsets to generate movement options
    private final Position[] MovementDirections = new Position[]{new Position(1, 0), new Position(0, 1), new Position(0, -1), new Position(-1, 0),
            new Position(1, 1), new Position(1, -1), new Position(-1, 1), new Position(-1, -1)};

    /**
     * Create a new queen instance. First move variable is automatically set to true, thus the queen is acting as
     * it has not moved before.
     *
     * @param piecePosition - Position of the piece on the chess board
     * @param alliance - piece alliance according to the player aliance
     */
    public Queen(Position piecePosition, Alliance alliance) {
        super(PieceType.QUEEN, piecePosition, alliance, true);
    }

    /**
     * Create a new queen instance. First move variable is set to according to the parameter.
     *
     * @param piecePosition - the position of the piece on the chess board
     * @param alliance  - piece alliance according to the player alliance
     * @param isFirstMove - defines whether the piece has not moved yet (true - has not moved)
     */
    public Queen(Position piecePosition, Alliance alliance, boolean isFirstMove) {
        super(PieceType.QUEEN, piecePosition, alliance, isFirstMove);
    }

    /**
     * Method calculates all legal movements for the queen.
     * It does not take into account any possibility of check, check mate etc. It only calculates all squares that the
     * piece could legaly move to. It differs between movement to an empty tile and a movement to tile occupied by opponent piece.
     *
     * @param board - current state of the chess board
     * @return ArrayList of legal moves available to the chess piece
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (Position offsetDirection : MovementDirections) {

            Position positionTochange = new Position(this.piecePosition.getxCoordinate(), this.piecePosition.getyCoordinate());
            Position MoveDestination = positionTochange;
//            System.out.println("POSITION IN STARTING before 1st CHANGE " + MoveDestination );

            while (MoveDestination.isOnBoard()) {

                MoveDestination = MoveDestination.addPositionToPosition(offsetDirection);
//                System.out.println("POSITION IN STARTING after  CHANGE " + MoveDestination );

                if (MoveDestination.isOnBoard()) {
                    Tile tileConsidered = board.getTile(MoveDestination.getxCoordinate(), MoveDestination.getyCoordinate());
                    if (!tileConsidered.hasPiece()) {
//                        System.out.println("A");
                        legalMoves.add(new StandardMove(board, MoveDestination, this));
//                        System.out.println("LEGAL MOVES NOW " + legalMoves);
//                        System.out.println("B");
                    } else {
                        if (tileConsidered.getPiece().pieceAlliance != this.pieceAlliance) {
                            legalMoves.add(new AttackMove(board, MoveDestination, this, tileConsidered.getPiece()));
                            break;
                        }
                        break;
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    /**
     * Method creates a new instance of queen that is positioned on the move destination position.
     * Eg. method creates new queen at the place the player wants it to move.
     *
     * @param move - desired move to be made by the chess piece
     * @return Queen - a new instance of the chess piece located at the move destination
     */
    @Override
    public Queen makeMove(Move move) {
        return new Queen(move.getMoveDestination(), move.getMovedPiece().getPieceAlliance(), false);
    }

    /**
     * Method creates a deep copy of the chess piece.
     * Best used when creating the copy of the chess board that is deep.
     *
     * @return Queen - deep copy of the instance of queen.
     */
    public Queen getCopy() {
        Queen copyPiece = new Queen(this.piecePosition, this.pieceAlliance, this.isFirstMove);
        return copyPiece;
    }

    /**
     * Print method used to represent the chess piece type when saving the game and creating the save game txt.
     *
     * @return String - representation of the chess piece type
     */
    @Override
    public String printToSave() {
        return "queen";
    }
}
