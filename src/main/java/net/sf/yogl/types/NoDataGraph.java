   
package net.sf.yogl.types;

import net.sf.yogl.adjacent.keyValue.KeyValueEdge;
import net.sf.yogl.adjacent.keyValue.KeyValueGraph;
import net.sf.yogl.adjacent.keyValue.KeyValueVertex;
import net.sf.yogl.exceptions.GraphException;

/** Simple graph which does not contains any data. Nodes and links
 *  are just identified by keys.
 *  This example shows how to hide the underlying types used to define the Vertex and the Edge.
 */
public final class NoDataGraph extends KeyValueGraph<String, Object, String, Object> {

	public NoDataGraph(){
		super();
	}

	public void tryAddNode(String nodeKey, boolean isRoot) throws GraphException{
		super.addRootVertex(new KeyValueVertex<String, Object, String, Object>(nodeKey, null), isRoot);
	}
	
	public void tryAddLinkFirst(String fromVertexKey, String toVertexKey, String edgeKey) {
		KeyValueVertex<String, Object, String, Object> fromVertex = super.vertices.get(fromVertexKey);
		KeyValueVertex<String, Object, String, Object> toVertex = super.vertices.get(toVertexKey);
		fromVertex.tryAddEdgeFirst(new KeyValueEdge<String, Object, String, Object>(toVertexKey, edgeKey, null, toVertex));
	}
}
