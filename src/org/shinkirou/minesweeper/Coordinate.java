/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.shinkirou.minesweeper;

/**
 *
 * @author SHiNKiROU
 */
public class Coordinate {
	public int x;
	public int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Coordinate x=" + this.x + ", y=" + this.y;
	}
}
