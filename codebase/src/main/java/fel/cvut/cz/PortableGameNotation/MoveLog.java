package fel.cvut.cz.PortableGameNotation;

import fel.cvut.cz.Move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Creates a list storing all the moves that have been made so far during the game
 * Useful for later printing out of the PGN
 *
 * @author Ryzny
 */
public class MoveLog {

    private static final Logger LOG = Logger.getLogger(MoveLog.class.getName());

    private List<Move> moves;



    /**
     *Create the move log
     */
    public MoveLog() {
        this.moves = new ArrayList<>();
    }

    public List<Move> trimMoveLog(int length){
        List<Move> movesCopy = moves.subList(0, length);
        return movesCopy;
    }

    public void setMoves(List<Move> moves){
        this.moves = moves;
    }

    public List<Move> getMoves() {
        return this.moves;
    }

    /**
     * Add move to the log
     *
     * @param move - Move that has been made
     */
    public void addMove(final Move move) {
        this.moves.add(move);
    }

    /**
     * Get the size of the log, eg number of moves made so far
     * @return int size of the log
     */
    public int size() {
        return this.moves.size();
    }

    /**
     *Delete all content from the log
     */
    public void clear() {
        this.moves.clear();
    }

    /**
     * Deletes a selected move from the move log
     *
     * @param index - int, index of the piece in the log
     * @return MoveLog, without the selected move to be deleted
     */
    public Move removeMove(final int index) {
        return this.moves.remove(index);
    }

    public Move getMove(final int moveIndex){
        return this.moves.get(moveIndex);
    }

    public boolean removeMove(final Move move) {
        return this.moves.remove(move);
    }


}