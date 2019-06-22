package net.sf.yogl;

import java.util.Collection;

public interface VertexIntf<VC extends VertexIntf<VC, TC>, TC extends EdgeIntf<TC, VC>> {

	public void setFreeEntry(boolean freeEntry);
	
	public boolean isFreeEntry();
	
	public void setVisitCounts(int visitCounts);
	
	public int getVisitsCount();
	
	public int incVisitCounts();
	
	public void clearVisitsCount();
	
	public int incrementIncomingEdges ();
	
	public int decrementIncomingEdges ();
	
	public Collection<TC> getOutgoingEdges();
	
	public void tryAddEdgeFirst(TC edge);
	
	public void tryAddEdgeLast(TC edge);
}
