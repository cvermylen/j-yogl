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
   
package net.sf.yogl;

import java.util.Iterator;
import java.util.Vector;

import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.impl.Edge;
import net.sf.yogl.impl.ImplementationGraph;
import net.sf.yogl.impl.Vertex;

/** A LinksIterator allows to retrieve all links from a given graph.
 *  When pointing to a specific link, the iterator gives also
 *  which nodes the link connect.
 */
public class LinksIterator implements Iterator {

	private Vector predNodes = null;
	private Vector links = null;
	private Vector succNodes = null;
	private int counter = 0;

	public LinksIterator(ImplementationGraph graph) throws NodeNotFoundException {

		int count = graph.getLinkCount();
		int index = 0;
		predNodes = new Vector(count);
		links = new Vector(count);
		succNodes = new Vector(count);

		Iterator nodesIter = graph.nodesKeySet().iterator();
		while (nodesIter.hasNext()) {
			Object key = nodesIter.next();
			Vertex vertex = graph.findVertexByKey(key);
			Edge[]neighbors = vertex.getNeighbors();
			for(int i=0; i < neighbors.length; i++) {
				predNodes.add(index, key);
				links.add(index, neighbors[i].getUserValue());
				Object destKey = neighbors[i].getNextVertexKey();
				succNodes.add(index++, destKey);
			}
		}
	}

	/** Returns true if there are still un-read elements in the iterator
	 */
	public boolean hasNext() {

		return counter < links.size();
	}

	/** Read the next link from the iterator
	 */
	public Object next() {

		Object result = null;
		if (hasNext()) {
			result = links.elementAt(counter);
			counter++;
		}
		return result;
	}

	/** Returns the source node of the last 'read' link
	 */
	public Object getOriginator() {

		Object result = null;
		if ((counter <= links.size()) && (counter > 0)) {
			result = predNodes.elementAt(counter - 1);
		}
		return result;
	}

	/** Returns the sink node of the last 'read' node
	 */
	public Object getDestination() {

		Object result = null;
		if ((counter <= links.size()) && (counter > 0)) {
			result = succNodes.elementAt(counter - 1);
		}
		return result;
	}

	public void remove()
		throws UnsupportedOperationException, IllegalStateException {

		throw new UnsupportedOperationException("remove not defined in LinksIterator");
	}
}
