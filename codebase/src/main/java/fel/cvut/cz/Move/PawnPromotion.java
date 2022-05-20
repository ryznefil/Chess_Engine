package fel.cvut.cz.Move;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Chessman.*;
import fel.cvut.cz.GUI.PawnPromotionWindow;
import fel.cvut.cz.Player.PlayerType;
import fel.cvut.cz.PortableGameNotation.utilitiesPGN;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class creates a representation of the pawn promotion move.
 *
 * @author Ryzny
 */
public class PawnPromotion extends PawnMove {

    private static final Logger LOG = Logger.getLogger(PawnPromotion.class.getName());

    final Move PawnMove;
    final Pawn PawnToPromote;
    GeneralPiece pieceReplacingPawn;

    /**
     * Creates an instance of the pawn promotion move.
     *
     * @param pawnMove - move to be made by the pawn that ends in the position legal for promotion
     */
    public PawnPromotion(final Move pawnMove) {
        super(pawnMove.getOriginalGameBoard(), pawnMove.getMoveDestination(), pawnMove.getMovedPiece());
        this.PawnMove = pawnMove;
        this.PawnToPromote = (Pawn) pawnMove.getMovedPiece();
        this.pieceReplacingPawn = null;
        this.moveType = MoveType.PAWN_PROMOTION;
    }


    /**
     * Method does the pawn promotion move required by the move object and creates a copy of the board which it returns.
     * If the player is human, he can choose a piece type the pawn will be promoted to.
     * Computer players automatically chose to promote to Queen.
     *
     * @return Board - deep copy of the board after the move is done
     */
    @Override
    public Board executeMove() {
        System.out.println(PawnMove.getMovedPiece().getPieceType() + " " + PawnMove.getMovedPiece().getPiecePosition() + " " + PawnMove.getMoveDestination());
        //Execute the move leading up to the promotion
        final Board pawnMovedBoard = this.PawnMove.executeMove();
        //Get the piece the user wants, if the player is human
        if (originalGameBoard.getCurrentPlayer().getPlayerType() == PlayerType.HUMAN) {
            String piece = PawnPromotionWindow.SelectPieceToPromote();
            while (piece == null) {
                piece = PawnPromotionWindow.SelectPieceToPromote();
            }
            try {
            if (piece == "Queen") {
                pieceReplacingPawn = new Queen(PawnMove.getMoveDestination(), PawnToPromote.getPieceAlliance(), false);
            } else if (piece == "Knight") {
                pieceReplacingPawn = new Knight(PawnMove.getMoveDestination(), PawnToPromote.getPieceAlliance(), false);
            } else if (piece == "Rook") {
                pieceReplacingPawn = new Rook(PawnMove.getMoveDestination(), PawnToPromote.getPieceAlliance(), false);
            } else {
                throw new RuntimeException("Selection failed, failed to execute, something is wrong");
            }}catch (Exception ex){
                LOG.log(Level.SEVERE, "SELECTION OF THE CHESS PIECE TO PROMOTE TO FAILED!", ex);
            }
        } else {
            //Computer player always promotes to Queen
            pieceReplacingPawn = new Queen(PawnMove.getMoveDestination(), PawnToPromote.getPieceAlliance(), false);
        }
        pawnMovedBoard.getTile(MoveDestination).setPiece(pieceReplacingPawn);
        pawnMovedBoard.copiedBoardSetup();

        return pawnMovedBoard;
    }

    /**
     * PGN format representing printing method
     * @return 
     */
    @Override
    public String toString() {
        return utilitiesPGN.getPositionAtCoordinate(this.getCurrentPosition()) + "-" + utilitiesPGN.getPositionAtCoordinate(this.MoveDestination) + "=" + this.pieceReplacingPawn.getPieceType();

    }
}
