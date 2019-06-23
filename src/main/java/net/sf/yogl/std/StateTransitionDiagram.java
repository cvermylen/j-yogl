   
package net.sf.yogl.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import java.util.stream.Collectors;

import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.LinkNotFoundException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.exceptions.StdExecutionException;
import net.sf.yogl.uniqueElements.UniqueElementsGraph;

/** The STD implementation is based on a directed graph with the following
 *  restrictions:
 *  - The class 'State' replaces the class 'Vertex'
 *  - The class 'Transition' replaces the class 'Edge'
 *  - the graph must have 1! entry state
 *  - each node must extend the class State
 *  - each link must extend the class Transition
 *  - the graph cannot be modified concurrently
 * 
 *  Exception processing.
 *  'NodeNotFoundException', 'GraphCorruptedException' reflects an internal 
 *  error in the Std (Std damaged, not usable anymore). 
 */

public class StateTransitionDiagram<VK extends Comparable<VK>, EK extends Comparable<EK>, 
		DS extends State<VK, EK, DS, TS, PAR>, 
		TS extends Transition<EK, VK, TS, DS, PAR>,
		PAR>
		extends UniqueElementsGraph <DS, TS, VK, EK>{
	/** current graph the iterator is pointing to
	 */
//	private Graph<SK extends Comparable<SK>,TK extends Comparable<TK>, X extends UniqueVertex<SK, TK> & State<SK, TK>, Y extends UniqueEdge<TK, SK> & Transition<TK, SK>> graph = null;

	/** Contains the path followed by the iterator from
	 *  the initialisation to the current node.
	 *  The top value (peek) contains the 'current' value.
	 *  The vector element 0 refers to the initial value.
	 */
	private Stack<DS> vertexKeysPath = new Stack<>();
	private Stack<TS> transitionPath = new Stack<>();

	/** Builds up the state transition diagram with the given graph.
	 *  Checks that each node is a State and each link is a Transition.
	 *  The graph must have a unique entry point. This state must not
	 *  have an incoming transition.
	 */
	public StateTransitionDiagram() {
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
	public boolean doActiveTransition(TS transition, PAR parameter)
		throws StdExecutionException, NodeNotFoundException, LinkNotFoundException {

		DS currentState = vertexKeysPath.peek();
		if (transition == null) {
			LinkNotFoundException e =
				new LinkNotFoundException(
					"No such transition from current node:" + vertexKeysPath.peek());
			e.setLink(null);
			throw e;
		}
		DS nextState = transition.getToVertex();
		//check the path
		if ((currentState.checkBeforeExit(nextState, transition, parameter))
			&& (transition.testAction(currentState, parameter))
			&& (nextState.checkBeforeEntry(currentState, transition, parameter)) == false)
			return false;
		//Exit the current state
		if (!currentState.onExit(nextState, transition, parameter))
			return false;
		//walk through the transition
		try {
			if (!transition.doTransition(currentState, parameter)) {
				currentState.reEntryAfterBacktrack(null, transition, parameter, null);
				return false;
			}
		} catch (StdExecutionException e) {
			currentState.reEntryAfterBacktrack(null, transition, parameter, e);
			throw e;
		}
		//Enter the new current state
		try {
			if (nextState.onEntry(currentState, transition, parameter)) {
				vertexKeysPath.push(transition.getToVertex());
				transitionPath.push(transition);
				transition.incVisitCounts();
			} else {
				transition.actionAfterBacktrack(currentState, parameter, null);
				currentState.reEntryAfterBacktrack(null, transition, parameter, null);
				return false;
			}
		} catch (StdExecutionException e) {
			transition.actionAfterBacktrack(currentState, parameter, e);
			currentState.reEntryAfterBacktrack(null, transition, parameter, e);
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
	public <SP> boolean doTransition(TS useThisTransition)
		throws StdExecutionException, NodeNotFoundException, LinkNotFoundException {

		return doActiveTransition(useThisTransition, null);
	}

	/** The method just gives a list of the outgoing transitions, whithout
	 *  any check.
	 * @return the list of all transitions from the currentNode,
	 *         or <null> if the current state is an end state.
	 * @exception NodeNotFoundException indicates an internal error (the 
	 *         identification of the current node does not match any valid
	 *         state in the graph).
	 */
	public Collection<TS> getTransitions() throws NodeNotFoundException {
		Collection<TS> list = new ArrayList<>();
		if (vertexKeysPath.size() > 0) {
			DS currentState = vertexKeysPath.peek();
			list = currentState.getOutgoingEdges();
		}
		return list;
	}

	/** Returns the value of all states that are accessible from the current
	 *  state, or <null> if the if the current state is an end state. 
	 */
	public Collection<DS> getNextStates() throws GraphCorruptedException {
		return vertexKeysPath.peek().getOutgoingEdges().stream().map(transition ->transition.getToVertex()).collect(Collectors.toList());
	}

	/** 
	 *  @return the value of the node currently pointed by the STD.
	 *  @exception NodeNotFoundException indicates an internal error (the 
	 *         identification of the current node does not match any valid
	 *         state in the graph).
	 */
	public DS getCurrentState() {

		return vertexKeysPath.peek();
	}

	/**
	 * @return the value of the currentState key value. This key is unique in
	 *  the STD.
	 */
	public DS getCurrentStateKey(){
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
	public boolean backtrack(PAR parameter)
		throws NodeNotFoundException, StdExecutionException {

		if (vertexKeysPath.size() < 1)
			return false;

		DS currentState = vertexKeysPath.pop();
		TS t = transitionPath.peek();
		DS previousState = vertexKeysPath.peek();
		vertexKeysPath.push(currentState);
		if (!currentState.exitAfterBacktrack(previousState, t, parameter))
			return false;
		if (!previousState.reEntryAfterBacktrack(currentState, t, parameter, null))
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
	public boolean testActiveTransition(TS transition, PAR parameter)
		throws StdExecutionException, NodeNotFoundException, LinkNotFoundException {

		boolean found = false;
		DS currentState = vertexKeysPath.peek();
		if (transition == null) {
			LinkNotFoundException e =
				new LinkNotFoundException(
					"No such transition from current node:" + vertexKeysPath.peek());
//			e.setGraph(graph);
			e.setLink(null);
			throw e;
		}
		DS nextState = transition.getToVertex();
		found =
			(currentState.checkBeforeExit(nextState, transition, parameter)
				& transition.testAction(currentState, parameter)
				& nextState.checkBeforeEntry(currentState, transition, parameter));
		return found;
	}

	/** Amongst all the transitions accessible from the current node, test the one
	 *  that can be used to access the next current node.
	 *  @Return the Transition object that has been used to go to the next State
	 *          or null if the Std remains in the same state.
	 */
	public TS doAutoTransition(PAR parameter)
		throws
			StdExecutionException,
			NodeNotFoundException,
			LinkNotFoundException,
			GraphCorruptedException {
		DS currentState = vertexKeysPath.peek();
		Iterator<TS> possibleTransitionIter = currentState.getOutgoingEdges().iterator();
		while (possibleTransitionIter.hasNext()) {
			TS possibleTransition = possibleTransitionIter.next();
			if (possibleTransition.testAction(currentState, parameter)) {
				doActiveTransition(possibleTransition, parameter);
				return possibleTransition;
			}
		}
		return null;
	}

	/** Tester method to check if the candidate transition given
	 *  as parameter is possible or not.
	 *  @param link candidate transition
	 *  @return true if the transition is possible, false otherwise
	 */

	public boolean testTransition(TS transition)
		throws StdExecutionException, NodeNotFoundException, LinkNotFoundException {
		return testActiveTransition(transition, null);
	}
}
