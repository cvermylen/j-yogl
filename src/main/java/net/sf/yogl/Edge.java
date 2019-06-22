package net.sf.yogl;

public abstract class Edge<TC extends EdgeIntf<TC, VC>, VC extends VertexIntf<VC, TC>> implements EdgeIntf<TC, VC>{

	private VC toVertex;
	
	private int visitCounts = 0;
	
	public VC getToVertex() {
		return toVertex;
	}
	
	public void setToVertex(VC vertex) {
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
