package fel.cvut.cz.GUI;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Move.Move;
import fel.cvut.cz.Player.Alliance;
import fel.cvut.cz.PortableGameNotation.MoveLog;
import fel.cvut.cz.Utilities.DataModel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Game history panel is used to display done moves in the PGN format.
 *
 * @author Ryzner
 */
public class GameHistoryPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(GameHistoryPanel.class.getName());

    private final DataModel model;
    private final JScrollPane scrollPane;
    private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(200, 400);


    /**
     * Constructor of the panel
     *
     */
    public GameHistoryPanel() {
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Method is used to reflect new moves in the panel
     * It can also modifies the last PGN notation to reflect for check mate or check
     *
     *
     * @param board
     * @param moveHistory
     */
    public void redo(final Board board, final MoveLog moveHistory) {
        int currentRow = 0;
        this.model.clear();
        //get all moves from the move log representing the move history of the game and place them into the table
        for (final Move move : moveHistory.getMoves()) {
            final String moveText = move.toString();
            if (move.getMovedPiece().getPieceAlliance() == Alliance.WHITE) {
                this.model.setValueAt(moveText, currentRow, 0);
            } else if (move.getMovedPiece().getPieceAlliance() == Alliance.BLACK) {
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }

        //Check if the last move resulted in check or check mate
        if (moveHistory.getMoves().size() > 0) {
            final Move lastMove = moveHistory.getMoves().get(moveHistory.size() - 1);
            final String moveText = lastMove.toString();

            if (lastMove.getMovedPiece().getPieceAlliance() == Alliance.WHITE) {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow, 0);
            } else if (lastMove.getMovedPiece().getPieceAlliance() == Alliance.BLACK) {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow - 1, 1);
            }
        }

        //implement scrollPane in case the number of moves exceeds the viewing capacity
        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

    }

    //Add check mate and check notation to the PGN
    private static String calculateCheckAndCheckMateHash(final Board board) {
        if (board.getCurrentPlayer().CheckMate()) {
            return "#";
        } else if (board.getCurrentPlayer().Check()) {
            return "+";
        }
        return "";
    }

}
