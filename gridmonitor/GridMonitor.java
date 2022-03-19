import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A class that monitors a grid from a solar array.
 * 
 * @author Digno JR Teogalbo
 * @version CS 221 Section 002 Spring 2022
 */
public class GridMonitor implements GridMonitorInterface {
    private double[][] baseGrid;
    private int width;
    private int height;

    private int i;
    private int j;

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    GridMonitor gridMonitor = new GridMonitor(args[i]);
                    System.out.println(args[i]);
                    System.out.println(gridMonitor.toString());
                }
            }
        } catch (FileNotFoundException err) {
            System.out.println(err.toString());
        }
    }

    /**
     * Constructor for the GridMonitor class. Reads files from the compiled source directory.
     * 
     * @param filename string of the filename including .txt extension
     * @throws FileNotFoundException thrown when the file does not exist, cannot be read, or the file has invalid formatting
     */
    public GridMonitor(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner fileScanner = new Scanner(file);

        try {
            String firstLine = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(firstLine);

            height = lineScanner.nextInt();
            width = lineScanner.nextInt();

            lineScanner.close();
            baseGrid = new double[height][width];

            for (i = 0; i < height; i++) {
                String line = fileScanner.nextLine();
                lineScanner = new Scanner(line);

                for (j = 0; j < width; j++) {
                    double value = lineScanner.nextDouble();
                    if (Math.abs(value) >= Double.MAX_VALUE / 4.0) {
                        lineScanner.close();
                        throw new FileNotFoundException("Value too large.");
                    } else {
                        baseGrid[i][j] = value;
                    }
                }

                lineScanner.close();
            }

        } catch (NoSuchElementException err) {
            fileScanner.close();
            throw new FileNotFoundException("Invalid file formatting.");
        }

        fileScanner.close();
    }

    @Override
    public double[][] getBaseGrid() {
        double[][] base = new double[height][width];

        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                base[i][j] = baseGrid[i][j];
            }
        }

        return base;
    }

    @Override
    public double[][] getSurroundingSumGrid() {
        double[][] sumGrid = new double[height][width];

        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                double top = (i - 1 >= 0) ? baseGrid[i-1][j] : baseGrid[i][j];
                double right = (j + 1 < width) ? baseGrid[i][j+1] : baseGrid[i][j];
                double bottom = (i + 1 < height) ? baseGrid[i+1][j] : baseGrid[i][j];
                double left = (j - 1 >= 0) ? baseGrid[i][j-1] : baseGrid[i][j];

                double totalSum = top + right + bottom + left;

                sumGrid[i][j] = totalSum;
            }
        }

        return sumGrid;
    }

    @Override
    public double[][] getSurroundingAvgGrid() {
        double[][] sumAvgGrid = getSurroundingSumGrid();

        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                sumAvgGrid[i][j] /= 4.0;
            }
        }

        return sumAvgGrid;
    }

    @Override
    public double[][] getDeltaGrid() {
        double[][] deltaGrid = getSurroundingAvgGrid();

        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                deltaGrid[i][j] = Math.abs(deltaGrid[i][j] / 2.0);
            }
        }

        return deltaGrid;
    }

    @Override
    public boolean[][] getDangerGrid() {
        boolean[][] dangerGrid = new boolean[height][width];
        double[][] avg = getSurroundingAvgGrid();
        double[][] delta = getDeltaGrid();
        
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                double value = baseGrid[i][j];
                double low = avg[i][j] - delta[i][j];
                double high = avg[i][j] + delta[i][j];

                dangerGrid[i][j] = (value < low || value > high);
            }
        }

        return dangerGrid;
    }
    
    @Override
    public String toString() {
        String output = "";

        output += "Base Grid\n";
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                output += String.format("[%.1f] ", baseGrid[i][j]);
            }
            output += "\n";
        }

        output += "\nSurrounding Sum Grid\n";
        double[][] sumGrid = getSurroundingSumGrid();
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                output += String.format("[%.1f] ", sumGrid[i][j]);
            }
            output += "\n";
        }

        output += "\nSurrounding Avg. Grid\n";
        double[][] avgGrid = getSurroundingAvgGrid();
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                output += String.format("[%.1f] ", avgGrid[i][j]);
            }
            output += "\n";
        }

        output += "\nSurrounding Deltas Grid\n";
        double[][] deltaGrid = getDeltaGrid();
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                output += String.format("[%.1f] ", deltaGrid[i][j]);
            }
            output += "\n";
        }

        output += "\nDanger Grid\n";
        boolean[][] dangerGrid = getDangerGrid();
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                output += String.format("[%s] ", dangerGrid[i][j]);
            }
            output += "\n";
        }

        return output;
    }
}
