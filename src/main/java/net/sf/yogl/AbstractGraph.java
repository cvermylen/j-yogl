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

/** The interface described here is the top hierarchy of the decorator pattern.
 * Methods listed hereafter are common to all graph implementations.
 * I'll try to explain, in a few words, my view on this pattern.
 * Two types of classes are implemented:
 * - the 'service' classes, like AdjListGraph, implements the common and
 *   basic 'graph' mechanisms. It defines the graph as a set of nodes
 *   and links. The major 'pre-condition' is that each user-defined object
 *   pointed to by the nodes and the links have a key (unique identifier).
 *   Only the constructors are directly used by the users.
 * - the 'decorator' classes, like DupLinksGraph, offer an extra service
 *   to the 'service' classes. Each decorator inherits from the AdapterGraph.
 * The Adapter class, GraphAdapter, is NOT a decorator. It is a kind of
 * accessor.
 * An interface, AbstractGraph, defines the methods provided by the
 * 'graph' package.
 * Each decorator that offer an extra service on some methods, implements
 * the new service and calls the basic service via its superclass.
 * For the other methods, the decorator calls the service on the graph
 * object. This allows multiple decorators on the same graph. It is the user
 * responsibility to find the right combinations of decorators.
 */

public interface AbstractGraph {

	/** Insert a new link between 2 nodes. This new link will be placed 
	 *  'before' any existing link between these 2 nodes.
	 *  @param nodeKeyFrom identify the starting node for the link
	 *  @param nodeKeyTo identify the destination node for the link
	 *  @param linkKey uniquely identify the link between 'from' and 'to'
	 *  @param linkValue
	 */
	public void addLinkFirst(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey,
		Object linkValue)
		throws NodeNotFoundException, DuplicateLinkException, GraphCorruptedException;

	/** Insert a new link between 2 nodes. This new link will be placed 
	 *  'before' any existing link between these 2 nodes.
	 *  @param nodeKeyFrom identify the starting node for the link
	 *  @param nodeKeyTo identify the destination node for the link
	 *  @param linkKey uniquely identify the link between 'from' and 'to'
	 */
	public void addLinkFirst(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey)
		throws NodeNotFoundException, DuplicateLinkException, GraphCorruptedException;

	/** Insert a new directed edge from nodeFrom to nodeTo and
	 * @param nodeFrom starting point of the edge
	 * @param nodeTo destination
	 * @exception VertexNotFoundException if nodeFrom or nodeTo does
	 *            not exists.
	 * @exception DuplicateEdgeException if this edge already exists
	 *            between nodeFrom and nodeTo
	 */
	public void addLinkLast(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey,
		Object linkValue)
		throws NodeNotFoundException, DuplicateLinkException, GraphCorruptedException;

	/** Insert a new directed edge from nodeFrom to nodeTo and
	 * @param nodeFrom starting point of the edge
	 * @param nodeTo destination
	 * @exception VertexNotFoundException if nodeFrom or nodeTo does
	 *            not exists.
	 * @exception DuplicateEdgeException if this edge already exists
	 *            between nodeFrom and nodeTo
	 */
	public void addLinkLast(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey)
		throws NodeNotFoundException, DuplicateLinkException, GraphCorruptedException;

	/** This method inserts a new node in the graph.
	 * Pre-condition: the node object does not exists in the graph
	 * The presence of the node in the graph is implemented by the
	 * 'equals' method. Once defined, a node value cannot be changed.
	 * @param nodeKey uniquely identify the new node to insert in the graph
	 * @param nodeValue refers any user-defined object
	 * @param type is the type that will be associated to the node
	 * @exception DuplicateVertexException thrown if the node is
	 *            already present in the graph.
	 */
	public void addNode(Object nodeKey, Object nodeValue)
		throws GraphException;

	/** Return an iterator to browse ALL nodes in the graph that can
	 *  be accessed from the startingNode. The method 'next()' in the
	 *  iterator will return the nodes in the order defined by the 
	 *  breadth first' policy.
	 *  @param startingNodeKey identify a Node in the graph which will be
	 *  the starting point of the traversal.
	 *  @param maxCycles fixes the number of times each node can be
	 *         visited. A value of 1 indicates that each node will be
	 *         returned max. 1 time, this is thus a way to avoid cycling.
	 *         Accepted values are: [1 .. n]. There is no way to express
	 *         an infinite value.
	 */
	public BreadthFirstIterator breadthFirstIterator(
		Object startingNodeKey,
		int maxCycles)
		throws GraphException;

	/** Clones the structure of the graph. User values are not cloned.
	 */
	public Object clone();

