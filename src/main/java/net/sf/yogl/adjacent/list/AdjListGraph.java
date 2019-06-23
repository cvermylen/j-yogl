
package net.sf.yogl.adjacent.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import net.sf.yogl.BreadthFirstIterator;
import net.sf.yogl.DepthFirstIterator;
import net.sf.yogl.Graph;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.types.VertexType;

/**
 * Adjacency list-based graph. Vertices are stored in a resizeable
 * vector and edges are stored in double linked lists.
 * Each vertex contain one user-defined object(called nodes). Objects of this type
 * must be unique in the graph, since they can be retrieved based on
 * their value. The equals function must be overriden.
 * Nodes are connected between each other by 'links'. Nodes are user-defined
 * Currently two vertices can be linked by
 * maximum one edge. This limitation can be removed if E provides
 * the 'equals' function.
 * Preconditions: the user-defined object must define the following methods:
 *  - boolean equals(Object node); that performs a comparison based on the
 *                                final type of the object and the value of
 *                                that object.
 *  - String toString(); returns stringified representation of the object.
 *  - Object clone(); duplicate this object
 *
 * @version 1.0
 */

public final class AdjListGraph <V, E> implements Graph <AdjListVertex<V, E>, AdjListEdge<V, E>> {

	/** Contains the list of all entry points in the graph.
	 *  When a new node is created, it is by default inserted into the set.
	 */
	private ArrayList<AdjListVertex<V, E>> roots = new ArrayList<>();

	private int outCount = 0;
	
	public AdjListGraph() {
	}

	
	public AdjListVertex<V, E> addRootVertex(AdjListVertex<V, E> v) {
		this.roots.add(v);
		return v;
	}

	@Override
	public AdjListVertex<V, E> tryAddVertex(AdjListVertex<V, E> vertex, boolean isRoot) {
		if (isRoot)
			this.roots.add(vertex);
		return vertex;
	}
	
	@Override
	public boolean isStartVertex (AdjListVertex<V, E> vertex) {
		return roots.contains(vertex);
	}
	
	/**
	 * @param maxCycles fixes the number of times each node can be
	 *        visited. A value of 1 indicates that each node will be
	 *        returned max. 1 time, this is thus a way to avoid cycling.
	 *        Accepted values are: [1 .. n]. There is no way to express
	 *        an infinite value.
	 * @param startingNodeKey is the entry point used for the traversal. 
	 * 		If null, the algorithm will use all nodes marked as 'START'.
	 * @throws NodeNotFoundException 
	 */
	@Override
	public BreadthFirstIterator<AdjListVertex<V, E>, AdjListEdge<V, E>> breadthFirstIterator(
		int maxCycles) throws NodeNotFoundException{
		return new BreadthFirstIterator<AdjListVertex<V, E>, AdjListEdge<V, E>>(this, maxCycles);
	}

	/** @see ComparableKeysGraph#clone
	 */
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	/** @see ComparableKeysGraph#deepCopy
	 */
	public void deepCopy(Graph<AdjListVertex<V, E>, AdjListEdge<V, E>> dest) throws GraphException {

		for(AdjListVertex<V, E> root: roots){
			dest.tryAddVertex(root.deepCopy(), true);
		}
	}

	/** @see ComparableKeysGraph#depthFirstIterator
	 */
	@Override
	public DepthFirstIterator<AdjListVertex<V, E>, AdjListEdge<V, E>> depthFirstIterator(Collection<AdjListVertex<V, E>> startVertices, int maxCycling)
		throws GraphException {
		
		return new DepthFirstIterator<AdjListVertex<V, E>, AdjListEdge<V, E>>(startVertices, maxCycling);
	}

	@Override
	public Collection<AdjListVertex<V, E>> getRoots() {
		return roots;
	}
	
	@Override
	public boolean isEmpty() {
		return this.roots.isEmpty();
	}

	public void traverse(Consumer<V>nodeConsumer, Consumer<E>linkConsumer){
		for(AdjListVertex<V, E> root: roots){
			consumeNode(nodeConsumer, linkConsumer, root);
		}
	}
	
	private void traverseNode(AdjListVertex<V, E> node, Consumer<V>nodeConsumer, Consumer<E>linkConsumer){
		for(AdjListEdge<V, E> edge: node.getOutgoingEdges()){
			if(edge.getVisitsCount() == 0) {
				linkConsumer.accept(edge.getUserValue());
				AdjListVertex<V, E> target = edge.getToVertex();
				consumeNode(nodeConsumer, linkConsumer, target);
				edge.incVisitCounts();
			}
		}
	}

