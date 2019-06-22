package net.sf.yogl;

public interface EdgeIntf <TC extends EdgeIntf<TC, VC>, VC extends VertexIntf<VC, TC>> {

	public VC getToVertex();
	
	public void setToVertex(VC vertex);
	
	public void incVisitCounts();

	public int getVisitsCount();
	
	public void setVisitsCount(int count);
	
	public void clearVisitsCount();
}
