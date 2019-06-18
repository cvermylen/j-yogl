   
package net.sf.yogl.adjacent.keyMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.sf.yogl.DepthFirstIterator;
import net.sf.yogl.Edge;
import net.sf.yogl.Graph;
import net.sf.yogl.Vertex;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.types.VertexType;

@FunctionalInterface
interface TriFunction<A, B, C, D> {
	D apply (A a, B b, C c);
}

/** This class contains several sttand-alone utilities
 */

public final class GraphUtil {

	/** A subgraph is a connex subset of an existing graph.
	 *  The resulting subgraph is a new graph, with new vertices and edges, will contain one entry point
	 *  and one exit point.
	 */
	public static <V extends Vertex<E>, E extends Edge<V>> void subgraph(Graph<V, E> graph, Graph<V, E> result,
			V startNode, V endNodeKey, Function<V, V> vertexCtor, BiFunction<V, E, E> edgeCtor) throws GraphException {

		if (graph == null) {
			throw new NullPointerException("input parameter graph is null");
		}
		if (result == null) {
			throw new NullPointerException("input parameter result is null");
		}
		if (startNode == null) {
			throw new NullPointerException("input parameter startNode is null");
		}
		if (endNodeKey == null) {
			throw new NullPointerException("input parameter endNode is null");
		}
		int degree = graph.getMaxOutDegree();
		DepthFirstIterator<V, E> iter = graph.depthFirstIterator(Arrays.asList(startNode), degree);
		while (iter.hasNext()) {
			if (endNodeKey.equals(iter.next())) {
				List<V> nodes = iter.nodePath();
				ArrayList<V> nodesArray = new ArrayList(Arrays.asList(nodes));
				nodesArray.add(endNodeKey);
				Iterator<V> nodesIter = nodesArray.iterator();
				List<E> links = iter.linkPath();
				//links.removeFirst();
				//Check the validity of link & node lists.
				//The node list must contain 1 more element than the link list.
				//Otherwise there is a mismatch
				if (nodesArray.size() - links.size() - 1 != 0) {
					throw new GraphCorruptedException(
						"mismatch between link & node lists " ); }
				V leftNodeKey = nodesIter.next();
				V rightNodeKey = null;
				V leftDup = vertexCtor.apply(leftNodeKey);
				result.tryAddNode(leftDup);
				for(E link: links){
					rightNodeKey = nodesIter.next();
					V rightDup = vertexCtor.apply(rightNodeKey);
					if (rightNodeKey.equals(endNodeKey)) {
						result.tryAddNode(rightDup);

					} else {
						result.tryAddNode(rightDup);
					}
					
					result.tryAddLinkLast(leftDup, edgeCtor.apply(rightDup, link));
					leftNodeKey = rightNodeKey;
				}
			}
		}

	}

	/** Returns the part of the graph that is above the given node
	 */
	public static <V extends Vertex<E>, E extends Edge<V>> void headgraph(Graph<V, E> source, Graph<V, E> destination, V endNodeKey,
			Function<V, V> vertexCtor, BiFunction<V, E, E> edgeCtor)
			throws GraphException {

		if (endNodeKey == null)
			return;
		switch (source.getNodeType(endNodeKey)) {
			case START :
			case STARTEND :
				destination.tryAddNode(vertexCtor.apply(endNodeKey));
				return;
			default:
		}
		Collection<V> startList = source.getVertices(VertexType.START);
		//Iterator iter = startList.iterator();
		for (V startKey: startList) {
			//while(iter.hasNext()){
			subgraph(source, destination, startKey, endNodeKey, vertexCtor, edgeCtor);
		}
	}

	/** Returns the part of the graph that is underneath the given node.
	 *
	 */
	public static <V extends Vertex<E>, E extends Edge<V>> void tailgraph(Graph<V, E> source, Graph<V, E> destination, V startNodeKey,
			Function<V, V> vertexCtor, BiFunction<V, E, E> edgeCtor)
			throws GraphException {

		if (startNodeKey == null)
			return;
		switch (source.getNodeType(startNodeKey)) {
			case END :
			case STARTEND :
				destination.tryAddNode(vertexCtor.apply(startNodeKey));
				return;
			default:
		}
		int degree = source.getMaxOutDegree();
		DepthFirstIterator<V, E> iter = source.depthFirstIterator(Arrays.asList(startNodeKey), degree);
		V firstKey = iter.next();
		VertexType type;
		if (iter.hasNext()) {
			type = VertexType.START;
		} else {
			type = VertexType.STARTEND;
		}
		destination.tryAddNode(vertexCtor.apply(firstKey));

		while (iter.hasNext()) {
			V refKey = iter.next();
			List<V> path = iter.nodePath();
			V predKey = path.get(path.size() -1);
			V v = vertexCtor.apply(refKey);
			destination.tryAddNode(v);
			destination.tryAddLinkLast(predKey, edgeCtor.apply(v, null));
		}
	}

