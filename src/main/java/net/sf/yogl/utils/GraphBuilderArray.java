   
package net.sf.yogl.utils;

import java.util.HashSet;
import java.util.Set;

import net.sf.yogl.Graph;
import net.sf.yogl.adjacent.keyValue.KeyValueEdge;
import net.sf.yogl.adjacent.keyValue.KeyValueGraph;
import net.sf.yogl.adjacent.keyValue.KeyValueVertex;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.types.NoDataGraph;

public class GraphBuilderArray {

	/** Utility method to create a simple graph containing no data.
	 *  The method uses the information in the 'srcData' to fill in
	 *  the graph.
	 * @param srcData is an array where each line describes a relation
	 *        between 2 nodes.
	 *        The element at [i][0] contains the source node id
	 *        The element at [i][1] contains the destination node id
	 *        The element at [i][2] contains the link id
	 * @param destGraph on entry, the graph must be empty.
	 * @throws GraphException
	 */
	public static void buildNoDataGraph(String[][] srcData, NoDataGraph destGraph) throws GraphException {
		if (destGraph.getNodeCount() > 0) {
			throw new GraphCorruptedException("The graph must be empty");
		}
		for (int i = 0; i < srcData.length; i++) {
			boolean isRoot = false;
			for (int j=0; j < srcData.length; i++) {
				if(srcData[j][1].contentEquals(srcData[i][0])) {
					isRoot = true;
					break;
				}
			}
			destGraph.tryAddNode(srcData[i][0], isRoot);
			destGraph.tryAddNode(srcData[i][1], false);
		}
		for (int i = 0; i < srcData.length; i++) {
			destGraph.tryAddLinkFirst(srcData[i][0], srcData[i][1], srcData[i][2]);
		}
	}

	/** Insert the content of the array into the graph.
	 * 
	 * @param srcData contains the information to build the graph. Each line
	 *        describes a node OR a link. If the line contains 2 elements, then
	 *        we have a node; a line with 4 elements describes a link.
	 *        The node line:
	 *        	Element at[i][0] contains the node id
	 *          Element at[i][1] contains the node data
	 *        The link line:
	 *          Element at[i][0] contains the source node id
	 *          Element at[i][1] contains the destination node id
	 *          Element at[i][2] contains the link id
	 *          Element at[i][3] contains the link data.
	 * @param destGraph
	 * @throws GraphException
	 */
	public static void buildGraph(String[][] srcData, Graph destGraph) throws GraphException {
		KeyValueGraph<String, String, String, String> pg = new KeyValueGraph<>();
		Set<String>nonRootVerticeKeys = new HashSet<>();
		for (int i=0; i < srcData.length; i++) {
			if (srcData[i].length == 4)
				nonRootVerticeKeys.add(srcData[i][1]);
		}
		for (int i = 0; i < srcData.length; i++) {
			if(srcData[i].length == 2){
				KeyValueVertex<String, String, String, String> vertex = new KeyValueVertex<>(srcData[i][0], srcData[i][1]);
				pg.addRootVertex(vertex, nonRootVerticeKeys.contains(srcData[i][0]));
			}
		}
		for (int i = 0; i < srcData.length; i++) {
			if(srcData[i].length == 4){
				KeyValueVertex<String, String, String, String> fromVertex = pg.getVertex(srcData[i][0]);
				KeyValueVertex<String, String, String, String> toVertex = pg.getVertex(srcData[i][1]);
				KeyValueEdge<String, String, String, String> edge = new KeyValueEdge<String, String, String, String>(srcData[i][1], srcData[i][2], srcData[i][3], toVertex);
				fromVertex.tryAddEdge(edge);
			}
		}
	}
}
