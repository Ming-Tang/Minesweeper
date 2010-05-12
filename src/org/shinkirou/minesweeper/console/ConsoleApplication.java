package org.shinkirou.minesweeper.console;

import java.util.Scanner;
import org.shinkirou.minesweeper.Board;
import org.shinkirou.minesweeper.MinesweeperSolver;

/**
 * The interactive command line Minesweeper Solver.
 * @author SHiNKiROU
 */
public class ConsoleApplication {

	private ConsoleApplication() {
	}

	public static void main(String[] args) {
		int w = 9, h = 9, m = 10;
		Board b = new Board(w, h, m);
		MinesweeperSolver s = new MinesweeperSolver(b);
		switch (args.length) {
			case 0:
				// Beginner level - leave as-is
				break;
			case 3:
				// Set parameters
				try {
					w = Integer.parseInt(args[0]);
					h = Integer.parseInt(args[1]);
					m = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					System.err.println("Error while parsing the numbers.");
					System.exit(1);
				}
				break;
			default:
				System.err.println("Error: invalid arguments.");
				System.exit(1);
				break;
		}

		s = new MinesweeperSolver(b);
		Scanner input = new Scanner(System.in);
		while ( ! (b.isSolved() || b.isFailed())) {
			System.out.println(b.display());
			System.err.print("> ");

			String line = input.nextLine();
			String[] vals = line.split("\\s+");

			int x = 0, y = 0;
			boolean mark = false;
			try {
				if (vals[0].equals("m")) {
					// mark square
					x = Integer.parseInt(vals[1]);
					y = Integer.parseInt(vals[2]);
					b.mark(x, y);
				} else if (vals[0].equals("c")) {
					// cheat
					s.iteration();
				} else if (vals[0].equals("r")) {
					// random
					x = (int) (Math.random() * b.getWidth());
					y = (int) (Math.random() * b.getHeight());
					System.err.println("Probing " + x + ", " + y);
					b.probe(x, y);
				} else if (vals[0].equals("x")) {
					System.exit(0);
				} else {
					if (vals.length < 2) {
						System.err.println("Expecting two numbers.");
						continue;
					}
					x = Integer.parseInt(vals[0]);
					y = Integer.parseInt(vals[1]);
					b.probe(x, y);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("Coordinate out of bounds: " + x + ", " + y + ".");
			} catch (NumberFormatException e) {
				System.err.println("Invalid numbers.");
			} catch (Exception e) {
				continue;
			}
		}
		if (b.isSolved()) {
			System.out.println(b.display());
			System.err.println("Solved!");
		}
		if (b.isFailed()) {
			System.out.println(b.display());
			System.err.println("Game Over");
		}
	}
}
