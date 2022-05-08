import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * This is the grid panel which represents the circuit board for the CircuitTracerGUI.
 * 
 * @author Digno JR Teogalbo
 * @version CS221-002 Spring 2022
 */
class GridPanel extends JPanel {
    private int rows;
    private int cols;

    /**
     * Initializes the a grid of cells with the given CircuitBoard.
     * 
     * @param board CircuitBoard to create the grid
     */
    public GridPanel(CircuitBoard board) {
        this.rows = board.numRows();
        this.cols = board.numCols();
        setLayout(new GridLayout(rows, cols));
        setPreferredSize(new Dimension(400, 400));
        createGrid(board);
    }

    /**
     * Removes all existing components, then creates and adds every cell in the given board to the panel.
     * 
     * @param board CircuitBoard to create the grid
     */
    public void createGrid(CircuitBoard board) {
        removeAll();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                add(new Cell(board.charAt(r, c)));
            }
        }

        repaint();
        revalidate();
    }
}