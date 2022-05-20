package fel.cvut.cz.GUI;

import javax.swing.*;

/**
 *Sum of methods to create various types of pop up dialogs.
 *
 *
 * @author Ryzner
 */
public class Baners {

    /**
     *Create an Error dialog
     * @param textToDisplay - error message
     */
    public static void createErrorBanner(String textToDisplay){
        JOptionPane.showMessageDialog(null, textToDisplay, "ERROR OCCURED!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     *Create a warning dialog to display
     * @param header - warning header
     * @param textToDisplay - warning message
     */
    public static void createWarningBanner(String header, String textToDisplay){
        JOptionPane.showMessageDialog(null, textToDisplay, header, JOptionPane.WARNING_MESSAGE);
    }

    /**
     *Information diaog to display.
     *
     * @param header - infomation header
     * @param textToDisplay - information text
     */
    public static void createInfoBanner(String header, String textToDisplay){
        JOptionPane.showMessageDialog(null, textToDisplay, header, JOptionPane.WARNING_MESSAGE);
    }

}
