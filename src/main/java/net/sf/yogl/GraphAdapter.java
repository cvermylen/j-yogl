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

import java.util.Collection;

import net.sf.yogl.exceptions.DuplicateLinkException;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.impl.ImplementationGraph;

/** The Adapter defines 3 methods that can't be overloaded, and 6 that can
 * be overloaded.
 */
public class GraphAdapter implements AbstractGraph {

	//refer to the actula implementation of the graph
	protected ImplementationGraph graph = null;

	public GraphAdapter(AbstractGraph graph) {
		if(graph instanceof ImplementationGraph)
		this.graph = (ImplementationGraph)graph;
	}

	protected final ImplementationGraph getImplementation(){
		return graph;
	}
	
	/** @see graph.AbstractGraph#getCountNodes
	 */
	public final int getNodeCount() {

		return graph.getNodeCount();
	}

	/** @see graph.AbstractGraph#getCountLinks
	 */
	public final int getLinkCount() {

		return graph.getLinkCount();
	}

	/** @see graph.AbstractGraph#getLinks
	 */
	public Object[] getOutgoingLinksKeys(Object nodeKeyFrom)
		throws GraphException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");

		return graph.getOutgoingLinksKeys(nodeKeyFrom);
	}

	/** @see graph.AbstractGraph#getLinks
	 */
	public Object[] getLinksKeysBetween(Object nodeKeyFrom, Object nodeKeyTo)
		throws NodeNotFoundException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Parameter 'to' is null");

		return graph.getLinksKeysBetween(nodeKeyFrom, nodeKeyTo);
	}

	public Object[][] getOutgoingLinksKeysAndNodesKeys(Object nodeKeyTo)
		throws GraphException {
		return graph.getOutgoingLinksKeysAndNodesKeys(nodeKeyTo);
	}

	/** @see graph.AbstractGraph#getNeighbors
	 */
	public Object[] getSuccessorNodesKeys(Object nodeKeyFrom, int steps)
		throws GraphException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'node' is null");

		return graph.getSuccessorNodesKeys(nodeKeyFrom, steps);
	}

	/** @see graph.AbstractGraph#getNeighbors
	 */
	public Object[] getSuccessorNodesKeys(Object nodeKeyFrom, Object link)
		throws GraphException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'node' is null");
		if (link == null)
			throw new NullPointerException("Parameter 'link' is null");

		return graph.getSuccessorNodesKeys(nodeKeyFrom, link);
	}

	/** @see graph.AbstractGraph#getNodes
	 */
	public final Object[] getNodesKeys() throws GraphCorruptedException {

		return graph.getNodesKeys();
	}

	/** @see graph.AbstractGraph#getNodes
	 */
	public final Object[] getNodesKeys(int nodeType)
		throws GraphCorruptedException {

		return graph.getNodesKeys(nodeType);
	}

	public final Object getNodeValue(Object nodeKey) {
		return graph.getNodeValue(nodeKey);
	}

	/** @see graph.AbstractGraph#getPredecessors
	 */
	public Object[] getPredecessorNodesKeys(Object nodeKeyTo)
		throws GraphException {

		if (nodeKeyTo == null) {
			throw new NodeNotFoundException("Parameter 'node' is null");
		}
		return graph.getPredecessorNodesKeys(nodeKeyTo);
	}

	/** @see graph.AbstractGraph#getPredecessors
	 */
	public Object[] getPredecessorNodesKeys(Object nodeKeyTo, Object link)
		throws GraphException {

		if ((nodeKeyTo == null) || (link == null)) {
			throw new NodeNotFoundException("Parameter 'node' is null");
		}
		return graph.getPredecessorNodesKeys(nodeKeyTo, link);
	}

	/** @see graph.AbstractGraph#getIncomingLinks
	 */
	public Object[] getIncomingLinksKeys(Object nodeKeyTo)
		throws NodeNotFoundException {
		if (nodeKeyTo == null) {
			throw new NodeNotFoundException("Parameter 'nodeTo' is null");
		}
		return graph.getIncomingLinksKeys(nodeKeyTo);
	}

	/** @see graph.AbstractGraph#getIncomingLinks
	 */
	public Object[][] getIncomingLinksKeysAndNodesKeys(Object nodeKeyTo)
		throws NodeNotFoundException {
		if (nodeKeyTo == null) {
			throw new NodeNotFoundException("Parameter 'nodeTo' is null");
		}
		return graph.getIncomingLinksKeysAndNodesKeys(nodeKeyTo);
	}

	/** @see graph.AbstractGraph#getType
	 */
	public final int getNodeType(Object nodeKey) throws GraphException {

		if (nodeKey == null)
			throw new NodeNotFoundException("Parameter 'node' is null");
		return graph.getNodeType(nodeKey);
	}

	/** @see graph.AbstractGraph#insertLink
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

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Paramater 'to' is null");
		if (linkKey == null)
			throw new NullPointerException("Parameter 'link' is null");

		graph.addLinkLast(nodeKeyFrom, nodeKeyTo, linkKey, linkValue);
	}

	/** @see graph.AbstractGraph#insertLink
		 */
		public void addLinkLast(
			Object nodeKeyFrom,
			Object nodeKeyTo,
			Object linkKey)
			throws
				NodeNotFoundException,
				DuplicateLinkException,
				GraphCorruptedException {

			if (nodeKeyFrom == null)
				throw new NodeNotFoundException("Parameter 'from' is null");
			if (nodeKeyTo == null)
				throw new NodeNotFoundException("Paramater 'to' is null");
			if (linkKey == null)
				throw new NullPointerException("Parameter 'link' is null");

			graph.addLinkLast(nodeKeyFrom, nodeKeyTo, linkKey);
		}
		
	public void tryAddLinkLast(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey,
		Object linkValue)
		throws NodeNotFoundException, GraphCorruptedException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Paramater 'to' is null");
		if (linkKey == null)
			throw new NullPointerException("Parameter 'link' is null");

		graph.tryAddLinkLast(nodeKeyFrom, nodeKeyTo, linkKey, linkValue);
	}

	/** @see graph.AbstractGraph#insertLinkFirst
	 */
	public void addLinkFirst(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey,
		Object linkValue)
		throws
			NodeNotFoundException,
			DuplicateLinkException,
			GraphCorruptedException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Paramater 'to' is null");
		if (linkKey == null)
			throw new NullPointerException("Parameter 'link' is null");

		graph.addLinkFirst(nodeKeyFrom, nodeKeyTo, linkKey, linkValue);
	}

	/** @see graph.AbstractGraph#insertLinkFirst
		 */
		public void addLinkFirst(
			Object nodeKeyFrom,
			Object nodeKeyTo,
			Object linkKey)
			throws
				NodeNotFoundException,
				DuplicateLinkException,
				GraphCorruptedException {

			if (nodeKeyFrom == null)
				throw new NodeNotFoundException("Parameter 'from' is null");
			if (nodeKeyTo == null)
				throw new NodeNotFoundException("Paramater 'to' is null");
			if (linkKey == null)
				throw new NullPointerException("Parameter 'link' is null");

			graph.addLinkFirst(nodeKeyFrom, nodeKeyTo, linkKey);
		}
		
	public void tryAddLinkFirst(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey,
		Object linkValue)
		throws NodeNotFoundException, GraphCorruptedException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Paramater 'to' is null");
		if (linkKey == null)
			throw new NullPointerException("Parameter 'link' is null");

		graph.tryAddLinkFirst(nodeKeyFrom, nodeKeyTo, linkKey, linkValue);
	}

	/** @see graph.AbstractGraph#insertNode
	 * @exception DuplicateVertexException inherited from implementation
	 * @exception NullPointerException if node parameter is null
	 */
	public void addNode(Object nodeKey, Object nodeValue)
		throws GraphException {

		if (nodeValue == null) {
			throw new NodeNotFoundException("Parameter 'node' is null");
		}

		graph.addNode(nodeKey, nodeValue);
	}

	/** @see graph.AbstractGraph#isEmpty
	 */
	public final boolean isEmpty() {

		return graph.isEmpty();
	}

	/** @see graph.AbstractGraph#linksIterator
	 */
	public LinksIterator linksKeysIterator() throws GraphException {

		return graph.linksKeysIterator();
	}

	/** @see graph.AbstractGraph#nodesIterator
	 */
	public Collection nodesValues() {

		return graph.nodesValues();
	}

	/** @see graph.AbstractGraph#nodesIterator
	 */
	public final Collection nodesKeySet() {

		return graph.nodesKeySet();
	}

	/** @see graph.AbstractGraph#removeLink
	 */
	public void removeLink(Object nodeKeyFrom, Object nodeKeyTo, Object link)
		throws GraphException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Paramater 'to' is null");
		if (link == null)
			throw new NullPointerException("Parameter 'link' is null");

		graph.removeLink(nodeKeyFrom, nodeKeyTo, link);
	}

	public void removeAllLinksBetween(Object nodeKeyFrom, Object nodeKeyTo)
		throws GraphException {
		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Paramater 'to' is null");
		graph.removeAllLinksBetween(nodeKeyFrom, nodeKeyTo);
	}

	/** @see graph.AbstractGraph#removeNode
	 */
	public void removeNode(Object nodeKey) throws NodeNotFoundException {
		if (nodeKey == null) {
			throw new NodeNotFoundException("Parameter 'node' is null");
		}

		graph.removeNode(nodeKey);
	}

	public final void setAllVisitCounts(int count) throws GraphException {
		graph.setAllVisitCounts(count);
	}

	public void setVisitCount(Object nodeKey, int passage)
		throws GraphException {

		if (nodeKey == null)
			throw new NodeNotFoundException("Parameter 'node' is null");
		graph.setVisitCount(nodeKey, passage);
	}

	public int getVisitCount(Object nodeKey) throws GraphException {

		if (nodeKey == null)
			throw new NodeNotFoundException("Parameter 'node' is null");
		return graph.getVisitCount(nodeKey);
	}

	public int incVisitCount(Object nodeKey) throws GraphException {

		if (nodeKey == null)
			throw new NodeNotFoundException("Parameter 'node' is null");
		return graph.incVisitCount(nodeKey);
	}

	/** @see graph.AbstractGraph#insertNode
	 *  @exception NullPointerException if node parameter is null
	 */
	public Object tryAddNode(Object nodeKey, Object nodeValue) {

		Object dummy = graph.tryAddNode(nodeKey, nodeValue);
		return nodeValue;
	}

	public int getDepth(Object startingNodeKey) throws GraphException {
		return graph.getDepth(startingNodeKey);
	}

	public final BreadthFirstIterator breadthFirstIterator(
		Object startingNodeKey,
		int maxCycling)
		throws GraphException {
		return graph.breadthFirstIterator(startingNodeKey, maxCycling);
	}

	public boolean[][] getAdjacencyMatrix() throws GraphException {
		return graph.getAdjacencyMatrix();
	}

	public Object clone() {
		return graph.clone();
	}

	public void deepCopy(AbstractGraph dest) throws GraphException {
		graph.deepCopy(dest);
	}

	public final boolean existsNode(Object nodeKey) {
		return graph.existsNode(nodeKey);
	}

	/** @return a list of relations between nodes. The String contains as
	 *  many lines (\n terminated) as the number of links defined in the graph.
	 *  The line is a triple <source node id><link id><dest node id>
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		try {
			Object[] nodes = graph.getNodesKeys();
			for (int i = 0; i < nodes.length; i++) {
				Object[][] el =
					graph.getOutgoingLinksKeysAndNodesKeys(nodes[i]);
				if ((el != null) && (el.length > 0)) {
					for (int j = 0; j < el.length; j++) {
						result.append(nodes[i].toString()).append(" -> ");
						result.append(el[j][0].toString()).append(" -> ");
						result.append(el[j][1].toString()).append("\n");
					}
				} else {
					Object[]inc = graph.getIncomingLinksKeys(nodes[i]);
					if((inc == null) || (inc.length < 1)){
						result.append(nodes[i].toString()).append("- no incoming links -\n");
					}
				}
			}
		} catch (GraphException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public DepthFirstIterator depthFirstIterator(Object startingNodeKey, int maxCycling)
		throws GraphException {
		return graph.depthFirstIterator(startingNodeKey, maxCycling);
	}

	public final Object[] getAllStartNodeKeys() {
		return graph.getAllStartNodeKeys();
	}

	public final boolean isStartNode(Object nodeKey) {
		return graph.isStartNode(nodeKey);
	}

	public Object getOutgoingLinkValue(Object nodeKeyFrom, Object linkKey)
		throws NodeNotFoundException {
		return graph.getOutgoingLinkValue(nodeKeyFrom, linkKey);
	}

	public Collection linksValues() {
		return graph.linksValues();
	}

	public int getMaxInDegree() {
		return graph.getMaxInDegree();
	}

	public int getMaxOutDegree() {
		return graph.getMaxOutDegree();
	}

}