	/** Returns a string containing the graph as traversed in depth first
	 *  Can be useful for debugging
	 *  @param graph the whole graph to be stringified
	     *  @param indent justify some tabs from the left
	 */
	public static <V extends Vertex<E>, E extends Edge<V>> String depthFirstToString(Graph<V, E> graph, int indent)
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
		DepthFirstIterator<V, E> iter = graph.depthFirstIterator(null, 1);
		while (iter.hasNext()) {
			String temp = new String((iter.next()).toString());
			List<V> path = iter.nodePath();
			List<E> links = iter.linkPath();
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
	public static <V extends Vertex<E>, E extends Edge<V>> void insertSubgraph(Graph<V, E> dest,
			V insertionPointVertex, Graph<V, E> subgraph, Function<V, V> vertexCtor) throws GraphException {

		/* Do the following validation:
		 * 1- the destination graph contains the insertion point
		 * 2- the subgraph contains 1! start node and 1! end node
		 */
		if (dest == null)
			throw new NullPointerException("parameter dest is null");
		if (insertionPointVertex == null)
			throw new NullPointerException("parameter insertionPoint is null");
		if (subgraph == null)
			throw new NullPointerException("parameter subgraph is null");

		//Insert all nodes from subgraph into the dest graph
		Collection<V> intermediateNodes = subgraph.getVertices(VertexType.NONE);
		intermediateNodes.forEach(vertex -> dest.tryAddNode(vertexCtor.apply(vertex)));
		//extract the start & end nodes from the subgraph
		//extract the start nodes from the subgraph. Should be max 1.
		Collection<V> startList = null;
		startList = subgraph.getVertices(VertexType.START);
		if (startList.size() != 1) {
			throw new GraphException(
				"parameter subgraph should "
					+ "contain 1 and only 1 'start' node");
		}
		V startNodeKey = startList.stream().findFirst().get();
		
		//extract the end node from the subgraph. Should be max 1.
		Collection<V> endList = null;
		endList = subgraph.getVertices(VertexType.END);
		if (endList.size() != 1) {
			throw new GraphException(
				"parameter graph should " + "contain 1 and only 1 'end' node");
		}
		V endNodeKey = endList.parallelStream().findFirst().get();
		
		dest.tryAddNode(vertexCtor.apply(startNodeKey));
		dest.tryAddNode(vertexCtor.apply(endNodeKey));

		//Insert all links from subgraph into dest graph
		LinksIterator<V, E> allLinks = subgraph.edgesIterator();
		while (allLinks.hasNext()) {
			E link = allLinks.next();
			dest.tryAddLinkLast(allLinks.getOriginator(), allLinks.getDestination(), link, null);
		}

		//Connect subgraph to dest graph

		Collection<V> predecessors = null;
		predecessors = dest.getPredecessorVertices(insertionPointVertex);
		Iterator<V> predsIter = predecessors.iterator();
		while (predsIter.hasNext()) {
//		for(int j=0; j < predecessors.size(); j++) { //for each predecessor
			V pred = predsIter.next();
			Collection<E> links = null;
			links = dest.getLinksKeysBetween(pred, insertionPointVertex);
			Iterator<E> linksIter = links.iterator();
			while (linksIter.hasNext()) {
				E edge = linksIter.next();
//			for(int i=0; i < links.length; i++) {
				//remove existing link
				dest.removeLink(pred, insertionPointVertex, edge);
				//and connect new subgraph
				dest.addLinkLast(pred, startNodeKey, edge, null);
			}
		}

		Collection<V> successors = null;
		successors = dest.getSuccessorVertices(insertionPointVertex, 1);
		Iterator<V> succIter = successors.iterator();
		while (succIter.hasNext()) {
			V succ = succIter.next();
//		for(int i=0; i < successors.size(); i++) {
			Collection<E> links = null;
			links = dest.getLinksKeysBetween(insertionPointVertex, succ);
			Iterator<E> linksIter = links.iterator();
			while (linksIter.hasNext()) {
				E edge = linksIter.next();
			
//			for(int j=0; j < links.length; j++) {
				dest.removeLink(insertionPointVertex, succ, edge);
				dest.addLinkLast(endNodeKey, succ, edge, null);
			}
		}
	}
}