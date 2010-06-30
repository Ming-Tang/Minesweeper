/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shinkirou.minesweeper;

/**
 * Represents a coordinate.
 * @author SHiNKiROU
 */
public class Coordinate implements Cloneable, Comparable<Coordinate> {

	public int x;
	public int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if ( ! (obj instanceof Coordinate)) {
			return false;
		}
		Coordinate other = (Coordinate) obj;
		return this.x == other.x && this.y == other.y;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + this.x;
		hash = 53 * hash + this.y;
		return hash;
	}

	@Override
	protected Object clone() {
		return new Coordinate(x, y);
	}

	public int compareTo(Coordinate that) {
		if (this.x == that.x) {
			return this.y - that.y;
		} else {
			return this.x - that.x;
		}
	}
}
