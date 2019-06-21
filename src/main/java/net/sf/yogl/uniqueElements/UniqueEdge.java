package net.sf.yogl.uniqueElements;

import net.sf.yogl.Edge;

public class UniqueEdge<EK extends Comparable<EK>, VK extends Comparable<VK>> extends Edge<UniqueVertex<VK, EK>>{
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
		if (anotherObject instanceof UniqueEdge<?, ?>) {
			@SuppressWarnings("unchecked")
			UniqueEdge<EK, VK> toVertex = (UniqueEdge<EK, VK>) anotherObject;
			result = this.getKey().equals(toVertex.getKey());
		}
		return result;
	}
}
