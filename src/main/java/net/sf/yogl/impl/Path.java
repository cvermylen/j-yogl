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
   
package net.sf.yogl.impl;

import java.util.Stack;

/** Class used to manage the path of vertices followed during
 * the traversal. Object are pushed on a stack with the number of adjacent
 * neighbors. Each time an object is poped from the traversal stack, the
 * top object of the path stack is decremented. When  the count value of the top
 * object reaches 0, the top object is removed.
 */

public class Path{
	
	/** Reference to the vertex' node
	 */
	private Object nodeKey = null;
	
	private Edge popedValue = null;
	
	/** Reference to links exiting from this vertex
	 *  Order in this stack is extremely important.
	 *  It MUST follow the same order as the one in the
	 *  vStack.
	 */
	private Stack outgoingEdges = null;
	
	public Path(Object nodeKey, Edge[]eArray){
		this.nodeKey = nodeKey;
		this.outgoingEdges = new Stack();
		outgoingEdges.push(new Edge(null, null, null));
			for (int i=eArray.length-1; i>= 0; i--){
				outgoingEdges.push(((Edge)eArray[i]));
			}
	}

	/** dec consumes the next edge from the current
	 *  path node.
	 *  @return the edge used to access to the next
	 *  node
	 */
	public Edge dec(){
		
		Edge usedEdge = (Edge)outgoingEdges.pop();
		popedValue = usedEdge;
		return usedEdge;
	}

	/** getter method
	 * @return the count number
	 */
	public int getCount(){
		return outgoingEdges.size() - 1;
	}

	/** getter method
	 * @return the vertex index
	 */
	public Object getNodeKey(){
		return nodeKey;
	}
	
	/** getter method
	 *  @return the current edge
	 */
	public Edge getEdge(){
		Edge edge = null;
		
		if(!outgoingEdges.isEmpty()){
			if(outgoingEdges.size() > 1){ //last element is empty string
				edge = (Edge)outgoingEdges.peek();
			}
		}
		return edge;
	}
	
	public Edge getPopedValue(){
		
		return popedValue;
	}
	
	public String toString(){
		
		String result = "Path(Node("+nodeKey+")PopedValue("+popedValue+"))";
		return result;
	}
}