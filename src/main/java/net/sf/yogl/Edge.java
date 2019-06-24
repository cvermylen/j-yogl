package net.sf.yogl;

public abstract class Edge<TC extends Edge<TC, VC>, VC extends Vertex<VC, TC>> {

	private VC toVertex;
	
	private int visitCounts = 0;
	
	public Edge(VC toVertex) {
		this.toVertex = toVertex;
	}
	
	public VC getToVertex() {
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
