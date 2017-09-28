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

import java.util.List;

import net.sf.yogl.adjacent.keyMap.AdjKeyEdge;
import net.sf.yogl.adjacent.keyMap.AdjKeyVertex;
import net.sf.yogl.adjacent.keyMap.ComparableKeysGraph;
import net.sf.yogl.exceptions.NodeNotFoundException;

/** Defines the methods that needs to be implemented by any graph 
 *  implementation in order for it to be used by utility classes
 *  like iterators ...
 *  VK Vertex Key
 *  VV Vertex Value
 *  EK Edge Key
 *  EV Edge Value
 */
public interface ImplementationGraph<VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> extends ComparableKeysGraph<VK, VV, EK, EV> {

	/** Finds the node in 'vertices' and, if it exists, return the
	 *  corresponding Vertex from vertices. If it doesn't, throw an exception.
	 *  pre-condition: node is non-null
	 *  @param node refers to the user-defined node object
	 *  @exception NodeNotFoundException  thrown if rhs was not
	 *            found in the graph.
	 */
	public AdjKeyVertex<VK, VV, EK, EV> findVertexByKey(VK key)throws NodeNotFoundException;
	
	/** Returns a list of Vertex which are directly adjacent to the node.
	 */
	public List<AdjKeyVertex<VK, VV, EK, EV>>getSuccessorVertices(AdjKeyVertex<VK, VV, EK, EV> node)throws NodeNotFoundException ;
	
	public AdjKeyVertex<VK, VV, EK, EV>[] getSuccessorVertices(AdjKeyVertex<VK, VV, EK, EV> vertex, AdjKeyEdge<VK, EK, EV> link)
		throws NodeNotFoundException;
}
