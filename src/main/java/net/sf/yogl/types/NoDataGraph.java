   
package net.sf.yogl.types;

import net.sf.yogl.adjacent.keyValue.ValueEdge;
import net.sf.yogl.adjacent.keyValue.KeyValueGraph;
import net.sf.yogl.adjacent.keyValue.KeyValueVertex;
import net.sf.yogl.exceptions.GraphException;

/** Simple graph which does not contains any data. Nodes and links
 *  are just identified by keys.
 *  This example shows how to hide the underlying types used to define the Vertex and the Edge.
 */
public final class NoDataGraph extends KeyValueGraph<String, Object, String> {

	public NoDataGraph(){
		super();
	}

	public void tryAddNode(String nodeKey, boolean isRoot) throws GraphException{
		super.addRootVertex(new KeyValueVertex<String, Object, String>(nodeKey, null), isRoot);
	}
	
	public void tryAddLinkFirst(String fromVertexKey, String toVertexKey, String edgeValue) {
		KeyValueVertex<String, Object, String> fromVertex = super.vertices.get(fromVertexKey);
		KeyValueVertex<String, Object, String> toVertex = super.vertices.get(toVertexKey);
		fromVertex.tryAddEdgeFirst(new ValueEdge<String, Object, String>(edgeValue, toVertex));
	}
}
