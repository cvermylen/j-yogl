package net.sf.yogl;

public abstract class Edge<EDGE extends Edge<EDGE, VERTEX>, VERTEX extends Vertex<VERTEX, EDGE>> {

	private VERTEX toVertex;
	
	private int visitCounts = 0;
	
	public Edge(VERTEX toVertex) {
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
}
