package org.shinkirou.minesweeper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a set of a piece of constraint information that contains
 * the set of coordinates and number of mines.
 * @author SHiNKiROU
 */
public class Constraint implements Set<Coordinate> {
	private HashSet<Coordinate> set;
	private byte mines;

	public Constraint() {
		this((byte) 1);
	}

	public Constraint(byte mines) {
		this.mines = mines;
		set = new HashSet<Coordinate>();
	}

	public Constraint(byte mines, Collection<Coordinate> elems) {
		this.mines = mines;
		set = new HashSet<Coordinate>(elems);
	}

	public Constraint(byte mines, int initialCap) {
		this.mines = mines;
		set = new HashSet<Coordinate>(initialCap);
	}

	public Constraint(byte mines, int initialCap, float loadFactor) {
		this.mines = mines;
		set = new HashSet<Coordinate>(initialCap, loadFactor);
	}

	/**
	 * Gets the number of mines.
	 * @return The number of mines.
	 */
	public byte getMines() {
		return mines;
	}

	/**
	 * Sets the number of mines.
	 * @param mines The number of mines.
	 */
	public void setMines(byte mines) {
		this.mines = mines;
	}

	public Set<Coordinate> getSet() {
		return set;
	}

	public int size() {
		return set.size();
	}

	public boolean isEmpty() {
		return set.isEmpty();
	}

	public boolean contains(Object o) {
		return set.contains((Coordinate) o);
	}

	public Iterator<Coordinate> iterator() {
		return set.iterator();
	}

	public Object[] toArray() {
		return set.toArray();
	}

	public <T> T[] toArray(T[] ts) {
		return set.toArray(ts);
	}

	public boolean add(Coordinate e) {
		return set.add(e);
	}

	public boolean remove(Object o) {
		return set.remove((Coordinate) o);
	}

	public boolean containsAll(Collection<?> clctn) {
		return set.containsAll(clctn);
	}

	public boolean addAll(Collection<? extends Coordinate> clctn) {
		return set.addAll(clctn);
	}

	public boolean retainAll(Collection<?> clctn) {
		return set.retainAll(clctn);
	}

	public boolean removeAll(Collection<?> clctn) {
		return set.removeAll(clctn);
	}

	public void clear() {
		set.clear();
	}

	@Override
	public String toString() {
		return set.toString() + " (" + mines + " mines)";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Constraint)) {
			return false;
		}
		Constraint other = (Constraint) obj;
		return mines == other.mines && set.equals(other.set);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 59 * hash + (this.set != null ? this.set.hashCode() : 0);
		hash = 59 * hash + this.mines;
		return hash;
	}
}
