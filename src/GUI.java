import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Represents the graphical user interface for the Logipix solver,
 * providing visual components for user interaction and displaying the puzzle grid.
 */
public class GUI extends JFrame implements ActionListener {
    // Constants representing dimensions and constraints for the GUI components.
    private final int bw = 200, bh = 60, beps = 40, maxW = 1400, maxH = 880 ;

    // Variable to determine the size of each cell in the grid.
    private int sizeB = 30;
    
    // Variables for storing the dimensions of the grid.
    private int width, height;

    // Panel hosting the grid.
    private JPanel logipixGrid;

    // Button to initiate the solving process.
    private JButton startButton;

    // Two-dimensional array representing the individual cells in the instance grid.
    private JButton logipixCells[][];

    // The solver algorithm to be run.
    private Runnable solver;

    // Flag to prevent the solver from being launched multiple times.
    private boolean launched = false;

    /**
     * Constructs the GUI with specified grid dimensions and solver algorithm.
     *
     * @param width  The width of the instance grid.
     * @param height The height of the instance grid.
     * @param solver The solver algorithm encapsulated as a Runnable.
     */
    public GUI(int width, int height, Runnable solver) {
        // Initial setup for the frame.
        this.width = width;
        this.height = height;
        this.solver = solver;
        this.setTitle("Logipix");
        
        // Adjusting cell size to fit within maximum frame dimensions.
        if (sizeB * width + bw > maxW) {
        	sizeB = Math.floorDiv(maxW - bw, width) ;
        }
        if(sizeB * height + beps > maxH) {
        	sizeB = Math.floorDiv(maxH - beps, height);
        }

        // Configuring frame properties.
        this.setSize(sizeB * width + bw, sizeB * height + beps);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.black);
        this.setLayout(null);

        // Initializing components.
        init_button();
        init_grid();

        // Adding components to the frame and making it visible.
        this.add(logipixGrid);
        this.add(startButton);
        this.setVisible(true);
    }

    /**
     * Initializes the start button with its properties and event listener.
     */
    private void init_button() {
        startButton = new JButton("start solving");
        startButton.setSize(bw, bh);
        startButton.setLocation(sizeB * width, (sizeB * height - bh) / 2);
        startButton.addActionListener(this); // Registering current window to listen to action events.
        startButton.setOpaque(true);
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.DARK_GRAY);
    }

    /**
     * Creates the puzzle grid layout and initializes the properties of each cell.
     */
    private void init_grid() {
        // Configuring the panel that holds the instance grid.
        logipixGrid = new JPanel();
        logipixGrid.setSize(sizeB * width, sizeB * height);
        logipixGrid.setLocation(0, 0);
        logipixGrid.setLayout(null);
        logipixGrid.setOpaque(true);
        logipixGrid.setBackground(Color.BLACK);

        logipixCells = new JButton[height][width];

        UIManager.put("Button.disabledText", Color.BLACK);

        // Initializing each cell in the grid with properties.
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                logipixCells[i][j] = new JButton();
                logipixCells[i][j].setSize(sizeB, sizeB);
                logipixCells[i][j].setLocation(j * sizeB, i * sizeB);
                logipixCells[i][j].setBackground(Color.GRAY);
                logipixCells[i][j].setEnabled(false);
                logipixCells[i][j].setMargin(new Insets(0, 0, 0, 0));
                logipixCells[i][j].setOpaque(true);
                logipixCells[i][j].setFont(new Font("Arial", Font.PLAIN, 10));
                logipixGrid.add(logipixCells[i][j]);
            }
        }

    }

    
    /**
     * Updates the specified cell to indicate a non-used state.
     *
     * @param i Row index of the cell.
     * @param j Column index of the cell.
     */
    public void changeGrey(int i, int j) {
        logipixCells[i][j].setBackground(Color.GRAY);
    }

    /**
     * Updates the specified cell to indicate an in progress state.
     *
     * @param i Row index of the cell.
     * @param j Column index of the cell.
     */
    public void changeRed(int i, int j) {
        logipixCells[i][j].setBackground(Color.RED);
    }

    /**
     * Updates the specified cell to indicate a solution state.
     *
     * @param i Row index of the cell.
     * @param j Column index of the cell.
     */
    public void changeGreen(int i, int j) {
        logipixCells[i][j].setBackground(Color.GREEN);
    }

    /**
     * Sets the displayed value for the specified cell.
     *
     * @param i     Row index of the cell.
     * @param j     Column index of the cell.
     * @param value Numeric value to display in the cell.
     */
    public void setValue(int i, int j, int value) {
        logipixCells[i][j].setText(((Integer) value).toString());
    }

    /**
     * Responds to action from the start button.
     *
     * @param e The event object containing details about the action event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!launched) {
            Thread solverThread = new Thread(new Runnable() {
                public void run() {
                    solver.run();
                }
            });
            solverThread.start();
            launched = true;
        } else
            System.out.println("Solver déjà lancé !");
    }
}