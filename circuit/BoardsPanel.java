import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The panel that displays the options of boards to select for the CircuitTracerGUI.
 * 
 * @author Digno JR Teogalbo
 * @version CS221-002 Spring 2022
 */
class BoardsPanel extends JPanel {
    private CircuitTracerGUI circuitTracer;

    /**
     * Initializes the BoardsPanel for the CircuitTracerGUI
     * 
     * @param circuitTracer the circuit tracer program
     */
    public BoardsPanel(CircuitTracerGUI circuitTracer) {
        this.circuitTracer = circuitTracer;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JButton initial = new BoardButton("Original", circuitTracer.getCircuitTracer().getBoard());
        add(initial);

        int i = 1;
        for (TraceState path : circuitTracer.getCircuitTracer().getBestPaths()) {
            add(new BoardButton(String.format("Solution %d", i), path.getBoard()));
            i++;
        }
    }

    /**
     * BoardButton class for the BoardsPanel
     */
    private class BoardButton extends JButton {
        /**
         * Creates a button for the board.
         * 
         * @param text text for the button
         * @param board circuit board assigned for the button
         */
        public BoardButton(String text, CircuitBoard board) {
            super(text);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    circuitTracer.setCurrentBoard(board);
                }
            });
        }
    }
}