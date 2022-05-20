/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Board;

import fel.cvut.cz.Chessman.GeneralPiece;
import fel.cvut.cz.Player.Alliance;
import fel.cvut.cz.Utilities.Position;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * The tile class serves as a foundation for the chess board Each of the 64
 * tiles of the chess board is represented by one instance of the Tile class Has
 * basic methods and enables to create occupied and empty field
 *
 * @author Ryzny
 */
public class Tile implements Serializable{

    private static final Logger LOG = Logger.getLogger(Tile.class.getName());

    private final int xCoordinate;
    private final int yCoordinate;
    private GeneralPiece pieceOnTile;

    /**
     *
     */
    protected Position TilePosition;

    /**
     * Constructor for an empty chess board tile, eg. a tile without a chess piece.
     * The tile encapsulates its position on the chessboard.
     *
     * @param x0 - xCoordinate representing chess Board row
     * @param y0 - yCoordinate representing the chess board column
     */
    public Tile(final int x0, final int y0) {
        this.xCoordinate = x0;
        this.yCoordinate = y0;
        this.TilePosition = new Position(x0, y0);
        this.pieceOnTile = null;
    }

    /**
     * Constructor for an empty chess board tile, eg. a tile without a chess piece.
     * The tile encapsulates its position on the chessboard.
     *
     * @param position - Position of the tile on the chessboard
     */
    public Tile(final Position position) {
        this.TilePosition = position;
        this.pieceOnTile = null;
        this.xCoordinate = position.getxCoordinate();
        this.yCoordinate = position.getyCoordinate();
    }

    /**
     * Constructor for an occupied chess board tile, eg. a tile with a chess piece.
     * The tile encapsulates its position on the chessboard and the piece that is on it.
     *
     * @param x0 - xCoordinate representing chess Board row
     * @param y0 - yCoordinate representing the chess board column
     * @param piece - piece that is occupying the tile
     */
    public Tile(final int x0, final int y0, GeneralPiece piece) {
        this.xCoordinate = x0;
        this.yCoordinate = y0;
        this.TilePosition = new Position(x0, y0);
        this.pieceOnTile = piece;
    }

    /**
     * Constructor for an occupied chess board tile, eg. a tile with a chess piece.
     * The tile encapsulates its position on the chessboard and the piece that is on it.
     *
     * @param position - Position of the tile on the chess board
     * @param piece - piece on occupying the tile
     */
    public Tile(final Position position, GeneralPiece piece) {
        this.TilePosition = position;
        this.xCoordinate = position.getxCoordinate();
        this.yCoordinate = position.getyCoordinate();
        this.pieceOnTile = piece;
    }

    /**
     * Method is used to check whether a given chess tile represented by the position is empty or has a chess piece on it.
     *
     * @return boolean, true - if tile has a chess piece on it, false - there is no piece
     */
    public boolean hasPiece() {
        return pieceOnTile != null;
    }

    //return the piece on the Tile, null if empty

    /**
     * Getter method to access the chess piece located on the tile.
     *
     * @return chess piece encapsulated by the tile
     */
    public GeneralPiece getPiece() {
        return this.pieceOnTile;
    }


    /**
     * Setter method to put the chess piece on an empty tile an make it occupied.
     *
     * @param piece - chess piece to be placed on the tile.
     */
    public void setPiece(final GeneralPiece piece) {
        this.pieceOnTile = piece;
    }

    /**
     *If a tile is occupied by a chess piece, the method can delete it and make the tile empty.
     */
    public void deletePiece() {
        if (this.pieceOnTile != null) {
            this.pieceOnTile = null;
        }
    }

    /**
     * Getter method for the xCoordinate (row coordinate) of the tile.
     *
     * @return int, xCoordinate of the tile
     */
    public int getxCoordinate() {
        return xCoordinate;
    }

    /**
     * Getter method for the yCoordinate (column coordinate) of the tile.
     *
     * @return int, yCoordinate of the tile
     */
    public int getyCoordinate() {
        return yCoordinate;
    }

    /**
     * Getter method for the chess piece that is located on tile.
     *
     * @return piece, chess piece located on the tile
     */
    public GeneralPiece getPieceOnTile() {
        return pieceOnTile;
    }

    /**
     * @return
     */
    public Position getTilePosition() {
        return TilePosition;
    }

    /**
     * Method used for printing ASCII representation of the tile. White alliance piece is represented in uppercase,
     * black piece in lower case
     *
     * @return String representation of the tile.
     */
    @Override
    public String toString() {
        if (this.pieceOnTile == null) {
            return "–";
        } else {
            if (getPiece().getPieceAlliance() == Alliance.BLACK) {
                return getPiece().toString().toLowerCase();
            } else {
                return getPiece().toString().toUpperCase();
            }
        }
    }

    /**
     * Method used to print out the tile while saving the game. Prints out all the information encapsulated in the tile.
     *
     * @return String representing the tile data
     */
    public String saveTileDetails() {
        if (this.getPiece() == null) {
            return ("#POSITION# " + this.TilePosition.savePrint() + " #PIECE_ON_TILE# " + "null" + " #");
        } else {
            return ("#POSITION# " + this.TilePosition.savePrint() + " #PIECE_ON_TILE# " + this.pieceOnTile.printToSave() +
                    " #PIECE_ALLIANCE# " + this.pieceOnTile.getPieceAlliance().toString().toLowerCase() + " #HASN'T MOVED YET# " + this.pieceOnTile.isFirstMove() + " #");
        }
    }

}
