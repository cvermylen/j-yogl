package net.sf.yogl.adjacent.key;

import java.util.Collection;
import java.util.HashMap;

import net.sf.yogl.Vertex;

public abstract class InternalKeyVertex<VERTEX extends InternalKeyVertex<VERTEX, EDGE, VERTEX_KEY, EDGE_KEY>, EDGE extends InternalKeyEdge<EDGE, VERTEX, EDGE_KEY, VERTEX_KEY>, VERTEX_KEY extends Comparable<VERTEX_KEY>, EDGE_KEY extends Comparable<EDGE_KEY>> 
		extends Vertex<VERTEX, EDGE>{

	private VERTEX_KEY key;
	
	private HashMap<EDGE_KEY, EDGE> edges= new HashMap<>();

	public InternalKeyVertex(){
		super();
	}
	
	public InternalKeyVertex(VERTEX_KEY key) {
		super();
		this.key = key;
	}
	
	@Override
	public Collection<EDGE> getOutgoingEdges() {
		return edges.values();
	}

	public EDGE getOutgoingEdge(EDGE_KEY edgeKey) {
		return edges.get(edgeKey);
	}
	
	@Override
	public void tryAddEdge(EDGE edge) {
		if (!edges.containsKey(edge.getKey())) {
			edges.put(edge.getKey(), edge);
		}
	}
	
	public void removeEdge(EDGE edge){
		edges.remove(edge.getKey());
	}
	
	public VERTEX_KEY getKey() {
		return key;
	}
	
	public void cloneTo(InternalKeyVertex<VERTEX, EDGE, VERTEX_KEY, EDGE_KEY> copyToVertex){
		super.cloneTo(copyToVertex);
		copyToVertex.key = this.key;
		
	}
	
	@Override
	public boolean equals (Object anotherObject) {
		boolean result = false;
		if (anotherObject instanceof InternalKeyVertex<?, ?, ?, ?>) {
			@SuppressWarnings("unchecked")
			InternalKeyVertex<VERTEX, EDGE, VERTEX_KEY, EDGE_KEY> toVertex = (InternalKeyVertex<VERTEX, EDGE, VERTEX_KEY, EDGE_KEY>) anotherObject;
			result = this.getKey().equals(toVertex.getKey());
		}
		return result;
	}
}
