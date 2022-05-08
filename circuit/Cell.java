import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * This is a single cell for a circuit board grid.
 * 
 * @author Digno JR Teogalbo
 * @version CS221-002 Spring 2022
 */
public class Cell extends JLabel {
    private static final Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

    /**
     * Constructor to create a cell from a given char.
     * 
     * @param symbol char which denotes the type of cell
     */
    public Cell(char symbol) {
        switch (symbol) {
            case 'T':
                setForeground(Color.GREEN);
                break;
            case '1':
                setForeground(Color.BLUE);
                break;
            case '2':
                setForeground(Color.RED);
                break;
            case 'O':
                setForeground(Color.LIGHT_GRAY);
                break;
            default:
                setForeground(Color.BLACK);
                break;
        }

        setPreferredSize(new Dimension(10, 10));
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(border);
        setOpaque(true);
        setFont(new Font("Arial", Font.BOLD, 40));
        setText(symbol + "");
    }

    /**
     * Changes the symbol of the current cell.
     * 
     * @param symbol char of the new symbol
     */
    public void changeCell(char symbol) {
        switch (symbol) {
            case 'T':
                setForeground(Color.GREEN);
                break;
            case '1':
                setForeground(Color.BLUE);
                break;
            case '2':
                setForeground(Color.RED);
                break;
            case 'O':
                setForeground(Color.LIGHT_GRAY);
                break;
            default:
                setForeground(Color.BLACK);
                break;
        }

        setText(symbol + "");
    }
}
