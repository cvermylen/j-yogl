   
package net.sf.yogl.types;
/**
 * The VertexType is used to set up the type of each vertex regarding the
 * graph. From a graph perspective, we distinguish 3 types of nodes:
 * entry points, end nodes and between them intermediates nodes.
 */

public enum VertexType {
	/** intermediate vertex. Has predecessors AND successors */
	NONE,

	/** starting vertex in the graph. Has successors only, no predessors */
	START,

	/** indicates a final(terminating) vertex. Has predecessors only,
	 * no successors */
	END,

	/** If the graph contains only one vertex, this latter is a start & end
	 *  node
	 */
	STARTEND;
}
