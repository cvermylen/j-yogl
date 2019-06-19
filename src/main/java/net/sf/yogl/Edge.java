package net.sf.yogl;

public abstract class Edge<V extends Vertex<?>> {

	private V toVertex;
	
	private int visitCounts = 0;
	
	public V getToVertex() {
		return toVertex;
	}
	
	public void setToVertex(V vertex) {
		this.toVertex = vertex;
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
