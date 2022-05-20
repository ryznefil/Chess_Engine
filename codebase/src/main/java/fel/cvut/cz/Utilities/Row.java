package fel.cvut.cz.Utilities;

import java.util.logging.Logger;

/**
 *Rows for the move history table
 * @author Ryzny
 */
public class Row {

    private static final Logger LOG = Logger.getLogger(Row.class.getName());

    private String whiteMove;
    private String blackMove;

    /**
     *
     * @return
     */
    public String getWhiteMove() {
        return this.whiteMove;
    }

    /**
     *
     * @return
     */
    public String getBlackMove() {
        return this.blackMove;
    }

    /**
     *
     * @param move
     */
    public void setWhiteMove(final String move) {
        this.whiteMove = move;
    }

    /**
     *
     * @param move
     */
    public void setBlackMove(final String move) {
        this.blackMove = move;
    }

}
