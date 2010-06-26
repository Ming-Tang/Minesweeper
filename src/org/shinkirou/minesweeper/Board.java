package org.shinkirou.minesweeper;

import java.util.Random;
import java.util.Stack;

/**
 * A class for minesweeper board.
 * @author SHiNKiROU
 */
public class Board {
	private int width;
	private int height;
	private int mineCount;

	private byte[][] values;
	private boolean[][] probes;
	private boolean[][] marks;

	/**
	 * Constructs and initializes a new minesweeper board.
	 * @param width The width of the initial board, as an array size unit.
	 * @param height The height of the initial board, as an array size unit.
	 * @param mineCount Number of mines.
	 */
	public Board(int width, int height, int mineCount) {
		initialize(width, height, mineCount);
	}

	/**
	 * Initializes the board.
	 * @param w The width of the initial board, as an array size unit.
	 * @param h The height of the initial board, as an array size unit.
	 * @param mines Number of mines.
	 * @throws IllegalArgumentException When one or more parameters are &lt; 1.
	 */
	private void initialize(int w, int h, int mines) throws IllegalArgumentException {
		if (w < 1 || h < 1 || mines < 1) {
			// cannot initialize arrays with size if width < 1,
			// height < 1 or mineCount < 1
			throw new IllegalArgumentException(
				"Invalid board configuration: width="
				+ width + ", height="
				+ height + ", mineCount = "
				+ mines + ". They must be greater than 0."
			);
		} else {
			// set state variables
			width = w;
			height = h;
			mineCount = mines;

			// initialize board
			values = new byte[height][width];
			probes = new boolean[height][width];
			marks = new boolean[height][width];

			label: {
				Random r = new Random();
				int m = 0;
				int n = mines;
				// place some random mines
				while (m < n) {
					int x = r.nextInt(width);
					int y = r.nextInt(height);
					// make sure it is not an existing mine
					if (values[y][x] != 9) {
						// place it
						values[y][x] = 9;
						m ++;
					}
				}
			}

			// count the number hints
			for (int y = 0; y < height; y ++) {
				for (int x = 0; x < width; x ++) {
					if (values[y][x] != 9) {
						byte n = 0;
						// top-left
						if (y - 1 > -1 && x - 1 > -1 && values[y - 1][x - 1] == 9) {
							n ++;
						}
						// top
						if (y - 1 > -1 && values[y - 1][x] == 9) {
							n ++;
						}
						// top-right
						if (y - 1 > -1 && x + 1 < width && values[y - 1][x + 1] == 9) {
							n ++;
						}
						// mid-left
						if (x - 1 > -1 && values[y][x - 1] == 9) {
							n ++;
						}
						// mid-right
						if (x + 1 < width && values[y][x + 1] == 9) {
							n ++;
						}
						// lower-left
						if (y + 1 < height && x - 1 > -1 && values[y + 1][x - 1] == 9) {
							n ++;
						}
						// lower
						if (y + 1 < height && values[y + 1][x] == 9) {
							n ++;
						}
						// lower-right
						if (y + 1 < height && x + 1 < width && values[y + 1][x + 1] == 9) {
							n ++;
						}
						// set number hint
						values[y][x] = n;
					}
				}
			}
		}
	}

	/**
	 * Probes a square. Reveals the value under it.
	 * May set the board state to solved or failed depends on what is under
	 * it or if all mines are clicked.
	 * @param x The X coordinate of the square, as an array index.
	 * @param y The Y coordinate of the square, as an array index.
	 */
	public void probe(int x, int y) {
		// cannot probe marked squares
		if (marks[y][x]) {
			return;
		}

		// probe it
		probes[y][x] = true;

		// if probed a mine: fail
		if (values[y][x] == 9) {
			return;
		} else {
			if (values[y][x] == 0) {
				// auto-probe squares beside it
				// the stack that tracks squares to be looked
				Stack<Coordinate> stack = new Stack<Coordinate>();
				// push this coordinate
				stack.push(new Coordinate(x, y));
				// this is a stack-based flood-fill algorithm
				while (!stack.empty()) {
					// get coordinates
					Coordinate i = stack.pop();
					int sx = i.x;
					int sy = i.y;
					probes[sy][sx] = true;

					// look around
					// top-left
					if (sy - 1 > -1 && sx - 1 > -1 && !probes[sy - 1][sx - 1]) {
						if (values[sy - 1][sx - 1] == 0) {
							// if empty: remember to look ahead for it
							stack.push(new Coordinate(sx - 1, sy - 1));
						} else if (!marks[sy - 1][sx - 1]) {
							// if not marked: probe it
							probes[sy - 1][sx - 1] = true;
						}
					}
					// top
					if (sy - 1 > -1 && !probes[sy - 1][sx]) {
						if (values[sy - 1][sx] == 0) {
							// if empty: remember to look ahead for it
							stack.push(new Coordinate(sx, sy - 1));
						} else if (!marks[sy][sx]) {
							// if not marked: probe it
							probes[sy - 1][sx] = true;
						}
					}
					// top-right
					if (sy - 1 > -1 && sx + 1 < width && !probes[sy - 1][sx + 1]) {
						if (values[sy - 1][sx + 1] == 0) {
							// if empty: remember to look ahead for it
							stack.push(new Coordinate(sx + 1, sy - 1));
						} else if (!marks[sy - 1][sx + 1]) {
							// if not marked: probe it
							probes[sy - 1][sx + 1] = true;
						}
					}
					// mid-left
					if (sx - 1 > -1 && !probes[sy][sx - 1]) {
						if (values[sy][sx - 1] == 0) {
							// if empty: remember to look ahead for it
							stack.push(new Coordinate(sx - 1, sy));
						} else if (!marks[sy][sx - 1]) {
							// if not marked: probe it
							probes[sy][sx - 1] = true;
						}
					}
					// mid-right
					if (sx + 1 < width && !probes[sy][sx + 1]) {
						if (values[sy][sx + 1] == 0) {
							// if empty: remember to look ahead for it
							stack.push(new Coordinate(sx + 1, sy));
						} else if (!marks[sy][sx + 1]) {
							// if not marked: probe it
							probes[sy][sx + 1] = true;
						}
					}
					// lower-left
					if (sy + 1 < height && sx - 1 > -1 && !probes[sy + 1][sx - 1]) {
						if (values[sy + 1][sx - 1] == 0) {
							// if empty: remember to look ahead for it
							stack.push(new Coordinate(sx - 1, sy + 1));
						} else if (!marks[sy + 1][sx - 1]) {
							// if not marked: probe it
							probes[sy + 1][sx - 1] = true;
						}
					}
					// lower
					if (sy + 1 < height && !probes[sy + 1][sx]) {
						if (values[sy + 1][sx] == 0) {
							// if empty: remember to look ahead for it
							stack.push(new Coordinate(sx, sy + 1));
						} else if (!marks[sy + 1][sx]) {
							// if not marked: probe it
							probes[sy + 1][sx] = true;
						}
					}
					// lower-right
					if (sy + 1 < height && sx + 1 < width && !probes[sy + 1][sx + 1]) {
						if (values[sy + 1][sx + 1] == 0) {
							// if empty: remember to look ahead for it
							stack.push(new Coordinate(sx + 1, sy + 1));
						} else if (!marks[sy + 1][sx + 1]) {
							// if not marked: probe it
							probes[sy + 1][sx + 1] = true;
						}
					}
				}
			}
		}
	}

