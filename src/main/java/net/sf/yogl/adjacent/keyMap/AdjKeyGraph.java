  
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

import net.sf.yogl.BreadthFirstIterator;
import net.sf.yogl.LinksIterator;
import net.sf.yogl.adjacent.list.AdjListDepthFirstIterator;
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

public final class AdjKeyGraph <VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> implements ImplementationGraph <VK, VV, EK, EV> {

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

	/** Insert a new vertex in the graph and associate a type to it.
	 * Each vertex must be unique in the graph.
	 * Pre-condition: node must be non-null
	 * @param node new user-defined node
	 * @param int type of the vertex
	 * @exception DuplicateVertexException if the user-defined node
	 *            is already present in the graph
	 */
	public void addNode(VK nodeKey, VV nodeValue)
		throws DuplicateNodeException {

		if (!existsNode(nodeKey)) {
			AdjKeyVertex<VK, VV, EK, EV> rVertex = new AdjKeyVertex<>(nodeKey, nodeValue);
			this.vertices.put(nodeKey, rVertex);
			this.allStartNodeKeys.add(nodeKey);
		} else {
			DuplicateNodeException e = new DuplicateNodeException();
			e.setGraph(this);
			e.setNodeKey(nodeKey);
			throw e;
		}
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
	public BreadthFirstIterator breadthFirstIterator(
		VK startingNodeKey,
		int maxCycles)
		throws GraphException {
		return new BreadthFirstIterator(this, startingNodeKey, maxCycles);
	}

	/** @see ComparableKeysGraph#clone
	 */
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	/** @see ComparableKeysGraph#deepCopy
	 */
	public void deepCopy(ComparableKeysGraph<VK, VV, EK, EV> dest) throws GraphException {

		for(AdjKeyVertex<VK, VV, EK, EV> v : this.getVertices(0xFFFFFFFF)){
			dest.addNode(v.getKey(), v.getUserValue());
		}

		LinksIterator<VK, VV, EK, EV> iter = this.linksKeysIterator();
		while (iter.hasNext()) {
			EK linkValue = (EK)iter.next(); //REFACTOR: iterator should return keys in this case, not values
			VK fromKey = iter.getOriginator();
			VK toKey = iter.getDestination();
			dest.addLinkLast(fromKey, toKey, linkValue);
		}
	}

	/** @see ComparableKeysGraph#depthFirstIterator
	 */
	public AdjListDepthFirstIterator depthFirstIterator(VK node, int maxCycling)
		throws GraphException {
		return new AdjListDepthFirstIterator(this, node, maxCycling);
	}

	/** @see ComparableKeysGraph#existsNode
	 */
	public boolean existsNode(VK nodeKey) {
		return vertices.containsKey(nodeKey);
	}

	/** Finds the node in 'vertices' and, if it exists, return the
	 * index in vertices. If it doesn't, throw an exception.
	 * pre-condition: node is non-null
	 * @param node refers to the user-defined node object
	 * @exception VertexNotFoundException  thrown if rhs was not
	 *            found in the graph.
	 */
	private VV findNodeByKey(VK nodeKey) throws NodeNotFoundException {

		AdjKeyVertex<VK, VV, EK, EV> v = findVertexByKey(nodeKey);
		return v.getUserValue();
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
	public EK[] getLinksKeysBetween(VK nodeFromKey, VK nodeToKey)
		throws NodeNotFoundException {

		ArrayList<EK> result = new ArrayList<>();
		AdjKeyVertex<VK,VV, EK, EV> p = findVertexByKey(nodeFromKey);
		//retrieve all edges
		AdjKeyEdge<VK, EK, EV>[] eList = p.getNeighbors();
		for (int i = 0; i < eList.length; i++) {
			// compare references, not the values
			if (eList[i]
				.getNextVertexKey()
				.equals(findVertexByKey(nodeToKey))) {
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
	public boolean isStartNode(VK nodeKey) {
		return allStartNodeKeys.contains(nodeKey);
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

	/** Returns the links that have as destination the given node. This
	 *  method looks for 'predecessor' nodes and their relation to the
	 *  given node.
	 */
	public EK[] getIncomingLinksKeys(VK nodeToKey)
		throws NodeNotFoundException {
		ArrayList<EK> result = new ArrayList<>();
		AdjKeyVertex<VK,VV,EK,EV> to = findVertexByKey(nodeToKey);
		Iterator<AdjKeyVertex<VK,VV,EK,EV>> iter = vertices.values().iterator();
		while (iter.hasNext()) {
			AdjKeyVertex<VK,VV,EK,EV> vertex = iter.next();
			AdjKeyEdge<VK,EK,EV>[] edge = vertex.getEdgeTo(nodeToKey);
			if (null != edge) {
				for (int i = 0; i < edge.length; i++) {
					result.add(edge[i].getEdgeKey());
				}
			}
		}
		return (EK[])result.toArray();
	}

	/** Return the list of direct predecessors and their associated
	 * link
	 * x[0][i] = node; x[1][i] = link
	 * TODO: should be replaced by a Map....
	 */
	public Object[][] getIncomingLinksKeysAndNodesKeys(VK nodeToKey)
		throws NodeNotFoundException {
		ArrayList result = new ArrayList();
		Iterator<Map.Entry<VK, AdjKeyVertex<VK,VV,EK,EV>>> nodesIter = this.vertices.entrySet().iterator();
		while (nodesIter.hasNext()) {
			Map.Entry<VK, AdjKeyVertex<VK,VV,EK,EV>> entry = nodesIter.next();
			AdjKeyVertex<VK,VV,EK,EV> vertex = entry.getValue();
			AdjKeyEdge<VK,EK,EV>[] edges = vertex.getEdgeTo(nodeToKey);
			for (int i = 0; i < edges.length; i++) {
				if (edges[i].getNextVertexKey().equals(nodeToKey)) {
					Object[] r = new Object[2];
					r[1] = edges[i].getEdgeKey();
					r[0] = entry.getKey();
				}
			}

		}
		return (Object[][]) result.toArray(new Object[result.size()][2]);
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
	public List<AdjKeyVertex<VK,VV,EK,EV>> getVertices(int nodeType) throws GraphCorruptedException {

		ArrayList<AdjKeyVertex<VK,VV,EK,EV>> list = new ArrayList<>();
		try {
			Iterator<Map.Entry<VK, AdjKeyVertex<VK,VV,EK,EV>>> iter = vertices.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<VK, AdjKeyVertex<VK,VV,EK,EV>> entry = iter.next();
				VK key = entry.getKey();
				AdjKeyVertex<VK,VV,EK,EV> vertex = entry.getValue();
				if ((getNodeType(key) & nodeType) != 0) {
					if ((getNodeType(key) == VertexType.START)
						|| (getNodeType(key) == VertexType.STARTEND)) {
						list.add(0, vertex);
					} else {
						list.add(vertex);
					}
				}
			}
			return list;
		} catch (NodeNotFoundException e1) {
			throw new GraphCorruptedException(e1);
		}
	}

	/** @see ComparableKeysGraph#getType
	 */
	public int getNodeType(VK nodeKey) throws NodeNotFoundException {
		if (this.allStartNodeKeys.contains(nodeKey)) {
			AdjKeyVertex<VK, VV, EK, EV> vertex = findVertexByKey(nodeKey);
			if ((vertex.getNeighbors() == null)
				|| (vertex.getNeighbors().length == 0)) {
				return VertexType.STARTEND;
			} else {
				return VertexType.START;
			}
		} else {
			AdjKeyVertex<VK, VV, EK, EV> vertex = findVertexByKey(nodeKey);
			if ((vertex.getNeighbors() == null)
				|| (vertex.getNeighbors().length == 0)) {
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

	/** @see ComparableKeysGraph#getOutgoingLinksKeys
	 */
	public EK[] getOutgoingLinksKeys(VK nodeFromKey)
		throws GraphException {

		ArrayList<EK> result = new ArrayList<>();
		AdjKeyVertex<VK, VV, EK, EV> p = findVertexByKey(nodeFromKey);
		//retrieve all edges
		AdjKeyEdge<VK, EK, EV>[] eList = p.getNeighbors();
		if (eList != null) {
			for (int i = 0; i < eList.length; i++) {
				// compare references, not the values
				EK value = eList[i].getEdgeKey();
				result.add(value);
			}
		}
		return (EK[])result.toArray();
	}

	/** Returns an array of all outgoing edges and the associated
	 * destination node.
	 */
	public Object[][] getOutgoingLinksKeysAndNodesKeys(VK nodeKeyFrom)
		throws GraphException {
		AdjKeyVertex<VK,VV,EK,EV> v = this.findVertexByKey(nodeKeyFrom);
		AdjKeyEdge<VK,EK,EV>[] edges = v.getNeighbors();
		Object[][] result = new Object[edges.length][2];
		for (int i = 0; i < edges.length; i++) {
			result[i][0] = edges[i].getEdgeKey();
			result[i][1] = edges[i].getNextVertexKey();
		}
		return result;
	}

	/** return all predecessor nodes to a given node.
	 */
	public VK[] getPredecessorNodesKeys(VK nodeToKey)
		throws GraphException {

		ArrayList<VK> result = new ArrayList<>();
		AdjKeyVertex<VK,VV,EK,EV> to = findVertexByKey(nodeToKey);
		Iterator<Map.Entry<VK, AdjKeyVertex<VK,VV,EK,EV>>>iter = vertices.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<VK, AdjKeyVertex<VK,VV,EK,EV>> entry = iter.next();
			AdjKeyVertex<VK,VV,EK,EV> vertex = entry.getValue();
			if (null != (vertex.getEdgeTo(to.getKey()))) {
				result.add(entry.getKey());
			}
		}
		return (VK[])result.toArray();
	}

	/** Return all predecessor nodes for a given node and link
	 */
	public VK[] getPredecessorNodesKeys(VK nodeToKey, EK link) {
		ArrayList<VK> result = new ArrayList<>();
		Iterator<Map.Entry<VK, AdjKeyVertex<VK, VV, EK, EV>>> iter = vertices.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<VK, AdjKeyVertex<VK, VV, EK, EV>> entry = iter.next();
			AdjKeyVertex<VK, VV, EK, EV> vertex = entry.getValue();
			AdjKeyEdge<VK, EK, EV>[] edges = vertex.getEdgeTo(nodeToKey);
			if ((null != edges) && (edges.length > 0)) {
				if (edges[0].getUserValue().equals(link)) {
					result.add(entry.getKey());
				}
			}
		}
		return (VK[])result.toArray();
	}

	/** Ask the graph to return the list of all nodes that are adjacent
	 * to the node given in parameter. In other words, it will return
	 * the nodes that are connected to the parameter by one (maximum) edge.
	 * @return list of nodes
	 * @param node is the node for which we ask to retrieve all
	 *       neighbors
	 * @param steps distance in links between the given node and the
	 *       requested neighbors. The minimal distance is 1: returns the
	 *       immediate neighbors.
	 */
	public VK[] getSuccessorNodesKeys(VK nodeFromKey, int steps)
		throws GraphException {
		ArrayList<VK> result = new ArrayList<>();
		if (steps == 1) {
			AdjKeyVertex<VK, VV, EK, EV> p = findVertexByKey(nodeFromKey);
			AdjKeyEdge<VK, EK, EV>[] eList = p.getNeighbors();
			if (eList != null) {
				for (int i = 0; i < eList.length; i++) {
					result.add(eList[i].getNextVertexKey());
				}
			}
		} else {
			throw new GraphException("Not yet implemented");
		}
		return (VK[])result.toArray();
	}

	/** special getter method
	 * pre-condition: node & link are non-null
	 * @return the list of all neighbors accessible via 'edge'
	 * @see getNeighbors
	 * @param vertex reference node from which it is desired to retrieve
	 *       all neighbors.
	 * @param edge describes the connection edge that must match
	 * @exception VertexNotFoundException if 'vertex' does not exists
	 */
	public VK[] getSuccessorNodesKeys(VK nodeFromKey, EK link)
		throws GraphException {
		ArrayList<VK> result = new ArrayList<>();
		AdjKeyVertex<VK, VV, EK, EV> p = findVertexByKey(nodeFromKey);
		AdjKeyEdge<VK, EK, EV>[] eList = p.getNeighbors();
		if (eList != null) {
			for (int i = 0; i < eList.length; i++) {
				result.add(eList[i].getNextVertexKey());
			}
		}
		return (VK[])result.toArray();
	}

	/** Returns a list of Vertex which are directly adjacent to the node.
	 */
	public List<AdjKeyVertex<VK, VV, EK, EV>> getSuccessorVertices(AdjKeyVertex<VK, VV, EK, EV> node)
		throws NodeNotFoundException {
		AdjKeyEdge<VK, EK, EV>[] edges = node.getNeighbors();
		List<AdjKeyVertex<VK, VV, EK, EV>>result = new ArrayList<>();
		for (int i = 0; i < edges.length; i++) {
			result.add(i, findVertexByKey(edges[i].getNextVertexKey()));
		}
		return result;
	}

	/** Return a list of Vertex which are immediately adjacent to the given
	 *  Vertex
	 */
	public AdjKeyVertex<VK,VV,EK,EV>[] getSuccessorVertices(AdjKeyVertex<VK,VV,EK,EV> vertex, AdjKeyEdge<VK, EK, EV> link)
		throws NodeNotFoundException {
		ArrayList<AdjKeyVertex<VK,VV,EK,EV>> result = new ArrayList<>();
		AdjKeyEdge<VK,EK,EV>[] eList = vertex.getNeighbors();
		if (eList != null) {
			result = new ArrayList<>();
			//loop for each edge connected to the vertex

			for (int i = 0; i < eList.length; i++) {
				EV edgeValue = eList[i].getUserValue();
				if ((link == null)
					|| ((edgeValue != null) && (edgeValue.equals(link)))) {
					AdjKeyVertex<VK,VV,EK,EV> v1 = findVertexByKey(eList[i].getNextVertexKey());
					result.add(v1);
				}
			}
		}
		return result.toArray(new AdjKeyVertex[result.size()]);
	}

	/** @see ComparableKeysGraph#getPassage
	 */
	public int getVisitCount(VK nodeKey) throws GraphException {

		AdjKeyVertex<VK, VV, EK, EV> vertex = findVertexByKey(nodeKey);
		return vertex.getVisitsCount();
	}

	/** @see ComparableKeysGraph#incPassage
	 */
	public int incVisitCount(VK nodeKey) throws GraphException {

		AdjKeyVertex<VK, VV, EK, EV> vertex = findVertexByKey(nodeKey);
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
			AdjKeyEdge<VK, EK, EV> newEdge = new AdjKeyEdge<>(linkKey, nodeKeyTo, linkValue);
			//retrieve all edges connected to node1
			AdjKeyVertex<VK, VV, EK, EV> vertexFrom = findVertexByKey(nodeKeyFrom);
			AdjKeyEdge<VK, EK, EV>[] eList1 = vertexFrom.getNeighbors();
			//check that edge does not exists
			for (int i = 0; i < eList1.length; i++) {
				if (newEdge.equals(eList1[i])) {
					throw new DuplicateEdgeException(vertexFrom, newEdge);
				}
			}
			vertexFrom.addEdgeFirst(newEdge);
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

	/** @see ComparableKeysGraph#addLinkFirst
	 */
	public void addLinkFirst(
		VK nodeKeyFrom,
		VK nodeKeyTo,
		EK linkKey)
		throws
			DuplicateLinkException,
			NodeNotFoundException,
			GraphCorruptedException {
		this.addLinkFirst(nodeKeyFrom, nodeKeyTo, linkKey, null);
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
			AdjKeyEdge<VK, EK, EV> newEdge = new AdjKeyEdge<>(linkKey, nodeKeyTo, linkValue);
			//retrieve all edges connected to node1
			AdjKeyVertex<VK, VV, EK, EV> vertexFrom = findVertexByKey(nodeKeyFrom);
			AdjKeyEdge<VK, EK, EV>[] eList1 = vertexFrom.getNeighbors();
			//check that edge does not exists
			for (int i = 0; i < eList1.length; i++) {
				if (newEdge.equals(eList1[i])) {
					throw new DuplicateEdgeException(vertexFrom, newEdge);
				}
			}
			vertexFrom.addEdgeLast(newEdge);
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

	/** @see ComparableKeysGraph#addLinkLast
	 */
	public void addLinkLast(
		VK nodeKeyFrom,
		VK nodeKeyTo,
		EK linkKey)
		throws
			NodeNotFoundException,
			DuplicateLinkException,
			GraphCorruptedException {
		this.addLinkLast(nodeKeyFrom, nodeKeyTo, linkKey, null);
	}

	/** Method to check the contents of the graph
	 * @return true if the graph does not contains any node
	 */

	public boolean isEmpty() {
		return this.vertices.isEmpty();
	}

	/** @see graph.ComparableKeysGraph#linksIterator
	 */
	public LinksIterator<VK, VV, EK, EV> linksKeysIterator() throws GraphException {

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
			AdjKeyEdge<VK, EK, EV>[] edges = vertex.getNeighbors();
			for (int i = 0; i < edges.length; i++) {
				v.add(edges[i].getUserValue());
			}
		}
		return v;
	}

	/** Removes all links between the given nodes.
	 */
	public void removeAllLinksBetween(VK nodeKeyFrom, VK nodeKeyTo)
		throws GraphException {
		AdjKeyVertex<VK, VV, EK, EV> from = this.findVertexByKey(nodeKeyFrom);
		AdjKeyEdge<VK, EK, EV>[] edges = from.getEdgeTo(nodeKeyTo);
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

		AdjKeyVertex<VK, VV, EK, EV> vertexFrom = findVertexByKey(nodeFromKey);
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

		AdjKeyVertex<VK, VV, EK, EV> v = this.findVertexByKey(nodeKey);
		this.numberOfEdges -= v.getCountEdges();
		//remove all links to the node
		Iterator<AdjKeyVertex<VK, VV, EK, EV>> vertexIter = vertices.values().iterator();
		while (vertexIter.hasNext()) {
			AdjKeyVertex<VK, VV, EK, EV> vertex = vertexIter.next();
			AdjKeyEdge<VK, EK, EV>[] edges = vertex.getEdgeTo(nodeKey);
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
		AdjKeyVertex<VK, VV, EK, EV> vertex = findVertexByKey(nodeKey);
		vertex.setVisitCounts(count);
	}

	/** @see ComparableKeysGraph#tryAddNode
	 */
	public Object tryAddNode(VK nodeKey, VV nodeValue) {

		try {
			addNode(nodeKey, nodeValue);
		} catch (GraphException e) {
		}
		return null;
	}

	/** @see ComparableKeysGraph#tryAddLinkFirst
	 */
	public void tryAddLinkFirst(
		VK nodeFrom,
		VK nodeTo,
		EK linkKey,
		EV linkValue)
		throws NodeNotFoundException, GraphCorruptedException {

		try {
			addLinkFirst(nodeFrom, nodeTo, linkKey, linkValue);
		} catch (DuplicateLinkException e) {
			//No exception raised
		}
	}

	/** @see ComparableKeysGraph#tryAddLinkLast
	 */
	public void tryAddLinkLast(
		VK nodeFromKey,
		VK nodeToKey,
		EK linkKey,
		EV linkValue)
		throws NodeNotFoundException, GraphCorruptedException {

		try {
			addLinkLast(nodeFromKey, nodeToKey, linkKey, linkValue);
		} catch (DuplicateLinkException e) {
		}
	}

	/** @see ComparableKeysGraph#getOutgoingLinkValue(java.lang.Object, java.lang.Object)
	 */
	public EV getOutgoingLinkValue(VK nodeKeyFrom, EK linkKey)
		throws NodeNotFoundException {
		EV result = null;
		AdjKeyVertex<VK, VV, EK, EV> vertex = this.findVertexByKey(nodeKeyFrom);
		AdjKeyEdge<VK, EK, EV> edge = vertex.getEdge(linkKey);
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
			AdjKeyEdge<VK, EK, EV>[]edges = vertex.getNeighbors();
			for(int i=0; i < edges.length; i++){
				VK vertexKey = edges[i].getNextVertexKey();
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

}