	/** Clones the structure of the graph. User values are cloned.
	 *  Internal variables (traversal) are reset.
	 */
	public void deepCopy(AbstractGraph dest) throws GraphException;

	/** Return an iterator to browse ALL nodes in the graph that can
	 *  be accessed from the startingNode. The method 'next()' in the
	 *  iterator will return the nodes in the order defined by the 
	 *  depth first' policy.
	 *  @param startingNodeKey identify a Node in the graph which will be
	 *  the starting point of the traversal.
	 *  @param maxCycles fixes the number of times each node can be
	 *         visited. A value of 1 indicates that each node will be
	 *         returned max. 1 time, this is thus a way to avoid cycling.
	 *         Accepted values are: [1 .. n]. There is no way to express
	 *         an infinite value.
	 */
	public DepthFirstIterator depthFirstIterator(Object startingNodeKey, int maxCycling)
		throws GraphException;

	/** Test for the presence of a given node key.
	 * @param nodeKey identify the node
	 * @return true if the key is already present in the graph.
	 */
	public boolean existsNode(Object nodeKey);

	/** Returns a matrix of boolean values, where each node is at the
	 *  same position as in the array returned by the 'getNodes' method
	 */
	public boolean[][] getAdjacencyMatrix() throws GraphException;

	/** Return the list of all entry points in the graph.
	 */
	public Object[] getAllStartNodeKeys();

	/** Return the depth of the graph. This method may perform a full 
	 *  traversal of the graph (depend on the implementation). The result
	 *  can be interpreted as the maximum distance between the root node
	 *  and any other accessible node.
	 *  The root node is at level 0.
	 */
	public int getDepth(Object startingNodeKey) throws GraphException;

	/** Returns the links that have as destination the given node. This 
	 *  method looks for 'predecessor' nodes and their relation to the
	 *  given node.
	 */
	public Object[] getIncomingLinksKeys(Object nodeKeyTo)
		throws NodeNotFoundException;

	/** Return the list of direct predecessors and their associated
	 *  link
	 *  x[0][i] = node; x[1][i] = link
	 */
	public Object[][] getIncomingLinksKeysAndNodesKeys(Object nodeKeyTo)
		throws NodeNotFoundException;

	/** Return the total number of edges contained in the graph.
	 * @return number of edges.
	 */
	public int getLinkCount();

	/** Returns the max degree by only taking into account the incoming
	 *  edges
	 */
	public int getMaxInDegree();
	
	/** Returns the max degree by only taking into account the outgoing
	 *  edges
	 */
	public int getMaxOutDegree();
	
	/** This method return the list of all links between 2 nodes.
	 * Pre-condition: the 2 nodes must exists in the graph.
	 * @param nodeKeyFrom
	 * @param nodeKeyTo
	 */
	public Object[] getLinksKeysBetween(Object nodeKeyFrom, Object nodeKeyTo)
		throws NodeNotFoundException;

	/** Return the number of node contained in the graph. This
	 * number will include all types of nodes: entry nodes, as well as
	 * end nodes and 'regular' nodes
	 * @return number of nodes
	 */
	public int getNodeCount();

	/** Ask the graph to return the list of all valid user-defined
	 *  nodes.
	 *  @return a list with all nodes
	 */
	public Object[] getNodesKeys() throws GraphCorruptedException;

	/** Return the list of nodes that are of a particular type
	 *  @param nodeType has a value described in VertexType
	 */
	public Object[] getNodesKeys(int nodeType) throws GraphCorruptedException;

	/** Returns the type associated to the node.
	 *  The type is specified by a value defined in VertexType.
	 *  @return a value from VertexType
	 *  @exception GraphException if the node does not exists
	 */
	public int getNodeType(Object nodeKey) throws GraphException;

	/** Returns the user-defined value associated with the given key.
	 * @param nodeKey
	 * @return
	 */
	public Object getNodeValue(Object nodeKey);

	/** Return the list of all outgoing links
	 *  @param nodeFrom nodes from which we list the links
	 */
	public Object[] getOutgoingLinksKeys(Object nodeKeyFrom)
		throws GraphException;

	/** Returns an array of all outgoing edges and the associated 
	 *  destination node.
	 */
	public Object[][] getOutgoingLinksKeysAndNodesKeys(Object nodeKeyFrom)
		throws GraphException;

	/** Returns the value associated to the link identified by the two
	 *  parameters.
	 * @param nodeKeyFrom where the link is coming from
	 * @param linkKey identify the link
	 * @throws NodeNotFoundException
	 */
	public Object getOutgoingLinkValue(Object nodeKeyFrom, Object linkKey)
		throws NodeNotFoundException;

