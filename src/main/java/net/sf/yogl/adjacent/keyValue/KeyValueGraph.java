  
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

public class KeyValueGraph <VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> implements Graph <KeyValueVertex<VK, VV, EK, EV>, KeyValueEdge<VK, VV, EK, EV>> {

	/** Since it should be too tedious to traverse the whole graph
	 * just to count the number of edges, this data is stored here.
	 */
	private int numberOfEdges = 0;

	/** 'vertices' contains all vertices of the graph. This vector
	 * is automatically resized when all entries are occupied.
	 */
	protected Map<VK, KeyValueVertex<VK, VV, EK, EV>> vertices = new HashMap<>();

	/** Contains the list of all entry points in the graph.
	 *  When a new node is created, it is by default inserted into the set.
	 */
	private HashSet<VK> allStartNodeKeys = new HashSet<VK>();

	/** basic ctor, no default value
	 */
	public KeyValueGraph(){}

	/** Will duplicate the content of the vertex and insert it into this graph.
	 * Method is used to copy vertices from graph to graph
	 */
	public KeyValueVertex<VK, VV, EK, EV> addRootVertex (KeyValueVertex<VK, VV, EK, EV> vertex, boolean isRoot) {
		KeyValueVertex<VK, VV, EK, EV> result = null;
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
	public BreadthFirstIterator<KeyValueVertex<VK, VV, EK, EV>, KeyValueEdge<VK, VV, EK, EV>> breadthFirstIterator(
			int maxCycles) throws NodeNotFoundException {
		return new BreadthFirstIterator<KeyValueVertex<VK, VV, EK, EV>, KeyValueEdge<VK, VV, EK, EV>>(this, maxCycles);
	}

	/** @see ComparableKeysGraph#clone
	 */
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	/** @see ComparableKeysGraph#depthFirstIterator
	 */
	public DepthFirstIterator<KeyValueVertex<VK, VV, EK, EV>, KeyValueEdge<VK, VV, EK, EV>> depthFirstIterator(Collection<KeyValueVertex<VK, VV, EK, EV>> startVertices, 
			int maxCycling)
		throws GraphException {
		return new DepthFirstIterator<KeyValueVertex<VK, VV, EK, EV>, KeyValueEdge<VK, VV, EK, EV>>(startVertices, maxCycling);
	}

	/** @see ComparableKeysGraph#existsNode
	 */
	public boolean existsNode(VK nodeKey) {
		return vertices.containsKey(nodeKey);
	}

	/** Finds the node in 'vertices' and, if it exists, return the
	 *  corresponding Vertex from vertices. If it doesn't, throw an exception.
	 *  pre-condition: node is non-null
	 * @param node refers to the user-defined node object
	 * @exception VertexNotFoundException  thrown if rhs was not
	 *            found in the graph.
	 */
	public KeyValueVertex<VK,VV, EK, EV> findVertexByKey(VK nodeKey)
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
	public List<KeyValueEdge<VK, VV, EK, EV>> getLinksKeysBetween(KeyValueVertex<VK, VV, EK, EV> nodeFromKey, KeyValueVertex<VK, VV, EK, EV> nodeToKey)
		throws NodeNotFoundException {

		ArrayList<KeyValueEdge<VK, VV, EK, EV>> result = new ArrayList<>();
		Iterator<KeyValueEdge<VK, VV, EK, EV>> edgesIter = nodeFromKey.getNeighbors().iterator();
		while (edgesIter.hasNext()) {
			KeyValueEdge<VK, VV, EK, EV> edge = edgesIter.next();
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
		Iterator<KeyValueVertex<VK, VV, EK, EV>> entriesIter = vertices.values().iterator();
		while (entriesIter.hasNext()) {
			Iterator<KeyValueEdge<VK, VV, EK, EV>> successorsIter = entriesIter.next().getNeighbors().iterator();
			while (successorsIter.hasNext()) {
				VK next = successorsIter.next().getNextVertexKey();
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
	public Collection<VK> getAllStartNodeKeys() {
		return allStartNodeKeys;
	}

	/** @return true if the node is an entry point in the graph.
	 * @param nodeKey identify the node.
	 */
	@Override
	public boolean isStartVertex(KeyValueVertex<VK, VV, EK, EV> nodeKey) {
		return allStartNodeKeys.contains(nodeKey.getKey());
	}

	/** Return the depth of the graph. This method may perform a full
	 * traversal of the graph (depend on the implementation). The result
	 * can be interpreted as the maximum distance between the root node
	 * and any other accessible node.
	 * The root node is at level 0.
	 */
	public int getDepth(VK startingNode) throws GraphException {
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
	public Set<VK> getNodesKeys() throws GraphCorruptedException {
		return vertices.keySet();
	}

	/** @see ComparableKeysGraph#getNodes
	 */
	public List<KeyValueVertex<VK,VV,EK,EV>> getVertices(VertexType type) throws NodeNotFoundException {

		ArrayList<KeyValueVertex<VK, VV, EK, EV>> list = new ArrayList<>();
		Iterator<KeyValueVertex<VK, VV, EK, EV>> iter = vertices.values().iterator();
		while (iter.hasNext()) {
			KeyValueVertex<VK, VV, EK, EV> vertex = iter.next();
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

	public KeyValueVertex<VK, VV, EK, EV> getVertex (VK key) {
		return this.vertices.get(key);
	}
	
	/** @see ComparableKeysGraph#getType
	 */
	public VertexType getVertexType(KeyValueVertex<VK, VV, EK, EV> vertex) {
		if (this.allStartNodeKeys.contains(vertex.getKey())) {
			if ((vertex.getNeighbors() == null)
				|| (vertex.getNeighbors().size() == 0)) {
				return VertexType.STARTEND;
			} else {
				return VertexType.START;
			}
		} else {
			if ((vertex.getNeighbors() == null)
				|| (vertex.getNeighbors().size() == 0)) {
				return VertexType.END;
			} else {
				return VertexType.NONE;
			}
		}
	}

	/** @see ComparableKeysGraph#getNodeValue
	 */
	public VV getNodeValue(VK nodeKey) {
		KeyValueVertex<VK, VV, EK, EV> v = this.vertices.get(nodeKey);
		if (v == null)
			return null;
		return v.getUserValue();
	}

	/** return all predecessor nodes to a given node.
	 */
	public Collection<KeyValueVertex<VK, VV, EK, EV>> getPredecessorVertices(KeyValueVertex<VK, VV, EK, EV> toVertex) {

		ArrayList<KeyValueVertex<VK, VV, EK, EV>> result = new ArrayList<>();
		Iterator<Map.Entry<VK, KeyValueVertex<VK,VV,EK,EV>>>iter = vertices.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<VK, KeyValueVertex<VK,VV,EK,EV>> entry = iter.next();
			KeyValueVertex<VK,VV,EK,EV> vertex = entry.getValue();
			if (null != (vertex.getEdgeTo(toVertex.getKey()))) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	/** Return all predecessor nodes for a given node and link
	 */
	public KeyValueVertex<VK, VV, EK, EV> getPredecessorVertex(KeyValueVertex<VK, VV, EK, EV> destVertex, KeyValueEdge<VK, VV, EK, EV> edge) {
		KeyValueVertex<VK, VV, EK, EV> sourceVertex = null;
		Iterator<Map.Entry<VK, KeyValueVertex<VK, VV, EK, EV>>> iter = vertices.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<VK, KeyValueVertex<VK, VV, EK, EV>> entry = iter.next();
			KeyValueVertex<VK, VV, EK, EV> vertex = entry.getValue();
			Iterator<KeyValueEdge<VK, VV, EK, EV>> edgesIter = vertex.getOutgoingEdges().iterator();
			while (edgesIter.hasNext()) {
				KeyValueEdge<VK, VV, EK, EV> testedEdge = edgesIter.next();
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
	public List<KeyValueVertex<VK, VV, EK, EV>> getSuccessorVertices(KeyValueVertex<VK, VV, EK, EV> node) {
		Iterator<KeyValueEdge<VK, VV, EK, EV>> edgesIter = node.getNeighbors().iterator();
		List<KeyValueVertex<VK, VV, EK, EV>>result = new ArrayList<>();
		while (edgesIter.hasNext()) {
			result.add(edgesIter.next().getToVertex());
		}
		return result;
	}

	/** @see ComparableKeysGraph#getPassage
	 */
	public int getVisitCount(VK nodeKey) throws GraphException {

		KeyValueVertex<VK, VV, EK, EV> vertex = vertices.get(nodeKey);
		return vertex.getVisitsCount();
	}

	/** @see ComparableKeysGraph#incPassage
	 */
	public int incVisitCount(VK nodeKey) throws GraphException {

		KeyValueVertex<VK, VV, EK, EV> vertex = vertices.get(nodeKey);
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
	public LinksIterator<KeyValueVertex<VK, VV, EK, EV>, KeyValueEdge<VK, VV, EK, EV>> linksKeysIterator() throws GraphException {

		return new LinksIterator<>(this);
	}

	/** Method to retrieve all nodes, via an iterator. Return the 'user value'
	 */
	public Collection<VK> nodesKeySet() {

		LinkedList<VK> nodes = new LinkedList<>();
		Iterator<VK> vertexIter = vertices.keySet().iterator();
		while (vertexIter.hasNext()) {
			VK key = vertexIter.next();
			nodes.addLast(key);
		}
		return nodes;
	}

	/** Method to retrieve all nodes, via an iterator. Return the 'user value'
	 */
	public Collection<VV> nodesValues() {

		LinkedList<VV> nodes = new LinkedList<>();
		Iterator<KeyValueVertex<VK, VV, EK, EV>> vertexIter = vertices.values().iterator();
		while (vertexIter.hasNext()) {
			KeyValueVertex<VK, VV, EK, EV> vertex = vertexIter.next();
			nodes.addLast(vertex.getUserValue());
		}
		return nodes;
	}

	/** @see ComparableKeysGraph#linksValues()
	 */
	public Collection<EV> linksValues() {
		LinkedList<EV> v = new LinkedList<>();
		Iterator<KeyValueVertex<VK, VV, EK, EV>> verticesIter = vertices.values().iterator();
		while (verticesIter.hasNext()) {
			KeyValueVertex<VK, VV, EK, EV> vertex = verticesIter.next();
			Iterator<KeyValueEdge<VK, VV, EK, EV>> edgesIter = vertex.getNeighbors().iterator();
			while (edgesIter.hasNext()) {
				v.add(edgesIter.next().getUserValue());
			}
		}
		return v;
	}

	/** Removes all links between the given nodes.
	 */
	public void removeAllLinksBetween(VK nodeKeyFrom, VK nodeKeyTo)
		throws GraphException {
		KeyValueVertex<VK, VV, EK, EV> from = this.vertices.get(nodeKeyFrom);
		Iterator<KeyValueEdge<VK, VV, EK, EV>> edgesIter = from.getEdgeTo(nodeKeyTo).iterator();
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
		VK nodeFromKey,
		VK nodeToKey,
		EK linkKey)
		throws GraphException {

		KeyValueVertex<VK, VV, EK, EV> vertexFrom = vertices.get(nodeFromKey);
		vertexFrom.removeEdgeByKey(linkKey);

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
	public void removeNode(VK nodeKey) throws NodeNotFoundException {

		KeyValueVertex<VK, VV, EK, EV> v = this.vertices.get(nodeKey);
		this.numberOfEdges -= v.getCountEdges();
		//remove all links to the node
		Iterator<KeyValueVertex<VK, VV, EK, EV>> vertexIter = vertices.values().iterator();
		while (vertexIter.hasNext()) {
			KeyValueVertex<VK, VV, EK, EV> vertex = vertexIter.next();
			Iterator<KeyValueEdge<VK, VV, EK, EV>> edgesIter = vertex.getEdgeTo(nodeKey).iterator();
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
		Iterator<KeyValueVertex<VK, VV, EK, EV>> nodeValuesIter = this.vertices.values().iterator();
		while (nodeValuesIter.hasNext()) {
			KeyValueVertex<VK, VV, EK, EV> vertex = nodeValuesIter.next();
			vertex.setVisitCounts(count);
		}
	}

	/** @see ComparableKeysGraph#setPassage
	 */
	public void setVisitCount(VK nodeKey, int count)
		throws GraphException {
		KeyValueVertex<VK, VV, EK, EV> vertex = vertices.get(nodeKey);
		vertex.setVisitCounts(count);
	}

	/** @see ComparableKeysGraph#getOutgoingLinkValue(java.lang.Object, java.lang.Object)
	 */
	public EV getOutgoingLinkValue(VK nodeKeyFrom, EK linkKey)
		throws NodeNotFoundException {
		EV result = null;
		KeyValueVertex<VK, VV, EK, EV> vertex = this.vertices.get(nodeKeyFrom);
		KeyValueEdge<VK, VV, EK, EV> edge = vertex.getEdgeByKey(linkKey);
		if (edge != null)
			result = edge.getUserValue();
		return result;
	}

	public int getMaxInDegree() {
		int max = 0;
		HashMap<VK, Integer> counts = new HashMap<>();
		Iterator<KeyValueVertex<VK, VV, EK, EV>> verticesIter = vertices.values().iterator();
		while(verticesIter.hasNext()){
			KeyValueVertex<VK, VV, EK, EV> vertex = verticesIter.next();
			Iterator<KeyValueEdge<VK, VV, EK, EV>>edgesIter = vertex.getNeighbors().iterator();
			while(edgesIter.hasNext()){
				VK vertexKey = edgesIter.next().getNextVertexKey();
				if(!counts.containsKey(vertexKey)){
					counts.put(vertexKey, new Integer(1));
					max = Math.max(max, 1);
				}else{
					Integer val = (Integer)counts.get(vertexKey);
					int temp = val.intValue() + 1;
					max = Math.max(max, temp);
					counts.put(vertexKey, new Integer(temp));
				}
			}
		}
		return max;
	}

	public int getMaxOutDegree() {
		int max = 0;
		Iterator<KeyValueVertex<VK, VV, EK, EV>> verticesIter = vertices.values().iterator();
		while(verticesIter.hasNext()){
			KeyValueVertex<VK, VV, EK, EV> vertex = verticesIter.next();
			max = Math.max(max, vertex.getCountEdges());
		}
		return max;
	}

	@Override
	public Collection<KeyValueVertex<VK, VV, EK, EV>> getRoots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearAllVisitCounts() {
		// TODO Auto-generated method stub
		
	}
}