package net.sf.yogl;

import net.sf.yogl.exceptions.NodeNotFoundException;

public abstract class Edge<EDGE extends Edge<EDGE, VERTEX>, VERTEX extends Vertex<VERTEX, EDGE>> {

	private VERTEX toVertex;
	
	private int visitCounts = 0;
	
	/** no-param ctor is required for generic util methods
	 * 
	 */
	public Edge() {}
	
	public Edge(VERTEX toVertex) throws NodeNotFoundException {
		if (toVertex == null) throw new NodeNotFoundException("Edge cannot refers to a null vertex");
		this.toVertex = toVertex;
	}
	
	public void setToVertex(VERTEX toVertex) {
		this.toVertex = toVertex;
	}
	
	public VERTEX getToVertex() {
		return toVertex;
	}
	
	public void incVisitCounts() {
		visitCounts++;
	}

	public int getVisitsCount(){
        return visitCounts;
    }
	
	public void setVisitsCount(int count){
		this.visitCounts = count;
	}
	
	public void clearVisitsCount(){
    	this.visitCounts = 0;
    }
	
	/** Edges are duplicated with a clone method
	 * 
	 */
	public <D_EDGE extends Edge<EDGE, VERTEX>> void cloneTo(D_EDGE copyToEdge){
		copyToEdge.setToVertex(this.getToVertex());
	}
}
