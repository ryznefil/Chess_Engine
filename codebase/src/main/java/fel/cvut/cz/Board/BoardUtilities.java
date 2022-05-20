/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Board;

import fel.cvut.cz.Chessman.*;
import fel.cvut.cz.GUI.Baners;
import fel.cvut.cz.Player.Alliance;
import fel.cvut.cz.Player.PlayerType;
import fel.cvut.cz.Utilities.Position;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static fel.cvut.cz.Board.Board.BOARD_COLUMNS;
import static fel.cvut.cz.Board.Board.BOARD_ROWS;

/**
 * @author Ryzner
 */
public class BoardUtilities {

    private static final Logger LOG = Logger.getLogger(BoardUtilities.class.getName());

    /**
     * Method asks the current player whether he is in stalemate, eg. if he can make any more moves.
     * Returns boolean
     *
     * @param board - current chess board state
     * @return true - player is in stalemate, false - game is not
     */
    public static boolean isStalemate(final Board board) {
        if (board.getCurrentPlayer().Stalemate()) {
            return true;
        }
        return false;
    }

    /**
     * Method asks player class and it replies whether player is in check mate or not.
     *
     * @param board - current chess board state
     * @return true - player is in check mate, false - game is not
     */
    public static boolean isCheckMate(final Board board) {
        if (board.getCurrentPlayer().CheckMate()) {
            return true;
        }
        return false;
    }

    /**
     * Method is used to save a current game state into preferably txt file.
     * It saves the current board state in a standardized way as well as both both player and timer states.
     * Says who is current player, his type and alliance. The same for opponent.
     * <p>
     * Standardization - lines:
     * 1) current player - alliance, type
     * 2) opponent - alliance, type
     * 3) chess timers states in seconds - white player, black player timers
     * 4) all tiles position that contain pieces, pieces type, pieces alliance and information about first move
     *
     * @param board    - current chess board state
     * @param fileName - desired file name
     * @param time     - printed version of both timer states (eg. white player timer and black player timer)
     */
    public void saveBoard(Board board, String fileName, String time) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF8"));

            writer.write(board.printCurrentPlayer() + System.lineSeparator());
            writer.write(board.printOpponentPlayer() + System.lineSeparator());

            writer.write(time + System.lineSeparator());

