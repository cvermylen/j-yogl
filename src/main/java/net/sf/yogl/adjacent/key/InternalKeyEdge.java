package net.sf.yogl.adjacent.key;

import net.sf.yogl.Edge;
import net.sf.yogl.exceptions.NodeNotFoundException;

public abstract class InternalKeyEdge<EDGE extends InternalKeyEdge<EDGE, VERTEX, EDGE_KEY, VERTEX_KEY>, VERTEX extends InternalKeyVertex<VERTEX, EDGE, VERTEX_KEY, EDGE_KEY>, EDGE_KEY extends Comparable<EDGE_KEY>, VERTEX_KEY extends Comparable<VERTEX_KEY>> 
		extends Edge<EDGE, VERTEX>{
	
	private EDGE_KEY key;
	
	public InternalKeyEdge(){
		super();
	}
	
	public InternalKeyEdge(EDGE_KEY key, VERTEX toVertex) throws NodeNotFoundException {
		super(toVertex);
		this.key = key;
	}
	
	public EDGE_KEY getKey() {
		return key;
	}
	
	public void cloneTo(InternalKeyEdge<EDGE, VERTEX, EDGE_KEY, VERTEX_KEY> copyToEdge){
		super.cloneTo(copyToEdge);
		copyToEdge.key = this.key;
	}
	
	@Override
	public boolean equals (Object anotherObject) {
		boolean result = false;
		if (anotherObject instanceof InternalKeyEdge<?, ?, ?, ?>) {
			@SuppressWarnings("unchecked")
			InternalKeyEdge<EDGE, VERTEX, EDGE_KEY, VERTEX_KEY> toVertex = (InternalKeyEdge<EDGE, VERTEX, EDGE_KEY, VERTEX_KEY>) anotherObject;
			result = this.getKey().equals(toVertex.getKey());
		}
		return result;
	}
}
