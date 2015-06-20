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

import net.sf.yogl.AbstractGraph;
import net.sf.yogl.exceptions.NodeNotFoundException;

/** Defines the methods that needs to be implemented by any graph 
 *  implementation in order for it to be used by utility classes
 *  like iterators ...
 */
public interface ImplementationGraph extends AbstractGraph {

	/** Finds the node in 'vertices' and, if it exists, return the
	 *  corresponding Vertex from vertices. If it doesn't, throw an exception.
	 *  pre-condition: node is non-null
	 *  @param node refers to the user-defined node object
	 *  @exception NodeNotFoundException  thrown if rhs was not
	 *            found in the graph.
	 */
	public Vertex findVertexByKey(Object key)throws NodeNotFoundException;
	
	/** Returns a list of Vertex which are directly adjacent to the node.
	 */
	public Vertex[]getSuccessorVertices(Vertex node)throws NodeNotFoundException ;
	
	public Vertex[] getSuccessorVertices(Vertex vertex, Object link)
		throws NodeNotFoundException;
}
