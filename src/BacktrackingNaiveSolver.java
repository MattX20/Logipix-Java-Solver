import java.io.IOException;
import java.util.Vector;


/**
 * Implements a naive backtracking algorithm to solve the instance.
 * This solver extends the base functionality provided by BaseSolver.
 */
public class BacktrackingNaiveSolver extends BaseSolver {
    // Tracks the current position in the solving process.
    private int cluePosition = 0;
    // Holds cells with clues (non-empty cells).
    Vector<Cell> numberedCells; 

    /**
     * Constructs a BacktrackingNaiveSolver and initializes the instance configuration.
     *
     * @param fileName The file path of the instance's input data.
     * @throws IOException If an error occurs while reading the file.
     */
    BacktrackingNaiveSolver(String fileName) throws IOException {
        super(fileName); // Initialize the base solver.
        numberedCells = new Vector<Cell>();

        // Populate the vector with non-empty cells.
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (configuration[i][j].value > 0)
                    numberedCells.add(configuration[i][j]);
    }

    /**
     * Checks if a cell can be used for backtraking within the context of the current grid and constraints.
     *
     * @param i Row position in the grid.
     * @param j Column position in the grid.
     * @param l Remaining length for the current clue.
     * @return true if the move is valid; otherwise, false.
     */
    private boolean isValid(int i, int j, int l) {
        return (i > -1) && (i < n) && (j > -1) && (j < m) && (configuration[i][j].state == Cell.State.NotColored)
                && (l == 0 || configuration[i][j].value == 0);
    }

    /**
     * Recursive backtracking algorithm for path-finding in the instance.
     *
     * @param current The current cell in focus.
     * @param l       The remaining length from the current clue.
     * @return true if a solution path is found; otherwise, false.
     */
    private boolean backtracking(Cell current, int l) {
        // If the current cell is already colored, it is already in use in a previously explored path.
        // Can only occur when increasing cluePosition
        if (current.state != Cell.State.NotColored) {
            
            // Move to the next clue position as the current one is part of an existing path.
            cluePosition++;

            // If all clues are processed, the instance is solved.
            if (cluePosition == numberedCells.size())
                return true;

            // Get the next cell with a number to continue the path exploration.
            Cell newCurrent = numberedCells.get(cluePosition);

            // Recurse with the new cell. If a solution is found, return true.
            if (backtracking(newCurrent, newCurrent.value - 1))
                return true;
            
            // If no solution is found, backtrack.
            cluePosition--;
            return false;
        }

        // If the path length matches the clue (i.e., we've reached the expected path length).
        if (l == 0) {
            // Check if the current cell value matches the current clue value.
            if (current.value == numberedCells.get(cluePosition).value) {
                 // Temporarily color the cell (red).
                current.state = Cell.State.MaybeColored;
                gui.changeRed(current.i, current.j);
                // Move to the next clue position
                cluePosition++;

                // If all clues are processed, the instance is solved.
                if (cluePosition == numberedCells.size())
                    return true;

                // Get the next cell with a number to continue the path exploration.
                Cell newCurrent = numberedCells.get(cluePosition);

                // Recurse with the new cell. If a solution is found, return true.
                if (backtracking(newCurrent, newCurrent.value - 1))
                    return true;
                
                // If no solution is found, change the color of the cell and backtrack
                current.state = Cell.State.NotColored;
                gui.changeGrey(current.i, current.j);
                cluePosition--;
                return false;
            } 
            else
                return false; 
        }

        // Temporarily color the cell (red).
        current.state = Cell.State.MaybeColored;
        gui.changeRed(current.i, current.j);

        // Explore all possible directions from the current cell.
        for (int k = 0; k < 4; k++) {
            // If moving in this direction is valid (within bounds and unexplored),
            // move in the direction and continue the path exploration recursively.
            if (isValid(current.i + mvt[k][0], current.j + mvt[k][1], l - 1)) {
                if (backtracking(configuration[current.i + mvt[k][0]][current.j + mvt[k][1]], l - 1))
                    return true; // A valid solution is found.
            }
        }
        // If no solution is found, change the color of the cell and backtrack
        current.state = Cell.State.NotColored;
        gui.changeGrey(current.i, current.j);
        return false;
    }

    /**
     * Initiates the solving process. Overrides the abstract solve method in BaseSolver.
     */
    @Override
    void solve() {
        if (numberedCells.size() == 0)
            return; // No non-empty cells, nothing to solve.

        // Start the backtracking process from the first clue.
        Cell start = numberedCells.get(cluePosition);
        backtracking(start, start.value - 1);

        // Turn the temporarily colored cells (red) into colored cells (green) after a solution has been found.
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (configuration[i][j].state == Cell.State.MaybeColored)
                    gui.changeGreen(i, j);

    }
}
