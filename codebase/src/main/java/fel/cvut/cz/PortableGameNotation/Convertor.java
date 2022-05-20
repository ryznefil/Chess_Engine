/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.PortableGameNotation;

import java.util.logging.Logger;

/**
 * @author Ryzny
 */
public class Convertor {


    private static final Logger LOG = Logger.getLogger(Convertor.class.getName());

    /**
     * Convert standard rown notation char to X coordinates
     *
     * @param rank
     * @return
     */
    public int convertCharToX(char rank) throws RuntimeException {
        switch (rank) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
        }
        throw new RuntimeException("That is an invalid position");
    }

    //------------------------------------------------------------------------------------------
    //Convert number to chars

    /**
     * @param rank
     * @return
     */
    public char getRankChar(int rank) throws RuntimeException {
        switch (rank) {
            case 0:
                return 'a';
            case 1:
                return 'b';
            case 2:
                return 'c';
            case 3:
                return 'd';
            case 4:
                return 'e';
            case 5:
                return 'f';
            case 6:
                return 'g';
            case 7:
                return 'h';
        }
        throw new RuntimeException("That is an invalid position");
    }
}
