package net.sf.yogl;

public abstract class Edge {

	private int visitCounts = 0;
	
	public abstract Vertex getOutgoingVertex();
	
	public abstract Edge clone();
	
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
