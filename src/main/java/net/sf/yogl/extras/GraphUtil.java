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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.sf.yogl.AbstractGraph;
import net.sf.yogl.DepthFirstIterator;
import net.sf.yogl.GraphAdapter;
import net.sf.yogl.LinksIterator;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.types.VertexType;

/** This class contains several sttand-alone utilities
 */

public final class GraphUtil {

	/** A subgraph is a connex subset of an existing graph.
	 *  The resulting subgraph will contain one entry point
	 *  and one exit point.
	 */
	public static void subgraph(
		GraphAdapter graph,
		GraphAdapter result,
		Object startNodeKey,
		Object endNodeKey)
		throws GraphException {

		if (graph == null) {
			throw new NullPointerException("input parameter graph is null");
		}
		if (result == null) {
			throw new NullPointerException("input parameter result is null");
		}
		if (startNodeKey == null) {
			throw new NullPointerException("input parameter startNode is null");
		}
		if (endNodeKey == null) {
			throw new NullPointerException("input parameter endNode is null");
		}
		int degree = graph.getMaxOutDegree();
		DepthFirstIterator iter = graph.depthFirstIterator(startNodeKey, degree);
		while (iter.hasNext()) {
			if (endNodeKey.equals(iter.next())) {
				Object[]nodes = iter.nodePath();
				ArrayList nodesArray = new ArrayList(Arrays.asList(nodes));
				nodesArray.add(endNodeKey);
				Iterator nodesIter = nodesArray.iterator();
				Object[]links = iter.linkPath();
				//links.removeFirst();
				//Check the validity of link & node lists.
				//The node list must contain 1 more element than the link list.
				//Otherwise there is a mismatch
				if (nodesArray.size() - links.length - 1 != 0) {
					throw new GraphCorruptedException(
						"mismatch between link & node lists " ); }
				Object leftNodeKey = nodesIter.next();
				Object leftNodeValue = graph.getNodeValue(leftNodeKey);
				Object rightNodeKey = null;
				Object rightNodeValue = null;
				Object link = null;
				result.tryAddNode(leftNodeKey, leftNodeValue);
				for(int i=0; i < links.length; i++){

					rightNodeKey = nodesIter.next();
					link = links[i];
					if (rightNodeKey.equals(endNodeKey)) {
						rightNodeValue = graph.getNodeValue(rightNodeKey);
						result.tryAddNode(rightNodeKey, rightNodeValue);

					} else {
						result.tryAddNode(rightNodeKey, rightNodeValue);
					}
					result.tryAddLinkLast(leftNodeKey, rightNodeKey, link, null);
					leftNodeKey = rightNodeKey;
				}
			}
		}

	}

