/* Copyright (C) 2003 Symphonix
   
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

import net.sf.yogl.adjacent.list.AdjListEdge;
import net.sf.yogl.adjacent.list.AdjListVertex;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.LinkNotFoundException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.exceptions.StdDefinitionException;
import net.sf.yogl.exceptions.StdExecutionException;
import net.sf.yogl.impl.ImplementationGraph;
import net.sf.yogl.std.State;
import net.sf.yogl.std.Transition;

/** The STD implementation is based on a directed graph with the following
 *  restrictions:
 *  - the graph must have 1! entry state
 *  - each node must extend the class State
 *  - each link must extend the class Transition
 *  - the graph cannot be modified concurrently
 * 
 *  Exception processing.
 *  'NodeNotFoundException', 'GraphCorruptedException' reflects an internal 
 *  error in the Std (Std damaged, not usable anymore). 
 */

public final class StateTransitionDiagram {
	/** current graph the iterator is pointing to
	 */
	private ImplementationGraph graph = null;

	/** Contains the path followed by the iterator from
	 *  the initialisation to the current node.
	 *  The top value (peek) contains the 'current' value.
	 *  The vector element 0 refers to the initial value.
	 */
	private Stack vertexKeysPath = new Stack();
	private Stack transitionPath = new Stack();

	/** Builds up the state transition diagram with the given graph.
	 *  Checks that each node is a State and each link is a Transition.
	 *  The graph must have a unique entry point. This state must not
	 *  have an incoming transition.
	 */
	public StateTransitionDiagram(GraphAdapter graph)
		throws StdDefinitionException {

		if (graph == null) {
			throw new NullPointerException("parameter graph is null");
		}

		Iterator nodesIter = graph.nodesValues().iterator();
		while (nodesIter.hasNext()) {
			Object node = nodesIter.next();
			if ((node == null) || (!(node instanceof State)))
				throw new StdDefinitionException("Each node value must be a State");
		}

		Iterator linksIter = graph.linksValues().iterator();
		while (linksIter.hasNext()) {
			Object link = linksIter.next();
			if ((link == null) || (!(link instanceof Transition)))
				throw new StdDefinitionException("Each link value must be a Transition");
		}
		this.graph = graph.getImplementation();
		Collection startNodeKeys = graph.getAllStartNodeKeys();
		if (startNodeKeys.isEmpty()) {
			throw new StdDefinitionException("graph must have 1 unique entry");
		}
		vertexKeysPath.push(startNodeKeys);
	}

	/** The init method is used to initialize the Std.
	 *  post-condition: the current vertex is reset to the entry
	 *  point of the STD.
	 * @param value is passed to the std entry node.onEntry method.
	 */
	public void init(Object parameter) throws Exception {
		Object lastVisitedKey = vertexKeysPath.elementAt(0);
		vertexKeysPath.clear();
		vertexKeysPath.push(lastVisitedKey);
		State state = findStateByVertexKey(vertexKeysPath.peek());
		state.onEntry(lastVisitedKey, null, null, parameter);
	}

