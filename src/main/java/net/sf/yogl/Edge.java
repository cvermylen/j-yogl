package net.sf.yogl;

public abstract class Edge<V extends Vertex<?>> {

	private int visitCounts = 0;
	
	public abstract V getOutgoingVertex();
	
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
