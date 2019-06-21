package net.sf.yogl.uniqueElements;

import java.util.Collection;

import net.sf.yogl.VertexIntf;

public interface UniqueVertexIntf<VK extends Comparable<VK>, EK extends Comparable<EK>> extends VertexIntf<UniqueEdgeIntf<EK, VK>>{

	@Override
	public Collection<UniqueEdgeIntf<EK, VK>> getOutgoingEdges();

	@Override
	public void tryAddEdgeFirst(UniqueEdgeIntf<EK, VK> edge);

	@Override
	public void tryAddEdgeLast(UniqueEdgeIntf<EK, VK> edge);
	
	public void removeEdge(UniqueEdgeIntf<EK, VK> edge);
	
	public VK getKey();
	
}