	private void consumeNode(Consumer<V> nodeConsumer, Consumer<E> linkConsumer, AdjListVertex<V, E> target) {
		if(target.getVisitsCount() == 0){
			nodeConsumer.accept(target.getUserValue());
			traverseNode(target, nodeConsumer, linkConsumer);
			target.incVisitCounts();
		}
	}
	
	public void clearAllVisitCounts(){
		List<AdjListVertex<V, E>>allNodes = new ArrayList<>();
		List<AdjListEdge<V, E>>allEdges = new ArrayList<>();
		for(AdjListVertex<V, E> root: roots){
			clearVertexAndSubgraph(allNodes, allEdges, root);
		}
		allNodes.stream().forEach(v -> v.clearVisitsCount());
		allEdges.stream().forEach(e -> e.clearVisitsCount());
	}
	
	private void clear(AdjListVertex<V, E> node, List<AdjListVertex<V, E>>allNodes, List<AdjListEdge<V, E>>allEdges){
		for(AdjListEdge<V, E> edge: node.getOutgoingEdges()){
			if(edge.getVisitsCount() != Integer.MAX_VALUE) {
				clearEdgeAndSubgraph(allNodes, allEdges, edge);
			}
		}
	}

	private void clearEdgeAndSubgraph(List<AdjListVertex<V, E>> allNodes, List<AdjListEdge<V, E>> allEdges,
			AdjListEdge<V, E> edge) {
		
		edge.setVisitsCount(Integer.MAX_VALUE);
		allEdges.add(edge);
		AdjListVertex<V, E> target = edge.getToVertex();
		clearVertexAndSubgraph(allNodes, allEdges, target);
	}

	private void clearVertexAndSubgraph(List<AdjListVertex<V, E>> allNodes, List<AdjListEdge<V, E>> allEdges,
			AdjListVertex<V, E> target) {
		
		if(target.getVisitsCount() != Integer.MAX_VALUE){
			target.setVisitCounts(Integer.MAX_VALUE);
			clear(target, allNodes, allEdges);
			allNodes.add(target);
		}
	}
	
	public synchronized int getMaxOutDegree() {
		
		for(AdjListVertex<V, E> root: roots){
			consumeVertex(root, v -> {outCount = Math.max(outCount, v.getOutgoingEdges().size());}, (e)->{;});
		}
		clearAllVisitCounts();
		return outCount;
	}
	
	private void traverseVertex(AdjListVertex<V, E> node, Consumer<AdjListVertex<V, E>>vertexConsumer, Consumer<AdjListEdge<V, E>>edgeConsumer){
		for(AdjListEdge<V, E> edge: node.getOutgoingEdges()){
			if(edge.getVisitsCount() == 0) {
				edgeConsumer.accept(edge);
				AdjListVertex<V, E> target = edge.getToVertex();
				consumeVertex(target, vertexConsumer, edgeConsumer);
				edge.incVisitCounts();
			}
		}
	}

	private void consumeVertex(AdjListVertex<V, E> target, Consumer<AdjListVertex<V, E>> nodeConsumer, Consumer<AdjListEdge<V, E>> linkConsumer) {
		if(target.getVisitsCount() == 0){
			traverseVertex(target, nodeConsumer, linkConsumer);
			nodeConsumer.accept(target);
			target.incVisitCounts();
		}
	}

	@Override
	public int getMaxInDegree() {
		return this.roots.size();
	}

	@Override
	public int getNodeCount() {
		throw new RuntimeException("Not yet implemented");
	}


	@Override
	public int getLinkCount() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Collection<AdjListVertex<V, E>> getVertices(VertexType type) throws GraphException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Collection<AdjListVertex<V, E>> getPredecessorVertices(AdjListVertex<V, E> vertex) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public AdjListVertex<V, E> getPredecessorVertex(AdjListVertex<V, E> destVertex, AdjListEdge<V, E> edge) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Collection<AdjListVertex<V, E>> getSuccessorVertices(AdjListVertex<V, E> vertex) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public VertexType getVertexType(AdjListVertex<V, E> vertex) {
		// TODO Auto-generated method stub
		return null;
	}
}