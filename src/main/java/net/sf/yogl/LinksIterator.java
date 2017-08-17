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

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.impl.Edge;
import net.sf.yogl.impl.ImplementationGraph;
import net.sf.yogl.impl.Vertex;

/** A LinksIterator allows to retrieve all links from a given graph.
 *  When pointing to a specific link, the iterator gives also
 *  which nodes the link connect.
 */
public class LinksIterator<VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> implements Iterator<EV> {

	private ArrayList<VK> predNodes = null;
	private ArrayList<EV> links = null;
	private ArrayList<VK> succNodes = null;
	private int counter = 0;

	public LinksIterator(ImplementationGraph<VK, VV, EK, EV> graph) throws NodeNotFoundException {

		int count = graph.getLinkCount();
		int index = 0;
		predNodes = new ArrayList<VK>(count);
		links = new ArrayList<EV>(count);
		succNodes = new ArrayList<VK>(count);

		Iterator<VK> nodesIter = graph.nodesKeySet().iterator();
		while (nodesIter.hasNext()) {
			VK key = nodesIter.next();
			Vertex<VK, VV, EK, EV> vertex = graph.findVertexByKey(key);
			Edge<VK, EK, EV>[]neighbors = vertex.getNeighbors();
			for(int i=0; i < neighbors.length; i++) {
				predNodes.add(index, key);
				links.add(index, neighbors[i].getUserValue());
				VK destKey = neighbors[i].getNextVertexKey();
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
	public EV next() {

		EV result = null;
		if (hasNext()) {
			result = links.get(counter);
			counter++;
		}
		return result;
	}

	/** Returns the source node of the last 'read' link
	 */
	public VK getOriginator() {

		VK result = null;
		if ((counter <= links.size()) && (counter > 0)) {
			result = predNodes.get(counter - 1);
		}
		return result;
	}

	/** Returns the sink node of the last 'read' node
	 */
	public VK getDestination() {

		VK result = null;
		if ((counter <= links.size()) && (counter > 0)) {
			result = succNodes.get(counter - 1);
		}
		return result;
	}

	public void remove()
		throws UnsupportedOperationException, IllegalStateException {

		throw new UnsupportedOperationException("remove not defined in LinksIterator");
	}
}
