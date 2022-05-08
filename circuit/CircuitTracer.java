import java.awt.Point;
import java.util.ArrayList;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail, dteogalbo
 * @version CS221-002 Spring 2022
 */
public class CircuitTracer {
	private CircuitBoard board;
	private ArrayList<TraceState> bestPaths;

	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		new CircuitTracer(args); //create this with args
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private void printUsage() {
		System.out.println("Usage: java CircuitTracer [-s | -q] [-c | -g] filename");
		System.out.println("======================================================");
		System.out.println("Generate traces for circuit boards.");
		System.out.println("======================================================");
		System.out.println("-s\n\tUse stack for storage");
		System.out.println("-q\n\tUse queue for storage");
		System.out.println("-c\n\tDisplay output to console");
		System.out.println("-g\n\tDisplay output to GUI");
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {
		if (!isValidArgs(args)) return;

		boolean stack = args[0].equals("-s");
		boolean console = args[1].equals("-c");

		try {
			board = new CircuitBoard(args[2]);
		} catch (Exception err) {
			if (console) {
				System.out.println(err);
			} else {
				new CircuitTracerGUI(err);
			}
			return;
		}

		Storage<TraceState> stateStore = (stack) ? Storage.getStackInstance() : Storage.getQueueInstance();
		bestPaths = new ArrayList<>();
		Point start = board.getStartingPoint();
		int x = start.x;
		int y = start.y;

		if (board.isOpen(x + 1, y)) {
			stateStore.store(new TraceState(board, x + 1, y));
		}

		if (board.isOpen(x - 1, y)) {
			stateStore.store(new TraceState(board, x - 1, y));
		}

		if (board.isOpen(x, y + 1)) {
			stateStore.store(new TraceState(board, x, y + 1));
		}

		if (board.isOpen(x, y - 1)) {
			stateStore.store(new TraceState(board, x, y - 1));
		}

		while (!stateStore.isEmpty()) {
			TraceState next = stateStore.retrieve();

			if (next.isComplete()) {
				if (bestPaths.isEmpty()) {
					bestPaths.add(next);
				} else if (next.pathLength() == bestPaths.get(0).pathLength()) {
					bestPaths.add(next);
				} else if (next.pathLength() < bestPaths.get(0).pathLength()) {
					bestPaths.clear();
					bestPaths.add(next);
				}
			} else {
				x = next.getRow();
				y = next.getCol();

				if (next.isOpen(x + 1, y)) {
					stateStore.store(new TraceState(next, x + 1, y));
				}

				if (next.isOpen(x - 1, y)) {
					stateStore.store(new TraceState(next, x - 1, y));
				}

				if (next.isOpen(x, y + 1)) {
					stateStore.store(new TraceState(next, x, y + 1));
				}

				if (next.isOpen(x, y - 1)) {
					stateStore.store(new TraceState(next, x, y - 1));
				}
			}
		}

		if (console) {
			bestPaths.forEach(path -> System.out.println(path.getBoard()));
		} else {
			new CircuitTracerGUI(this);
		}
	}

	/**
	 * Validates the passed in arguments and returns true if the arguments are valid.
	 * 
	 * @param args String[] of args to validate
	 * @return boolean true if the arguments passed are valid
	 */
	private boolean isValidArgs(String[] args) {
		if (args.length != 3) {
			printUsage();
			return false;
		}

		if (!args[0].equals("-s") && !args[0].equals("-q")) {
			printUsage();
			return false;
		}

		if (!args[1].equals("-c") && !args[1].equals("-g")) {
			printUsage();
			return false;
		}

		return true;
	}

	/**
	 * Returns the CircuitBoard being processed by CircuitTracer.
	 * 
	 * @return CircuitBoard being processed by CircuitTracer
	 */
	public CircuitBoard getBoard() {
		return board;
	}

	/**
	 * Sets the CircuitBoard to process by the CircuitTracer.
	 * 
	 * @param board CircuitBoard to process
	 */
	public void setBoard(CircuitBoard board) {
		this.board = board;
	}

	/**
	 * Returns a list of the best paths.
	 * 
	 * @return ArrayList<TraceState> of the best paths
	 */
	public ArrayList<TraceState> getBestPaths() {
		return bestPaths;
	}

	/**
	 * Sets the list of best paths.
	 * 
	 * @param bestPaths ArrayList<TraceState> best paths to set
	 */
	public void setBestPaths(ArrayList<TraceState> bestPaths) {
		this.bestPaths = bestPaths;
	}
} // class CircuitTracer
