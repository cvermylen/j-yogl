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
   
package net.sf.yogl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;

/** Given a specific graph, this iterator returns the next vertex in a
 * depth first order. Iterators cannot be concurrent on the same graph.
 * This limitation is due to the local variables in vertices and edges
 * being modified during the traversal.
 */

public abstract class DepthFirstIterator<V extends Vertex<E>, E extends Edge<V>> {
    
	class LinearEdgesIterator<V extends Vertex<E>, E extends Edge<V>>{
		
		/** Reference to the vertex' node
		 */
		private V old_node = null;
		
		/** Reference to links exiting from this vertex
		 *  Order in this stack is extremely important.
		 *  It MUST follow the same order as the one in the
		 *  vStack.
		 */
		private LinkedList<E> outgoingEdges = new LinkedList<>();
		
		public LinearEdgesIterator(V node){
			this.old_node = node;
			outgoingEdges = node.getOutgoingEdges().stream().collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
		}

		public boolean hasMoreEdges() {
			return outgoingEdges.size() > 0;
		}
		
		public E nextEdge() {
			return outgoingEdges.pop();
		}
		
		/** getter method
		 * @return the vertex index
		 */
		public V getVertex(){
			return old_node;
		}
		
		public E peekEdge() {
			return outgoingEdges.peek();
		}
		
	}
	
	protected V currentVertex;
	
    /** The vStack contains all nodes in the order they are visited.
     * This stack is used to simulate the recursivity.
     * The Objects contained in the stack will be of type 'Vertex'
     */
    protected LinkedList<LinearEdgesIterator<V, E>> vStack = new LinkedList<>();
    
    /** Maximum number of times a node can be visited
     */
    protected int maxCycling;
    
    /** The ctor builds the iterator on the graph given as parameter.
     * This iterator works directly on the graph elements, not on a copy
     * of it.
     * @param graph is a valid, possibly decorated, concrete graph.
     */
    public DepthFirstIterator(Collection<V> roots)
    throws GraphException{
        
    	roots.stream().forEach(r -> {pushVertex(r);});
    }
    
    /** Used to iterate through the graph
     * @return true if the next() method will return something
     */
    public boolean hasNext(){
        
        return (vStack.size() > 0);
    }

    protected void moveToNextVertex(){
    	if(vStack.size() == 0){
    		return;
    	}
    	if(hasVertexAlreadyBeenVisited(vStack.peek())){
    		E e = getNextEdge();
    		currentVertex = e.getOutgoingVertex();
    	}else{
    		currentVertex = vStack.peek().getVertex();
    		currentVertex.incVisitCounts();
    	}
    }
    
    private boolean hasVertexAlreadyBeenVisited(LinearEdgesIterator<V, E> topVertex){
    	return topVertex == null || topVertex.getVertex().getVisitsCount() > 0;
    }
    
    private E getNextEdge() {
    	E result = null;
    	do {
    		LinearEdgesIterator<V, E> nextEdgeIter = vStack.peek();
    		if(!nextEdgeIter.hasMoreEdges()){
    			vStack.pop();
    		}else{
    			result = nextEdgeIter.nextEdge();
    			pushVertex(result.getOutgoingVertex());
    		}
    	}while(result == null && hasNext());
    	return result;
    }
    
    private void pushVertex(V vertex) {
    	vStack.addLast(new LinearEdgesIterator<V, E>(vertex));
    }
    
    /** This method is part of the Iterator interface. It is not
     * implemented here since we consider that many iterators
     * may run concurrently on the same graph
     */
    public void remove()
    throws UnsupportedOperationException, IllegalStateException{
        
        throw new UnsupportedOperationException("");
    }
    
    /** @return the list of all nodes defining the path between the current vertex
     * and the graph root
     * @throws NodeNotFoundException 
     */
    public List<V>nodePath() {
        
        return vStack.stream().map(r -> r.getVertex()).collect(Collectors.toList());
    }
    
    /** @return the list of all links used to access the current vertex
     */
    public List<E>linkPath() {
        
    	return vStack.stream().map(r -> r.peekEdge()).collect(Collectors.toList());
    }
}