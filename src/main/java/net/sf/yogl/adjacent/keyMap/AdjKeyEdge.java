 package net.sf.yogl.adjacent.keyMap;

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

public class AdjKeyEdge <VK extends Comparable<VK>, EK extends Comparable<EK>, EV> extends Edge<AdjKeyVertex>{
	
	private AdjKeyGraph graph;
	
	/** used by traversal algorithms to indicate the number
	 * of times the edge has been visited.
	 */
	private int traversals = 0;

	// index in the vertices vector. Is used as a pointer
	// to the 'next' vertex.
	private VK nextVertexKey = null;

	//Unique identifier for this edge. Uniqueness is applicable to
	//edges contained in the outgoing list of the Vertex.
	private EK edgeKey = null;
	
	// Value associated to the edge. It is assumed that the 'equals' 
	// function is defined on the user object.
	private EV userValue = null;

	/** 
	 * @param v points to the destination vertex. 
	 * @param rValue refers to an object that is of the type used
	 * to define all edges in the graph.
	 */
	public AdjKeyEdge(AdjKeyGraph graph, EK edgeKey, VK nextVertexKey, EV userValue) {
		this.edgeKey = edgeKey;
		this.nextVertexKey = nextVertexKey;
		this.userValue = userValue;
	}

	/** duplicate referenced edge
	 * @return a copy of this Edge
	 */
	public AdjKeyEdge<VK, EK, EV> clone() {
		AdjKeyEdge<VK, EK, EV> dup = new AdjKeyEdge<VK, EK, EV>(this.edgeKey, this.nextVertexKey, this.userValue);
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

		if (rhs instanceof AdjKeyEdge) {
			result = edgeKey.equals(((AdjKeyEdge<VK, EK, EV>) rhs).edgeKey);
		}
		return result;
	}

	/** getter method
	 * @return the 'value' referred to
	 */
	public EV getUserValue() {
		return userValue;
	}

	/** setter method
	 * @param traversals new value for this data member
	 */
	public void setVisitCount(int traversals) {
		this.traversals = traversals;
	}

	public void setUserValue(EV userValue) {

		this.userValue = userValue;
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

	/** setter method
	 * @param vertex is the new Vertex this edge points to
	 */
	public void setNextVertexKey(VK nextVertexKey) {
		this.nextVertexKey = nextVertexKey;
	}

	/** getter method
	 * @ return the vertex this edge points to
	 */
	public VK getNextVertexKey() {
		return nextVertexKey;
	}

	/** Stringified version of Edge
	 *  @return a string describing the contents of the Edge
	 */
	public String toString() {

		return "traversals("
			+ traversals
			+ ")vertex("
			+ nextVertexKey.toString()
			+ ")key("
			+ edgeKey.toString()
			+ ")value("
			+ userValue.toString()
			+ ")";
	}

	/**
	 * @return
	 */
	public EK getEdgeKey() {
		return edgeKey;
	}

	@Override
	public AdjKeyVertex getOutgoingVertex() {
		// TODO Auto-generated method stub
		return graph.getVertex (nextVertexKey);
	}

}
