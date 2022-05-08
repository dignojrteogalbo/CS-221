import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import java.awt.*;

/**
 * This is the Circuit Tracer GUI
 * 
 * @author Digno JR Teogalbo
 * @version CS221-002 Spring 2022
 */
public class CircuitTracerGUI extends JComponent {
    private CircuitTracer circuitTracer;
    private CircuitBoard currentBoard;
    private GridPanel gridPanel;
    private JPanel boardsPanel;

    /**
     * GUI Constructor for thrown exceptions.
     * 
     * @param error exception thrown from circuit tracer program
     */
    public CircuitTracerGUI(Exception error) {
        JFrame frame = new JFrame("Circuit Tracer");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        String errorContent = (error.getMessage() != null) ? error.getMessage() : error.toString();
        JOptionPane.showMessageDialog(frame, errorContent, error.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    /**
     * GUI Constructor for valid files.
     * 
     * @param circuitTracer the circuit tracer program
     */
    public CircuitTracerGUI(CircuitTracer circuitTracer) {
        this.circuitTracer = circuitTracer;
        currentBoard = circuitTracer.getBoard();
        gridPanel = new GridPanel(currentBoard);
        boardsPanel = new BoardsPanel(this);
        showGUI();
    }

    /**
     * Shows the GUI.
     */
    public void showGUI() {
        JFrame frame = new JFrame("Circuit Tracer");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(480, 480));
        Container pane = frame.getContentPane();
        frame.setJMenuBar(new MenuBar(this));
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gridPanel, new JScrollPane(boardsPanel));
        sp.setOneTouchExpandable(true);
        sp.setContinuousLayout(true);
        pane.add(sp);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Sets the current displayed board.
     * 
     * @param board CircuitBoard to display
     */
    public void setCurrentBoard(CircuitBoard board) {
        currentBoard = board;
        gridPanel.createGrid(currentBoard);
    }

    /**
     * Returns the current displayed circuit board.
     * 
     * @return current displayed circuit board
     */
    public CircuitBoard getCurrentBoard() {
        return currentBoard;
    }

    /**
     * Returns the CircuitTracer program
     * 
     * @return CircuitTracer
     */
    public CircuitTracer getCircuitTracer() {
        return circuitTracer;
    }
}