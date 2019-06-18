  
package net.sf.yogl.adjacent.keyMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.sf.yogl.BreadthFirstIterator;
import net.sf.yogl.DepthFirstIterator;
import net.sf.yogl.exceptions.DuplicateLinkException;
import net.sf.yogl.exceptions.DuplicateNodeException;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.impl.DuplicateEdgeException;
import net.sf.yogl.impl.DuplicateVertexException;
import net.sf.yogl.impl.ImplementationGraph;
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

public class AdjKeyGraph <VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> implements ImplementationGraph <VK, VV, EK, EV> {

	/** Since it should be too tedious to traverse the whole graph
	 * just to count the number of edges, this data is stored here.
	 */
	private int numberOfEdges = 0;

	/** 'vertices' contains all vertices of the graph. This vector
	 * is automatically resized when all entries are occupied.
	 */
	Map<VK, AdjKeyVertex<VK, VV, EK, EV>> vertices = null;

	/** Contains the list of all entry points in the graph.
	 *  When a new node is created, it is by default inserted into the set.
	 */
	private HashSet<VK> allStartNodeKeys = new HashSet<VK>();

	/** basic ctor, no default value
	 */
	public AdjKeyGraph(Map<VK, AdjKeyVertex<VK, VV, EK, EV>> prototype) throws GraphCorruptedException {
		if (!prototype.isEmpty()) {
			throw new GraphCorruptedException("The map must be empty");
		}
		vertices = prototype;
	}

