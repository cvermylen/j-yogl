package net.sf.yogl;

import java.util.Collection;

public interface Graph<V extends Vertex> {

	/** Returns the max degree by only taking into account the incoming
	 *  edges
	 */
//	public int getMaxInDegree();

	/** Returns the max degree by only taking into account the outgoing
	 *  edges
	 */
	public int getMaxOutDegree();

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
	
	public BreadthFirstIterator breadthFirstIterator(int maxCycle);
}
