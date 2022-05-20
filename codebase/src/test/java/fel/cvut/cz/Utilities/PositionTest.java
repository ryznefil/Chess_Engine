/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Utilities;

import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryzny
 */
public class PositionTest {

    /**
     *
     */
    public PositionTest() {
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
     * Test of addPositionToPosition method, of class Position.
     */
    @Test
    public void testAddPositionToPosition() {
        System.out.println("addPositionToPosition");
        Position position = new Position(1, 1);
        Position instance = new Position(2, 2);
        Position expResult = new Position(3, 3);
        Position result = instance.addPositionToPosition(position);
        assertEquals(expResult.getxCoordinate(), result.getxCoordinate());
        assertEquals(expResult.getyCoordinate(), result.getyCoordinate());
    }

    /**
     * Test of positionsEqual method, of class Position.
     */
    @Test
    public void testPositionsEqual() {
        System.out.println("positionsEqual");
        Position comparedPosition = new Position(2, 2);
        Position instance = new Position(2, 2);
        boolean expResult = true;
        boolean result = instance.positionsEqual(comparedPosition);
        assertEquals(expResult, result);
    }

    /**
     * Test of isOnBoard method, of class Position.
     */
    @Test
    public void testIsOnBoard_0args() {
        System.out.println("isOnBoard");
        Position instance = new Position(2, 2);
        boolean expResult = true;
        boolean result = instance.isOnBoard();
        assertEquals(expResult, result);

        Position instance2 = new Position(8, 10);
        boolean expResult2 = false;
        boolean result2 = instance2.isOnBoard();
        assertEquals(expResult2, result2);

    }

    /**
     * Test of setxCoordinate method, of class Position.
     */
    @Test
    public void testSetxCoordinate() {
        System.out.println("setxCoordinate");
        int xCoordinate = 5;
        int expResult = 5;
        Position instance = new Position(3, 5);
        instance.setxCoordinate(xCoordinate);
        assertEquals(expResult, instance.getxCoordinate());
    }

    /**
     * Test of setyCoordinate method, of class Position.
     */
    @Test
    public void testSetyCoordinate() {
        System.out.println("setyCoordinate");
        int yCoordinate = 1;
        int expResult = 1;
        Position instance = new Position(3, 5);
        instance.setyCoordinate(yCoordinate);
        assertEquals(expResult, instance.getyCoordinate());
    }

    /**
     * Test of getxCoordinate method, of class Position.
     */
    @Test
    public void testGetxCoordinate() {
        System.out.println("getxCoordinate");
        Position instance = new Position(5, 5);
        int expResult = 5;
        int result = instance.getxCoordinate();
        assertEquals(expResult, result);

    }

    /**
     * Test of getyCoordinate method, of class Position.
     */
    @Test
    public void testGetyCoordinate() {
        System.out.println("getyCoordinate");
        Position instance = new Position(5, 3);
        int expResult = 3;
        int result = instance.getyCoordinate();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Position.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Position instance = new Position(5, 5);
        String expResult = "Position{xCoordinate=5, yCoordinate=5}";
        String result = instance.toString();
        assertEquals(expResult, result);

    }

}
