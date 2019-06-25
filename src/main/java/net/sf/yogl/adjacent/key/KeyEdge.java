package net.sf.yogl.adjacent.key;

import net.sf.yogl.exceptions.NodeNotFoundException;

public class KeyEdge <EDGE_KEY extends Comparable<EDGE_KEY>, VERTEX_KEY extends Comparable<VERTEX_KEY>, EDGE extends KeyEdge<EDGE_KEY, VERTEX_KEY, EDGE, VERTEX>, VERTEX extends KeyVertex<VERTEX_KEY, EDGE_KEY, VERTEX, EDGE>>
extends InternalKeyEdge <EDGE, VERTEX, EDGE_KEY, VERTEX_KEY>{

	public KeyEdge() {
		super();
	}
	
	public KeyEdge(EDGE_KEY key, VERTEX toVertex) throws NodeNotFoundException {
		super(key, toVertex);
		// TODO Auto-generated constructor stub
	}

	public void cloneTo(KeyEdge<EDGE_KEY, VERTEX_KEY, EDGE, VERTEX> copyToEdge){
		super.cloneTo(copyToEdge);
	}
}
