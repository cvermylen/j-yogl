   
package net.sf.yogl.std;

import net.sf.yogl.exceptions.StdExecutionException;

/** The purpose of this class is to be extended to give each State its
 *  expected behaviour. It defines a set of methods which are called on 
 *  pre-defined events on the STD. All the states are 'contained' in the
 *  STD, and the STD points at any time to one 'current State'.
 */
public abstract class State {

	/** The method is called when testing the transition arriving to this
	 *  state. This method is called by:
	 *  StateTransitionDiagram.doActiveTransition
	 *  StateTransitionDiagram.doAutoTransition
	 *  StateTransitionDiagram.doTransition
	 *  StateTransitionDiagram.testActiveTransition
	 *  StateTransitionDiagram.testTransition
	 *  The method is NOT called before entering on the root state for the
	 *  first time (during the construction of the STD).
	 * @param thisStateKey identify the state in the STD. Each State has a unique
	 *        identifier.
	 * @param from is the actual currentState. Cannot be null.
	 * @param parameter is an extra data that is passed by the user. Only
	 *        the 'active' methods have access to this parameter. When
	 *        using the 'doTransition' and the 'testTransition', this
	 *        parameter is 'null'.
	 * @param using is the Transition that will be used to enter 'this' state.
	 *        Cannot be null.
	 * @return true if the method completed successfully. False means that this
	 *         state cannot be assigned as the 'current state' according to the
	 *         given parameter values.
	 * @throws Exception
	 */
	public boolean checkBeforeEntry(
		Object thisStateKey,
		State from,
		Transition using,
		Object parameter)
		throws StdExecutionException {
		return true;
	}

	/** The method is called when this state is set as the new 'current state'.
	 *  It is assumed that this state can be set as the 'current state'. The
	 *  necessary validation should have be performed by the 'checkBeforeEntry'
	 *  method.
	 * Attention: the parameters 'from' and 'using' are null when the 
	 * STD is entering the 'root' node for the first time (+/- = coming from
	 * nowhere.
	 * This method is called by:
	 *  StateTransitionDiagram.doActiveTransition
	 *  StateTransitionDiagram.doAutoTransition
	 *  StateTransitionDiagram.doTransition
	 * @param thisStateKey identify the state in the STD. Each State has a unique
	 *        identifier.
	 * @param from is the State we are coming from. At this stage, 'this' is
	 *        already the current state.
	 * @param using is the transition used to access this state. The method
	 *        'doAction' in this transition has already been executed.
	 * @param parameter any user defined value, as passed via the methods
	 *        described above.
	 * @return true if the state definition is correct according to the
	 *         parameter values (the context). False means that this state
	 *         cannot be maintained and the STD has to 'backtrack' to the
	 *         previous state (rollback).
	 * @throws Exception if thrown, the STD automaticall backtracks to the
	 *         previous node.
	 */
	public boolean onEntry(Object thisStateKey, State from, Transition using, Object parameter)
		throws StdExecutionException {
		return true;
	}

	/** This method is called by the backtrack method on the previous state
	 *  'this' will become the new 'currentState'.
	 *  If an error occurs during a normal transition move (doTransition, ..),
	 *  this method is also called on the starting state. This automatic
	 *  backatrack is triggered by an exception raised or a 'false' return
	 *  code during the processing of the following methods:
	 *  Transition.doAction
	 *  State.onExit 
	 * @param thisStateKey identify the state in the STD. Each State has a unique
	 *        identifier.
	 * @param from identify the previous current state (manual backtrack)
	 *        or the state who raised an exception during 'onEntry' processing.
	 *        This parameter can also be null if the error was caused by the
	 *        'doAction' processing of the transition.
	 * @param using is the transition used to access this state. This transition
	 *        has been traversed in 'reverse'.
	 * @param parameter is the user defined value used during the call that caused
	 *        the backtrack.
	 * @return true or false. This does not affect the processing. 
	 * @throws Exception even if an exception is raised, this state is the 
	 *         'current state'.
	 */
	public boolean entryAfterBacktrack(
		Object thisStateKey,
		State from,
		Transition using,
		Object parameter,
		Exception e)
		throws StdExecutionException {
		return true;
	}

	/** The method is called to check if this state can be left.
	 *  Called by
	 *  StateTransitionDiagram.doActiveTransition
	 *  StateTransitionDiagram.doAutoTransition
	 *  StateTransitionDiagram.doTransition
	 *  StateTransitionDiagram.testActiveTransition
	 *  StateTransitionDiagram.testTransition
	 * @param thisStateKey identify the state in the STD. Each State has a unique
	 *        identifier.
	 * @param to is the new desired current state
	 * @param using is the transition to be used to access the 'to' state
	 *        from this state. 
	 * @param parameter any user-defined value
	 * @return true if the state can be left. False if this state cannot be left,
	 *         according to the values in the context.
	 * @throws Exception
	 */
	public boolean checkBeforeExit(
		Object thisStateKey,
		State to,
		Transition using,
		Object parameter)
		throws StdExecutionException {
		return true;
	}

	/** This method is executed when the STD leaves this state (identified
	 *  as the current state) to join a new state. In any circonstances, the
	 *  method 'checkBeforeExit' is called before 'onExit'.
	 * Called by
	 *  StateTransitionDiagram.doActiveTransition
	 *  StateTransitionDiagram.doAutoTransition
	 *  StateTransitionDiagram.doTransition
	 * @param thisStateKey identify the state in the STD. Each State has a unique
	 *        identifier.
	 * @param to identify the new destination
	 * @param using transition between this state and the to
	 * @param parameter
	 * @return true if the iterator can 'exit' the state
	 * @throws Exception an exception raised here does not have any impact
	 *         on any other states or transitions. The STD ermains in its
	 *         current state.
	 */
	public boolean onExit(Object thisStateKey, State to, Transition using, Object parameter)
		throws StdExecutionException {
		return true;
	}

	/** This method is called by the backtrack method on the current state.
	 * It is not called when an exception is raised from any 'state' method.
	 * @param thisStateKey identify the state in the STD. Each State has a unique
	 *        identifier.
	 * @param to is the desired destination
	 * @param using
	 * @param parameter
	 * @return if the state can be left using this method
	 * @throws Exception
	 */
	public boolean exitAfterBacktrack(
		Object thisStateKey,
		State to,
		Transition using,
		Object parameter)
		throws StdExecutionException {
		return true;
	}
}
