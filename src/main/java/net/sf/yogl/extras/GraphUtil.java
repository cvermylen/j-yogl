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
   
package net.sf.yogl.extras;

import java.util.List;

import net.sf.yogl.adjacent.keyMap.ComparableKeysGraph;
import net.sf.yogl.adjacent.keyMap.GraphAdapter;
import net.sf.yogl.adjacent.list.AdjListDepthFirstIterator;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.types.VertexType;

/** This class contains several sttand-alone utilities
 */

public final class GraphUtil {

	/** Returns the part of the graph that is above the given node
	 */
	public static void headgraph(
		GraphAdapter source,
		GraphAdapter destination,
		Object endNodeKey)
		throws GraphException {

		if (endNodeKey == null)
			return;
		switch (source.getNodeType((Comparable)endNodeKey)) {
			case VertexType.START :
			case VertexType.STARTEND :
				Object endNodeValue = source.getNodeValue((Comparable)endNodeKey);
				destination.tryAddNode((Comparable)endNodeKey, endNodeValue);
				return;
		}
		List startList = source.getVertices(VertexType.START);
		//Iterator iter = startList.iterator();
		for (Object startKey: startList) {
			//while(iter.hasNext()){
//			subgraph(source, destination, (Comparable)startKey, (Comparable)endNodeKey);
		}
	}

	/** Returns the part of the graph that is underneath the given node.
	 *
	 */
	public static void tailgraph(
		GraphAdapter source,
		GraphAdapter destination,
		Object startNodeKey)
		throws GraphException {

		if (startNodeKey == null)
			return;
		switch (source.getNodeType((Comparable)startNodeKey)) {
			case VertexType.END :
			case VertexType.STARTEND :
				Object startNodeValue = source.getNodeValue((Comparable)startNodeKey);
				destination.tryAddNode((Comparable)startNodeKey, startNodeValue);
				return;
		}
		int degree = source.getMaxOutDegree();
		AdjListDepthFirstIterator iter = source.depthFirstIterator((Comparable)startNodeKey, degree);
		Object firstKey = iter.next();
		Object firstValue = source.getNodeValue((Comparable)firstKey);
		int type;
		if (iter.hasNext()) {
			type = VertexType.START;
		} else {
			type = VertexType.STARTEND;
		}
		destination.tryAddNode((Comparable)firstKey, firstValue);

		while (iter.hasNext()) {
			Object refKey = iter.next();
			Object refValue = source.getNodeValue((Comparable)refKey);
			List path = iter.nodePath();
			Object predKey = path.get(path.size() -1);
			Object predValue = source.getNodeValue((Comparable)predKey);
			destination.tryAddNode((Comparable)refKey, refValue);
			//TODO next line commented to suppress compile eror on usedLink
//			destination.tryAddLinkLast((Comparable)predKey, (Comparable)refKey, (Comparable)iter.usedLink(), null);
		}
	}

	/** Returns a string containing the graph as traversed in depth first
	 *  Can be useful for debugging
	 *  @param graph the whole graph to be stringified
	     *  @param indent justify some tabs from the left
	 */
	public static String depthFirstToString(ComparableKeysGraph graph, int indent)
		throws GraphException {

		StringBuffer result = new StringBuffer();
		StringBuffer tabs = new StringBuffer();
		for (int i = 0; i < indent; i++) {
			tabs.append("\t");
		}
		result.append(tabs);
		result.append("node count(");
//		result.append(graph.getNodeCount());
		result.append(")\n");
		result.append(tabs);
		result.append("link count(");
		result.append(graph.getLinkCount());
		result.append(")\n");
		AdjListDepthFirstIterator iter = graph.depthFirstIterator(null, 1);
		while (iter.hasNext()) {
			String temp = new String((iter.next()).toString());
			List path = iter.nodePath();
			List links = iter.linkPath();
			result.append(tabs);
			for(int i=0; i < path.size(); i++) {
				result.append("[");
				result.append(path.get(i));
				result.append("]");
				result.append(links.get(i));
			}
			result.append("[" + temp + "]");
			result.append("\n//--------------------------------\n");
		}

		return result.toString();
	}

	/*public static SortedVectorSet graph2SortedVector(AbstractGraph graph)
		throws GraphException {

		SortedVectorSet result = new SortedVectorSet();
		DepthFirstIterator iter = graph.depthFirstIterator(null, 1);
		while (iter.hasNext()) {
			Comparable o = (Comparable) iter.next();
			result.put(o);
		}
		return result;
	}*/

}