
package net.sf.yogl.adjacent.list;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.sf.yogl.Vertex;
/**
 * The Vertex describes the node of the graph. It is a container for a
 * user defined object.
 *  - the 'value' field refers to this object
 *  - it also refers to a list with all outgoing edges.
 * Some additional status information is also contained:
 *  - an integer indicating the number of times this node has been visited
 *    by an algorithm
 *  - a boolean indicating if the vertex is free or not
 *  V = user value Object
 */

public class AdjListVertex <VERTEX_VALUE, EDGE_VALUE> extends Vertex<AdjListVertex<VERTEX_VALUE, EDGE_VALUE>, AdjListEdge<VERTEX_VALUE, EDGE_VALUE>> {
        
    private ArrayList<AdjListEdge<VERTEX_VALUE, EDGE_VALUE>> outgoingEdges = new ArrayList<>();
    
    VERTEX_VALUE userValue = null;
    
    public AdjListVertex(){
    	super();
    }
    
    public AdjListVertex(VERTEX_VALUE userValue){
    	super();
    	if(userValue == null) throw new IllegalArgumentException("Null paramter not allowed");
        this.userValue = userValue;
    }
    
    public void cloneTo(AdjListVertex<VERTEX_VALUE, EDGE_VALUE> copyToVertex){
        super.cloneTo(copyToVertex);
        copyToVertex.setFreeEntry(isFreeEntry());
        copyToVertex.setVisitCounts(getVisitsCount());
        copyToVertex.outgoingEdges = outgoingEdges;
    }
    
    public VERTEX_VALUE getUserValue(){
        return userValue;
    }
    
    public List<AdjListEdge<VERTEX_VALUE, EDGE_VALUE>> getOutgoingEdges(){
        
    	return this.outgoingEdges;
    }
    
    public AdjListEdge<VERTEX_VALUE, EDGE_VALUE> addLinkLast(AdjListEdge<VERTEX_VALUE, EDGE_VALUE> edge) {
    	return this.addEdgeLast(edge);
    }
    
    public AdjListEdge<VERTEX_VALUE, EDGE_VALUE> addEdgeLast(AdjListEdge<VERTEX_VALUE, EDGE_VALUE> newEdge){
        if (newEdge != null){
        	outgoingEdges.add(newEdge);
        }
        return newEdge;
    }
    
    public AdjListEdge<VERTEX_VALUE, EDGE_VALUE> addLinkFirst(AdjListEdge<VERTEX_VALUE, EDGE_VALUE> edge) {
    	return this.addEdgeFirst(edge);
    }
    
    public AdjListEdge<VERTEX_VALUE, EDGE_VALUE> addEdgeFirst(AdjListEdge<VERTEX_VALUE, EDGE_VALUE> newEdge){
        if (newEdge != null){
        	outgoingEdges.add(0, newEdge);
        }
        return newEdge;
    }

    public void removeEdge(AdjListEdge<VERTEX_VALUE, EDGE_VALUE> edge){
        if (edge != null){
            outgoingEdges.remove(edge);
        }
    }
    
    public void removeEdgeAt(int pos) {
    	outgoingEdges.remove(pos);
    }
    
    public void removeIf(Predicate<AdjListEdge<VERTEX_VALUE, EDGE_VALUE>> p){
    	outgoingEdges.removeIf(p);
    }
    
    public void clearEdges(){
        outgoingEdges.clear();
    }
    
	public void tryAddEdgeFirst(AdjListEdge<VERTEX_VALUE, EDGE_VALUE> edge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tryAddEdge(AdjListEdge<VERTEX_VALUE, EDGE_VALUE> edge) {
		// TODO Auto-generated method stub
		
	}
	
	public void tryAddEdgeLast(AdjListEdge<VERTEX_VALUE, EDGE_VALUE> edge) {
		// TODO Auto-generated method stub
		
	}
}
