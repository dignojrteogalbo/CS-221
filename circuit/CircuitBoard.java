import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail, dteogalbo
 * @version CS221-002 Spring 2022
 */
public class CircuitBoard {
	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;

	//constants you may find useful
	private final int ROWS; //initialized in constructor
	private final int COLS; //initialized in constructor
	private final char OPEN = 'O'; //capital 'o'
	private final char CLOSED = 'X';
	private final char TRACE = 'T';
	private final char START = '1';
	private final char END = '2';
	private final String ALLOWED_CHARS = "OXT12";

	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot read the file
	 * @throws InvalidFileFormatException for any other format or content issue that prevents reading a valid input file
	 */
	public CircuitBoard(String filename) throws FileNotFoundException {
		Scanner fileScan = new Scanner(new File(filename));

		try {
			String firstLine = fileScan.nextLine();
			int[] dimensions = testFirstLine(firstLine);
			ROWS = dimensions[0];
			COLS = dimensions[1];
			board = new char[ROWS][COLS];
		} catch (NoSuchElementException err) {
			throw new InvalidFileFormatException("Missing first line!");
		}

		int rowCount = 0;

		while (fileScan.hasNextLine()) {
			rowCount++;
			String rowString = fileScan.nextLine();

			if (rowCount <= ROWS) {
				char[] rowItems = testRow(rowString);
				testColumns(rowItems, rowCount - 1);
			}
		}

		if (rowCount != ROWS) {
			String errorString = String.format("Expected %d rows, found %d rows!", ROWS, rowCount);
			throw new InvalidFileFormatException(errorString);
		}

		if (startingPoint == null) {
			throw new InvalidFileFormatException("Missing start point!");
		}

		if (endingPoint == null) {
			throw new InvalidFileFormatException("Missing end point!");
		}

		fileScan.close();
	}

	/**
	 * Validates the first line of the file. Returns an int[] with the parsed dimensions of the circuit board grid.
	 * 
	 * @param firstLine String the first line of the file
	 * @return int[] dimensions of the board with index 0 being the number of rows and index 1 being the number of columns
	 * @throws InvalidFileFormatException 
	 * 			if the number of rows is a non-integer value, 
	 * 			if the number of columns is a non-integer value, 
	 * 			if the number of rows is missing, 
	 * 			if the number of columns is missing
	 */
	private int[] testFirstLine(String firstLine) throws InvalidFileFormatException {
		int[] dimensions = new int[2];
		Scanner firstLineScanner = new Scanner(firstLine);

		try {
			dimensions[0] = firstLineScanner.nextInt();
		} catch (InputMismatchException err) {
			firstLineScanner.close();
			throw new InvalidFileFormatException("Non-integer value for row!");
		} catch (NoSuchElementException err) {
			firstLineScanner.close();
			throw new InvalidFileFormatException("Missing number of rows!");
		}

		try {
			dimensions[1] = firstLineScanner.nextInt();
		} catch (InputMismatchException err) {
			firstLineScanner.close();
			throw new InvalidFileFormatException("Non-integer value for column!");
		} catch (NoSuchElementException err) {
			firstLineScanner.close();
			throw new InvalidFileFormatException("Missing number of columns!");
		}

		firstLineScanner.close();
		return dimensions;
	}

	/**
	 * Validates a row of text in a file. Returns a char array with the valid characters parsed from the file.
	 * 
	 * @param row String of the current row
	 * @return char[] valid board items in the row
	 * @throws InvalidFileFormatException
	 * 			if the number of row items is greater than the number of columns
	 * 			if an item in the row has a length greater than 1,
	 * 			if an item in the row is not an allowed character
	 */
	private char[] testRow(String row) throws InvalidFileFormatException {
		char[] rowItems = new char[COLS];
		Scanner rowScanner = new Scanner(row);
		int index = 0;

		while (rowScanner.hasNext()) {
			String boardItem = rowScanner.next();
			char boardChar = boardItem.charAt(0);

			if (boardItem.length() > 1) {
				rowScanner.close();
				throw new InvalidFileFormatException("Board item has a length greater than 1!");
			}
			
			if (ALLOWED_CHARS.indexOf(boardChar) == -1) {
				rowScanner.close();
				throw new InvalidFileFormatException("Board item is not an allowed character!");
			}

			if (index >= COLS) {
				rowScanner.close();
				String errorString = String.format("Expected %d columns, found %d columns!", COLS, index + 1);
				throw new InvalidFileFormatException(errorString);
			}

			rowItems[index] = boardChar;
			index++;
		}

		if (index != COLS) {
			rowScanner.close();
			String errorString = String.format("Expected %d columns, found %d columns!", COLS, index);
			throw new InvalidFileFormatException(errorString);
		}

		rowScanner.close();
		return rowItems;
	}

	/**
	 * Validates every row item in a row. Sets the starting point and ending point after parsing every row item.
	 * 
	 * @param rowItems char[] valid row items with (length == number of columns)
	 * @param rowIndex int index of the row
	 * @throws InvalidFileFormatException
	 * 			if there are empty chars in rowItems,
	 * 			if there are more than 1 start point,
	 * 			if there are more than 1 end point
	 */
	private void testColumns(char[] rowItems, int rowIndex) throws InvalidFileFormatException {
		for (int i = 0; i < COLS; i++) {
			if (rowItems[i] == 0) {
				String errorString = String.format("Expected %d columns, found %d columns!", COLS, i + 1);
				throw new InvalidFileFormatException(errorString);
			}

			if (rowItems[i] == TRACE) {
				throw new InvalidFileFormatException("Found existing trace in board!");
			}

			if (rowItems[i] == START) {
				if (startingPoint == null) {
					startingPoint = new Point(rowIndex, i);
				} else {
					throw new InvalidFileFormatException("Found more than 1 start point!");
				}
			}

			if (rowItems[i] == END) {
				if (endingPoint == null) {
					endingPoint = new Point(rowIndex, i);
				} else {
					throw new InvalidFileFormatException("Found more than 1 end point!");
				}
			}

			board[rowIndex][i] = rowItems[i];
		}
	}
	
	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/** utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}
	
	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}
	
	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}
	
	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}
	
	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}
	
	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}
	
	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
}// class CircuitBoard
