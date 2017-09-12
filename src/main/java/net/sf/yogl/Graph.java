package net.sf.yogl;

import java.util.Collection;

public interface Graph {

	/** Returns the max degree by only taking into account the incoming
	 *  edges
	 */
	public int getMaxInDegree();

	/** Returns the max degree by only taking into account the outgoing
	 *  edges
	 */
	public int getMaxOutDegree();

	/** Return the number of node contained in the graph. This
	 * number will include all types of nodes: entry nodes, as well as
	 * end nodes and 'regular' nodes
	 * @return number of nodes
	 */
	public int getNodeCount();

	/** Check if the graph is empty or not. That is, if the graph contains
	 * at least one node.
	 */
	public boolean isEmpty();
	
	/** By default, a new node added to a graph is a root
	 * 
	 */
	public void addNode(Vertex v);
	
	public Collection<Vertex>getRoots();
}