            for (int i = 0; i < BOARD_ROWS; i++) {
                for (int j = 0; j < BOARD_COLUMNS; j++) {
                    if (board.getTile(i, j).hasPiece()) {
                        writer.write(board.getTile(i, j).saveTileDetails() + System.lineSeparator());
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "FILE NOT FOUND!", ex);
            Baners.createErrorBanner("FAILED TO CREATE THE FILE!");
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "INVALID ENCODING FORMAT!", ex);
            Baners.createErrorBanner("INVALID ENCODING FORMAT!");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "FAIL OPENING THE FILE!", ex);
            Baners.createErrorBanner("FAIL OPENING THE FILE!");
        } finally {
            try {
                writer.close();
                Baners.createInfoBanner("Game saved!", "Game saved successfully!");
            } catch (IOException ex) {
                Baners.createErrorBanner("FILE FAILED TO CLOSE. GAME NOT SAVED!");
                LOG.log(Level.SEVERE, "FILE FAILED TO CLOSE!", ex);
            }
        }
    }

    /**
     * Method is used to load the game from the saved file, can be used to set up individual game as well.
     * It takes in the standardized saved file and reconstructs the chess board state that it then sends back to the GUI.
     * <p>
     * Standardization - lines:
     * 1) current player - alliance, type
     * 2) opponent - alliance, type
     * 3) chess timers states in seconds - white player, black player timers
     * 4) all tiles position that contain pieces, pieces type, pieces alliance and information about first move
     *
     * @param fileName - name of the file the data is loaded from.
     * @param originalGameBoard
     * @return Board - the chess game board constructed from the save file
     */
    public Board loadBoard(String fileName, Board originalGameBoard) {

        String input;
        String pieceType;
        String piecePosition;
        String pieceAlliance;
        String isFirstMove;
        String currentPlayer;
        String Opponent;
        Board loadedBoard = null;
        Board originalBoard = originalGameBoard;

        Position truePosition;
        Alliance trueAlliance;
        Alliance currentPlayerAlliance = null;
        PlayerType currentPlayerType = null;
        PlayerType opponentPlayerType = null;
        GeneralPiece pieceOnTile = null;
        boolean firstMove;
        int whitePlayerT = 0;
        int blackPlayerT = 0;
        int lineCount = 1;

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "INVALID FILE NAME OR LOCATION - FILE NOT FOUND!", ex);
            Baners.createErrorBanner("INVALID FILE NAME OR LOCATION - FILE NOT FOUND!");
        }

        try {
            //READ FIRST THREE LINES CONTAINING - CURRENT PLAYER, OPPONENT, TIMERS
            for (int i = 1; i < 4; i++) {

                input = br.readLine();
                System.out.println(input);
                String[] playerInfo = input.split("#");

                for (int j = 0; j < playerInfo.length; j++) {
                    playerInfo[i] = playerInfo[i].trim();
                }
                if (i == 1) {
                    currentPlayer = playerInfo[2].trim();
                    System.out.println(playerInfo[2]);
                    lineCount++;

                    switch (currentPlayer) {
                        case "WHITE":
                            currentPlayerAlliance = Alliance.WHITE;
                            break;
                        case "BLACK":
                            currentPlayerAlliance = Alliance.BLACK;
                            break;
                        default:
                            throw new RuntimeException("Invalid current player alliance! Check first line of the file.");
                    }

                    String CurType = playerInfo[4].trim();

                    //Backout current player type
                    switch (CurType) {
                        case "HUMAN":
                            currentPlayerType = PlayerType.HUMAN;
                            break;
                        case "COMPUTER":
                            currentPlayerType = PlayerType.COMPUTER;
                            break;
                        default:
                            throw new RuntimeException("Invalid current player type! Check first line of the file.");
                    }


                } else if (i == 2) {
                    Opponent = playerInfo[2].trim();
                    String OpType = playerInfo[4].trim();
                    lineCount++;
                    //backout opponent player type
                    switch (OpType) {
                        case "HUMAN":
                            opponentPlayerType = PlayerType.HUMAN;
                            break;
                        case "COMPUTER":
                            opponentPlayerType = PlayerType.COMPUTER;
                            break;
                        default:
                            throw new RuntimeException("Invalid opponent type! Check second line of the file.");
                    }


                } else {
                    lineCount++;
                    try {
                        whitePlayerT = Integer.parseInt(playerInfo[2].trim());
                        blackPlayerT = Integer.parseInt(playerInfo[4].trim());
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, "FAILED PARSING THE SAVE GAME TIME!", ex);
                        Baners.createErrorBanner("Invalid timestamp in the saved file! Check the third line of the file.");
                    }
                }

            }

            if (currentPlayerAlliance == Alliance.WHITE) {
                loadedBoard = new Board(currentPlayerType, opponentPlayerType, whitePlayerT, blackPlayerT);
            } else {
                loadedBoard = new Board(opponentPlayerType, currentPlayerType, whitePlayerT, blackPlayerT);
            }


            loadedBoard.startingGameSetup();
            for (int i = 0; i < BOARD_ROWS; i++) {
                for (int j = 0; j < BOARD_COLUMNS; j++) {
                    loadedBoard.setTile(new Position(i, j), new Tile(i, j));
                }
            }
            if (currentPlayerAlliance == Alliance.WHITE) {
                loadedBoard.setCurrentPlayerToWhite();
            } else {
                loadedBoard.setCurrentPlayerToBlack();
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "FAILED FORMATTING OF THE PLAYERS or TIME INPUT LINES!", ex);
        }
        //Read the remaining lines of the file containing information about all the pieces on the board and
        //their positions
        try {
            while ((input = br.readLine()) != null) {

                input = input.replace(" ", "");
                String[] separatedInput = input.split("#");

                for (int i = 0; i < separatedInput.length; i++) {
                    separatedInput[i] = separatedInput[i].trim();
                }

                piecePosition = separatedInput[2];
                pieceType = separatedInput[4];
                pieceAlliance = separatedInput[6];
                pieceAlliance = pieceAlliance.trim();
                isFirstMove = separatedInput[8];

                piecePosition = piecePosition.replace("(", "");
                piecePosition = piecePosition.replace(")", "");
                piecePosition = piecePosition.trim();
                String[] positionInput = piecePosition.split(",");

                //GET POSITION
                truePosition = new Position(Integer.parseInt(positionInput[0]), Integer.parseInt(positionInput[1]));

                //ASSIGN ALLIANCE
                switch (pieceAlliance) {
                    case "black":
                        trueAlliance = Alliance.BLACK;
                        break;
                    case "white":
                        trueAlliance = Alliance.WHITE;
                        break;
                    default:
                        throw new RuntimeException("INVALID ALLIANCE!" + " On line: " + lineCount);
                }

                //ASSING isFrstMove
                switch (isFirstMove) {
                    case "true":
                        firstMove = true;
                        break;
                    case "false":
                        firstMove = false;
                        break;
                    default:
                        throw new RuntimeException("INVLALID INPUT TO FIRST MOVE!" + " On line: " + lineCount);

                }

                //BACKOUT PIECE TYPE, CREATE NEW PIECE ON THE PREPARED BOARD
                switch (pieceType) {
                    case "king":
                        loadedBoard.getTile(truePosition).setPiece(new King(truePosition, trueAlliance, firstMove));
                        break;
                    case "queen":
                        loadedBoard.getTile(truePosition).setPiece(new Queen(truePosition, trueAlliance, firstMove));
                        break;
                    case "rook":
                        loadedBoard.getTile(truePosition).setPiece(new Rook(truePosition, trueAlliance, firstMove));
                        break;
                    case "knight":
                        loadedBoard.getTile(truePosition).setPiece(new Knight(truePosition, trueAlliance, firstMove));
                        break;
                    case "bishop":
                        loadedBoard.getTile(truePosition).setPiece(new Bishop(truePosition, trueAlliance, firstMove));
                        break;
                    case "pawn":
                        loadedBoard.getTile(truePosition).setPiece(new Pawn(truePosition, trueAlliance, firstMove));
                        break;
                    default:
                        throw new RuntimeException("INVLALID PIECE TYPE!" + " On line: " + lineCount);
                }
                lineCount++;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "FAIL IN FORMATTING OF THE PIECES SECTION!" + "On line " + lineCount, ex);
            Baners.createErrorBanner("Loading of the game failed! Check the log for more information.");

        }

        try {
            br.close();
            Baners.createInfoBanner("Load game", "Game was loaded sucessfully!");
            loadedBoard.copiedBoardSetup();
            return loadedBoard;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "PROBLEM CLOSING THE FILE THAT IS READ FROM!", ex);
        }

        return originalBoard;
    }


//    //SERIALIZATION METHODS, NEVER USED
//    public void write(Board board, String name){
//        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(name))){
//            out.writeObject(board);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    //SERIALIZATION METHODS, NEVER USED
//    public Board readBoard(String name) throws IOException, ClassNotFoundException {
//        ObjectInputStream in = null;
//        try {
//            in = new ObjectInputStream(new FileInputStream(name));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return (Board) in.readObject();
//        }

}

