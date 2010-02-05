package org.shinkirou.minesweeper;

import org.shinkirou.util.SetOperations;
import java.util.HashSet;

/**
 * The class for the minesweeper solving algorithm.
 * @author SHiNKiROU
 */
public class MinesweeperSolver {
	private Board board;
	private HashSet<Constraint> sets;
	private int count;

	/**
	 * Constructs an instance of <code>MinesweeperSolver</code>
	 * @param board The board to be solved.
	 */
	public MinesweeperSolver(Board board) {
		this.board = board;
		this.sets = new HashSet<Constraint>();
		this.count = 0;
	}

	/**
	 * Perform an {@link MinesweeperSolver#iteration(boolean)} without
	 * any guesswork.
	 */
	public void iteration() {
		iteration(false);
	}

	/**
	 * Perform an iteration of the solving process.
	 * @param guess True to perform guesswork.
	 */
	public void iteration(boolean guess) {
		count ++;
		// make sure the board is not solved or failed
		if (board.isSolved() || board.isFailed()) {
			throw new IllegalStateException("The board is already solved or failed.");
		}
		sets.clear();
		// 1: find all constraints
		for (int y = 0, w = board.getWidth(), h = board.getHeight(); y < h; y ++) {
			for (int x = 0; x < w; x ++) {
				byte n = board.getInformation(x, y);
				if (n > 0 && n < 9) {
					Constraint e = new Constraint();
					// look around
					// top-left
					if (y - 1 > -1 && x - 1 > -1) {
						// get the known information of the square
						byte i = board.getInformation(x - 1, y - 1);
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new Coordinate(x - 1, y - 1));
						}
					}
					// top
					if (y - 1 > -1) {
						// get the known information of the square
						byte i = board.getInformation(x, y - 1);
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new Coordinate(x, y - 1));
						}
					}
					// top-right
					if (y - 1 > -1 && x + 1 < board.getWidth()) {
						// get the known information of the square
						byte i = board.getInformation(x + 1, y - 1);
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new Coordinate(x + 1, y - 1));
						}
					}
					// mid-left
					if (x - 1 > -1) {
						// get the known information of the square
						byte i = board.getInformation(x - 1, y);
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new Coordinate(x - 1, y));
						}
					}
					// mid-right
					if (x + 1 < board.getWidth()) {
						// get the known information of the square
						byte i = board.getInformation(x + 1, y);
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new Coordinate(x + 1, y));
						}
					}
					// lower-left
					if (y + 1 < board.getHeight() && x - 1 > -1) {
						// get the known information of the square
						byte i = board.getInformation(x - 1, y + 1);
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new Coordinate(x - 1, y + 1));
						}
					}
					// lower
					if (y + 1 < board.getHeight()) {
						// get the known information of the square
						byte i = board.getInformation(x, y + 1);
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new Coordinate(x, y + 1));
						}
					}
					// lower-right
					if (y + 1 < board.getHeight() && x + 1 < board.getWidth()) {
						// get the known information of the square
						byte i = board.getInformation(x + 1, y + 1);
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new Coordinate(x + 1, y + 1));
						}
					}
					e.setMines(n);
					// add only if the set is not empty
					if (e.size() > 0) {
						sets.add(e);
					}
				}
			}
		}

		// 2: keep using the subset rule until exhausted
		boolean changed = true;
		while (changed) {
			changed = false;
			for (Constraint e1 : sets) {
				for (Constraint e2 : sets) {
					if (!SetOperations.identity(e1, e2)) {
						// if e1 proper subset e2, c = e2 diff e1,
						//    mines of c = mines of e2 - mines of e1
						if (SetOperations.properSubset(e1, e2)) {
							Constraint c = (Constraint) SetOperations.difference(e1, e2);
							c.setMines((byte) (e2.getMines() - e1.getMines()));
							if (c.size() > 0) {
								sets.add(c);
							}
							changed = true;
						}
					}
				}
			}
		}

		// 3: mark or probe the squares
		boolean probed = false;
		for (Constraint e : sets) {
			byte m = e.getMines();

			if (m == 0) {
				// if there are 0 mines, all the squares are safe
				for (Coordinate c : e) {
					board.probe(c.x, c.y);
				}
				probed = true;
			} else if (m == e.size()) {
				// if the no. of mines is same as no. of squares, all the
				// squares are mines
				for (Coordinate c : e) {
					board.mark(c.x, c.y);
				}
				probed = true;
			}
		}

		// 4: perform some guesswork if constraint solving did not work
		//   (don't guess if the minesweeper is solved)
		if (guess && !(board.isSolved() || board.isFailed())) {
			if (sets.size() == 0) {
				board.probe((int) (Math.random() * board.getWidth()), (int) (Math.random() * board.getHeight()));
			} else if (!probed) {
				float bestProbability = 1;
				Constraint best = null;
				for (Constraint e : sets) {
					if (e.probability() < bestProbability) {
						best = e;
					}
				}
				Coordinate[] coords = best.toArray(new Coordinate[best.size()]);
				Coordinate coord = coords[(int) (Math.random() * coords.length)];
				board.probe(coord.x, coord.y);
			}
		}
	}

	/**
	 * Check if the board was failed to be solved.
	 * @return True of the board was failed to be solved.
	 */
	public boolean isFailed() {
		return board.isFailed();
	}

	/**
	 * Check if the board is solved.
	 * @return True if the board is solved.
	 */
	public boolean isSolved() {
		return board.isSolved();
	}

	/**
	 * Gets the sets of the constraints.
	 * @return The constraints.
	 */
	public HashSet<Constraint> getSets() {
		return this.sets;
	}

	/**
	 * Gets the iteration count.
	 * @return The iteration count.
	 */
	public int getCount() {
		return count;
	}
}
