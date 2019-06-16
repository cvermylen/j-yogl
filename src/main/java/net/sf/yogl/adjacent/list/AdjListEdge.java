/* Copyright (C) 2003 Symphonix
   
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.
   
   You should have received a copy of the GNU Library General Public
   License along with this library; if not, write to the Free
   Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
   MA 02111-1307, USA */
   
package net.sf.yogl.adjacent.list;

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

public class AdjListEdge <VV, EV> extends Edge<AdjListVertex<VV, EV>>{
	
	// index in the vertices vector. Is used as a pointer
	// to the 'next' vertex.
	private AdjListVertex<VV, EV> nextVertex = null;

	// Value associated to the edge. It is assumed that the 'equals' 
	// function is defined on the user object.
	private EV userValue = null;

	/** 
	 * @param v points to the destination vertex. 
	 * @param rValue refers to an object that is of the type used
	 * to define all edges in the graph.
	 */
	public AdjListEdge(EV userValue) {
		this.userValue = userValue;
	}

	public AdjListEdge(AdjListVertex<VV, EV> v) {
		this.nextVertex = v;
	}
	
	public AdjListVertex<VV, EV> setNextNode(VV value) {
		AdjListVertex<VV, EV> result = new AdjListVertex<>(value);
		return this.setNextVertex(result);
	}
	
	public AdjListVertex<VV, EV> setNextVertex(AdjListVertex<VV, EV> v){
		this.nextVertex = v;
		return v;
	}
	
	/** duplicate referenced edge
	 * @return a copy of this Edge
	 */
	public AdjListEdge<VV, EV> clone() {
		AdjListEdge<VV, EV> dup = new AdjListEdge<>(this.userValue);
		dup.setNextVertex(this.nextVertex);
		return dup;
	}

	/** getter method
	 * @return the 'value' referred to
	 */
	public EV getUserValue() {
		return userValue;
	}

	public void setUserValue(EV userValue) {

		this.userValue = userValue;
	}

	/** setter method
	 * @param vertex is the new Vertex this edge points to
	 */
	public void setOutgoingVertex(AdjListVertex<VV, EV> nextVertex) {
		this.nextVertex = nextVertex;
	}

	/** getter method
	 * @ return the vertex this edge points to
	 */
	public AdjListVertex<VV, EV> getOutgoingVertex() {
		return nextVertex;
	}

}