	/** Returns the nodes that have a 'predecessor' relationship with nodeTo.
	 *  A predecessor is a node that is the originator of a link whose
	 *  destination is indicated by the parameter.
	 *  @param nodeTo destination node
	 */
	public Object[] getPredecessorNodesKeys(Object nodeKeyTo)
		throws GraphException;

	/** Ditto but for a given link
	 */
	public Object[] getPredecessorNodesKeys(Object nodeKeyTo, Object link)
		throws GraphException;

	/** Ask the graph to return the list of all nodes that are adjacent
	 * to the node given in parameter. In other words, it will return
	 * the nodes that are connected to the parameter by one (maximum) edge.
	 * @return list of nodes
	 * @param node is the node for which we ask to retrieve all
	 *        neighbors
	     * @param steps distance in links between the given node and the
	     *        requested neighbors. The minimal distance is 1: returns the
	     *        immediate neighbors.
	 */
	public Object[] getSuccessorNodesKeys(Object nodeKey, int steps)
		throws GraphException;

	/** Ask the graph to return the list of all nodes that are adjacent
	 * to the node in parameter via the link also given in parameter.
	 * @return a list of nodes
	 * @param node is the node for which we ask to retrieve all
	 *        neighbors
	 * @param link this link connect the first node to the list of
	 *        vertices returned by the method.
	 */
	public Object[] getSuccessorNodesKeys(Object nodeKey, Object link)
		throws GraphException;

	/** Return the content of a local integer variable stored on each
	 *  node. This variable is typically used to control graph traversals.
	 */
	public int getVisitCount(Object nodeKey) throws GraphException;

	/** Increment the passage variable by 1. The new value is returned.
	 */
	public int incVisitCount(Object nodeKey) throws GraphException;

	/** Check if the graph is empty or not. That is, if the graph contains
	 * at least one node.
	 */
	public boolean isEmpty();

	/** Test if the identified node is an entry point.
	 * @param nodeKey node identifier
	 * @return true if is an entry point
	 */
	public boolean isStartNode(Object nodeKey);

	/** Returns an iterator on all existing links.
	 *  Links returned by this iterator are not garanteed to be
	 *  returned in a well defined order.
	 */
	public LinksIterator linksKeysIterator() throws GraphException;

	/** The iterator returned by the nodeIterator method is useful
	 * to retrieve all nodes contained in the graph.
	 */
	public Collection nodesKeySet();

	/** The iterator returned by the nodeIterator method is useful
	 * to retrieve all nodes contained in the graph.
	 */
	public Collection nodesValues();

	/** return an non ordered set with all link values.
	 */
	public Collection linksValues();
	
	/** Removes all links between the given nodes.
	 */
	public void removeAllLinksBetween(Object nodeKeyFrom, Object nodeKeyTo)
		throws GraphException;

	/** Method to remove an edge between two vertices.
	 * @param nodeFrom originating node
	 * @param nodeTo destination node
	 * @param link to be removed
	 */
	public void removeLink(Object nodeKeyFrom, Object nodeKeyTo, Object link)
		throws GraphException;

	/** This method will mark the node as unused. The list of neighbors
	 * is flushed. There is (currently) no reference counting associated
	 * to the node. As a consequence, if a node is not accessible
	 * anymore, it is not removed. Also, all incoming edges to the
	 * removed node are not removed.
	 * @param node node to be removed
	 */
	public void removeNode(Object nodeKey) throws NodeNotFoundException;

	/** Initialize the content of the variable to a given value.
		 */
	public void setAllVisitCounts(int count) throws GraphException;

	/** Initialize the content of the variable to a given value.
	 */
	public void setVisitCount(Object nodeKey, int count) throws GraphException;

	/** This method does not throw an exception if the link already exists
	 *  between the two nodes.
	 * @param nodeKeyFrom
	 * @param nodeKeyTo
	 * @param link
	 * @throws GraphException
	 */
	public void tryAddLinkFirst(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey,
		Object linkValue)
		throws NodeNotFoundException, GraphCorruptedException;

	/** This method does not throw an exception if the link already exists
	 *  between the two nodes.
	 */
	public void tryAddLinkLast(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey,
		Object linkValue)
		throws NodeNotFoundException, GraphCorruptedException;

	/** This method is exactly the same as 'addNode' but does not throw an
	 *  exception if the node is already present.
	 * @param nodeKey
	 * @param nodeValue
	 * @param type
	 * @return
	 */
	public Object tryAddNode(Object nodeKey, Object nodeValue);

}