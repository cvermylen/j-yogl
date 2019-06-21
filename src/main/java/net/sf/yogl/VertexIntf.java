package net.sf.yogl;

import java.util.Collection;

public interface VertexIntf<E extends EdgeIntf<?>> {

	public void setFreeEntry(boolean freeEntry);
	
	public boolean isFreeEntry();
	
	public void setVisitCounts(int visitCounts);
	
	public int getVisitsCount();
	
	public int incVisitCounts();
	
	public void clearVisitsCount();
	
	public int incrementIncomingEdges ();
	
	public int decrementIncomingEdges ();
	
	public Collection<E> getOutgoingEdges();
	
	public void tryAddEdgeFirst(E edge);
	
	public void tryAddEdgeLast(E edge);
}