	/** Will duplicate the content of the vertex and insert it into this graph.
	 * Method is used to copy vertices from graph to graph
	 */
	public AdjKeyVertex<VK, VV, EK, EV> tryAddVertex (AdjKeyVertex<VK, VV, EK, EV> vertex, boolean isRoot) {
		AdjKeyVertex<VK, VV, EK, EV> result = null;
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
	public BreadthFirstIterator<AdjKeyVertex<VK, VV, EK, EV>, AdjKeyEdge<VK, VV, EK, EV>> breadthFirstIterator(
			int maxCycles) throws GraphException {
		return new BreadthFirstIterator<AdjKeyVertex<VK, VV, EK, EV>, AdjKeyEdge<VK, VV, EK, EV>>(this, maxCycles);
	}

	/** @see ComparableKeysGraph#clone
	 */
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	/** @see ComparableKeysGraph#deepCopy
	 */
	public void deepCopy(AdjKeyGraph<VK, VV, EK, EV> dest) throws GraphException {

		for(AdjKeyVertex<VK, VV, EK, EV> v : this.getVertices(VertexType.ANY)){
			dest.tryAddVertex(v.clone(), this.isStartVertex(v));
		}

		LinksIterator<AdjKeyVertex<VK, VV, EK, EV>, AdjKeyEdge<VK, VV, EK, EV>> iter = this.linksKeysIterator();
		while (iter.hasNext()) {
			EK linkValue = (EK)iter.next(); //REFACTOR: iterator should return keys in this case, not values
			VK fromKey = iter.getOriginator();
			VK toKey = iter.getDestination();
			dest.addLinkLast(fromKey, toKey, linkValue);
		}
	}

	/** @see ComparableKeysGraph#depthFirstIterator
	 */
	public DepthFirstIterator<AdjKeyVertex<VK, VV, EK, EV>, AdjKeyEdge<VK, VV, EK, EV>> depthFirstIterator(Collection<AdjKeyVertex<VK, VV, EK, EV>> startVertices, 
			int maxCycling)
		throws GraphException {
		return new DepthFirstIterator<AdjKeyVertex<VK, VV, EK, EV>, AdjKeyEdge<VK, VV, EK, EV>>(startVertices, maxCycling);
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
	public AdjKeyVertex<VK,VV, EK, EV> findVertexByKey(VK nodeKey)
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
	public List<AdjKeyEdge<VK, VV, EK, EV>EK[] getLinksKeysBetween(VK nodeFromKey, VK nodeToKey)
		throws NodeNotFoundException {

		ArrayList<EK> result = new ArrayList<>();
		AdjKeyVertex<VK,VV, EK, EV> p = vertices.get(nodeFromKey);
		//retrieve all edges
		AdjKeyEdge<VK, VV, EK, EV>[] eList = p.getNeighbors();
		for (int i = 0; i < eList.length; i++) {
			// compare references, not the values
			if (eList[i]
				.getNextVertexKey()
				.equals(vertices.get(nodeToKey))) {
				EK o = eList[i].getEdgeKey();
				result.add(o);
			}
		}
		return (EK[])result.toArray();
	}

	/** Build the list of all entry points in the graph.
	 *  An entry point is a node with no 'predecessor'.
	 */
	private void buildAllStartNodeKeys() {
		HashSet<VK> result = new HashSet<VK>(vertices.keySet());
		Iterator entriesIter = vertices.entrySet().iterator();
		while (entriesIter.hasNext()) {
			Map.Entry entry = (Map.Entry) entriesIter.next();
			AdjKeyVertex v = (AdjKeyVertex) entry.getValue();
			AdjKeyEdge[] successors = v.getNeighbors();
			for (int i = 0; i < successors.length; i++) {
				Object next = successors[i].getNextVertexKey();
				if (result.contains(next)) {
					result.remove(next);
				}
			}
		}
		allStartNodeKeys = result;
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
	public boolean isStartVertex(AdjKeyVertex<VK, VV, EK, EV> nodeKey) {
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
	public List<AdjKeyVertex<VK,VV,EK,EV>> getVertices(VertexType type) throws GraphCorruptedException {

		ArrayList<AdjKeyVertex<VK, VV, EK, EV>> list = new ArrayList<>();
		Iterator<AdjKeyVertex<VK, VV, EK, EV>> iter = vertices.values().iterator();
		while (iter.hasNext()) {
			AdjKeyVertex<VK, VV, EK, EV> vertex = iter.next();
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

	AdjKeyVertex<VK, VV, EK, EV> getVertex (VK key) {
		return this.vertices.get(key);
	}
	
	/** @see ComparableKeysGraph#getType
	 */
	public VertexType getVertexType(AdjKeyVertex<VK, VV, EK, EV> nodeKey) throws NodeNotFoundException {
		AdjKeyVertex<VK, VV, EK, EV> vertex = vertices.get(nodeKey);
		if (this.allStartNodeKeys.contains(nodeKey)) {
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
		AdjKeyVertex<VK, VV, EK, EV> v = this.vertices.get(nodeKey);
		if (v == null)
			return null;
		return v.getUserValue();
	}

	/** return all predecessor nodes to a given node.
	 */
	public Collection<AdjKeyVertex<VK, VV, EK, EV>> getPredecessorVertices(AdjKeyVertex<VK, VV, EK, EV> refVertex)
		throws GraphException {

		ArrayList<AdjKeyVertex<VK, VV, EK, EV>> result = new ArrayList<>();
		AdjKeyVertex<VK,VV,EK,EV> to = vertices.get(refVertex);
		Iterator<Map.Entry<VK, AdjKeyVertex<VK,VV,EK,EV>>>iter = vertices.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<VK, AdjKeyVertex<VK,VV,EK,EV>> entry = iter.next();
			AdjKeyVertex<VK,VV,EK,EV> vertex = entry.getValue();
			if (null != (vertex.getEdgeTo(to.getKey()))) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	/** Return all predecessor nodes for a given node and link
	 */
	public AdjKeyVertex<VK, VV, EK, EV> getPredecessorVertex(AdjKeyVertex<VK, VV, EK, EV> destVertex, AdjKeyEdge<VK, VV, EK, EV> edge) {
		AdjKeyVertex<VK, VV, EK, EV> sourceVertex = null;
		Iterator<Map.Entry<VK, AdjKeyVertex<VK, VV, EK, EV>>> iter = vertices.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<VK, AdjKeyVertex<VK, VV, EK, EV>> entry = iter.next();
			AdjKeyVertex<VK, VV, EK, EV> vertex = entry.getValue();
			Iterator<AdjKeyEdge<VK, VV, EK, EV>> edgesIter = vertex.getOutgoingEdges().iterator();
			while (edgesIter.hasNext()) {
				AdjKeyEdge<VK, VV, EK, EV> testedEdge = edgesIter.next();
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
	public List<AdjKeyVertex<VK, VV, EK, EV>> getSuccessorVertices(AdjKeyVertex<VK, VV, EK, EV> node)
		throws NodeNotFoundException {
		Iterator<AdjKeyEdge<VK, VV, EK, EV>> edgesIter = node.getNeighbors().iterator();
		List<AdjKeyVertex<VK, VV, EK, EV>>result = new ArrayList<>();
		while (edgesIter.hasNext()) {
			result.add(edgesIter.next().getOutgoingVertex());
		}
		return result;
	}

	/** @see ComparableKeysGraph#getPassage
	 */
	public int getVisitCount(VK nodeKey) throws GraphException {

		AdjKeyVertex<VK, VV, EK, EV> vertex = vertices.get(nodeKey);
		return vertex.getVisitsCount();
	}

	/** @see ComparableKeysGraph#incPassage
	 */
	public int incVisitCount(VK nodeKey) throws GraphException {

		AdjKeyVertex<VK, VV, EK, EV> vertex = vertices.get(nodeKey);
		return vertex.incVisitCounts();
	}

	/** @see ComparableKeysGraph#addLinkFirst
	 */
	public void addLinkFirst(
		VK nodeKeyFrom,
		VK nodeKeyTo,
		EK linkKey,
		EV linkValue)
		throws
			DuplicateLinkException,
			NodeNotFoundException,
			GraphCorruptedException {

		if (!this.existsNode(nodeKeyTo)) {
			NodeNotFoundException e = new NodeNotFoundException();
			e.setGraph(this);
			e.setNodeKey(nodeKeyTo);
			throw e;
		}
		try {
			//create the new edge
			AdjKeyEdge<VK, VV, EK, EV> newEdge = new AdjKeyEdge<>(this, linkKey, nodeKeyTo, linkValue);
			//retrieve all edges connected to node1
			AdjKeyVertex<VK, VV, EK, EV> vertexFrom = vertices.get(nodeKeyFrom);
			Iterator<AdjKeyEdge<VK, VV, EK, EV>> eList1Iter = vertexFrom.getNeighbors().iterator();
			//check that edge does not exists
			while (eList1Iter.hasNext()) {
				if (newEdge.equals(eList1Iter.next())) {
					throw new DuplicateEdgeException(vertexFrom, newEdge);
				}
			}
			vertexFrom.tryAddEdgeFirst(newEdge);
			if (this.allStartNodeKeys.contains(nodeKeyTo)) {
				this.allStartNodeKeys.remove(nodeKeyTo);
			}
			numberOfEdges++;
			buildAllStartNodeKeys();
		} catch (DuplicateEdgeException e2) {
			DuplicateLinkException r = new DuplicateLinkException();
			r.setGraph(this);
			r.setNodeKeyFrom(nodeKeyFrom);
			r.setLink(linkKey);
			r.setNodeKeyTo(nodeKeyTo);
			throw r;
		}
	}

	/** Insert a new directed edge from vertex1 to vertex2 and
	 * associate the weight to it.
	 * Pre-condition:  node1, node2 & link are non-null
	 * @param vertex1 starting point of the edge
	 * @param vertex2 destination
	 * @param edge user-defined edge to be associated to the edge
	 * @exception VertexNotFoundException if vertex1 or vertex2 does
	 *            not exists
	 * @exception DuplicateVertexException
	 * @exception DuplicateLinkException if this edge already exists
	 *            between vertex1 and vertex2
	 * @exception GraphCorruptedException if some element in the graph
	 *            is badly constructed
	 */
	public void addLinkLast(
		VK nodeKeyFrom,
		VK nodeKeyTo,
		EK linkKey,
		EV linkValue)
		throws
			NodeNotFoundException,
			DuplicateLinkException,
			GraphCorruptedException {

		if (!this.existsNode(nodeKeyTo))
			throw new NodeNotFoundException(nodeKeyTo.toString());
		try {
			//create the new edge
			AdjKeyEdge<VK, VV, EK, EV> newEdge = new AdjKeyEdge<>(this, linkKey, nodeKeyTo, linkValue);
			//retrieve all edges connected to node1
			AdjKeyVertex<VK, VV, EK, EV> vertexFrom = vertices.get(nodeKeyFrom);
			Iterator<AdjKeyEdge<VK, VV, EK, EV>> eList1Iter = vertexFrom.getNeighbors().iterator();
			//check that edge does not exists
			while (eList1Iter.hasNext()) {
				if (newEdge.equals(eList1Iter.next())) {
					throw new DuplicateEdgeException(vertexFrom, newEdge);
				}
			}
			vertexFrom.tryAddEdgeLast(newEdge);
			if (this.allStartNodeKeys.contains(nodeKeyTo)) {
				this.allStartNodeKeys.remove(nodeKeyTo);
			}
			numberOfEdges++;
			buildAllStartNodeKeys();
		} catch (GraphCorruptedException e2) {
			e2.setGraph(this);
			throw e2;
		} catch (DuplicateEdgeException e3) {
			DuplicateLinkException r = new DuplicateLinkException();
			r.setGraph(this);
			r.setNodeKeyFrom(nodeKeyFrom);
			r.setLink(linkKey);
			r.setNodeKeyTo(nodeKeyTo);
			throw r;
		}
	}

	/** Method to check the contents of the graph
	 * @return true if the graph does not contains any node
	 */

	public boolean isEmpty() {
		return this.vertices.isEmpty();
	}

	/** @see graph.ComparableKeysGraph#linksIterator
	 */
	public LinksIterator<AdjKeyVertex<VK, VV, EK, EV>, AdjKeyEdge<VK, VV, EK, EV>> linksKeysIterator() throws GraphException {

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
		Iterator<AdjKeyVertex<VK, VV, EK, EV>> vertexIter = vertices.values().iterator();
		while (vertexIter.hasNext()) {
			AdjKeyVertex<VK, VV, EK, EV> vertex = vertexIter.next();
			nodes.addLast(vertex.getUserValue());
		}
		return nodes;
	}

	/** @see ComparableKeysGraph#linksValues()
	 */
	public Collection<EV> linksValues() {
		LinkedList<EV> v = new LinkedList<>();
		Iterator<AdjKeyVertex<VK, VV, EK, EV>> verticesIter = vertices.values().iterator();
		while (verticesIter.hasNext()) {
			AdjKeyVertex<VK, VV, EK, EV> vertex = verticesIter.next();
			Iterator<AdjKeyEdge<VK, VV, EK, EV>> edgesIter = vertex.getNeighbors().iterator();
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
		AdjKeyVertex<VK, VV, EK, EV> from = this.vertices.get(nodeKeyFrom);
		AdjKeyEdge<VK, VV, EK, EV>[] edges = from.getEdgeTo(nodeKeyTo);
		if (edges != null) {
			for (int i = 0; i < edges.length; i++) {
				from.removeEdge(edges[i]);
				numberOfEdges--;
			}
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

		AdjKeyVertex<VK, VV, EK, EV> vertexFrom = vertices.get(nodeFromKey);
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

		AdjKeyVertex<VK, VV, EK, EV> v = this.vertices.get(nodeKey);
		this.numberOfEdges -= v.getCountEdges();
		//remove all links to the node
		Iterator<AdjKeyVertex<VK, VV, EK, EV>> vertexIter = vertices.values().iterator();
		while (vertexIter.hasNext()) {
			AdjKeyVertex<VK, VV, EK, EV> vertex = vertexIter.next();
			AdjKeyEdge<VK, VV, EK, EV>[] edges = vertex.getEdgeTo(nodeKey);
			if (edges != null) {
				for (int i = 0; i < edges.length; i++) {
					vertex.removeEdge(edges[i]);
					this.numberOfEdges--;
				}
			}
		}
		this.vertices.remove(nodeKey);
		buildAllStartNodeKeys();
	}

	/** @see ComparableKeysGraph#setPassage
	 */
	public void setAllVisitCounts(int count) throws GraphException {
		Iterator<AdjKeyVertex<VK, VV, EK, EV>> nodeValuesIter = this.vertices.values().iterator();
		while (nodeValuesIter.hasNext()) {
			AdjKeyVertex<VK, VV, EK, EV> vertex = nodeValuesIter.next();
			vertex.setVisitCounts(count);
		}
	}

	/** @see ComparableKeysGraph#setPassage
	 */
	public void setVisitCount(VK nodeKey, int count)
		throws GraphException {
		AdjKeyVertex<VK, VV, EK, EV> vertex = vertices.get(nodeKey);
		vertex.setVisitCounts(count);
	}

	/** @see ComparableKeysGraph#getOutgoingLinkValue(java.lang.Object, java.lang.Object)
	 */
	public EV getOutgoingLinkValue(VK nodeKeyFrom, EK linkKey)
		throws NodeNotFoundException {
		EV result = null;
		AdjKeyVertex<VK, VV, EK, EV> vertex = this.vertices.get(nodeKeyFrom);
		AdjKeyEdge<VK, VV, EK, EV> edge = vertex.getEdgeByKey(linkKey);
		if (edge != null)
			result = edge.getUserValue();
		return result;
	}

	public int getMaxInDegree() {
		int max = 0;
		HashMap<VK, Integer> counts = new HashMap<>();
		Iterator<AdjKeyVertex<VK, VV, EK, EV>> verticesIter = vertices.values().iterator();
		while(verticesIter.hasNext()){
			AdjKeyVertex<VK, VV, EK, EV> vertex = verticesIter.next();
			Iterator<AdjKeyEdge<VK, VV, EK, EV>>edgesIter = vertex.getNeighbors().iterator();
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
		Iterator<AdjKeyVertex<VK, VV, EK, EV>> verticesIter = vertices.values().iterator();
		while(verticesIter.hasNext()){
			AdjKeyVertex<VK, VV, EK, EV> vertex = verticesIter.next();
			max = Math.max(max, vertex.getCountEdges());
		}
		return max;
	}

	@Override
	public Collection<AdjKeyVertex<VK, VV, EK, EV>> getRoots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearAllVisitCounts() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AdjKeyVertex<VK, VV, EK, EV>[] getSuccessorVertices(AdjKeyVertex<VK, VV, EK, EV> vertex,
			AdjKeyEdge<VK, VV, EK, EV> link) throws NodeNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}