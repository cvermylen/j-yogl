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

import java.util.LinkedList;

import net.sf.yogl.Edge;
import net.sf.yogl.Vertex;

/** Class used to manage the path of vertices followed during
 * the traversal. Object are pushed on a stack with the number of adjacent
 * neighbors. Each time an object is poped from the traversal stack, the
 * top object of the path stack is decremented. When  the count value of the top
 * object reaches 0, the top object is removed.
 * 
 * REFACTOR. Maintains a stack of vertices, each one containing a stack of edges. The 'current vertex' is the top vertex, edges are popped
 * until there is no edge left for the current vertex. Then the vertex is popped automatically.
 */

public class LinearEdgesIterator{
	
	/** Reference to the vertex' node
	 */
	private Vertex old_node = null;
	
	/** Reference to links exiting from this vertex
	 *  Order in this stack is extremely important.
	 *  It MUST follow the same order as the one in the
	 *  vStack.
	 */
	private LinkedList<Edge> outgoingEdges = new LinkedList<>();
	
	public LinearEdgesIterator(Vertex node){
		this.old_node = node;
		outgoingEdges = node.getOutgoingEdges().stream().collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
	}

	public boolean hasModeEdges() {
		return outgoingEdges.size() > 0;
	}
	
	public Edge nextEdge() {
		return outgoingEdges.pop();
	}
	
	/** getter method
	 * @return the vertex index
	 */
	public Vertex getVertex(){
		return old_node;
	}
	
	public Edge peekEdge() {
		return outgoingEdges.peek();
	}
	
}