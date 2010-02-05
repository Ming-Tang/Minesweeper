package org.shinkirou.util;

import java.util.Set;
import java.util.TreeSet;

/**
 * Set operations for {@link java.util.Set}.
 * @author SHiNKiROU
 */
public class SetOperations {

	private SetOperations() {
	}

	public static <T> boolean identity(Set<T> a, Set<T> b) {
		return a.size() == b.size() && a.containsAll(b);
	}

	public static <T> Set<T> union(Set<T> a, Set<T> b) {
		Set<T> tmp = new TreeSet<T>(a);
		tmp.addAll(b);
		return tmp;
	}

	public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
		Set<T> c = new TreeSet<T>();
		for (T x : a) {
			if (b.contains(x)) {
				c.add(x);
			}
		}
		return c;
	}

	public static <T> Set<T> difference(Set<T> a, Set<T> b) {
		Set<T> tmp = new TreeSet<T>(a);
		tmp.removeAll(b);
		return tmp;
	}

	public static <T> Set<T> symDifference(Set<T> a, Set<T> b) {
		Set<T> h;
		Set<T> i;

		h = union(a, b);
		i = intersection(a, b);
		return difference(h, i);
	}

	public static <T> boolean subset(Set<T> a, Set<T> b) {
		return b.containsAll(a);
	}

	public static <T> boolean superset(Set<T> a, Set<T> b) {
		return a.containsAll(b);
	}

	public static <T> boolean properSubset(Set<T> a, Set<T> b) {
		return a.size() != b.size() && subset(a, b);
	}

	public static <T> boolean properSuperset(Set<T> a, Set<T> b) {
		return a.size() != b.size() && superset(a, b);
	}
}
