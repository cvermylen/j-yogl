package net.sf.yogl.uniqueElements;

import net.sf.yogl.EdgeIntf;

public interface UniqueEdgeIntf <TC extends UniqueEdgeIntf<TC, VC, EK, VK>, VC extends UniqueVertexIntf<VC, TC, VK, EK>, EK extends Comparable<EK>, VK extends Comparable<VK>> 
		extends EdgeIntf<TC, VC>{
	public EK getKey();
	
}
