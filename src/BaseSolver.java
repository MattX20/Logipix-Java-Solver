import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Serves as an abstract base for different logipix solver algorithms.
 * Initializes the instance grid and provides the foundation for solving mechanisms 
 * to be specified by specific algorithms in derived classes.
 */
public abstract class BaseSolver {
    // Dimensions of the instance grid.
    int n, m;

    // Graphical user interface to visualize the instance.
    GUI gui;

    // Represents the current state of each cell in the puzzle.
    Cell[][] configuration;

    // Possible moves in the grid, representing right, down, left, and up.
    int mvt[][] = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    /**
     * Constructs a BaseSolver and initializes the instance configuration.
     *
     * @param fileName The file path of the instance's input data.
     * @throws IOException If an error occurs while reading the file.
     */
    BaseSolver(String fileName) throws IOException {
        // Load the puzzle file content into a string.
        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        Scanner scanner = new Scanner(content);

        // Initialize the dimensions of the grid.
        m = scanner.nextInt();
        n = scanner.nextInt();

        // Set up the initial configuration of the grid.
        configuration = new Cell[n][m];

        // Initialize the graphical user interface.
        gui = new GUI(m, n, () -> solve());

        // Populate the grid with cells, based on the input file.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int value = scanner.nextInt();
                configuration[i][j] = new Cell(i, j, value);

                // Update the GUI to print the number if the cell has a value greater than 0
                if (value > 0) {
                    gui.setValue(i, j, value);
                }
            }
        }

        scanner.close();
    }

    /**
     * Abstract method defining the solving mechanism.
     * This method must be implemented by subclasses with specific solving strategies.
     */
    abstract void solve();
}