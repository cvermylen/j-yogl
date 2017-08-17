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
import net.sf.yogl.impl.DuplicateVertexException;
import net.sf.yogl.impl.ImplementationGraph;
import net.sf.yogl.impl.Vertex;

/** The Adapter defines 3 methods that can't be overloaded, and 6 that can
 * be overloaded.
 */
public class GraphAdapter<VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> implements Graph<VK, VV, EK, EV> {

	//refer to the actual implementation of the graph
	protected ImplementationGraph<VK, VV, EK, EV> graph = null;

	protected final ImplementationGraph<VK, VV, EK, EV> getImplementation(){
		return graph;
	}
	
	/** @see graph.Graph#getCountNodes
	 */
	public final int getNodeCount() {

		return graph.getNodeCount();
	}

	/** @see graph.Graph#getCountLinks
	 */
	public final int getLinkCount() {

		return graph.getLinkCount();
	}

	/** @see graph.Graph#getLinks
	 */
	public EK[] getOutgoingLinksKeys(VK nodeKeyFrom)
		throws GraphException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");

		return graph.getOutgoingLinksKeys(nodeKeyFrom);
	}

	/** @see graph.Graph#getLinks
	 */
	public EK[] getLinksKeysBetween(VK nodeKeyFrom, VK nodeKeyTo)
		throws NodeNotFoundException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Parameter 'to' is null");

		return graph.getLinksKeysBetween(nodeKeyFrom, nodeKeyTo);
	}

	public Object[][] getOutgoingLinksKeysAndNodesKeys(VK nodeKeyTo)
		throws GraphException {
		return graph.getOutgoingLinksKeysAndNodesKeys(nodeKeyTo);
	}

	/** @see graph.Graph#getNeighbors
	 */
	public VK[] getSuccessorNodesKeys(VK nodeKeyFrom, int steps)
		throws GraphException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'node' is null");

		return graph.getSuccessorNodesKeys(nodeKeyFrom, steps);
	}

	/** @see graph.Graph#getNeighbors
	 */
	public VK[] getSuccessorNodesKeys(VK nodeKeyFrom, EK link)
		throws GraphException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'node' is null");
		if (link == null)
			throw new NullPointerException("Parameter 'link' is null");

		return graph.getSuccessorNodesKeys(nodeKeyFrom, link);
	}

	/** @see graph.Graph#getNodes
	 */
	public final VK[] getNodesKeys() throws GraphCorruptedException {

		return graph.getNodesKeys();
	}

	/** @see graph.Graph#getNodes
	 */
	public final Collection<Vertex<VK,VV,EK,EV>> getVertices(int nodeType)
		throws GraphCorruptedException {

		return graph.getVertices(nodeType);
	}

	public final VV getNodeValue(VK nodeKey) {
		return graph.getNodeValue(nodeKey);
	}

	/** @see graph.Graph#getPredecessors
	 */
	public VK[] getPredecessorNodesKeys(VK nodeKeyTo)
		throws GraphException {

		if (nodeKeyTo == null) {
			throw new NodeNotFoundException("Parameter 'node' is null");
		}
		return graph.getPredecessorNodesKeys(nodeKeyTo);
	}

	/** @see graph.Graph#getPredecessors
	 */
	public VK[] getPredecessorNodesKeys(VK nodeKeyTo, EK link)
		throws GraphException {

		if ((nodeKeyTo == null) || (link == null)) {
			throw new NodeNotFoundException("Parameter 'node' is null");
		}
		return graph.getPredecessorNodesKeys(nodeKeyTo, link);
	}

	/** @see graph.Graph#getIncomingLinks
	 */
	public EK[] getIncomingLinksKeys(VK nodeKeyTo)
		throws NodeNotFoundException {
		if (nodeKeyTo == null) {
			throw new NodeNotFoundException("Parameter 'nodeTo' is null");
		}
		return graph.getIncomingLinksKeys(nodeKeyTo);
	}

	/** @see graph.Graph#getIncomingLinks
	 */
	public Object[][] getIncomingLinksKeysAndNodesKeys(VK nodeKeyTo)
		throws NodeNotFoundException {
		if (nodeKeyTo == null) {
			throw new NodeNotFoundException("Parameter 'nodeTo' is null");
		}
		return graph.getIncomingLinksKeysAndNodesKeys(nodeKeyTo);
	}

	/** @see graph.Graph#getType
	 */
	public final int getNodeType(VK nodeKey) throws GraphException {

		if (nodeKey == null)
			throw new NodeNotFoundException("Parameter 'node' is null");
		return graph.getNodeType(nodeKey);
	}

	/** @see graph.Graph#insertLink
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

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Paramater 'to' is null");
		if (linkKey == null)
			throw new NullPointerException("Parameter 'link' is null");

		graph.addLinkLast(nodeKeyFrom, nodeKeyTo, linkKey, linkValue);
	}

	/** @see graph.Graph#insertLink
		 */
		public void addLinkLast(
			VK nodeKeyFrom,
			VK nodeKeyTo,
			EK linkKey)
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
		VK nodeKeyFrom,
		VK nodeKeyTo,
		EK linkKey,
		EV linkValue)
		throws NodeNotFoundException, GraphCorruptedException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Paramater 'to' is null");
		if (linkKey == null)
			throw new NullPointerException("Parameter 'link' is null");

		graph.tryAddLinkLast(nodeKeyFrom, nodeKeyTo, linkKey, linkValue);
	}

	/** @see graph.Graph#insertLinkFirst
	 */
	public void addLinkFirst(
		VK nodeKeyFrom,
		VK nodeKeyTo,
		EK linkKey,
		EV linkValue)
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

	/** @see graph.Graph#insertLinkFirst
		 */
		public void addLinkFirst(
			VK nodeKeyFrom,
			VK nodeKeyTo,
			EK linkKey)
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
		VK nodeKeyFrom,
		VK nodeKeyTo,
		EK linkKey,
		EV linkValue)
		throws NodeNotFoundException, GraphCorruptedException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Paramater 'to' is null");
		if (linkKey == null)
			throw new NullPointerException("Parameter 'link' is null");

		graph.tryAddLinkFirst(nodeKeyFrom, nodeKeyTo, linkKey, linkValue);
	}

	/** @see graph.Graph#insertNode
	 * @exception DuplicateVertexException inherited from implementation
	 * @exception NullPointerException if node parameter is null
	 */
	public void addNode(VK nodeKey, VV nodeValue)
		throws GraphException {

		if (nodeValue == null) {
			throw new NodeNotFoundException("Parameter 'node' is null");
		}

		graph.addNode(nodeKey, nodeValue);
	}

	/** @see graph.Graph#isEmpty
	 */
	public final boolean isEmpty() {

		return graph.isEmpty();
	}

	/** @see graph.Graph#linksIterator
	 */
	public LinksIterator linksKeysIterator() throws GraphException {

		return graph.linksKeysIterator();
	}

	/** @see graph.Graph#nodesIterator
	 */
	public Collection<VV> nodesValues() {

		return graph.nodesValues();
	}

	/** @see graph.Graph#nodesIterator
	 */
	public final Collection<VK> nodesKeySet() {

		return graph.nodesKeySet();
	}

	/** @see graph.Graph#removeLink
	 */
	public void removeLink(VK nodeKeyFrom, VK nodeKeyTo, EK link)
		throws GraphException {

		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Paramater 'to' is null");
		if (link == null)
			throw new NullPointerException("Parameter 'link' is null");

		graph.removeLink(nodeKeyFrom, nodeKeyTo, link);
	}

	public void removeAllLinksBetween(VK nodeKeyFrom, VK nodeKeyTo)
		throws GraphException {
		if (nodeKeyFrom == null)
			throw new NodeNotFoundException("Parameter 'from' is null");
		if (nodeKeyTo == null)
			throw new NodeNotFoundException("Paramater 'to' is null");
		graph.removeAllLinksBetween(nodeKeyFrom, nodeKeyTo);
	}

	/** @see graph.Graph#removeNode
	 */
	public void removeNode(VK nodeKey) throws NodeNotFoundException {
		if (nodeKey == null) {
			throw new NodeNotFoundException("Parameter 'node' is null");
		}

		graph.removeNode(nodeKey);
	}

	public final void setAllVisitCounts(int count) throws GraphException {
		graph.setAllVisitCounts(count);
	}

	public void setVisitCount(VK nodeKey, int passage)
		throws GraphException {

		if (nodeKey == null)
			throw new NodeNotFoundException("Parameter 'node' is null");
		graph.setVisitCount(nodeKey, passage);
	}

	public int getVisitCount(VK nodeKey) throws GraphException {

		if (nodeKey == null)
			throw new NodeNotFoundException("Parameter 'node' is null");
		return graph.getVisitCount(nodeKey);
	}

	public int incVisitCount(VK nodeKey) throws GraphException {

		if (nodeKey == null)
			throw new NodeNotFoundException("Parameter 'node' is null");
		return graph.incVisitCount(nodeKey);
	}

	/** @see graph.Graph#insertNode
	 *  @exception NullPointerException if node parameter is null
	 */
	public Object tryAddNode(VK nodeKey, VV nodeValue) {

		Object dummy = graph.tryAddNode(nodeKey, nodeValue);
		return nodeValue;
	}

	public int getDepth(VK startingNodeKey) throws GraphException {
		return graph.getDepth(startingNodeKey);
	}

	public final BreadthFirstIterator breadthFirstIterator(
		VK startingNodeKey,
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

	public void deepCopy(Graph dest) throws GraphException {
		graph.deepCopy(dest);
	}

	public final boolean existsNode(VK nodeKey) {
		return graph.existsNode(nodeKey);
	}

	/** @return a list of relations between nodes. The String contains as
	 *  many lines (\n terminated) as the number of links defined in the graph.
	 *  The line is a triple <source node id><link id><dest node id>
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		try {
			VK[] nodes = graph.getNodesKeys();
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

	public DepthFirstIterator depthFirstIterator(VK startingNodeKey, int maxCycling)
		throws GraphException {
		return graph.depthFirstIterator(startingNodeKey, maxCycling);
	}

	public final Collection<VK> getAllStartNodeKeys() {
		return graph.getAllStartNodeKeys();
	}

	public final boolean isStartNode(VK nodeKey) {
		return graph.isStartNode(nodeKey);
	}

	public EV getOutgoingLinkValue(VK nodeKeyFrom, EK linkKey)
		throws NodeNotFoundException {
		return graph.getOutgoingLinkValue(nodeKeyFrom, linkKey);
	}

	public Collection<EV> linksValues() {
		return graph.linksValues();
	}

	public int getMaxInDegree() {
		return graph.getMaxInDegree();
	}

	public int getMaxOutDegree() {
		return graph.getMaxOutDegree();
	}

}
