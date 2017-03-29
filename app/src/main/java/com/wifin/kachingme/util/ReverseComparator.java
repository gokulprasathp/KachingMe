package com.wifin.kachingme.util;

import java.io.Serializable;
import java.util.Comparator;

class ReverseComparator implements Comparator, Serializable {

	private final Comparator delegate;

	/**
	 * Construct an instance with the sepecified delegate {@link Comparator}.
	 * 
	 * @param delegate
	 *            The comparator to delegate to
	 */
	public ReverseComparator(Comparator delegate) {
		if (delegate == null) {
			throw new IllegalArgumentException("Delegate comparator is missing");
		}
		this.delegate = delegate;
	}

	/**
	 * Compare using the delegate Comparator, but reversing the result.
	 * 
	 * @param obj1
	 *            The first object to compare
	 * @param obj2
	 *            The second object to compare
	 * @return the result from the delegate
	 *         {@link Comparator#compare(Object, Object)} reversing the value
	 *         (i.e. positive becomes negative and vice versa)
	 */
	public int compare(Object obj1, Object obj2) {
		return delegate.compare(obj2, obj1); // parameters switched round
	}
}