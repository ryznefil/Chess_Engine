/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Chessman;

import fel.cvut.cz.Board.Board;
import fel.cvut.cz.Move.AttackMove;
import fel.cvut.cz.Move.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import fel.cvut.cz.Move.StandardMove;
import fel.cvut.cz.Player.Alliance;
import fel.cvut.cz.Player.PlayerType;
import fel.cvut.cz.Utilities.Position;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ryzny
 */
public class QueenTest {

    private static final Logger LOG = Logger.getLogger(QueenTest.class.getName());

    public QueenTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of calculateLegalMoves method, of class Queen.
     */
    @Test
    public void testCalculateLegalMoves() {
        LOG.log(Level.INFO, "Starting queen test");


        Board board = new Board(PlayerType.HUMAN, PlayerType.HUMAN, 7200, 7200);

        board.startingGameSetup();
        Pawn pawn1 = new Pawn(new Position(5,1), Alliance.BLACK);
        Pawn pawn2 = new Pawn(new Position(5,3), Alliance.BLACK);

        board.getTile(5, 1).setPiece(pawn1);
        board.getTile(5, 3).setPiece(pawn2);
        board.getTile(6, 2).deletePiece();
        board.getTile(6, 3).deletePiece();

        Queen instance = (Queen) board.getTile(7, 3).getPiece();

        Collection<Move> expResult = new ArrayList<>();
        expResult.add(new StandardMove(board,new Position(6,3) ,instance));
        expResult.add(new AttackMove(board,new Position(5,3) ,instance, pawn1));
        expResult.add(new StandardMove(board,new Position(6,2) ,instance));
        expResult.add(new AttackMove(board,new Position(5,1) ,instance, pawn2));

        Collection<Move> result = instance.calculateLegalMoves(board);

        int resultSize = result.size();
        int expeSize = expResult.size();

        System.out.println();
        assertEquals(expeSize, resultSize);


        LOG.log(Level.INFO, "Queen test passed");
    }



    
}
