package net.sf.yogl;

public interface EdgeIntf <V extends VertexIntf<?>> {

	public <T extends V> T getToVertex();
	
	public void setToVertex(V vertex);
	
	public void incVisitCounts();

	public int getVisitsCount();
	
	public void setVisitsCount(int count);
	
	public void clearVisitsCount();
}
