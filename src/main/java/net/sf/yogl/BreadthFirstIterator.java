/* Copyright (C) 2003 Symphonic
   
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
   
package net.sf.yogl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import net.sf.yogl.exceptions.*;
import net.sf.yogl.impl.ImplementationGraph;
import net.sf.yogl.impl.Vertex;

public final class BreadthFirstIterator implements Iterator {

	/** breadth first working structure
	 */
	private LinkedList queue = new LinkedList();

	private ImplementationGraph graph = null;

	/** Maximum number of times a node can be visited.
	 */
	private int maxCycling;

	/** Creates new BreadthFirstIterator. The instance is created by an
	 *  implementation of AbstractGraph
	 *  Precondition: graph & node must contain valid values. All node
	 *  counters (used to count the number of visits) must be reset.
	 */
	BreadthFirstIterator(
		ImplementationGraph graph,
		Object startingNodeKey,
		int maxCycling)
		throws GraphException {
		this.graph = graph;
		queue.add(startingNodeKey);
		this.maxCycling = maxCycling;
		graph.setAllVisitCounts(0);
	}

	/** Returns the next node key in breadth first order
	 */
	public Object next() throws NoSuchElementException {
		try {
			if (queue.isEmpty())
				throw new NoSuchElementException("Empty iterator");
			Object key = queue.removeFirst();
			if(key != null){
				Vertex current = graph.findVertexByKey(key);
				Object[]nextKeys = graph.getSuccessorNodesKeys(key, 1);
				for (int i = 0; i < nextKeys.length; i++) {
					Vertex next = graph.findVertexByKey(nextKeys[i]);
					if(next.getTraversals() < maxCycling){
						queue.addLast(nextKeys[i]);
						next.incTraversals();
					}
				}
			}
			return key;
		} catch (GraphException e1) {
			throw new NoSuchElementException(e1.getMessage());
		}
	}

	/** Return false when the iterator reaches the end of the graph
	 */
	public boolean hasNext() {
		return !queue.isEmpty();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
