   
package net.sf.yogl.impl;

import net.sf.yogl.Edge;
import net.sf.yogl.Vertex;
import net.sf.yogl.adjacent.list.AdjListEdge;
import net.sf.yogl.adjacent.list.AdjListVertex;
import net.sf.yogl.exceptions.GraphException;

/**
 * Each edge is uniquely identified by the 'from' vertex and the return value of its 'hash' function.
 * This mean that 2 edges having the same 'from' vertex may not have the same 'hash' return value.
 */
public class DuplicateEdgeException extends GraphException{
	
	private Vertex pred;
	private Edge edge;
	
	/**
	 * Constructor for DuplicateLinkException.
	 */
	public DuplicateEdgeException(Vertex pred, Edge edge) {
		super();
		this.pred = pred;
		this.edge = edge;
	}

	/**
	 * Constructor for DuplicateEdgeException.
	 */
	public DuplicateEdgeException() {
		super();
	}

	/**
	 * Constructor for DuplicateEdgeException.
	 * @param arg0
	 * @param arg1
	 */
	public DuplicateEdgeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor for DuplicateEdgeException.
	 * @param arg0
	 */
	public DuplicateEdgeException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * Constructor for DuplicateEdgeException.
	 * @param s
	 */
	public DuplicateEdgeException(String s) {
		super(s);
	}

	/**
	 * Constructor for DuplicateLinkException.
	 * @param arg0
	 */
	public DuplicateEdgeException(AdjListVertex pred, AdjListEdge edge, Throwable arg0) {
		super(arg0);
		this.pred = pred;
		this.edge = edge;
	}

	public String toString(){
		return edge.toString();
	}
}
