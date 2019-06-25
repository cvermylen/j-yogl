package net.sf.yogl.adjacent.key;

import net.sf.yogl.Edge;
import net.sf.yogl.exceptions.NodeNotFoundException;

public class InternalKeyEdge<TC extends InternalKeyEdge<TC, VC, EK, VK>, VC extends InternalKeyVertex<VC, TC, VK, EK>, EK extends Comparable<EK>, VK extends Comparable<VK>> 
		extends Edge<TC, VC>{
	private EK key;
	
	public InternalKeyEdge(EK key, VC toVertex) throws NodeNotFoundException {
		super(toVertex);
		this.key = key;
	}
	
	public EK getKey() {
		return key;
	}
	
	@Override
	public boolean equals (Object anotherObject) {
		boolean result = false;
		if (anotherObject instanceof InternalKeyEdge<?, ?, ?, ?>) {
			@SuppressWarnings("unchecked")
			InternalKeyEdge<TC, VC, EK, VK> toVertex = (InternalKeyEdge<TC, VC, EK, VK>) anotherObject;
			result = this.getKey().equals(toVertex.getKey());
		}
		return result;
	}
}
