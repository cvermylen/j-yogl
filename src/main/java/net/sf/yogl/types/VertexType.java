   
package net.sf.yogl.types;
/**
 * The VertexType is used to set up the type of each vertex regarding the
 * graph. From a graph perspective, we distinguish 3 types of nodes:
 * entry points, end nodes and between them intermediates nodes.
 */

public class VertexType {
	/** intermediate vertex. Has predecessors AND successors */
	public static final int NONE = 1;

	/** starting vertex in the graph. Has successors only, no predessors */
	public static final int START = 2;

	/** indicates a final(terminating) vertex. Has predecessors only,
	 * no successors */
	public static final int END = 4;

	/** If the graph contains only one vertex, this latter is a start & end
	 *  node
	 */
	public static final int STARTEND = 8;
}
