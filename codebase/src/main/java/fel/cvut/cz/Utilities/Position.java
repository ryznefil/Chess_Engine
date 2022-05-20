package fel.cvut.cz.Utilities;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Class position serves as a foundation for the whole chess game mechanics. The instance of the class encapsulates
 * the x and y coordinates that describe the position of the chess tile and chess piece on the chess board.
 * Position values range from 0 to 7 in both coordinates as to represent the double array representing the chess board.
 * <p>
 * Class provides foundation of methods that allow manipulation of the instance of the position class.
 *
 * @author Ryzner
 */
public class Position implements Serializable {

    private static final Logger LOG = Logger.getLogger(Position.class.getName());

    private int xCoordinate;
    private int yCoordinate;


    /**
     * Standard instance of the Position encapsulates the row and column of the board where either tile or piece are placed.
     *
     * @param row    row of the chess board
     * @param column column of the chess board
     */
    public Position(final int row, final int column) {
        this.xCoordinate = row;
        this.yCoordinate = column;
    }


    /**
     * @param position
     * @return
     */
    public Position addPositionToPosition(final Position position) {
        int x = this.xCoordinate + position.getxCoordinate();
        int y = this.yCoordinate + position.getyCoordinate();
        return new Position(x, y);
    }

    /**
     * Method serves to compare two instances of the Position class and to decide whether they point
     * to the same location on the board, eg. if they are the same although they are different instances of the Position class.
     *
     * @param comparedPosition other instance of Position
     * @return true - same coordinates, false - different coordinates
     */
    public boolean positionsEqual(final Position comparedPosition) {
        if (this.xCoordinate != comparedPosition.getxCoordinate() || this.yCoordinate != comparedPosition.getyCoordinate()) {
            return false;
        }
        return true;
    }

    /**
     * Method checks whether the instance of Position is located on the regular 8x8 chess board.
     * Position values range from 0 to 7 as to represent the double array representing the chess board.
     *
     * @return true - located on the board, false - invalid position
     */
    public boolean isOnBoard() {
        if ((this.xCoordinate > 7)
                || (this.yCoordinate > 7)
                || (this.xCoordinate < 0)
                || (this.yCoordinate < 0)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Setter for the row(x) coordinate of the position.
     *
     * @param xCoordinate
     */
    public void setxCoordinate(final int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    /**
     * Setter for the column(y) coordinate of the position.
     *
     * @param yCoordinate
     */
    public void setyCoordinate(final int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    /**
     * Getter for the x coordinate (row) of the position.
     *
     * @return row number
     */
    public int getxCoordinate() {
        return xCoordinate;
    }

    /**
     * Getter for the y coordinate (columns) of the position.
     *
     * @return column number
     */
    public int getyCoordinate() {
        return yCoordinate;
    }


    @Override
    public String toString() {
        return "Position{" + "xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + '}';
    }

    /**
     * Method creates a string that is used to represent the position in the saved game file.
     *
     * @return string to be saved into txt of the saved game
     */
    public String savePrint() {
        return "(" + this.getxCoordinate() + "," + this.getyCoordinate() + ")";
    }
}