	/** Returns the part of the graph that is above the given node
	 */
	public static void headgraph(
		GraphAdapter source,
		GraphAdapter destination,
		Object endNodeKey)
		throws GraphException {

		if (endNodeKey == null)
			return;
		switch (source.getNodeType(endNodeKey)) {
			case VertexType.START :
			case VertexType.STARTEND :
				Object endNodeValue = source.getNodeValue(endNodeKey);
				destination.tryAddNode(endNodeKey, endNodeValue);
				return;
		}
		Object[] startList = (Object[]) source.getNodesKeys(VertexType.START);
		//Iterator iter = startList.iterator();
		for (int i = 0; i < startList.length; i++) {
			//while(iter.hasNext()){
			Object startKey = startList[i];
			subgraph(source, destination, startKey, endNodeKey);
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
		switch (source.getNodeType(startNodeKey)) {
			case VertexType.END :
			case VertexType.STARTEND :
				Object startNodeValue = source.getNodeValue(startNodeKey);
				destination.tryAddNode(startNodeKey, startNodeValue);
				return;
		}
		int degree = source.getMaxOutDegree();
		DepthFirstIterator iter = source.depthFirstIterator(startNodeKey, degree);
		Object firstKey = iter.next();
		Object firstValue = source.getNodeValue(firstKey);
		int type;
		if (iter.hasNext()) {
			type = VertexType.START;
		} else {
			type = VertexType.STARTEND;
		}
		destination.tryAddNode(firstKey, firstValue);

		while (iter.hasNext()) {
			Object refKey = iter.next();
			Object refValue = source.getNodeValue(refKey);
			Object[]path = iter.nodePath();
			Object predKey = path[path.length -1];
			Object predValue = source.getNodeValue(predKey);
			destination.tryAddNode(refKey, refValue);
			destination.tryAddLinkLast(predKey, refKey, iter.usedLink(), null);
		}
	}

	/** Returns a string containing the graph as traversed in depth first
	 *  Can be useful for debugging
	 *  @param graph the whole graph to be stringified
	     *  @param indent justify some tabs from the left
	 */
	public static String depthFirstToString(AbstractGraph graph, int indent)
		throws GraphException {

		StringBuffer result = new StringBuffer();
		StringBuffer tabs = new StringBuffer();
		for (int i = 0; i < indent; i++) {
			tabs.append("\t");
		}
		result.append(tabs);
		result.append("node count(");
		result.append(graph.getNodeCount());
		result.append(")\n");
		result.append(tabs);
		result.append("link count(");
		result.append(graph.getLinkCount());
		result.append(")\n");
		DepthFirstIterator iter = graph.depthFirstIterator(null, 1);
		while (iter.hasNext()) {
			String temp = new String((iter.next()).toString());
			Object[]path = iter.nodePath();
			Object[]links = iter.linkPath();
			result.append(tabs);
			for(int i=0; i < path.length; i++) {
				result.append("[");
				result.append(path[i]);
				result.append("]");
				result.append(links[i]);
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

	/** Insert a graph into another, existing graph.
	 *  The subgraph must have a start and a end node. After insertion
	 *  these labels are removed from the nodes.
	 *  The subgraph replaces an existing node in the first graph. After
	 *  insertion, the node (called 'insertion point') does not exist
	 *  anymore. Ingoing and outgoing edges from the insertion point
	 *  are preserved. Ingoing edges are transferred from the insertion
	 *  point to the subgraph 'start' node and outgoing edges are
	 *  transferred from the insertion point to the subgraph end node.
	 *  @param dest graph that contains the insertion point node
	 *  @param insertionPoint node to be replaced by the subgraph
	 *  @param subgraph to be inserted at 'insertion point'
	 */
	public static void insertSubgraph(
		GraphAdapter dest,
		Object insertionPointKey,
		GraphAdapter subgraph)
		throws GraphException {

		/* Do the following validation:
		 * 1- the destination graph contains the insertion point
		 * 2- the subgraph contains 1! start node and 1! end node
		 */
		if (dest == null)
			throw new NullPointerException("parameter dest is null");
		if (insertionPointKey == null)
			throw new NullPointerException("parameter insertionPoint is null");
		if (subgraph == null)
			throw new NullPointerException("parameter subgraph is null");

		//Insert all nodes from subgraph into the dest graph
		Object[] intermediateNodes = subgraph.getNodesKeys(VertexType.NONE);
		//Iterator intermediateIter = intermediateNodes.iterator();
		//while(intermediateIter.hasNext()){
		for (int i = 0; i < intermediateNodes.length; i++) {
			Object nodeKey = intermediateNodes[i];
			Object nodeValue = subgraph.getNodeValue(nodeKey);
			dest.addNode(nodeKey, nodeValue);
		}
		//extract the start & end nodes from the subgraph
		//extract the start nodes from the subgraph. Should be max 1.
		Object[] startList = null;
		startList = subgraph.getNodesKeys(VertexType.START);
		if (startList.length != 1) {
			throw new GraphException(
				"parameter subgraph should "
					+ "contain 1 and only 1 'start' node");
		}
		Object startNodeKey = startList[0];
		Object startNodeValue = subgraph.getNodeValue(startNodeKey);
		
		//extract the end node from the subgraph. Should be max 1.
		Object[] endList = null;
		endList = subgraph.getNodesKeys(VertexType.END);
		if (endList.length != 1) {
			throw new GraphException(
				"parameter graph should " + "contain 1 and only 1 'end' node");
		}
		Object endNodeKey = endList[0];
		Object endNodeValue = subgraph.getNodeValue(endNodeKey);
		
		dest.addNode(startNodeKey, startNodeValue);
		dest.addNode(startNodeKey, endNodeValue);

		//Insert all links from subgraph into dest graph
		LinksIterator allLinks = subgraph.linksKeysIterator();
		while (allLinks.hasNext()) {
			Object link = allLinks.next();
			dest.addLinkLast(
				allLinks.getOriginator(),
				allLinks.getDestination(),
				link, null);
		}

		//Connect subgraph to dest graph

		Object[] predecessorsKeys = null;
		predecessorsKeys = dest.getPredecessorNodesKeys(insertionPointKey);
		for(int j=0; j < predecessorsKeys.length; j++) { //for each predecessor
			Object[] links = null;
			links = dest.getLinksKeysBetween(predecessorsKeys[j], insertionPointKey);
			for(int i=0; i < links.length; i++) {
				//remove existing link
				dest.removeLink(predecessorsKeys[j], insertionPointKey, links[i]);
				//and connect new subgraph
				dest.addLinkLast(predecessorsKeys[j], startNodeKey, links[i], null);
			}
		}

		Object[] successorsKeys = null;
		successorsKeys = dest.getSuccessorNodesKeys(insertionPointKey, 1);
		for(int i=0; i < successorsKeys.length; i++) {
			Object[] links = null;
			links = dest.getLinksKeysBetween(insertionPointKey, successorsKeys[i]);
			for(int j=0; j < links.length; j++) {
				dest.removeLink(insertionPointKey, successorsKeys[i], links[j]);
				dest.addLinkLast(endNodeKey, successorsKeys[i], links[j], null);
			}
		}
	}
}