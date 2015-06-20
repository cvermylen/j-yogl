/* Copyright (C) 2003 Symphonic
   
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.
   
   You should have received a copy of the GNU Library General Public
   License along with this library; if not, write to the Free
   Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
   MA 02111-1307, USA */
   
package net.sf.yogl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import net.sf.yogl.exceptions.DuplicateLinkException;
import net.sf.yogl.exceptions.DuplicateNodeException;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.impl.DuplicateEdgeException;
import net.sf.yogl.impl.Edge;
import net.sf.yogl.impl.ImplementationGraph;
import net.sf.yogl.impl.NodeContainer;
import net.sf.yogl.impl.Vertex;
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

public final class AdjListGraph implements ImplementationGraph {

	/** Since it should be too tedious to traverse the whole graph
	 * just to count the number of edges, this data is stored here.
	 */
	private int numberOfEdges = 0;

	/** 'vertices' contains all vertices of the graph. This vector
	 * is automatically resized when all entries are occupied.
	 */
	Map vertices = null;

	/** Contains the list of all entry points in the graph.
	 *  When a new node is created, it is by default inserted into the set.
	 */
	private HashSet allStartNodeKeys = new HashSet();

	/** basic ctor, no default value
	 */
	public AdjListGraph(Map prototype) throws GraphCorruptedException {
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
	public void addNode(Object nodeKey, Object nodeValue)
		throws DuplicateNodeException {

		if (!existsNode(nodeKey)) {
			Vertex rVertex = new Vertex(nodeValue);
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
	public BreadthFirstIterator breadthFirstIterator(
		Object startingNodeKey,
		int maxCycles)
		throws GraphException {
		return new BreadthFirstIterator(this, startingNodeKey, maxCycles);
	}

	/** @see AbstractGraph#clone
	 */
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	/** @see AbstractGraph#deepCopy
	 */
	public void deepCopy(AbstractGraph dest) throws GraphException {

		Object[] keys = this.getNodesKeys();
		for (int i = 0; i < keys.length; i++) {
			int type = this.getNodeType(keys[i]);
			Object value = this.getNodeValue(keys[i]);
			dest.addNode(keys[i], value);
		}

		LinksIterator iter = this.linksKeysIterator();
		while (iter.hasNext()) {
			Object linkValue = iter.next();
			Object fromKey = iter.getOriginator();
			Object toKey = iter.getDestination();
			dest.addLinkLast(fromKey, toKey, linkValue, null);
		}
	}

	/** @see AbstractGraph#depthFirstIterator
	 */
	public DepthFirstIterator depthFirstIterator(Object node, int maxCycling)
		throws GraphException {
		return new DepthFirstIterator(this, node, maxCycling);
	}

	/** @see AbstractGraph#existsNode
	 */
	public boolean existsNode(Object nodeKey) {
		return vertices.containsKey(nodeKey);
	}

	/** Finds the node in 'vertices' and, if it exists, return the
	 * index in vertices. If it doesn't, throw an exception.
	 * pre-condition: node is non-null
	 * @param node refers to the user-defined node object
	 * @exception VertexNotFoundException  thrown if rhs was not
	 *            found in the graph.
	 */
	private Object findNodeByKey(Object nodeKey) throws NodeNotFoundException {

		Vertex v = findVertexByKey(nodeKey);
		return v.getUserValue();
	}

	/** Finds the node in 'vertices' and, if it exists, return the
	 *  corresponding Vertex from vertices. If it doesn't, throw an exception.
	 *  pre-condition: node is non-null
	 * @param node refers to the user-defined node object
	 * @exception VertexNotFoundException  thrown if rhs was not
	 *            found in the graph.
	 */
	public Vertex findVertexByKey(Object nodeKey)
		throws NodeNotFoundException {
		if (!vertices.containsKey(nodeKey))
			throw new NodeNotFoundException(nodeKey.toString());
		return (Vertex) vertices.get(nodeKey);
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
	public Object[] getLinksKeysBetween(Object nodeFromKey, Object nodeToKey)
		throws NodeNotFoundException {

		ArrayList result = new ArrayList();
		Vertex p = findVertexByKey(nodeFromKey);
		//retrieve all edges
		Edge[] eList = p.getNeighbors();
		for (int i = 0; i < eList.length; i++) {
			// compare references, not the values
			if (eList[i]
				.getNextVertexKey()
				.equals(findVertexByKey(nodeToKey))) {
				Object o = eList[i].getUserValue();
				if (o instanceof NodeContainer) {
					o = ((NodeContainer) o).getValue();
				}
				result.add(o);
			}
		}
		return result.toArray();
	}

	/** Build the list of all entry points in the graph.
	 *  An entry point is a node with no 'predecessor'.
	 */
	private void buildAllStartNodeKeys() {
		HashSet result = new HashSet(vertices.keySet());
		Iterator entriesIter = vertices.entrySet().iterator();
		while (entriesIter.hasNext()) {
			Map.Entry entry = (Map.Entry) entriesIter.next();
			Vertex v = (Vertex) entry.getValue();
			Edge[] successors = v.getNeighbors();
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
	public Object[] getAllStartNodeKeys() {
		return allStartNodeKeys.toArray();
	}

	/** @return true if the node is an entry point in the graph.
	 * @param nodeKey identify the node.
	 */
	public boolean isStartNode(Object nodeKey) {
		return allStartNodeKeys.contains(nodeKey);
	}

	/** Return the depth of the graph. This method may perform a full
	 * traversal of the graph (depend on the implementation). The result
	 * can be interpreted as the maximum distance between the root node
	 * and any other accessible node.
	 * The root node is at level 0.
	 */
	public int getDepth(Object startingNode) throws GraphException {
		throw new GraphException("Not yet implemented");
	}

	/** Returns the links that have as destination the given node. This
	 *  method looks for 'predecessor' nodes and their relation to the
	 *  given node.
	 */
	public Object[] getIncomingLinksKeys(Object nodeToKey)
		throws NodeNotFoundException {
		ArrayList result = new ArrayList();
		Vertex to = findVertexByKey(nodeToKey);
		Iterator iter = vertices.values().iterator();
		while (iter.hasNext()) {
			Vertex vertex = (Vertex) iter.next();
			Edge[] edge = vertex.getEdgeTo(nodeToKey);
			if (null != edge) {
				for (int i = 0; i < edge.length; i++) {
					result.add(edge[i].getEdgeKey());
				}
			}
		}
		return result.toArray();
	}

	/** Return the list of direct predecessors and their associated
	 * link
	 * x[0][i] = node; x[1][i] = link
	 */
	public Object[][] getIncomingLinksKeysAndNodesKeys(Object nodeToKey)
		throws NodeNotFoundException {
		ArrayList result = new ArrayList();
		Iterator nodesIter = this.vertices.entrySet().iterator();
		while (nodesIter.hasNext()) {
			Map.Entry entry = (Map.Entry) nodesIter.next();
			Vertex vertex = (Vertex) entry.getValue();
			Edge[] edges = vertex.getEdgeTo(nodeToKey);
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

	/** @see AbstractGraph#getNodeKeys
	 */
	public Object[] getNodesKeys() throws GraphCorruptedException {

		return this.getNodesKeys((int) 0xFFFFFFFF);
	}

	/** @see AbstractGraph#getNodes
	 */
	public Object[] getNodesKeys(int nodeType) throws GraphCorruptedException {

		ArrayList list = new ArrayList();
		try {
			Iterator iter = vertices.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Vertex vertex = (Vertex) entry.getValue();
				if ((getNodeType(key) & nodeType) != 0) {
					if ((getNodeType(key) == VertexType.START)
						|| (getNodeType(key) == VertexType.STARTEND)) {
						list.add(0, key);
					} else {
						list.add(key);
					}
				}
			}
			return list.toArray();
		} catch (NodeNotFoundException e1) {
			throw new GraphCorruptedException(e1);
		}
	}

	/** @see AbstractGraph#getType
	 */
	public int getNodeType(Object nodeKey) throws NodeNotFoundException {
		if (this.allStartNodeKeys.contains(nodeKey)) {
			Vertex vertex = findVertexByKey(nodeKey);
			if ((vertex.getNeighbors() == null)
				|| (vertex.getNeighbors().length == 0)) {
				return VertexType.STARTEND;
			} else {
				return VertexType.START;
			}
		} else {
			Vertex vertex = findVertexByKey(nodeKey);
			if ((vertex.getNeighbors() == null)
				|| (vertex.getNeighbors().length == 0)) {
				return VertexType.END;
			} else {
				return VertexType.NONE;
			}
		}
	}

	/** @see AbstractGraph#getNodeValue
	 */
	public Object getNodeValue(Object nodeKey) {
		Vertex v = (Vertex) this.vertices.get(nodeKey);
		if (v == null)
			return null;
		return v.getUserValue();
	}

	/** @see AbstractGraph#getOutgoingLinksKeys
	 */
	public Object[] getOutgoingLinksKeys(Object nodeFromKey)
		throws GraphException {

		ArrayList result = new ArrayList();
		Vertex p = findVertexByKey(nodeFromKey);
		//retrieve all edges
		Edge[] eList = p.getNeighbors();
		if (eList != null) {
			for (int i = 0; i < eList.length; i++) {
				// compare references, not the values
				Object value = eList[i].getUserValue();
				if (value instanceof NodeContainer) {
					value = ((NodeContainer) value).getValue();
				}
				result.add(value);
			}
		}
		return result.toArray();
	}

	/** Returns an array of all outgoing edges and the associated
	 * destination node.
	 */
	public Object[][] getOutgoingLinksKeysAndNodesKeys(Object nodeKeyFrom)
		throws GraphException {
		Vertex v = this.findVertexByKey(nodeKeyFrom);
		Edge[] edges = v.getNeighbors();
		Object[][] result = new Object[edges.length][2];
		for (int i = 0; i < edges.length; i++) {
			result[i][0] = edges[i].getEdgeKey();
			result[i][1] = edges[i].getNextVertexKey();
		}
		return result;
	}

	/** return all predecessor nodes to a given node.
	 */
	public Object[] getPredecessorNodesKeys(Object nodeToKey)
		throws GraphException {

		ArrayList result = new ArrayList();
		Vertex to = (Vertex) findVertexByKey(nodeToKey);
		Iterator iter = vertices.keySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Vertex vertex = (Vertex) entry.getValue();
			if (null != (vertex.getEdgeTo(to))) {
				result.add(entry.getKey());
			}
		}
		return result.toArray();
	}

	/** Return all predecessor nodes for a given node and link
	 */
	public Object[] getPredecessorNodesKeys(Object nodeToKey, Object link) {
		ArrayList result = new ArrayList();
		Iterator iter = vertices.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Vertex vertex = (Vertex) entry.getValue();
			Edge[] edges = vertex.getEdgeTo(nodeToKey);
			if ((null != edges) && (edges.length > 0)) {
				if (edges[0].getUserValue().equals(link)) {
					result.add(entry.getKey());
				}
			}
		}
		return result.toArray();
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
	public Object[] getSuccessorNodesKeys(Object nodeFromKey, int steps)
		throws GraphException {
		ArrayList result = new ArrayList();
		if (steps == 1) {
			Vertex p = findVertexByKey(nodeFromKey);
			Edge[] eList = p.getNeighbors();
			if (eList != null) {
				for (int i = 0; i < eList.length; i++) {
					result.add(eList[i].getNextVertexKey());
				}
			}
		} else {
			throw new GraphException("Not yet implemented");
		}
		return result.toArray();
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
	public Object[] getSuccessorNodesKeys(Object nodeFromKey, Object link)
		throws GraphException {
		ArrayList result = new ArrayList();
		Vertex p = findVertexByKey(nodeFromKey);
		Edge[] eList = p.getNeighbors();
		if (eList != null) {
			for (int i = 0; i < eList.length; i++) {
				result.add(eList[i].getNextVertexKey());
			}
		}
		return result.toArray();
	}

	/** Returns a list of Vertex which are directly adjacent to the node.
	 */
	public Vertex[] getSuccessorVertices(Vertex node)
		throws NodeNotFoundException {
		Vertex[] result = null;
		Edge[] edges = node.getNeighbors();
		result = new Vertex[edges.length];
		for (int i = 0; i < edges.length; i++) {
			result[i] = findVertexByKey(edges[i].getNextVertexKey());
		}
		return result;
	}

	/** Return a list of Vertex which are immediately adjacent to the given
	 *  Vertex
	 */
	public Vertex[] getSuccessorVertices(Vertex vertex, Object link)
		throws NodeNotFoundException {
		ArrayList result = new ArrayList();
		Edge[] eList = vertex.getNeighbors();
		if (eList != null) {
			result = new ArrayList();
			//loop for each edge connected to the vertex

			for (int i = 0; i < eList.length; i++) {
				Object edgeValue = eList[i].getUserValue();
				if (edgeValue instanceof NodeContainer) {
					edgeValue = ((NodeContainer) edgeValue).getValue();
				}
				if ((link == null)
					|| ((edgeValue != null) && (edgeValue.equals(link)))) {
					Vertex v1 = findVertexByKey(eList[i].getNextVertexKey());
					result.add(v1);
				}
			}
		}
		return (Vertex[]) result.toArray(new Vertex[result.size()]);
	}

	/** @see AbstractGraph#getPassage
	 */
	public int getVisitCount(Object nodeKey) throws GraphException {

		Vertex vertex = findVertexByKey(nodeKey);
		return vertex.getTraversals();
	}

	/** @see AbstractGraph#incPassage
	 */
	public int incVisitCount(Object nodeKey) throws GraphException {

		Vertex vertex = findVertexByKey(nodeKey);
		return vertex.incTraversals();
	}

	/** @see AbstractGraph#addLinkFirst
	 */
	public void addLinkFirst(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey,
		Object linkValue)
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
			Edge newEdge = new Edge(linkKey, nodeKeyTo, linkValue);
			//retrieve all edges connected to node1
			Vertex vertexFrom = findVertexByKey(nodeKeyFrom);
			Edge[] eList1 = vertexFrom.getNeighbors();
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

	/** @see AbstractGraph#addLinkFirst
	 */
	public void addLinkFirst(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey)
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
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey,
		Object linkValue)
		throws
			NodeNotFoundException,
			DuplicateLinkException,
			GraphCorruptedException {

		if (!this.existsNode(nodeKeyTo))
			throw new NodeNotFoundException(nodeKeyTo.toString());
		try {
			//create the new edge
			Edge newEdge = new Edge(linkKey, nodeKeyTo, linkValue);
			//retrieve all edges connected to node1
			Vertex vertexFrom = findVertexByKey(nodeKeyFrom);
			Edge[] eList1 = vertexFrom.getNeighbors();
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

	/** @see AbstractGraph#addLinkLast
	 */
	public void addLinkLast(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey)
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

	/** @see graph.AbstractGraph#linksIterator
	 */
	public LinksIterator linksKeysIterator() throws GraphException {

		return new LinksIterator(this);
	}

	/** Method to retrieve all nodes, via an iterator. Return the 'user value'
	 */
	public Collection nodesKeySet() {

		LinkedList nodes = new LinkedList();
		Iterator vertexIter = vertices.keySet().iterator();
		while (vertexIter.hasNext()) {
			Object key = vertexIter.next();
			nodes.addLast(key);
		}
		return nodes;
	}

	/** Method to retrieve all nodes, via an iterator. Return the 'user value'
	 */
	public Collection nodesValues() {

		LinkedList nodes = new LinkedList();
		Iterator vertexIter = vertices.values().iterator();
		while (vertexIter.hasNext()) {
			Vertex vertex = (Vertex) vertexIter.next();
			nodes.addLast(vertex.getUserValue());
		}
		return nodes;
	}

	/** @see AbstractGraph#linksValues()
	 */
	public Collection linksValues() {
		Vector v = new Vector();
		Iterator verticesIter = vertices.values().iterator();
		while (verticesIter.hasNext()) {
			Vertex vertex = (Vertex) verticesIter.next();
			Edge[] edges = vertex.getNeighbors();
			for (int i = 0; i < edges.length; i++) {
				v.add(edges[i].getUserValue());
			}
		}
		return v;
	}

	/** Removes all links between the given nodes.
	 */
	public void removeAllLinksBetween(Object nodeKeyFrom, Object nodeKeyTo)
		throws GraphException {
		Vertex from = this.findVertexByKey(nodeKeyFrom);
		Edge[] edges = from.getEdgeTo(nodeKeyTo);
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
		Object nodeFromKey,
		Object nodeToKey,
		Object linkKey)
		throws GraphException {

		Vertex vertexFrom = findVertexByKey(nodeFromKey);
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
	public void removeNode(Object nodeKey) throws NodeNotFoundException {

		Vertex v = this.findVertexByKey(nodeKey);
		this.numberOfEdges -= v.getCountEdges();
		//remove all links to the node
		Iterator vertexIter = vertices.values().iterator();
		while (vertexIter.hasNext()) {
			Vertex vertex = (Vertex) vertexIter.next();
			Edge[] edges = vertex.getEdgeTo(nodeKey);
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

	/** @see AbstractGraph#setPassage
	 */
	public void setAllVisitCounts(int count) throws GraphException {
		Iterator nodeValuesIter = this.vertices.values().iterator();
		while (nodeValuesIter.hasNext()) {
			Vertex vertex = (Vertex) nodeValuesIter.next();
			vertex.setTraversals(count);
		}
	}

	/** @see AbstractGraph#setPassage
	 */
	public void setVisitCount(Object nodeKey, int count)
		throws GraphException {
		Vertex vertex = findVertexByKey(nodeKey);
		vertex.setTraversals(count);
	}

	/** @see AbstractGraph#tryAddNode
	 */
	public Object tryAddNode(Object nodeKey, Object nodeValue) {

		try {
			addNode(nodeKey, nodeValue);
		} catch (GraphException e) {
		}
		return null;
	}

	/** @see AbstractGraph#tryAddLinkFirst
	 */
	public void tryAddLinkFirst(
		Object nodeFrom,
		Object nodeTo,
		Object linkKey,
		Object linkValue)
		throws NodeNotFoundException, GraphCorruptedException {

		try {
			addLinkFirst(nodeFrom, nodeTo, linkKey, linkValue);
		} catch (DuplicateLinkException e) {
			//No exception raised
		}
	}

	/** @see AbstractGraph#tryAddLinkLast
	 */
	public void tryAddLinkLast(
		Object nodeFromKey,
		Object nodeToKey,
		Object linkKey,
		Object linkValue)
		throws NodeNotFoundException, GraphCorruptedException {

		try {
			addLinkLast(nodeFromKey, nodeToKey, linkKey, linkValue);
		} catch (DuplicateLinkException e) {
		}
	}

	/** @see AbstractGraph#getOutgoingLinkValue(java.lang.Object, java.lang.Object)
	 */
	public Object getOutgoingLinkValue(Object nodeKeyFrom, Object linkKey)
		throws NodeNotFoundException {
		Object result = null;
		Vertex vertex = this.findVertexByKey(nodeKeyFrom);
		Edge edge = vertex.getEdge(linkKey);
		if (edge != null)
			result = edge.getUserValue();
		return result;
	}

	public int getMaxInDegree() {
		int max = 0;
		HashMap counts = new HashMap();
		Iterator verticesIter = vertices.values().iterator();
		while(verticesIter.hasNext()){
			Vertex vertex = (Vertex)verticesIter.next();
			Edge[]edges = vertex.getNeighbors();
			for(int i=0; i < edges.length; i++){
				Object vertexKey = edges[i].getNextVertexKey();
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
		Iterator verticesIter = vertices.values().iterator();
		while(verticesIter.hasNext()){
			Vertex vertex = (Vertex)verticesIter.next();
			max = Math.max(max, vertex.getCountEdges());
		}
		return max;
	}

}