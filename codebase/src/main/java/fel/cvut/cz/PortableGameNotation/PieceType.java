/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.PortableGameNotation;

/**
 * Enumeration of the types of pieces in the chess game.
 * Each piece has an official PGN shortcut assigner
 *
 * @author Ryzner
 */
public enum PieceType {

    /**
     * Pawn chess piece PGN notation
     */
    PAWN("P"),

    /**
     * King chess piece PGN notation
     */
    KING("K"),

    /**
     * Queen chess piece PGN notation
     */
    QUEEN("Q"),

    /**
     * Bishop chess piece PGN notation
     */
    BISHOP("B"),

    /**
     * Knight chess piece PGN notation
     */
    KNIGHT("N"),

    /**
     * Rook chess piece PGN notation
     */
    ROOK("R");

    private final String PieceName;

    PieceType(final String PieceName) {
        this.PieceName = PieceName;
    }


    /**
     * Prints out the piece PGN notation
     * @return 
     */
    @Override
    public String toString() {
        return this.PieceName;
    }


}
