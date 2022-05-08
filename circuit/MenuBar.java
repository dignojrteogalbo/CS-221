import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * This is the menu bar for the CircuitTracerGUI.
 * 
 * @author Digno JR Teogalbo
 * @version CS221-002 Spring 2022
 */
public class MenuBar extends JMenuBar implements ActionListener {
    private CircuitTracerGUI circuitTracerGUI;
    private JMenu fileMenu;
    private JMenuItem exportItem, exportAllItem, quitItem;

    /**
     * Initializes the MenuBar with the file menu.
     * 
     * @param circuitTracerGUI parent CircuitTracerGUI program
     */
    public MenuBar(CircuitTracerGUI circuitTracerGUI) {
        this.circuitTracerGUI = circuitTracerGUI;
        fileMenu = new JMenu("File");
        CreateFileMenu();
    }

    /**
     * Creates the file menu option for the menu bar.
     */
    private void CreateFileMenu() {
        exportItem = new JMenuItem("Export Current Board");
        exportItem.addActionListener(this);
        fileMenu.add(exportItem);

        exportAllItem = new JMenuItem("Export All Solutions");
        exportAllItem.addActionListener(this);
        fileMenu.add(exportAllItem);

        fileMenu.addSeparator();

        quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);

        add(fileMenu);
    }

    /**
     * Exports the current board to a file.
     */
    private void exportSelected() {
        CircuitBoard board = circuitTracerGUI.getCurrentBoard();
        StringBuilder output = new StringBuilder(String.format("%d %d\n", board.numRows(), board.numCols()));
        output.append(board.toString());
        writeToFile(output.toString());
    }

    /**
     * Exports all board solutions to a file.
     */
    private void exportAll() {
        ArrayList<TraceState> allBoard = circuitTracerGUI.getCircuitTracer().getBestPaths();
        StringBuilder output = new StringBuilder();

        for (TraceState trace : allBoard) {
            CircuitBoard board = trace.getBoard();
            output.append(String.format("%d %d\n", board.numRows(), board.numCols()));
            output.append(board.toString() + "\n");
        }

        writeToFile(output.toString());
    }

    /**
     * Opens a dialogue window to select and save a file. Writes content to a new file.
     * 
     * @param content text content to write to file
     */
    private void writeToFile(String content) {
        JFileChooser fileChooser = new JFileChooser();
        int optionChoice = fileChooser.showSaveDialog(null);

        if (optionChoice != JFileChooser.APPROVE_OPTION) return;

        File file = fileChooser.getSelectedFile();
        int overwriteResponse = JOptionPane.YES_OPTION;

        if (file.exists()) {
            overwriteResponse = JOptionPane.showConfirmDialog(
                null,
                "File already exists, do you want to overwrite this file?",
                "Overwrite File?",
                JOptionPane.YES_NO_CANCEL_OPTION
            );
        }

        if (overwriteResponse == JOptionPane.YES_OPTION) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(content);
            } catch (Exception err) {
                System.out.println(err);
            }
        }
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source == exportItem) {
            exportSelected();
        } else if (source == exportAllItem) {
            exportAll();
        } else if (source == quitItem) {
            System.exit(0);
        }
    }
}
