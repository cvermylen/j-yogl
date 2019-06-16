/* Copyright (C) 2003 Symphonic
   
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
   
package net.sf.yogl.adjacent.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import net.sf.yogl.BreadthFirstIterator;
import net.sf.yogl.Graph;
import net.sf.yogl.adjacent.keyMap.ComparableKeysGraph;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;

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

public final class AdjListGraph <V, E> implements Graph <AdjListVertex<V, E>> {

	/** Contains the list of all entry points in the graph.
	 *  When a new node is created, it is by default inserted into the set.
	 */
	private ArrayList<AdjListVertex<V, E>> roots = new ArrayList<>();

	private int outCount = 0;
	
	public AdjListGraph() throws GraphCorruptedException {
	}

	@Override
	public AdjListVertex<V, E> addRootVertex(AdjListVertex<V, E> v) {
		this.roots.add(v);
		return v;
	}

	public AdjListVertex<V, E> addRootNode(V v) {
		AdjListVertex<V, E> result = new AdjListVertex<>(v);
		this.roots.add(result);
		return result;
	}
	/**
	 * @param maxCycles fixes the number of times each node can be
	 *        visited. A value of 1 indicates that each node will be
	 *        returned max. 1 time, this is thus a way to avoid cycling.
	 *        Accepted values are: [1 .. n]. There is no way to express
	 *        an infinite value.
	 * @param startingNodeKey is the entry point used for the traversal. 
	 * 		If null, the algorithm will use all nodes marked as 'START'.
	 */
	@Override
	public BreadthFirstIterator breadthFirstIterator(
		int maxCycles)
		throws GraphException {
		return new BreadthFirstIterator(this, maxCycles);
	}

	/** @see ComparableKeysGraph#clone
	 */
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	/** @see ComparableKeysGraph#deepCopy
	 */
	public void deepCopy(Graph<AdjListVertex<V, E>> dest) throws GraphException {

		for(AdjListVertex<V, E> root: roots){
			dest.addRootVertex(root.deepCopy());
		}
	}

	/** @see ComparableKeysGraph#depthFirstIterator
	 */
	public AdjListDepthFirstIterator<V, E> depthFirstIterator(int maxCycling)
		throws GraphException {
		
		AdjListVertex<V, E> artificialRoot = new AdjListVertex<>();
		roots.stream().forEach(r -> {artificialRoot.addEdgeLast(new AdjListEdge<V, E>(r));});
		
		return new AdjListDepthFirstIterator<>(artificialRoot, this);
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
				AdjListVertex<V, E> target = edge.getOutgoingVertex();
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
		AdjListVertex<V, E> target = edge.getOutgoingVertex();
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
				AdjListVertex<V, E> target = edge.getOutgoingVertex();
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
}