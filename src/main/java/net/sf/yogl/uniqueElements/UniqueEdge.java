package net.sf.yogl.uniqueElements;

import net.sf.yogl.Edge;

public class UniqueEdge<TC extends UniqueEdgeIntf<TC, VC, EK, VK>, VC extends UniqueVertexIntf<VC, TC, VK, EK>, EK extends Comparable<EK>, VK extends Comparable<VK>> 
		extends Edge<TC, VC>{
	private EK key;
	
	public UniqueEdge(EK key) {
		this.key = key;
	}
	
	public EK getKey() {
		return key;
	}
	
	@Override
	public boolean equals (Object anotherObject) {
		boolean result = false;
		if (anotherObject instanceof UniqueEdge<?, ?, ?, ?>) {
			@SuppressWarnings("unchecked")
			UniqueEdge<TC, VC, EK, VK> toVertex = (UniqueEdge<TC, VC, EK, VK>) anotherObject;
			result = this.getKey().equals(toVertex.getKey());
		}
		return result;
	}
}
