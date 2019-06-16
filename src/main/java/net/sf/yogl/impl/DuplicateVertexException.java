   
package net.sf.yogl.impl;

import net.sf.yogl.adjacent.list.AdjListVertex;
import net.sf.yogl.exceptions.GraphException;

/**
 * Each vertex whithin the graph must be unique. The vertex identifier is returned by the 'hash' method
 * (inherited from Object).This exception is thrown when the hash function of two vertices in the graph returns
 * the same value.
 */
public class DuplicateVertexException extends GraphException{
	private AdjListVertex vertex;
	
	/**
	 * Constructor for DuplicateVertexException.
	 */
	public DuplicateVertexException() {
		super();
	}

	/**
	 * Constructor for DuplicateVertexException.
	 * @param arg0
	 * @param arg1
	 */
	public DuplicateVertexException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor for DuplicateVertexException.
	 * @param arg0
	 */
	public DuplicateVertexException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * Constructor for DuplicateVertexException.
	 * @param s
	 */
	public DuplicateVertexException(String s) {
		super(s);
	}

	public DuplicateVertexException(AdjListVertex vertex){
		this.vertex = vertex;
	}
}
