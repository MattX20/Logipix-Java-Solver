import java.io.IOException;

/**
 * Main class for initiating the Logipix puzzle solver.
 * It allows to choose different solving algorithms.
 */
public class Logipix {

    /**
     * The entry point of the application.
     *
     * @param args The command-line arguments. Not utilized in this application.
     * @throws IOException If an exception occurre while reading the file.
     */
    public static void main(String args[]) throws IOException {
        // The relative path to the puzzle file.
    	String file = "data/5.txt";

        // Instantiate the desired solver to use on the instance.

        // Uncomment to use the naive backtracking solver.
    	//new BacktrackingNaiveSolver(file);

        // Uncomment to use the standard combination exclusion solver.
        //new CombinationExclusionSolver(file);

        // Uncomment to use the improved combination exclusion solver.
        new ImprovedCombinationExclusionSolver(file);
    }
}
