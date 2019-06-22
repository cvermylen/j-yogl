package net.sf.yogl.uniqueElements;

import java.util.Collection;

import net.sf.yogl.VertexIntf;

public interface UniqueVertexIntf<VC extends UniqueVertexIntf<VC, TC, VK, EK>, TC extends UniqueEdgeIntf<TC, VC, EK, VK>, VK extends Comparable<VK>, EK extends Comparable<EK>> 
		extends VertexIntf<VC, TC>{

	@Override
	public Collection<TC> getOutgoingEdges();

	public TC getOutgoingEdge(EK edgeKey);
	
	@Override
	public void tryAddEdgeFirst(TC edge);

	@Override
	public void tryAddEdgeLast(TC edge);
	
	public void removeEdge(TC edge);
	
	public VK getKey();
	
}
