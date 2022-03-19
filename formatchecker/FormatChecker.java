import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A class that tests files and outputs to console.
 * 
 * @author Digno JR Teogalbo
 * @version CS 221 Section 002 Spring 2022
 */
public class FormatChecker {
    private ArrayList<String> filenames;
    private int height;
    private int width;

    /**
     * FormatChecker constructor that instantiates the filenames ArrayList<String>.
     * 
     * @param filenames A string array containing filenames (including extension)
     */
    public FormatChecker(String[] filenames) {
        // creates an empty array list for filenames
        this.filenames = new ArrayList<>();

        // adds all the strings from given array to the instance filenames
        for (String filename : filenames) {
            this.filenames.add(filename);
        }
    }

    /**
     * Runs the tests for the provided test files and outputs file validity and errors to console.
     */
    public void testFiles() {
        for (String filename : filenames) {
            // access file
            File file = new File(filename);

            // print file name
            System.out.println(file.getName());

            // run tests for file
            runTests(file);

            // print empty line
            System.out.println();
        }
    }

    private void runTests(File file) {
        // create scanner to use for the tests
        try (Scanner fileScanner = new Scanner(file)) {
            // test the first line of file
            TestFirstLine(fileScanner);

            // test the grid contents of file
            TestGrid(fileScanner);

            // when both tests pass print valid
            // otherwise tests throw exceptions
            System.out.println("VALID");
        } catch (Exception err) {
            // print exceptions
            System.out.println(err.toString());

            // when exception is caught, test is failed
            // print invalid on failed test
            System.out.println("INVALID");
        } 
    }

    private void TestFirstLine(Scanner fileScanner) throws RuntimeException {
        // check if first line exists
        if (fileScanner.hasNextLine()) {
            String firstLine = fileScanner.nextLine();

            // read content from first line using scanner
            try (Scanner lineScanner = new Scanner(firstLine)) {
                try {
                    // expected first value is height
                    height = lineScanner.nextInt();
                } catch (InputMismatchException err) {
                    // height is not an int value
                    throw new InputMismatchException("Height is a non integer value!");
                } catch (NoSuchElementException err) {
                    // no height value given
                    throw new NoSuchElementException("Missing height of grid!");
                }

                try {
                    // next expected value is width
                    width = lineScanner.nextInt();
                } catch (InputMismatchException err) {
                    // width is not an int value
                    throw new InputMismatchException("Width is a non integer value!");
                } catch (NoSuchElementException err) {
                    // no width value given
                    throw new NoSuchElementException("Missing width of grid!");
                }

                if (lineScanner.hasNextInt()) {
                    // extraneous content exists in first line
                    throw new IllegalArgumentException("Extra content in first line!");
                }
            }
        } else {
            // first line does not exist
            throw new NoSuchElementException("Missing first line!");
        }
    }

    private void TestGrid(Scanner fileScanner) throws RuntimeException {
        // runs after testing first line
        // file scanner position is at second line
        // expecting start of grid

        for (int i = 0; i < height; i++) {
            // check if row exists
            if (fileScanner.hasNextLine()) {
                String nextLine = fileScanner.nextLine();

                try (Scanner lineScanner = new Scanner(nextLine)) {
                    // test for every expected element in row
                    for (int j = 0; j < width; j++) {
                        try {
                            lineScanner.nextDouble();
                        } catch (InputMismatchException err) {
                            // element is a non double value
                            throw new InputMismatchException("Grid content contains a non double value!");
                        } catch (NoSuchElementException err) {
                            // expected element does not exist
                            throw new NoSuchElementException("Number of grid columns is less than grid width!");
                        }
                    }

                    // extra element (column) exists
                    if (lineScanner.hasNext()) {
                        throw new IllegalArgumentException("Number of grid columns is greater than grid width!");
                    }
                }
            } else {
                // expected row does not exist
                throw new IllegalArgumentException("Number of grid rows is less than grid height!");
            }
        }

        // after checking all expected rows
        while (fileScanner.hasNextLine()) {
            String afterGrid = fileScanner.nextLine();
            
            if (!afterGrid.isBlank()) {
                // extra rows exist
                throw new IllegalArgumentException("Number of grid rows is greater than grid height!");
            }
        }
    }

    public static void main(String[] args) {
        // if (args.length == 0) {
        //     System.out.println("Missing parameters: No file name!");
        //     return;
        // };

        // FormatChecker formatChecker = new FormatChecker(args);
        // formatChecker.testFiles();
        String[] test = new String[] {
            "invalid1.dat", "invalid2.dat", "invalid3.dat",
            "invalid4.dat", "invalid5.dat", "invalid6.dat", 
            "invalid7.dat", "invalid8.dat", "invalid9.dat",
            "invalid10.dat", "invalid11.dat", "invalid12.dat",
            "valid1.dat", "valid2.dat", "valid3.dat"
        };

        FormatChecker formatChecker = new FormatChecker(test);
        formatChecker.testFiles();
    }
}