	/**
	 * Marks a square mine.
	 * @param x The X coordinate of the square, as an array index.
	 * @param y The Y coordinate of the square, as an array index.
	 */
	public void mark(int x, int y) {
		// cannot mark probed squares
		if (!probes[y][x]) {
			// mark it
			marks[y][x] = true;
		}
	}

	/**
	 * Unmarks a flag.
	 * @param x The X coordinate of the square, as an array index.
	 * @param y The Y coordinate of the square, as an array index.
	 */
	public void unmark(int x, int y) {
		marks[y][x] = false;
	}
	/**
	 * Gets the value of a square.
	 * @param x The X coordinate of the square, as an array index.
	 * @param y The Y coordinate of the square, as an array index.
	 * @return A number represents the state of the square. The values are:
	 * <ul>
	 *   <li><b>0:</b> Unknown.</li>
	 *   <li><b>1:</b> Not a mine.</li>
	 *   <li><b>2:</b> Marked as a mine.</li>
	 * </ul>
	 */
	public byte getValue(int x, int y) {
		if (marks[y][x]) {
			// square is marked
			return 2;
		} else if (probes[y][x]) {
			// square is probed
			return 1;
		} else {
			// square is unknown
			return 0;
		}
	}

	/**
	 * Gets the number value of a square, for displaying and solving.
	 * @param x The X coordinate of the square, as an array index.
	 * @param y The Y coordinate of the square, as an array index.
	 * @return A number represents the value of the square. The values are:
	 * <ul>
	 *   <li><b>0 to 8:</b> The number of mines around it.</li>
	 *   <li><b>9:</b> Marked as a mine.</li>
	 *   <li><b>10:</b> Unmarked and unprobed.</li>
	 * </ul>
	 */
	public byte getInformation(int x, int y) {
		if (marks[y][x]) {
			// square is marked
			return 9;
		} else if (probes[y][x]) {
			return values[y][x];
		} else {
			// square is unknown
			return 10;
		}
	}

	/**
	 * Returns a string representation of the board state.
	 * @return The string of the board state.
	 */
	public String display() {
		String buf = "+|";

		for (int i = 0; i < width; i ++) {
			buf += i % 10;
		}

		buf += "\n-+";

		for (int i = 0; i < width; i ++) {
			buf += "-";
		}

		buf += "\n";

		for (int y = 0; y < height; y ++) {
			buf += y % 10 + "|";
			for (int x = 0; x < width; x ++) {
				if (marks[y][x]) {
					buf += "#";
				} else if (probes[y][x]) {
					buf += values[y][x] == 9 ? "*" : values[y][x];
				} else {
					buf += " ";
				}
			}
			buf += "\n";
		}

		return buf;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getMineCount() {
		return mineCount;
	}

	public byte[][] getValues() {
		return values;
	}

	public boolean[][] getProbes() {
		return probes;
	}

	public boolean[][] getMarks() {
		return marks;
	}

	public boolean isSolved() {
		int n = 0;
		// count the marked squares
		for (int y = 0; y < height; y ++) {
			for (int x = 0; x < width; x ++) {
				if (marks[y][x] && values[y][x] == 9) {
					// a marked square and a mine under it
					n ++;
				} else if (!probes[y][x] && !marks[y][x]) {
					// if a square is unprobed and unmarked, not solved
					return false;
				}
			}
		}
		// set solved to true if the marked squares equals the number of mines
		return n == mineCount;
	}

	public boolean isFailed() {
		// loop through all squares
		for (int y = 0; y < height; y ++) {
			for (int x = 0; x < width; x ++) {
				// fail if probed a mine
				if (probes[y][x] && values[y][x] == 9) {
					return true;
				}
			}
		}
		return false;
	}
}
