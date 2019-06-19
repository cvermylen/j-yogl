   
package net.sf.yogl.extras;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import net.sf.yogl.Edge;
import net.sf.yogl.Graph;
import net.sf.yogl.Vertex;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;

/** Utility class that help to create a new graph. Inserting nodes in a graph
 *  requires to known for each node if it has predecessors or successors or
 *  both. With this class, the graph is created in two steps. First nodes
 *  and links are stored in a local structure, and when all of them are present,
 *  the graph is populated. Local structures are used to determine the position
 *  of each node in the graph.
 */
public class PreparedGraph<V extends Vertex<E>, E extends Edge<V>, VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> {
    
    private BiFunction<VK, VV, V> vertexCtor;
    
    private BiFunction<EK, EV, E> edgeCtor;

	private HashSet<V> heads = new HashSet<>();
    
    private List<E> links = new LinkedList<>();
    
    private LinkedHashMap<VK, V> nodes = new LinkedHashMap<>();
    
    private Graph<V, E> destGraph;
    
    /** Creates new PrepareInsert */
    public PreparedGraph(Graph<V, E> destGraph, BiFunction<VK, VV, V> vertexCtor, BiFunction<EK, EV, E> edgeCtor) {
    	this.destGraph = destGraph;
    	this.vertexCtor = vertexCtor;
    	this.edgeCtor = edgeCtor;
    }
    
    /** CReates a new node if it does not already exists. The duplicate
     *  existence is detected by the 'equals' method.
     *  @param node is the new node to be inserted in the graph
     */
    public void tryAddNode(VK nodeKey, VV nodeValue) {
        if(!nodes.containsKey(nodeKey)){
        	V vertex = vertexCtor.apply(nodeKey, nodeValue);
            nodes.put(nodeKey, vertex);
            heads.add(vertex);
        }
    }
    
    public void tryAddLink(VK nodeKeyFrom, VK nodeKeyTo, EK edgeKey, EV edgeValue) throws NodeNotFoundException{
    	V fromVertex = nodes.get(nodeKeyFrom);
    	V toVertex = nodes.get(nodeKeyTo);
    	if (fromVertex == null || toVertex == null) throw new NodeNotFoundException((fromVertex == null)?(nodeKeyFrom.toString()):(nodeKeyTo.toString()));
    	E edge = edgeCtor.apply(edgeKey, edgeValue);
    	fromVertex.tryAddEdgeLast(edge);
    	edge.setToVertex(toVertex);
    	links.add(edge);
    }
    
    public void populateGraph() throws GraphException {
    	links.stream().forEach(edge -> heads.remove(edge.getToVertex()));
    	
    	Iterator<V> allVertexIter = nodes.values().iterator();
    	while (allVertexIter.hasNext()) {
    		V vertex = allVertexIter.next();
    		destGraph.tryAddVertex(vertex, heads.contains(vertex));
    	}
    }
}
