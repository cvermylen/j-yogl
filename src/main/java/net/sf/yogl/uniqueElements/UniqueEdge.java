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
}
