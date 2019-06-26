
package net.sf.yogl.iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import net.sf.yogl.Edge;
import net.sf.yogl.Vertex;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;

/** Given a specific graph, this iterator returns the next vertex in a
 * depth first order. Iterators cannot be concurrent on the same graph.
 * This limitation is due to the local variables in vertices and edges
 * being modified during the traversal.
 */

public class DepthFirstIterator<VERTEX extends Vertex<VERTEX, EDGE>, EDGE extends Edge<EDGE, VERTEX>> implements Iterator<VERTEX> {
    
	protected class LinearEdgesIterator<V1 extends Vertex<V1, E1>, E1 extends Edge<E1, V1>>{
		
		/** Reference to the vertex' node
		 */
		private V1 old_node = null;
		
		/** Reference to links exiting from this vertex
		 *  Order in this stack is extremely important.
		 *  It MUST follow the same order as the one in the
		 *  vStack.
		 */
		private LinkedList<E1> outgoingEdges = new LinkedList<>();
		
		public LinearEdgesIterator(V1 node){
			this.old_node = node;
			outgoingEdges = node.getOutgoingEdges().stream().collect(Collectors.toCollection(LinkedList::new));
		}

		public boolean hasMoreEdges() {
			return outgoingEdges.size() > 0;
		}
		
		public E1 nextEdge() {
			return outgoingEdges.pop();
		}
		
		/** getter method
		 * @return the vertex index
		 */
		public V1 getVertex(){
			return old_node;
		}
		
		public E1 peekEdge() {
			return outgoingEdges.peek();
		}
	}
	
	protected VERTEX currentVertex;
	
	protected EDGE traversedEdge;
	
	public DepthFirstIterator(Collection<VERTEX> startNodes, int maxCycling) throws GraphException{
		this.maxCycling = maxCycling;
		startNodes.forEach(vertex -> pushVertex (vertex));
		moveToNextVertex();
	}
	
	public VERTEX next() throws NoSuchElementException {
		if (currentVertex == null)
			throw new NoSuchElementException();
		moveToNextVertex();
		popTheEdge();
		return currentVertex;
	}

    /** The vStack contains all nodes in the order they are visited.
     * This stack is used to simulate the recursivity.
     * The Objects contained in the stack will be of type 'Vertex'
     */
    protected LinkedList<LinearEdgesIterator<VERTEX, EDGE>> vStack = new LinkedList<>();
    
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
        return (vStack.size() > 1) || ((vStack.size() == 1) && (vStack.get(0).hasMoreEdges()));
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
    		pushVertex(getNextEdge().getToVertex());
    		traversedEdge = getNextEdge();
    	}
    	
    	currentVertex = (traversedEdge == null)?(vStack.getLast().getVertex()):(traversedEdge.getToVertex());
    	currentVertex.incVisitCounts();
    }
    
    protected void pushVertex(VERTEX v) {
    	if(v == null) throw new IllegalArgumentException("Cannot add null vertex to the graph");
    	vStack.addLast(new LinearEdgesIterator<VERTEX, EDGE>(v));
    }
    
    private boolean vertexHasOutgoingEdges(EDGE e) {
    	return e != null && e.getToVertex() != null && e.getToVertex().getOutgoingEdges().size() > 0;
    }
    
    private boolean vertexCanBeVisited(EDGE e){
    	return e != null && e.getToVertex() != null && e.getToVertex().getVisitsCount() >= maxCycling;
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
    
    private EDGE getNextEdge() {
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
    public List<VERTEX>nodePath() {
        
        return vStack.stream().map(r -> r.getVertex()).collect(Collectors.toList());
    }
    
    /** @return the list of all links used to access the current vertex
     */
    public List<EDGE>linkPath() {
        
    	return vStack.stream().map(r -> r.peekEdge()).collect(Collectors.toList());
    }
}