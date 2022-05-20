package fel.cvut.cz.Move;

import fel.cvut.cz.Board.Board;

import java.util.logging.Logger;

/**
 * Empty move represents an empty move, eg a move that is going nowhere. Tesing for potential error situations and
 * prevent the program from shutting down.
 *
 * @author Ryzner
 */
public class EmptyMove extends Move {

    private static final Logger LOG = Logger.getLogger(EmptyMove.class.getName());

    /**
     *Create an instance of the empty move.
     */
    public EmptyMove() {
        super(null, null);
    }

    /**
     * Situation when something went wrong inside the move factory or the whole move engine
     *
     * @return runtimeException
     *
     */
    @Override
    public Board executeMove() {
        throw new RuntimeException("NULL move, failed to execute a move, something is wrong");
    }

}
