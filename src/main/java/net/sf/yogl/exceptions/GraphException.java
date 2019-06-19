   
package net.sf.yogl.exceptions;

import net.sf.yogl.Edge;
import net.sf.yogl.Graph;
import net.sf.yogl.Vertex;

/**
 * 'GraphException' is the root class for all exceptions thrown by graph methods. The purpose of this class
 * is just to make more easy the handling of exceptions by the graph user.
 */
public class GraphException extends Exception {

	private static final long serialVersionUID = 8963356757643065040L;

	private Graph graph;
	
	/**
	 * Constructor for GraphException.
	 */
	public GraphException() {
		super();
	}

	/**
	 * Constructor for GraphException.
	 * @param arg0
	 * @param arg1
	 */
	public GraphException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor for GraphException.
	 * @param arg0
	 */
	public GraphException(Throwable arg0) {
		super(arg0);
	}

	public GraphException(String s){
		super(s);
	}
	/**
	 * @return
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * @param graph
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

}
