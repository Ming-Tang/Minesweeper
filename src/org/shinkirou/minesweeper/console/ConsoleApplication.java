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
		int w = 30, h = 15, m = 99;
		for (int i = 0; i < args.length; i ++) {
			if (i == 0) {
				w = Integer.parseInt(args[0]);
			} else if (i == 1) {
				h = Integer.parseInt(args[1]);
			} else if (i == 2) {
				m = Integer.parseInt(args[2]);
			}
		}

		Board b = new Board(w, h, m);
		MinesweeperSolver s = new MinesweeperSolver(b);
		Scanner input = new Scanner(System.in);
		while (!(b.isSolved() || b.isFailed())) {
			System.out.println(b.display());
			System.out.print("> ");

			String line = input.nextLine();
			String[] vals = line.split("\\s+");

			int x, y;
			boolean mark = false;
			try {
				if (vals[0].equals("m")) {
					mark = true;
					x = Integer.parseInt(vals[1]);
					y = Integer.parseInt(vals[2]);
				} else if (vals[0].equals("c")) {
					s.iteration();
					continue;
				} else {
					x = Integer.parseInt(vals[0]);
					y = Integer.parseInt(vals[1]);
				}
			} catch (Exception e) {
				continue;
			}

			try {
				if (mark) {
					b.mark(x, y);
				} else {
					b.probe(x, y);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Coordinate does not exist.");
			} catch (RuntimeException e) {
			}
		}
		if (b.isSolved()) {
			System.out.println("solved");
		}
		if (b.isFailed()) {
			System.out.println("failed");
		}
	}
}
