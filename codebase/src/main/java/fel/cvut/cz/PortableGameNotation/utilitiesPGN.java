package fel.cvut.cz.PortableGameNotation;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.GUI.Baners;
import fel.cvut.cz.Move.Move;
import fel.cvut.cz.Utilities.Position;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PGN standard chess format notation supporting methods
 *
 * @author Ryzny
 */
public class utilitiesPGN {

    private static final Logger LOG = Logger.getLogger(utilitiesPGN.class.getName());

    /**
     *Pointer to the char representation of the chess board that is accessible everywhere
     */
    public static final String[][] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();

    private static String positionToPGN(final Position position) {
        int x = position.getxCoordinate();
        int y = position.getyCoordinate();
        String PGNpart = ALGEBRAIC_NOTATION[x][y];
        return PGNpart;
    }

    /**
     * Converts position to the PGN representation
     * @param position current position on the chess board
     * @return String, pgn notation of the chess tile
     */
    public static String getPositionAtCoordinate(final Position position) {
        return positionToPGN(position);
    }

    //char "representation" of the chess board that is used when printing the PGN 8x8 as the standard chess board
    private static String[][] initializeAlgebraicNotation() {
        return new String[][]{
                {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"},
                {"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"},
                {"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"},
                {"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"},
                {"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"},
                {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"},
                {"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"},
                {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"}
        };
    }

    /**
     *Method generates the PGN file representation of the game containing all standard requisities.
     *
     * @param fileName - name of the file to save to
     * @param moveLog - the current move log of the game
     */
    public void saveGameToPGNFile(final String fileName, final MoveLog moveLog) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF8"));

            writer.write(calculateEventString() + System.lineSeparator());
            writer.write(calculateDateString() + System.lineSeparator());
            writer.write(calculatePlyCountString(moveLog) + System.lineSeparator());

            int logNumber = 0;
            int round = 1;
            for (Move move : moveLog.getMoves()) {
                if (logNumber % 2 == 0) {
                    writer.write(round + ".");
                    round++;
                }

                writer.write(move.toString() + " ");
                logNumber++;

                if (logNumber % 10 == 0) {
                    writer.write(System.lineSeparator());
                }

            }
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "FAILED TO CREATE THE FILE!", ex);
            Baners.createErrorBanner("Game failed to save to PGN!" + "\nCheck log for more information.");
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "INVALID ENCODING FORMAT!", ex);
            Baners.createErrorBanner("Game failed to save to PGN!" + "\nCheck log for more information.");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "FAIL", ex);
            Baners.createErrorBanner("Game failed to save to PGN!" + "\nCheck log for more information.");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error while saving occured!", ex);
        } finally {

            try {
                writer.close();
                Baners.createInfoBanner("Game saved!", "Game was successfully saved to PGN.");
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Error while closing file!", ex);
               // Baners.createErrorBanner("Game failed to save to PGN!" + "\nCheck log for more information.");
            }
        }
    }


    //Event line in the PGN
    private String calculateEventString() {
        return "[Event \"" + "PROJECT CHESS GAME" + "\"]";
    }

    //Date line in the PGN
    private String calculateDateString() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return "[Date \"" + dateFormat.format(new Date()) + "\"]";
    }

    //Moves player line in the PGN
    private String calculatePlyCountString(final MoveLog moveLog) {
        return "[PlyCount \"" + moveLog.size() + "\"]";
    }

    //    /**
//     * Method possibly used for conversion from PGN to position
//     *
//     * @param position
//     */
//    public Position(final String position) {
//        Convertor convertor = new Convertor();
//        this.xCoordinate = convertor.convertCharToX(position.charAt(0));
//        this.yCoordinate = Character.getNumericValue(position.charAt(1)) - 1;
//
//    }
}
