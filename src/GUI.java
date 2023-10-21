import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//GUI

public class GUI extends JFrame implements ActionListener {
    private final int bw = 200, bh = 60, beps = 40, maxW = 1400, maxH = 880 ;
    private int sizeB = 30;
    private int width, height;
    private JPanel logipixGrid;
    private JButton startButton;
    private JButton logipixCells[][];
    private Runnable solver;
    private boolean launched = false;

    public GUI(int width, int height, Runnable solver) {
        // frame settings
        this.width = width;
        this.height = height;
        this.solver = solver;
        this.setTitle("Logipix");
        
        if (sizeB * width + bw > maxW) {
        	sizeB = Math.floorDiv(maxW - bw, width) ;
        }
        if(sizeB * height + beps > maxH) {
        	sizeB = Math.floorDiv(maxH - beps, height);
        }
        
        this.setSize(sizeB * width + bw, sizeB * height + beps);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.black);
        this.setLayout(null);

        init_button();
        init_grid();

        this.add(logipixGrid);
        this.add(startButton);
        this.setVisible(true);
    }

    private void init_button() { // button settings
        startButton = new JButton("start solving");
        startButton.setSize(bw, bh);
        startButton.setLocation(sizeB * width, (sizeB * height - bh) / 2);
        startButton.addActionListener(this);
        startButton.setOpaque(true);
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.DARK_GRAY);
    }

    private void init_grid() { // grid settings
        logipixGrid = new JPanel();
        logipixGrid.setSize(sizeB * width, sizeB * height);
        logipixGrid.setLocation(0, 0);
        logipixGrid.setLayout(null);
        logipixGrid.setOpaque(true);
        logipixGrid.setBackground(Color.BLACK);

        logipixCells = new JButton[height][width];

        UIManager.put("Button.disabledText", Color.BLACK);

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

    
    //Cell status
    public void changeGrey(int i, int j) {
        logipixCells[i][j].setBackground(Color.GRAY);
    }

    public void changeRed(int i, int j) {
        logipixCells[i][j].setBackground(Color.RED);
    }

    public void changeGreen(int i, int j) {
        logipixCells[i][j].setBackground(Color.GREEN);
    }

    public void setValue(int i, int j, int value) {
        logipixCells[i][j].setText(((Integer) value).toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // define action when button is clicked (here start logipix solver)
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