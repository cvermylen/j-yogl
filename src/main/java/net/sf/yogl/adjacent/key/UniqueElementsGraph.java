package net.sf.yogl.adjacent.key;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.sf.yogl.Graph;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.iterators.BreadthFirstIterator;
import net.sf.yogl.iterators.DepthFirstIterator;
import net.sf.yogl.iterators.LinksIterator;
import net.sf.yogl.types.VertexType;

public class UniqueElementsGraph<VC extends InternalKeyVertex<VC, TC, VK, EK>, TC extends InternalKeyEdge<TC, VC, EK, VK>, VK extends Comparable<VK>, EK extends Comparable<EK>> 
		implements Graph<VC, TC>{
	/** Since it should be too tedious to traverse the whole graph
	 * just to count the number of edges, this data is stored here.
	 */
	private int numberOfEdges = 0;

	/** 'vertices' contains all vertices of the graph. This vector
	 * is automatically resized when all entries are occupied.
	 */
	protected Map<VK, VC> vertices = new HashMap<>();

	/** Contains the list of all entry points in the graph.
	 *  When a new node is created, it is by default inserted into the set.
	 */
	private HashSet<VK> allStartNodeKeys = new HashSet<VK>();

	/** basic ctor, no default value
	 */
	public UniqueElementsGraph(){}

	/** Will duplicate the content of the vertex and insert it into this graph.
	 * Method is used to copy vertices from graph to graph
	 */
	public VC addRootVertex (VC vertex, boolean isRoot) {
		VC result = null;
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
	public BreadthFirstIterator<VC, TC> breadthFirstIterator(
			int maxCycles) throws NodeNotFoundException {
		return new BreadthFirstIterator<VC, TC>(this, maxCycles);
	}

	/** @see ComparableKeysGraph#clone
	 */
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	/** @see ComparableKeysGraph#depthFirstIterator
	 */
	public DepthFirstIterator<VC, TC> depthFirstIterator(Collection<VC> startVertices, 
			int maxCycling)
		throws GraphException {
		return new DepthFirstIterator<VC, TC>(startVertices, maxCycling);
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
	public VC findVertexByKey(VK nodeKey)
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
	public List<TC> getLinksKeysBetween(VC fromVertex, VC toVertex)
		throws NodeNotFoundException {

		ArrayList<TC> result = new ArrayList<>();
		Iterator<TC> edgesIter = fromVertex.getOutgoingEdges().iterator();
		while (edgesIter.hasNext()) {
			TC edge = edgesIter.next();
			if (edge.getToVertex().equals(toVertex)) {
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
		Iterator<VC> entriesIter = vertices.values().iterator();
		while (entriesIter.hasNext()) {
			Iterator<TC> successorsIter = entriesIter.next().getOutgoingEdges().iterator();
			while (successorsIter.hasNext()) {
				VK next = successorsIter.next().getToVertex().getKey();
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
	 * @param vertex identify the node.
	 */
	@Override
	public boolean isStartVertex(VC vertex) {
		return allStartNodeKeys.contains(vertex.getKey());
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
	public List<VC> getVertices(VertexType type) throws NodeNotFoundException {

		ArrayList<VC> list = new ArrayList<>();
		Iterator<VC> iter = vertices.values().iterator();
		while (iter.hasNext()) {
			VC vertex = iter.next();
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

	public VC getVertex (VK key) {
		return this.vertices.get(key);
	}
	
	/** @see ComparableKeysGraph#getType
	 */
	public VertexType getVertexType(VC vertex) {
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

	/** return all predecessor nodes to a given node.
	 */
	public Collection<VC> getPredecessorVertices(VC toVertex) {

		Map<VK, VC> result = new HashMap<>();
		Iterator<VC> allVerticesIter = vertices.values().iterator();
		while (allVerticesIter.hasNext()) {
			Iterator<TC> edgeIter = allVerticesIter.next().getOutgoingEdges().iterator();
			while (edgeIter.hasNext()) {
				VC vertexCandidate = edgeIter.next().getToVertex();
				if (toVertex.getKey().equals(vertexCandidate.getKey())) {
					result.put(vertexCandidate.getKey(), vertexCandidate);
				}
			}
		}
		return result.values();
	}

	/** Return all predecessor nodes for a given node and link
	 */
	public VC getPredecessorVertex(VC destVertex, TC edge) {
		VC sourceVertex = null;
		Iterator<Map.Entry<VK, VC>> iter = vertices.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<VK, VC> entry = iter.next();
			VC vertex = entry.getValue();
			Iterator<TC> edgesIter = vertex.getOutgoingEdges().iterator();
			while (edgesIter.hasNext()) {
				TC testedEdge = edgesIter.next();
				if (testedEdge.equals(edge) && (testedEdge.getToVertex().getKey().equals(destVertex.getKey()))) {
					sourceVertex = vertex;
					break;
				}
			}
		}
		return sourceVertex;
	}

	/** Returns a list of Vertex which are directly adjacent to the node.
	 */
	public List<VC> getSuccessorVertices(VC node) {
		Iterator<TC> edgesIter = node.getOutgoingEdges().iterator();
		List<VC>result = new ArrayList<>();
		while (edgesIter.hasNext()) {
			result.add(edgesIter.next().getToVertex());
		}
		return result;
	}

	/** @see ComparableKeysGraph#getPassage
	 */
	public int getVisitCount(VK nodeKey) throws GraphException {

		VC vertex = vertices.get(nodeKey);
		return vertex.getVisitsCount();
	}

	/** @see ComparableKeysGraph#incPassage
	 */
	public int incVisitCount(VK nodeKey) throws GraphException {

		VC vertex = vertices.get(nodeKey);
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
	public LinksIterator<VC, TC> linksKeysIterator() throws GraphException {

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

	/** Removes all links between the given nodes.
	 */
	public void removeAllEdgesBetween(VC vertexFrom, VC vertexTo)
		throws GraphException {
		Iterator<TC> edgesIter = vertexFrom.getOutgoingEdges().iterator();
		while (edgesIter.hasNext()) {
			TC edgeCandidate = edgesIter.next();
			if (edgeCandidate.getToVertex().equals(vertexTo)) {
				vertexFrom.removeEdge(edgeCandidate);
			}
			numberOfEdges--;
		}
		buildAllStartNodeKeys();
	}

	/** This method will mark the vertex as unused. The list of neighbors
	 * is flushed. There is (currently) no reference counting associated
	 * to the vertex. As a consequence, if a vertex is not accessible
	 * anymore, it is not removed. Also, all incoming edges to the
	 * removed vertex are removed.
	 * @param node user-defined to be removed
	 */
	//Refactor: This method should be recursive, as removing 1 vertex may leave other nodes inaccessibles, which in turn should be removed as well.
	public void removeVertex(VC vertexToBeRemoved) throws NodeNotFoundException {

		Iterator<VC> vertexIter = vertices.values().iterator();
		while (vertexIter.hasNext()) {
			VC vertex = vertexIter.next();
			List<TC> edgesToBeRemoved = new ArrayList<>();
			Iterator<TC> edgesIter = vertex.getOutgoingEdges().iterator();
			while (edgesIter.hasNext()) {
				TC edge = edgesIter.next();
				if (edge.getToVertex().equals(vertexToBeRemoved)) {
					edgesToBeRemoved.add(edge);
				}
				Iterator<TC>tbrIter = edgesToBeRemoved.iterator();
				while (tbrIter.hasNext()) {
					TC tbr = tbrIter.next();
					VC v2 = tbr.getToVertex();
					int incEdges = v2.decrementIncomingEdges();
					vertex.removeEdge(tbr);
					if (incEdges == 0) {
						removeVertex(v2);
					}
				}
			}
		}
		this.vertices.remove(vertexToBeRemoved.getKey());
		buildAllStartNodeKeys();
	}

	/** @see ComparableKeysGraph#setPassage
	 */
	public void setAllVisitCounts(int count) throws GraphException {
		Iterator<VC> nodeValuesIter = this.vertices.values().iterator();
		while (nodeValuesIter.hasNext()) {
			VC vertex = nodeValuesIter.next();
			vertex.setVisitCounts(count);
		}
	}

	/** @see ComparableKeysGraph#setPassage
	 */
	public void setVisitCount(VK nodeKey, int count)
		throws GraphException {
		VC vertex = vertices.get(nodeKey);
		vertex.setVisitCounts(count);
	}

	public int getMaxInDegree() {
		int max = 0;
		HashMap<VK, Integer> counts = new HashMap<>();
		Iterator<VC> verticesIter = vertices.values().iterator();
		while(verticesIter.hasNext()){
			VC vertex = verticesIter.next();
			Iterator<TC>edgesIter = vertex.getOutgoingEdges().iterator();
			while(edgesIter.hasNext()){
				VK vertexKey = edgesIter.next().getToVertex().getKey();
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
		Iterator<VC> verticesIter = vertices.values().iterator();
		while(verticesIter.hasNext()){
			VC vertex = verticesIter.next();
			max = Math.max(max, vertex.getOutgoingEdges().size());
		}
		return max;
	}

	@Override
	public Collection<VC> getRoots() {
		return this.allStartNodeKeys.stream().map(vk -> vertices.get(vk)).collect(Collectors.toList());
	}

	@Override
	public void clearAllVisitCounts() {
		vertices.values().stream().forEach(vertex -> vertex.clearVisitsCount());
		
	}
}
