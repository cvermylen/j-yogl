package net.sf.yogl;

import java.util.Collection;

import net.sf.yogl.adjacent.keyMap.AdjKeyVertex;
import net.sf.yogl.adjacent.keyMap.LinksIterator;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.types.VertexType;

public interface Graph<V extends Vertex<E>, E extends Edge<V>> {

	/** Returns the max degree by only taking into account the incoming
	 *  edges
	 */
//	public int getMaxInDegree();

	/** Returns the max degree by only taking into account the outgoing
	 *  edges
	 */
	public int getMaxOutDegree();

	public int getMaxInDegree();
	
	public int getNodeCount();
	
	public int getLinkCount();
	
	/** Check if the graph is empty or not. That is, if the graph contains
	 * at least one node.
	 */
	public boolean isEmpty();
	
	/** By default, a new node added to a graph is a root
	 * 
	 */
	public V addRootVertex(V v);
	
	public Collection<V>getRoots();
	
	public void clearAllVisitCounts();
	
	public Collection<V> getPredecessorVertices(V vertex);
	
	public Collection<V> getSuccessorVertices(V vertex);
	
	public LinksIterator<V, E> edgesIterator();
	
	/** Return an iterator to browse ALL nodes in the graph that can
	 *  be accessed from the startingNode. The method 'next()' in the
	 *  iterator will return the nodes in the order defined by the 
	 *  breadth first' policy.
	 *  @param startingNodeKey identify a Node in the graph which will be
	 *  the starting point of the traversal.
	 *  @param maxCycles fixes the number of times each node can be
	 *         visited. A value of 1 indicates that each node will be
	 *         returned max. 1 time, this is thus a way to avoid cycling.
	 *         Accepted values are: [1 .. n]. There is no way to express
	 *         an infinite value.
	 */
	public BreadthFirstIterator<V, E> breadthFirstIterator(int maxCycle);
	
	/** Return an iterator to browse ALL nodes in the graph that can
	 *  be accessed from the startingNode. The method 'next()' in the
	 *  iterator will return the nodes in the order defined by the 
	 *  depth first' policy.
	 *  @param startingNodeKey identify a Node in the graph which will be
	 *  the starting point of the traversal.
	 *  @param maxCycles fixes the number of times each node can be
	 *         visited. A value of 1 indicates that each node will be
	 *         returned max. 1 time, this is thus a way to avoid cycling.
	 *         Accepted values are: [1 .. n]. There is no way to express
	 *         an infinite value.
	 */
	public DepthFirstIterator<V, E> depthFirstIterator(Collection<V> startVertex, int maxCycling)
		throws GraphException;
	
	public V tryAddNode(V vertex);
	
	public void tryAddLinkLast (V vertex, E edge);
	
	public VertexType getNodeType (V vertex);
	
	public Collection<V> getVertices (VertexType type);
}
