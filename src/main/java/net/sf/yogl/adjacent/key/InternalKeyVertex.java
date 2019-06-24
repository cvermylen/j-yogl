package net.sf.yogl.adjacent.key;

import java.util.Collection;
import java.util.HashMap;
import net.sf.yogl.Vertex;

public class InternalKeyVertex<VC extends InternalKeyVertex<VC, TC, VK, EK>, TC extends InternalKeyEdge<TC, VC, EK, VK>, VK extends Comparable<VK>, EK extends Comparable<EK>> 
		extends Vertex<VC, TC>{

	private VK key;
	
	private HashMap<EK, TC> edges= new HashMap<>();

	public InternalKeyVertex(VK key) {
		this.key = key;
	}
	
	@Override
	public Collection<TC> getOutgoingEdges() {
		return edges.values();
	}

	public TC getOutgoingEdge(EK edgeKey) {
		return edges.get(edgeKey);
	}
	
	@Override
	public void tryAddEdge(TC edge) {
		if (!edges.containsKey(edge.getKey())) {
			edges.put(edge.getKey(), edge);
		}
	}
	
	public void removeEdge(TC edge){
		edges.remove(edge.getKey());
	}
	
	public VK getKey() {
		return key;
	}
	
	@Override
	public boolean equals (Object anotherObject) {
		boolean result = false;
		if (anotherObject instanceof InternalKeyVertex<?, ?, ?, ?>) {
			@SuppressWarnings("unchecked")
			InternalKeyVertex<VC, TC, VK, EK> toVertex = (InternalKeyVertex<VC, TC, VK, EK>) anotherObject;
			result = this.getKey().equals(toVertex.getKey());
		}
		return result;
	}
}