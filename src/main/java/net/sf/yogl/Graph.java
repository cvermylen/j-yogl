package net.sf.yogl;

import java.util.Collection;

import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.iterators.BreadthFirstIterator;
import net.sf.yogl.iterators.DepthFirstIterator;
import net.sf.yogl.types.VertexType;

public interface Graph<VERTEX extends Vertex<VERTEX, EDGE>, EDGE extends Edge<EDGE, VERTEX>> {

	/** Returns the max degree by only taking into account the incoming
	 *  edges
	 */
//	public int getMaxInDegree();

	/** Returns the max degree by only taking into account the outgoing
	 *  edges
	 */
	public int getMaxOutDegree();

	public int getMaxInDegree();
	
	/** Return the number of vertices in this graph
	 * @return
	 */
	public int getNodeCount();
	
	public int getLinkCount();
	
	/** Check if the graph is empty or not. That is, if the graph contains
	 * at least one node.
	 */
	public boolean isEmpty();
	
	/** Test if the identified node is an entry point.
	 * @param nodeKey node identifier
	 * @return true if is an entry point
	 */
	public boolean isRootVertex(VERTEX vertex);
	
	/** Get all vertices that have no predecessors.
	 * 
	 * @return
	 */
	public Collection<VERTEX>getRoots();
	
	/** Return the list of nodes of a particular type
	 *  @param nodeType has a value described in VertexType
	 */
	public Collection<VERTEX> getVertices (VertexType type) throws GraphException;
	
	public void clearAllVisitCounts();
	
	/** Returns the nodes that have a 'predecessor' relationship with nodeTo.
	 *  A predecessor is a node that is the originator of a link whose
	 *  destination is indicated by the parameter.
	 *  @param nodeTo destination node
	 */
	public Collection<VERTEX> getPredecessorVertices(VERTEX vertex);
	
	/** Returns the vertex having the 'destVertex' as a successor via the given 'edge'
	 * @param destVertex
	 * @param edge
	 * @return
	 */
	public VERTEX getPredecessorVertex(VERTEX destVertex, EDGE edge);
	
	public Collection<VERTEX> getSuccessorVertices(VERTEX vertex);
	
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
	public BreadthFirstIterator<VERTEX, EDGE> breadthFirstIterator(int maxCycle) throws NodeNotFoundException;
	
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
	public DepthFirstIterator<VERTEX, EDGE> depthFirstIterator(Collection<VERTEX> startVertex, int maxCycling)
		throws GraphException;
	
	public VERTEX addRootVertex(VERTEX vertex, boolean isRoot);
	
	/** Returns the type associated to the node.
	 *  The type is specified by a value defined in VertexType.
	 *  @return a value from VertexType
	 *  @exception GraphException if the node does not exists
	 */
	public VertexType getVertexType (VERTEX vertex);
	
}
