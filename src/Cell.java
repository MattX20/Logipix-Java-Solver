/**
 * Represents a single cell within the puzzle grid.
 */
public class Cell {
    // Attributes used only by (improved) combination exclusion solver.
    public int position = -1, b1 = -1; 

    // 'i' and 'j' represent the cell's coordinates within the grid. 
    //'value' is the number assigned to the cell.
    public final int i, j, value;

    // The 'state' represents the current status of the cell in the solving process.
    public State state;

    /**
     * Enum describing possible states of a cell during the solving process.
     * COLORED: The cell is part of the solution.
     * MAYBE_COLORED: The cell is considered in the current tested configuration.
     * NOT_COLORED: The cell is not part of solution/tested configuration.
     */
    public enum State {
        Colored, MaybeColored, NotColored;
    }

    /**
     * Constructor for creating a new cell instance with its position and value.
     * 
     * @param i     The row position of the cell in the grid.
     * @param j     The column position of the cell in the grid.
     * @param value The number assigned to the cell.
     */
    Cell(int i, int j, int value) {
        this.i = i;
        this.j = j;
        this.value = value;
        state = State.NotColored;
    }

}
