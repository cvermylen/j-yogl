package net.sf.yogl.adjacent.key;


public class KeyVertex <VERTEX_KEY extends Comparable<VERTEX_KEY>, EDGE_KEY extends Comparable<EDGE_KEY>, VERTEX extends KeyVertex<VERTEX_KEY, EDGE_KEY, VERTEX, EDGE>, EDGE extends KeyEdge<EDGE_KEY, VERTEX_KEY, EDGE, VERTEX>>
extends InternalKeyVertex <VERTEX, EDGE, VERTEX_KEY, EDGE_KEY>{

	public KeyVertex(){
		super();
	}
	
	public KeyVertex(VERTEX_KEY key) {
		super(key);
		// TODO Auto-generated constructor stub
	}
	
	public void cloneTo(KeyVertex<VERTEX_KEY, EDGE_KEY, VERTEX, EDGE> copyToVertex) {
		super.cloneTo(copyToVertex);
	}
}
