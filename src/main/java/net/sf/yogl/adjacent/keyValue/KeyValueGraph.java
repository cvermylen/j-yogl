  
package net.sf.yogl.adjacent.keyValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.yogl.Graph;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.iterators.BreadthFirstIterator;
import net.sf.yogl.iterators.DepthFirstIterator;
import net.sf.yogl.iterators.LinksIterator;
import net.sf.yogl.types.VertexType;

/**
 * Adjacency list-based graph. Vertices are stored in a resizeable
 * vector and edges are stored in double linked lists.
 * Each vertex contain one user-defined object(called nodes). Objects of this type
 * must be unique in the graph, since they can be retrieved based on
 * their value. The equals function must be overriden.
 * Nodes are connected between each other by 'links'. Nodes are user-defined
 * Currently two vertices can be linked by
 * maximum one edge. This limitation can be removed if E provides
 * the 'equals' function.
 * Preconditions: the user-defined object must define the following methods:
 *  - boolean equals(Object node); that performs a comparison based on the
 *                                final type of the object and the value of
 *                                that object.
 *  - String toString(); returns stringified representation of the object.
 *  - Object clone(); duplicate this object
 *
 * @version 1.0
 */

public class KeyValueGraph <VERTEX_KEY extends Comparable<VERTEX_KEY>, VERTEX_VALUE, EDGE_VALUE> 
implements Graph <KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>, ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> {

	/** Since it should be too tedious to traverse the whole graph
	 * just to count the number of edges, this data is stored here.
	 */
	private int numberOfEdges = 0;

	/** 'vertices' contains all vertices of the graph. This vector
	 * is automatically resized when all entries are occupied.
	 */
	protected Map<VERTEX_KEY, KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> vertices = new HashMap<>();

	/** Contains the list of all entry points in the graph.
	 *  When a new node is created, it is by default inserted into the set.
	 */
	private HashSet<VERTEX_KEY> allStartNodeKeys = new HashSet<VERTEX_KEY>();

	/** basic ctor, no default value
	 */
	public KeyValueGraph(){}

	/** Will duplicate the content of the vertex and insert it into this graph.
	 * Method is used to copy vertices from graph to graph
	 */
	public KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> addRootVertex (KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex, boolean isRoot) {
		KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> result = null;
		if(!existsNode(vertex.getKey())) {
			this.vertices.put(vertex.getKey(), vertex);
			if (isRoot)
				this.allStartNodeKeys.add(vertex.getKey());
		}
		return result;
	}
	
	/**
	 * @param maxCycles fixes the number of times each node can be
	 *        visited. A value of 1 indicates that each node will be
	 *        returned max. 1 time, this is thus a way to avoid cycling.
	 *        Accepted values are: [1 .. n]. There is no way to express
	 *        an infinite value.
	 * @param startingNodeKey is the entry point used for the traversal. 
	 * 		If null, the algorithm will use all nodes marked as 'START'.
	 */
	@Override
	public BreadthFirstIterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>, ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> breadthFirstIterator(
			int maxCycles) throws NodeNotFoundException {
		return new BreadthFirstIterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>, ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>>(this, maxCycles);
	}

	/** @see ComparableKeysGraph#clone
	 */
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	/** @see ComparableKeysGraph#depthFirstIterator
	 */
	public DepthFirstIterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>, ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> depthFirstIterator(Collection<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> startVertices, 
			int maxCycling)
		throws GraphException {
		return new DepthFirstIterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>, ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>>(startVertices, maxCycling);
	}

	/** @see ComparableKeysGraph#existsNode
	 */
	public boolean existsNode(VERTEX_KEY nodeKey) {
		return vertices.containsKey(nodeKey);
	}

	/** Finds the node in 'vertices' and, if it exists, return the
	 *  corresponding Vertex from vertices. If it doesn't, throw an exception.
	 *  pre-condition: node is non-null
	 * @param node refers to the user-defined node object
	 * @exception VertexNotFoundException  thrown if rhs was not
	 *            found in the graph.
	 */
	public KeyValueVertex<VERTEX_KEY,VERTEX_VALUE, EDGE_VALUE> findVertexByKey(VERTEX_KEY nodeKey)
		throws NodeNotFoundException {
		if (!vertices.containsKey(nodeKey))
			throw new NodeNotFoundException(nodeKey.toString());
		return vertices.get(nodeKey);
	}

	/** Returns a matrix of boolean values, where each node is at the
	 * same position as in the array returned by the 'getNodes' method
	 */
	public boolean[][] getAdjacencyMatrix() throws GraphException {
		throw new GraphException("Not yet implemented");
	}

	/** Get the list of all outgoing links
	 * @return the list of all user-defined links joigning the 2 nodes
	 * @param node the user-defined node we wants to retrieve all links
	 *        starting from
	 * @exception VertexNotFoundException if 1 of the 2 nodes does not
	 *            exists.
	 */
	public List<ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> getLinksKeysBetween(KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> nodeFromKey, KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> nodeToKey)
		throws NodeNotFoundException {

		ArrayList<ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> result = new ArrayList<>();
		Iterator<ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> edgesIter = nodeFromKey.getOutgoingEdges().iterator();
		while (edgesIter.hasNext()) {
			ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> edge = edgesIter.next();
			if (edge.getToVertex().equals(nodeToKey)) {
				result.add(edge);
			}
		}
		return result;
	}

	/** Re-Build the list of all entry points in the graph.
	 *  An entry point is a node with no 'predecessor'.
	 */
	private void buildAllStartNodeKeys() {
		allStartNodeKeys.addAll(vertices.keySet());
		Iterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> entriesIter = vertices.values().iterator();
		while (entriesIter.hasNext()) {
			Iterator<ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> successorsIter = entriesIter.next().getOutgoingEdges().iterator();
			while (successorsIter.hasNext()) {
				KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> next = successorsIter.next().getToVertex();
				if (allStartNodeKeys.contains(next)) {
					allStartNodeKeys.remove(next);
				}
			}
		}
	}

	/** Build the list of all entry points in the graph.
	 *  An entry point is a node with no 'predecessor'.
	 * @return an array of node keys.
	 */
	public Collection<VERTEX_KEY> getAllStartNodeKeys() {
		return allStartNodeKeys;
	}

	/** @return true if the node is an entry point in the graph.
	 * @param nodeKey identify the node.
	 */
	@Override
	public boolean isRootVertex(KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> nodeKey) {
		return allStartNodeKeys.contains(nodeKey.getKey());
	}

	/** Return the depth of the graph. This method may perform a full
	 * traversal of the graph (depend on the implementation). The result
	 * can be interpreted as the maximum distance between the root node
	 * and any other accessible node.
	 * The root node is at level 0.
	 */
	public int getDepth(VERTEX_KEY startingNode) throws GraphException {
		throw new GraphException("Not yet implemented");
	}

	/** Getter method
	 * @return the total number of edges defined in the graph
	 */
	public int getLinkCount() {
		return numberOfEdges;
	}

	/** Getter method
	 * @return the total number of vertices contained in the graph
	 */
	public int getNodeCount() {
		return this.vertices.size();
	}

	/** @see ComparableKeysGraph#getNodeKeys
	 */
	public Set<VERTEX_KEY> getNodesKeys() throws GraphCorruptedException {
		return vertices.keySet();
	}

	/** @see ComparableKeysGraph#getNodes
	 */
	public List<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> getVertices(VertexType type) throws NodeNotFoundException {

		ArrayList<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> list = new ArrayList<>();
		Iterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> iter = vertices.values().iterator();
		while (iter.hasNext()) {
			KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex = iter.next();
			if (type.equals(VertexType.ANY) || type.equals(getVertexType(vertex))) {

				if ((getVertexType(vertex) == VertexType.START) || (getVertexType(vertex) == VertexType.STARTEND)) {
					list.add(0, vertex);
				} else {
					list.add(vertex);
				}
			}
		}
		return list;
	}

	public KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> getVertex (VERTEX_KEY key) {
		return this.vertices.get(key);
	}
	
	/** @see ComparableKeysGraph#getType
	 */
	public VertexType getVertexType(KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex) {
		if (this.allStartNodeKeys.contains(vertex.getKey())) {
			if ((vertex.getOutgoingEdges() == null)
				|| (vertex.getOutgoingEdges().size() == 0)) {
				return VertexType.STARTEND;
			} else {
				return VertexType.START;
			}
		} else {
			if ((vertex.getOutgoingEdges() == null)
				|| (vertex.getOutgoingEdges().size() == 0)) {
				return VertexType.END;
			} else {
				return VertexType.NONE;
			}
		}
	}

	/** @see ComparableKeysGraph#getNodeValue
	 */
	public VERTEX_VALUE getNodeValue(VERTEX_KEY nodeKey) {
		KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> v = this.vertices.get(nodeKey);
		if (v == null)
			return null;
		return v.getUserValue();
	}

	/** return all predecessor nodes to a given node.
	 */
	public Collection<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> getPredecessorVertices(KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> toVertex) {

		ArrayList<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> result = new ArrayList<>();
		Iterator<Map.Entry<VERTEX_KEY, KeyValueVertex<VERTEX_KEY,VERTEX_VALUE,EDGE_VALUE>>>iter = vertices.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<VERTEX_KEY, KeyValueVertex<VERTEX_KEY,VERTEX_VALUE,EDGE_VALUE>> entry = iter.next();
			KeyValueVertex<VERTEX_KEY,VERTEX_VALUE,EDGE_VALUE> vertex = entry.getValue();
			if (null != (vertex.getEdgeTo(toVertex.getKey()))) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	/** Return all predecessor nodes for a given node and link
	 */
	public KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> getPredecessorVertex(KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> destVertex, ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> edge) {
		KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> sourceVertex = null;
		Iterator<Map.Entry<VERTEX_KEY, KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>>> iter = vertices.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<VERTEX_KEY, KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> entry = iter.next();
			KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex = entry.getValue();
			Iterator<ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> edgesIter = vertex.getOutgoingEdges().iterator();
			while (edgesIter.hasNext()) {
				ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> testedEdge = edgesIter.next();
				if (testedEdge.equals(edge)) {
					sourceVertex = vertex;
					break;
				}
			}
		}
		return sourceVertex;
	}

	/** Returns a list of Vertex which are directly adjacent to the node.
	 */
	public List<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> getSuccessorVertices(KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> node) {
		Iterator<ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> edgesIter = node.getOutgoingEdges().iterator();
		List<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>>result = new ArrayList<>();
		while (edgesIter.hasNext()) {
			result.add(edgesIter.next().getToVertex());
		}
		return result;
	}

	/** @see ComparableKeysGraph#getPassage
	 */
	public int getVisitCount(VERTEX_KEY nodeKey) throws GraphException {

		KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex = vertices.get(nodeKey);
		return vertex.getVisitsCount();
	}

	/** @see ComparableKeysGraph#incPassage
	 */
	public int incVisitCount(VERTEX_KEY nodeKey) throws GraphException {

		KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex = vertices.get(nodeKey);
		return vertex.incVisitCounts();
	}

	/** Method to check the contents of the graph
	 * @return true if the graph does not contains any node
	 */

	public boolean isEmpty() {
		return this.vertices.isEmpty();
	}

	/** @see graph.ComparableKeysGraph#linksIterator
	 */
	public LinksIterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>, ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> linksKeysIterator() throws GraphException {

		return new LinksIterator<>(this);
	}

	/** Method to retrieve all nodes, via an iterator. Return the 'user value'
	 */
	public Collection<VERTEX_KEY> nodesKeySet() {

		LinkedList<VERTEX_KEY> nodes = new LinkedList<>();
		Iterator<VERTEX_KEY> vertexIter = vertices.keySet().iterator();
		while (vertexIter.hasNext()) {
			VERTEX_KEY key = vertexIter.next();
			nodes.addLast(key);
		}
		return nodes;
	}

	/** Method to retrieve all nodes, via an iterator. Return the 'user value'
	 */
	public Collection<VERTEX_VALUE> nodesValues() {

		LinkedList<VERTEX_VALUE> nodes = new LinkedList<>();
		Iterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> vertexIter = vertices.values().iterator();
		while (vertexIter.hasNext()) {
			KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex = vertexIter.next();
			nodes.addLast(vertex.getUserValue());
		}
		return nodes;
	}

	/** @see ComparableKeysGraph#linksValues()
	 */
	public Collection<EDGE_VALUE> linksValues() {
		LinkedList<EDGE_VALUE> v = new LinkedList<>();
		Iterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> verticesIter = vertices.values().iterator();
		while (verticesIter.hasNext()) {
			KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex = verticesIter.next();
			Iterator<ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> edgesIter = vertex.getOutgoingEdges().iterator();
			while (edgesIter.hasNext()) {
				v.add(edgesIter.next().getUserValue());
			}
		}
		return v;
	}

	/** Removes all links between the given nodes.
	 */
	public void removeAllLinksBetween(VERTEX_KEY nodeKeyFrom, VERTEX_KEY nodeKeyTo)
		throws GraphException {
		KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> from = this.vertices.get(nodeKeyFrom);
		Iterator<ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> edgesIter = from.getEdgeTo(nodeKeyTo).iterator();
		while (edgesIter.hasNext()) {
			from.removeEdge(edgesIter.next());
			numberOfEdges--;
		}
		buildAllStartNodeKeys();
	}

	/** Method to remove a link between two nodes.
	 * precondition: node1, node2 and link are non null.
	 * @param node1 originating vertex
	 * @param node2 destination vertex
	 * @param link between node1 and node2 to be removed
	 */
	public void removeLink(
		VERTEX_KEY nodeFromKey,
		VERTEX_KEY nodeToKey,
		ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> linkKey)
		throws GraphException {

		KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertexFrom = vertices.get(nodeFromKey);
		vertexFrom.removeEdge(linkKey);

		numberOfEdges--;
		buildAllStartNodeKeys();
	}

	/** This method will mark the vertex as unused. The list of neighbors
	 * is flushed. There is (currently) no reference counting associated
	 * to the vertex. As a consequence, if a vertex is not accessible
	 * anymore, it is not removed. Also, all incoming edges to the
	 * removed vertex are removed.
	 * @param node user-defined to be removed
	 */
	public void removeNode(VERTEX_KEY nodeKey) throws NodeNotFoundException {

		KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> v = this.vertices.get(nodeKey);
		this.numberOfEdges -= v.getCountEdges();
		//remove all links to the node
		Iterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> vertexIter = vertices.values().iterator();
		while (vertexIter.hasNext()) {
			KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex = vertexIter.next();
			Iterator<ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> edgesIter = vertex.getEdgeTo(nodeKey).iterator();
			while (edgesIter.hasNext()) {
				vertex.removeEdge(edgesIter.next());
				this.numberOfEdges--;
			}
		}
		this.vertices.remove(nodeKey);
		buildAllStartNodeKeys();
	}

	/** @see ComparableKeysGraph#setPassage
	 */
	public void setAllVisitCounts(int count) throws GraphException {
		Iterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> nodeValuesIter = this.vertices.values().iterator();
		while (nodeValuesIter.hasNext()) {
			KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex = nodeValuesIter.next();
			vertex.setVisitCounts(count);
		}
	}

	/** @see ComparableKeysGraph#setPassage
	 */
	public void setVisitCount(VERTEX_KEY nodeKey, int count)
		throws GraphException {
		KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex = vertices.get(nodeKey);
		vertex.setVisitCounts(count);
	}

	public int getMaxInDegree() {
		int max = 0;
		HashMap<VERTEX_KEY, Integer> counts = new HashMap<>();
		Iterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> verticesIter = vertices.values().iterator();
		while(verticesIter.hasNext()){
			KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex = verticesIter.next();
			Iterator<ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>>edgesIter = vertex.getOutgoingEdges().iterator();
			while(edgesIter.hasNext()){
				KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex2 = edgesIter.next().getToVertex();
				if(!counts.containsKey(vertex2)){
					counts.put(vertex2.getKey(), new Integer(1));
					max = Math.max(max, 1);
				}else{
					Integer val = (Integer)counts.get(vertex2.getKey());
					int temp = val.intValue() + 1;
					max = Math.max(max, temp);
					counts.put(vertex2.getKey(), new Integer(temp));
				}
			}
		}
		return max;
	}

	public int getMaxOutDegree() {
		int max = 0;
		Iterator<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> verticesIter = vertices.values().iterator();
		while(verticesIter.hasNext()){
			KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> vertex = verticesIter.next();
			max = Math.max(max, vertex.getCountEdges());
		}
		return max;
	}

	@Override
	public Collection<KeyValueVertex<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> getRoots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearAllVisitCounts() {
		// TODO Auto-generated method stub
		
	}
}