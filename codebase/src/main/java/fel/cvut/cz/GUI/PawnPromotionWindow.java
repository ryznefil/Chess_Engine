/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.GUI;

import javax.swing.*;

/**
 * Method creates a JPanel with ComboBox asking player, who is promoting a pawn, to choose the desired piece to promote to.
 *
 * @author Ryzner
 */
public class PawnPromotionWindow extends JPanel {

    /**
     * Static method that asks user to select a piece and returns his choice in a string - name of the piece.
     * If user cancels or closes the window it returns null
     *
     * @return String or null - representing users choice
     */
    public static String SelectPieceToPromote() {

        String result = null;

        JPanel panel = new JPanel();
        panel.add(new JLabel("Please select a piece type that the pawn will promote to:"));
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        String[] values = new String[]{"Queen", "Knight", "Rook"};
        for (String value : values) {
            model.addElement(value);
        }
        JComboBox comboBox = new JComboBox(model);
        panel.add(comboBox);

        int iResult = JOptionPane.showConfirmDialog(null, panel, "Pawn Promotion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (iResult) {
            case JOptionPane.OK_OPTION:
                result = (String) comboBox.getSelectedItem();
                break;
        }

        return result;
    }
}
