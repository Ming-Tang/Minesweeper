package org.shinkirou.minesweeper;

import org.shinkirou.util.SetOperations;
import java.util.*;

/**
 * The class for the minesweeper solving algorithm.
 * @author SHiNKiROU
 */
public class MinesweeperSolver {

	private Board board;
	private Set<Constraint> sets;
	private int count;
	private boolean inspected = false;

	/**
	 * Constructs an instance of <code>MinesweeperSolver</code>
	 * @param board The board to be solved.
	 */
	public MinesweeperSolver(Board board) {
		this.board = board;
		this.sets = new HashSet<Constraint>();
		this.count = 0;
	}

	public void inspect() {
		sets.clear();
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
					if ( ! e.isEmpty()) {
						sets.add(e);
					}
				}
			}
		}

		// 2: keep using the subset rule until exhausted
		boolean changed = true;
		while (changed) {
			changed = false;
			Constraint[] list = {};
			list = sets.toArray(list);
			// using Iterators causes ConcurrentModificationException
			for (Constraint e1 : list) {
				for (Constraint e2 : list) {
					if (!e1.equals(e2)) {
						// if e1 proper subset e2, c = e2 diff e1,
						//    mines of c = mines of e2 - mines of e1
						if (SetOperations.properSubset(e1.getSet(), e2.getSet())) {
							Set<Coordinate> set = SetOperations.difference(e2, e1);
							Constraint c = new Constraint(
								(byte) (e2.getMines() - e1.getMines()),
								set);
							changed = changed || sets.add(c);
						}
					}
				}
			}
		}
		inspected = true;
	}

	/**
	 * Perform an iteration of the solving process.
	 */
	public void iteration() {
		count ++;
		// make sure the board is not solved or failed
		if (board.isSolved() || board.isFailed()) {
			throw new IllegalStateException("The board is already solved or failed.");
		}
		// find all constraints
		if ( ! inspected) {
			inspect();
		}

		// mark or probe the squares
		for (Constraint e : sets) {
			byte m = e.getMines();

			if (m == 0) {
				// if there are 0 mines, all the squares are safe
				for (Coordinate c : e) {
					board.probe(c.x, c.y);
				}
			} else if (m == e.size()) {
				// if the no. of mines is same as no. of squares, all the
				// squares are mines
				for (Coordinate c : e) {
					board.mark(c.x, c.y);
				}
			}
		}
		inspected = false;
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
	public Set<Constraint> getSets() {
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
