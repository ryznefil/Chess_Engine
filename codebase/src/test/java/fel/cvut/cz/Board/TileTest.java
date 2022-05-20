/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Board;

import fel.cvut.cz.Chessman.GeneralPiece;
import fel.cvut.cz.Chessman.Queen;
import fel.cvut.cz.Player.Alliance;
import fel.cvut.cz.Utilities.Position;
import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryzny
 */
public class TileTest {

    /**
     *
     */
    public TileTest() {
    }

    /**
     *
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     *
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of hasPiece method, of class Tile.
     */
    @Test
    public void testHasPiece() {
        System.out.println("hasPiece");
        Tile instance = new Tile(new Position(1, 1), new Queen(new Position(1, 1), Alliance.WHITE));
        boolean expResult = true;
        boolean result = instance.hasPiece();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPiece method, of class Tile.
     */
    @Test
    public void testGetPiece() {
        System.out.println("getPiece");
        Position position = new Position(1, 1);
        Queen Karolina = new Queen(position, Alliance.WHITE);
        Tile instance = new Tile(position, Karolina);
        GeneralPiece expResult = Karolina;
        GeneralPiece result = instance.getPiece();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPiece method, of class Tile.
     */
    @Test
    public void testSetPiece() {
        System.out.println("setPiece");
        Position position = new Position(1, 1);
        Queen Karolina = new Queen(position, Alliance.WHITE);
        Tile instance = new Tile(position);
        instance.setPiece(Karolina);
        GeneralPiece result = instance.getPiece();
        GeneralPiece expResult = Karolina;
        assertEquals(expResult, result);

    }

    /**
     * Test of deletePiece method, of class Tile.
     */
    @Test
    public void testDeletePiece() {
        System.out.println("deletePiece");
        Position position = new Position(1, 1);
        Queen Karolina = new Queen(position, Alliance.WHITE);
        Tile instance = new Tile(position, Karolina);
        instance.deletePiece();
        GeneralPiece expected = null;
        GeneralPiece result = instance.getPiece();
        assertEquals(expected, result);
    }

    /**
     * Test of getxCoordinate method, of class Tile.
     */
    @Test
    public void testGetxCoordinate() {
        System.out.println("getxCoordinate");
        Tile instance = new Tile(new Position(1, 1));
        int expResult = 1;
        int result = instance.getxCoordinate();
        assertEquals(expResult, result);

    }

    /**
     * Test of getyCoordinate method, of class Tile.
     */
    @Test
    public void testGetyCoordinate() {
        System.out.println("getyCoordinate");
        Tile instance = new Tile(new Position(2, 3));
        int expResult = 3;
        int result = instance.getyCoordinate();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPieceOnTile method, of class Tile.
     */
    @Test
    public void testGetPieceOnTile() {
        System.out.println("getPieceOnTile");
        Position position = new Position(1, 1);
        Queen Karolina = new Queen(position, Alliance.WHITE);
        Tile instance = new Tile(position, Karolina);
        GeneralPiece expResult = Karolina;
        GeneralPiece result = instance.getPieceOnTile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTilePosition method, of class Tile.
     */
    @Test
    public void testGetTilePosition() {
        System.out.println("getTilePosition");
        Tile instance = new Tile(new Position(1, 1));
        Position Other = new Position(1, 1);
        boolean expected = true;
        boolean result = instance.getTilePosition().positionsEqual(Other);
        assertEquals(expected, result);
    }

}
