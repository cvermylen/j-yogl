
package net.sf.yogl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.sf.yogl.exceptions.NodeNotFoundException;

/** Given a specific graph, this iterator returns the next vertex in a
 * depth first order. Iterators cannot be concurrent on the same graph.
 * This limitation is due to the local variables in vertices and edges
 * being modified during the traversal.
 */

public abstract class DepthFirstIterator<V extends Vertex<E>, E extends Edge<V>> {
    
	protected class LinearEdgesIterator<V extends Vertex<E>, E extends Edge<V>>{
		
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
	
	protected E traversedEdge;
	
	public abstract <T> T next();
	
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
    protected DepthFirstIterator(){
    }
    
    /** Used to iterate through the graph
     * @return true if the next() method will return something
     */
    public boolean hasNext(){
        
        return (vStack.size() > 0);
    }

    protected void moveToNextVertex(){
    	if(vStack == null) throw new IllegalArgumentException();
    	if(vStack.size() == 0){
    		return;
    	}
    	while(!vertexCanBeVisited(getNextEdge()) && hasMoreNodesToVisit()){
    		popTheEdge();
    	}
    	traversedEdge = getNextEdge();
    	while(vertexHasOutgoingEdges(getNextEdge()) && vertexCanBeVisited(getNextEdge())){
    		pushVertex(getNextEdge().getOutgoingVertex());
    		traversedEdge = getNextEdge();
    	}
//    	currentVertex = vStack.getLast().getVertex();
    	
    	currentVertex = (traversedEdge == null)?(vStack.getLast().getVertex()):(traversedEdge.getOutgoingVertex());
    	currentVertex.incVisitCounts();
    	
    }
    
    protected void pushVertex(V v) {
    	if(v == null) throw new IllegalArgumentException("Cannot add null vertex to the graph");
    	vStack.addLast(new LinearEdgesIterator<V, E>(v));
    }
    
    private boolean vertexHasOutgoingEdges(E e) {
    	return e != null && e.getOutgoingVertex() != null && e.getOutgoingVertex().getOutgoingEdges().size() > 0;
    }
    
    private boolean vertexCanBeVisited(E e){
    	return e != null && e.getOutgoingVertex() != null && e.getOutgoingVertex().getVisitsCount() >= maxCycling;
    }
    
    private boolean hasMoreNodesToVisit() {
    	return vStack.size() > 0 && vStack.get(0).hasMoreEdges();
    }
    
    protected void popTheEdge() {
    	if(vStack.getLast().hasMoreEdges()){
    		vStack.getLast().nextEdge();
    	}else{
    		vStack.removeLast();
    	}
    }
    
    private E getNextEdge() {
    	return vStack.getLast().peekEdge();
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