package net.sf.yogl.uniqueElements;

import net.sf.yogl.EdgeIntf;

public interface UniqueEdgeIntf <EK extends Comparable<EK>, VK extends Comparable<VK>> extends EdgeIntf<UniqueVertexIntf<VK, EK>>{
	public EK getKey();
	
}
