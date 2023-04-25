package nl.tudelft.jpacman.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite to confirm that {@link Unit}s correctly (de)occupy squares.
 *
 * @author Jeroen Roosen 
 *
 */
class OccupantTest {

    /**
     * The unit under test.
     */
    private Unit unit;

    /**
     * Resets the unit under test.
     */
    @BeforeEach
    void setUp() {
        unit = new BasicUnit();
    }

    /**
     * Asserts that a unit has no square to start with.
     */
    @Test
    void noStartSquare() {
        // Remove the following placeholder:
        assertThat(unit).isNotNull();
        assertThat(unit.hasSquare()).isFalse();

    }

    /**
     * Tests that the unit indeed has the target square as its base after
     * occupation.
     */
    @Test
    void testOccupy() {
    	Square square = new BasicSquare();
    	unit.occupy(square);

    	assertThat(unit.getSquare()).isEqualTo(square);//unit拥有一个目标方格
    	assertThat(square.getOccupants()).contains(unit);//square容纳一个unit
    }

    @Test
    void testReoccupy(){
        Square square = new BasicSquare();
        unit.occupy(square);
        unit.occupy(square);

        //验证是否占据成功
        assertThat(unit.hasSquare()).isTrue();
        assertThat(unit.getSquare()).isEqualTo(square);
        //验证unit是否在squre的列表中
        assertThat(unit.invariant()).isTrue();
        assertThat(square.getOccupants()).contains(unit);
    }

}
