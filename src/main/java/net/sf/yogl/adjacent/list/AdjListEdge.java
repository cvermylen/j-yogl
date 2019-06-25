   
package net.sf.yogl.adjacent.list;

import net.sf.yogl.Edge;
import net.sf.yogl.exceptions.NodeNotFoundException;

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

public class AdjListEdge <VERTEX_VALUE, EDGE_VALUE> extends Edge<AdjListEdge<VERTEX_VALUE, EDGE_VALUE>, AdjListVertex<VERTEX_VALUE, EDGE_VALUE>>{
	
	// index in the vertices vector. Is used as a pointer
	// to the 'next' vertex.
	private AdjListVertex<VERTEX_VALUE, EDGE_VALUE> nextVertex = null;

	// Value associated to the edge. It is assumed that the 'equals' 
	// function is defined on the user object.
	private EDGE_VALUE userValue = null;

	public AdjListEdge(){
		super();
	}
	
	/** 
	 * @param v points to the destination vertex. 
	 * @param rValue refers to an object that is of the type used
	 * to define all edges in the graph.
	 */
	public AdjListEdge(EDGE_VALUE userValue, AdjListVertex<VERTEX_VALUE, EDGE_VALUE> toVertex) throws NodeNotFoundException {
		super(toVertex);
		this.userValue = userValue;
	}

	public AdjListVertex<VERTEX_VALUE, EDGE_VALUE> setNextNode(VERTEX_VALUE value) {
		AdjListVertex<VERTEX_VALUE, EDGE_VALUE> result = new AdjListVertex<>(value);
		return this.setNextVertex(result);
	}
	
	public AdjListVertex<VERTEX_VALUE, EDGE_VALUE> setNextVertex(AdjListVertex<VERTEX_VALUE, EDGE_VALUE> v){
		this.nextVertex = v;
		return v;
	}
	
	/** duplicate referenced edge
	 * @return a copy of this Edge
	 */
	public void cloneTO(AdjListEdge<VERTEX_VALUE, EDGE_VALUE> copyToEdge) {
		super.cloneTo(copyToEdge);
		copyToEdge.setNextVertex(this.nextVertex);
	}

	/** getter method
	 * @return the 'value' referred to
	 */
	public EDGE_VALUE getUserValue() {
		return userValue;
	}

	public void setUserValue(EDGE_VALUE userValue) {

		this.userValue = userValue;
	}

	/** setter method
	 * @param vertex is the new Vertex this edge points to
	 */
	public void setToVertex(AdjListVertex<VERTEX_VALUE, EDGE_VALUE> nextVertex) {
		this.nextVertex = nextVertex;
	}

	/** getter method
	 * @ return the vertex this edge points to
	 */
	public AdjListVertex<VERTEX_VALUE, EDGE_VALUE> getToVertex() {
		return nextVertex;
	}

}