	/** Force the current node to the node specified.
	 *  @param node the new node to become the 'currentNode'
	 *  @return true if the change could occur
	 */
	public boolean set(Object nodeKey) {

		boolean result = false;
		try {
			DepthFirstIterator df = graph.depthFirstIterator(null, 1);
			while (df.hasNext()) {
				Object o = df.next();
				if (nodeKey.equals(o)) {
					result = true;
					vertexKeysPath = new Stack();
					vertexKeysPath.addAll(Arrays.asList(df.nodePath()));
					vertexKeysPath.push(nodeKey);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return result;
		}
	}

	/** An 'active transition' will move the internal pointer from
	 *  the current state to the state pointed to by the transition
	 *  identified by 'key'. The following operations are performed:
	 *  checks if the current state can be left(currentState.checkBeforeExit),
	 *  if the given transition can be 'used' (transition.testAction)
	 *  and if the next state can be 'entered' (nextState.checkBeforeEntry).
	 *  If all of these tests are ok, then action is taken: the methods
	 *  currentState.onExit, transition.doAction and nextState.onEntry
	 *  are executed.
	 *  If an error occurs during one of these processings, the STD tremains
	 *  in the original state. 
	 *  @param key is the transition identification used to do the transition
	 *  @param parameter is a user-defined parameter for the 'doAction' method
	 *  @exception StdExecutionException if an error occured during a callback
	 *             on State or Transition
	 *  @exception NodeNotFoundException if the destination State does not exists,
	 *             or the identification of the current node does not match any valid
	 *         state in the graph).
	 *  @exception LinkNotFoundException if the transition identified by 'key'
	 *             is not attached to the current state
	 */
	public boolean doActiveTransition(Object transitionKey, Object parameter)
		throws StdExecutionException, NodeNotFoundException, LinkNotFoundException {

		State currentState = findStateByVertexKey(vertexKeysPath.peek());
		AdjListEdge edge = findEdgeByKey(vertexKeysPath.peek(), transitionKey);
		if (edge == null) {
			LinkNotFoundException e =
				new LinkNotFoundException(
					"No such transition from current node:" + vertexKeysPath.peek());
			e.setGraph(graph);
			e.setLink(transitionKey);
			throw e;
		}
		Transition t = (Transition) edge.getUserValue();
		Object k = edge.getNextVertexKey();
		State nextState = findStateByVertexKey(k);
		//check the path
		if ((currentState.checkBeforeExit(vertexKeysPath.peek(), nextState, t, parameter))
			&& (t.testAction(transitionKey, currentState, parameter, nextState))
			&& (nextState.checkBeforeEntry(k, currentState, t, parameter)) == false)
			return false;
		//Exit the current state
		if (!currentState.onExit(vertexKeysPath.peek(), nextState, t, parameter))
			return false;
		//walk through the transition
		try {
			if (!t.doAction(transitionKey, currentState, parameter, nextState)) {
				currentState.entryAfterBacktrack(vertexKeysPath.peek(), null, t, parameter, null);
				return false;
			}
		} catch (StdExecutionException e) {
			currentState.entryAfterBacktrack(vertexKeysPath.peek(), null, t, parameter, e);
			throw e;
		}
		//Enter the new current state
		try {
			if (nextState.onEntry(k, currentState, t, parameter)) {
				vertexKeysPath.push(edge.getNextVertexKey());
				transitionPath.push(t);
				edge.incVisitCount();
			} else {
				t.actionAfterBacktrack(transitionKey, null, parameter, currentState, null);
				currentState.entryAfterBacktrack(vertexKeysPath.peek(), null, t, parameter, null);
				return false;
			}
		} catch (StdExecutionException e) {
			t.actionAfterBacktrack(transitionKey, null, parameter, currentState, e);
			currentState.entryAfterBacktrack(vertexKeysPath.peek(), null, t, parameter, e);
			throw e;
		}
		return true;
	}

	/** Performs a transition from the 'current' node to the 'next'
	 *  node by following the connection labelled by the edge parameter.
	 *  @param key is the link for the candidate transition
	 *  @exception StdExecutionException if an error occured during a callback
	 *             on State or Transition
	 *  @exception NodeNotFoundException if the destination State does not exists
	 *  @exception LinkNotFoundException if the transition identified by 'key'
	 *             is not attached to the current state
	 */
	public boolean doTransition(Object key)
		throws StdExecutionException, NodeNotFoundException, LinkNotFoundException {

		return doActiveTransition(key, null);
	}

	/** The method just gives a list of the outgoing transitions, whithout
	 *  any check.
	 * @return the list of all transitions from the currentNode,
	 *         or <null> if the current state is an end state.
	 * @exception NodeNotFoundException indicates an internal error (the 
	 *         identification of the current node does not match any valid
	 *         state in the graph).
	 */
	public Transition[] getTransitions() throws NodeNotFoundException {

		if (vertexKeysPath.size() > 0) {
			ArrayList list = new ArrayList();
			AdjListVertex peek = graph.findVertexByKey((Comparable)vertexKeysPath.peek());
			AdjListEdge[] eList = peek.getNeighbors();
			for (int i = 0; i < eList.length; i++) {
				list.add(eList[i].getUserValue());
			}
			return (Transition[]) list.toArray(new Transition[list.size()]);
		} else {
			return null;
		}
	}

	/** Return the Transition object attached to the current node and
	 *  identified by the given key.
	 */
	public Transition getTransition(Object transitionKey) throws NodeNotFoundException {
		AdjListEdge edge = findEdgeByKey(vertexKeysPath.peek(), transitionKey);
		if (edge == null)
			return null;
		return (Transition) edge.getUserValue();
	}

	/** Checks if the given transition exists.
	 * @return true if the given key identify a transition from the current state
	 * @throws GraphCorruptedException
	 */
	public boolean isTransition(Object transitionKey) throws NodeNotFoundException{
		AdjListEdge edge = findEdgeByKey(vertexKeysPath.peek(), transitionKey);
		return (edge != null);
	}
	
	/** Returns the a list of all transition keys going out from the current node
	 */
	public Object[] getTransitionKeys() throws GraphCorruptedException {
		try {
			return graph.getSuccessorNodesKeys((Comparable)vertexKeysPath.peek(), 1);
		} catch (GraphException e) {
			throw new GraphCorruptedException(e);
		}
	}

	/** Returns the value of all states that are accessible from the current
	 *  state, or <null> if the if the current state is an end state. 
	 */
	public State[] getNextStates() throws GraphCorruptedException {
		try {
			Object[] keys =
				graph.getSuccessorNodesKeys((Comparable)vertexKeysPath.peek(), 1);
			State[] result = new State[keys.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = (State) graph.getNodeValue((Comparable)keys[i]);
			}
			return result;
		} catch (GraphException e) {
			throw new GraphCorruptedException(e);
		}
	}

	/** 
	 *  @return the value of the node currently pointed by the STD.
	 *  @exception NodeNotFoundException indicates an internal error (the 
	 *         identification of the current node does not match any valid
	 *         state in the graph).
	 */
	public State getCurrentState() throws NodeNotFoundException {

		return findStateByVertexKey(vertexKeysPath.peek());
	}

	/**
	 * @return the value of the currentState key value. This key is unique in
	 *  the STD.
	 */
	public Object getCurrentStateKey(){
		return vertexKeysPath.peek();
	}
	
	/** indicates if there is at least one possible transition to another
	 *  state.
	 * @return <false> if the current node is not an end node, <true> if
	 *         a transition to another state is possible.
	 * @throws NodeNotFoundException indicates an internal error (the 
	 *         identification of the current node does not match any valid
	 *         state in the graph).
	 */
	public boolean hasNext() throws NodeNotFoundException {

		return getTransitions() != null;
	}

	/** This method sets the STD currentNode to its previous value.
	 *  The functionality can be compared to 'undo'.
	 *  The methods 'exitAfterBacktrack' is called on the current state
	 *  and 'entryAfterBacktrack' is called on the previous state.
	 *  post. the current state is still the same if the return
	 *        value is false. The current state is reversed to the previous
	 *        if the return value is true.
	 * 	@return false if there is no 'back' value possible, 'true'
	 *          otherwise.
	 */
	public boolean backtrack(Object parameter)
		throws NodeNotFoundException, StdExecutionException {

		if (vertexKeysPath.size() < 1)
			return false;

		Object key = vertexKeysPath.pop();
		State currentState = this.findStateByVertexKey(key);
		Transition t = (Transition) transitionPath.peek();
		Object previousKey = vertexKeysPath.peek();
		vertexKeysPath.push(key);
		State previousState = this.findStateByVertexKey(previousKey);
		if (!currentState.exitAfterBacktrack(key, previousState, t, parameter))
			return false;
		if (!previousState
			.entryAfterBacktrack(previousKey, currentState, t, parameter, null))
			return false;
		vertexKeysPath.pop();
		transitionPath.pop();
		return true;
	}

	/** Test if it will be possible to leave the current state and use the
	 *  transition identified by <key> using the the given <parameter>.
	 *  3 callback methods are used (in the following order):
	 *  currentState.checkBeforeExit
	 *  transition<key>.testAction
	 *  state(pointed by previous transition).checkBeforeEntry
	 *  @exception StdExecutionException if an error occured during the
	 *  execution of one of the 3 methods.
	 */
	public boolean testActiveTransition(Object transitionKey, Object parameter)
		throws StdExecutionException, NodeNotFoundException, LinkNotFoundException {

		boolean found = false;
		State currentState = findStateByVertexKey(vertexKeysPath.peek());
		AdjListEdge edge = findEdgeByKey(vertexKeysPath.peek(), transitionKey);
		if (edge == null) {
			LinkNotFoundException e =
				new LinkNotFoundException(
					"No such transition from current node:" + vertexKeysPath.peek());
			e.setGraph(graph);
			e.setLink(transitionKey);
			throw e;
		}
		Transition t = (Transition) edge.getUserValue();
		State nextState = findStateByVertexKey(edge.getNextVertexKey());
		found =
			(currentState.checkBeforeExit(vertexKeysPath.peek(), nextState, t, parameter)
				& t.testAction(transitionKey, currentState, parameter, nextState)
				& nextState.checkBeforeEntry(edge.getNextVertexKey(), currentState, t, parameter));
		return found;
	}

	/** Amongst all the transitions accessible from the current node, test the one
	 *  that can be used to access the next current node.
	 *  @Return the Transition object that has been used to go to the next State
	 *          or null if the Std remains in the same state.
	 */
	public Transition doAutoTransition(Object parameter)
		throws
			StdExecutionException,
			NodeNotFoundException,
			LinkNotFoundException,
			GraphCorruptedException {
		AdjListVertex peek = graph.findVertexByKey((Comparable)vertexKeysPath.peek());
		State currentState = (State) peek.getUserValue();
		Object[] keys = getTransitionKeys();
		if (keys == null)
			return null;
		for (int i = 0; i < keys.length; i++) {
			if (testActiveTransition(keys[i], parameter)) {
				Transition t = getTransition(keys[i]);
				doActiveTransition(keys[i], parameter);
				return t;
			}
		}
		return null;
	}

	/** Tester method to check if the candidate transition given
	 *  as parameter is possible or not.
	 *  @param link candidate transition
	 *  @return true if the transition is possible, false otherwise
	 */

	public boolean testTransition(Object transitionKey)
		throws StdExecutionException, NodeNotFoundException, LinkNotFoundException {
		return testActiveTransition(transitionKey, null);
	}

	/** Returns the number of times the corresponding link has been traversed.
	 *  The link must be one of the possible transition from the current node.
	 *  @param link the user-defined object representing the link between 2 nodes
	 *  @return any non-negative number if the link is found
	 *  @exception InvalidEdgeException if the link does not exists.
	 */
	public int getVisitCount(Object transitionKey)
		throws LinkNotFoundException, NodeNotFoundException {

		boolean found = false;
		int result = -1;
		//retrieve 'link' in the edge list of the
		//current vertex.
		AdjListVertex peek = graph.findVertexByKey((Comparable)vertexKeysPath.peek());
		AdjListEdge edge = peek.getEdge((Comparable)transitionKey);
		if (edge == null) {
			throw new LinkNotFoundException(
				transitionKey.toString() + "not found from" + vertexKeysPath.peek());
		}
		return edge.getVisitCount();
	}

	private State findStateByVertexKey(Object key)
		throws NodeNotFoundException {
		State result = null;
		AdjListVertex vertex = graph.findVertexByKey((Comparable)key);
		if (vertex != null)
			result = (State) vertex.getUserValue();
		return result;
	}

	private AdjListEdge findEdgeByKey(Object vertexKey, Object edgeKey)
		throws NodeNotFoundException {
		AdjListEdge result = null;
		AdjListVertex vertex = graph.findVertexByKey((Comparable)vertexKey);
		if (vertex != null)
			result = vertex.getEdge((Comparable)edgeKey);
		return result;
	}
}
