   
package net.sf.yogl.types;

import java.util.HashMap;

import net.sf.yogl.adjacent.keyMap.AdjKeyEdge;
import net.sf.yogl.adjacent.keyMap.AdjKeyGraph;
import net.sf.yogl.adjacent.keyMap.AdjKeyVertex;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;

/** Simple graph which does not contains any data. Nodes and links
 *  are just identified by keys.
 *  This example shows how to hide the underlying types used to define the Vertex and the Edge.
 */
public final class NoDataGraph extends AdjKeyGraph<String, Object, String, Object> {

	public NoDataGraph()
			throws GraphCorruptedException {
		super(new HashMap<String, AdjKeyVertex<String, Object, String, Object>>());
	}

	public void tryAddNode(String nodeKey, boolean isRoot) throws GraphException{
		super.tryAddVertex(new AdjKeyVertex<String, Object, String, Object>(nodeKey, null), isRoot);
	}
	
	public void tryAddLinkFirst(String fromVertexKey, String toVertexKey, String edgeKey) {
		AdjKeyVertex<String, Object, String, Object> fromVertex = super.vertices.get(fromVertexKey);
		fromVertex.tryAddEdgeFirst(new AdjKeyEdge<String, Object, String, Object>(null, edgeKey, toVertexKey, null));
	}
}
