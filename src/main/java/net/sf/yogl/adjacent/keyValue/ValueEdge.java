 package net.sf.yogl.adjacent.keyValue;

import net.sf.yogl.Edge;

/** Edge is the container for user-defined edges. There is one list per vertex
 * (see vertex). The Edge object must be unique within this list.
 * An Edge contains the following information:
 * 
 *  - a reference to an existing object, containing the 'real' value associated
 *    to the edge. This object can be user-defined but must have an 'equals'
 *    function defined.
 *  - the 'traversal' variable is also used by some traversals algorithms.
 *    Useful to avoid these algorithms to loop 'forever' in graph cycles. 
 *  - the interger 'vertex' value points to the destination vertex.
 */

public class ValueEdge <VK extends Comparable<VK>, VV, EV> 
		extends Edge<ValueEdge<VK, VV, EV>, KeyValueVertex<VK, VV, EV>>{
	
	/** used by traversal algorithms to indicate the number
	 * of times the edge has been visited.
	 */
	private int traversals = 0;

	// Value associated to the edge. It is assumed that the 'equals' 
	// function is defined on the user object.
	private EV value = null;

	/** 
	 * @param v points to the destination vertex. 
	 * @param rValue refers to an object that is of the type used
	 * to define all edges in the graph.
	 */
	public ValueEdge(EV userValue, KeyValueVertex<VK, VV, EV> toVertex) {
		super(toVertex);
		this.value = userValue;
	}

	/** duplicate referenced edge
	 * @return a copy of this Edge
	 */
	public ValueEdge<VK, VV, EV> clone() {
		ValueEdge<VK, VV, EV> dup = new ValueEdge<VK, VV, EV>(this.value, super.getToVertex());
		return dup;
	}

	/** are equal if they points to the same destination
	 * If a value exists, compare them
	 * @param rhs referes to the edge value that will be compared to the
	 *        value contained in this object.
	 * @return a boolean value indicating if both 'values' matches.
	 *         The result does not take into account the status
	 *         information contained (traversals, vertex).
	 */
	public boolean equals(Object rhs) {
		boolean result = false;

		if (rhs instanceof ValueEdge) {
			result = this.value.equals(((ValueEdge<VK, VV, EV>) rhs).value);
		}
		return result;
	}

	/** getter method
	 * @return the 'value' referred to
	 */
	public EV getUserValue() {
		return value;
	}

	/** setter method
	 * @param traversals new value for this data member
	 */
	public void setVisitCount(int traversals) {
		this.traversals = traversals;
	}

	public void setUserValue(EV value) {

		this.value = value;
	}

	/** getter method
	 * @return the number of traversals
	 */
	public int getVisitCount() {
		return traversals;
	}

	/** special setter method: increments the current value of traversals
	 */
	public void incVisitCount() {
		traversals++;
	}

	/** Stringified version of Edge
	 *  @return a string describing the contents of the Edge
	 */
	public String toString() {

		return "traversals("
			+ traversals
			+ ")vertex("
			+ super.getToVertex().toString()
			+ ")value("
			+ value.toString()
			+ ")";
	}

}
