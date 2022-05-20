package fel.cvut.cz.Utilities;

import fel.cvut.cz.Move.Move;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Ryzny
 */
public class DataModel extends DefaultTableModel {

    private static final Logger LOG = Logger.getLogger(DataModel.class.getName());

    private final List<Row> values;
    private static final String[] NAMES = {"White", "Black"};

    /**
     *
     */
    public DataModel() {
        this.values = new ArrayList<>();
    }

    /**
     *
     */
    public void clear() {
        this.values.clear();
        setRowCount(0);
    }

    @Override
    public int getRowCount() {
        if (this.values == null) {
            return 0;
        }
        return this.values.size();
    }

    @Override
    public int getColumnCount() {
        return NAMES.length;
    }

    @Override
    public Object getValueAt(final int row, final int col) {
        final Row currentRow = this.values.get(row);
        if (col == 0) {
            return currentRow.getWhiteMove();
        } else if (col == 1) {
            return currentRow.getBlackMove();
        }
        return null;
    }

    @Override
    public void setValueAt(final Object aValue,
                           final int row,
                           final int col) {
        final Row currentRow;
        if (this.values.size() <= row) {
            currentRow = new Row();
            this.values.add(currentRow);
        } else {
            currentRow = this.values.get(row);
        }
        if (col == 0) {
            currentRow.setWhiteMove((String) aValue);
            fireTableRowsInserted(row, row);
        } else if (col == 1) {
            currentRow.setBlackMove((String) aValue);
            fireTableCellUpdated(row, col);
        }
    }

    @Override
    public Class<?> getColumnClass(final int col) {
        return Move.class;
    }

    @Override
    public String getColumnName(final int col) {
        return NAMES[col];
    }
}
