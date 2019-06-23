   
package net.sf.yogl.std;

import net.sf.yogl.exceptions.StdExecutionException;
import net.sf.yogl.uniqueElements.UniqueEdgeIntf;

/** The interface can be implemented by links in the State Transition diagram.
 *  When a link is TRAVERSED, the doAction is triggered.
 *  Before the link can be traversed, the 'testAction' can be triggered.
 *  In a STD, a node that does not implements this interface is a 'nil' node.
 */
public interface TransitionIntf<TK extends Comparable<TK>, SK extends Comparable<SK>, TS extends TransitionIntf<TK, SK, TS, DS, PAR>, DS extends StateIntf<SK, TK, DS, TS, PAR>, PAR> 
		extends UniqueEdgeIntf<TS, DS, TK, SK>{

	public TK getKey();
	
	/** The method will be called when the link is crossed. Before this
	 *  method is called by the STD engine, the 'testAction' method has also
	 *  be called.
	 * @param thisTransitionKey identifiy this transition in the STD. The key
	 *        is used by the user to walk through this transition.
	 * @param from identify which was the previous current state
	 * @param parameter is the user value
	 * @param to identify the next current state
	 * @throws Exception is equivalent to a 'false' return value.
	 * @return false if the transition cannot be performed. The current state
	 *         is reversed to the 'from' state. This is not the expected situation
	 *         since the 'testAction' method was called just before. If the result 
	 *         is 'false', there will be no backtrack operation on the from
	 *         state.
	 */
	public boolean doTransition(DS from, PAR parameter)
		throws StdExecutionException;

	/** The method is called during a backtrack operation.
	 * @param thisTransitionKey identifiy this transition in the STD. The key
	 *        is used by the user to walk through this transition.
	 * @param from
	 * @param parameter
	 * @param to
	 * @param e if not null, is the exception that caused the backtrack. If
	 *        null, the backtrack was directly called by the 'backtrack' method.
	 * @return
	 * @throws Exception
	 */
	public boolean actionAfterBacktrack(DS from, PAR parameter, Exception e)
		throws StdExecutionException;

	/** returns a predictable value of the action. It is used to determine
	 *  in advance if the transition can or cannot be 'crossed'.
	 *  @param thisTransitionKey identifiy this transition in the STD. The key
	 *        is used by the user to walk through this transition.
	 *  @param from identify the current state
	 *  @param parameter is any user defined value
	 *  @param to is the desired destination
	 *  @return true if this transition can be crossed according to the
	 *          given context. False means also that any further test
	 *          will not be performed.
	 *  @exception Exception 
	 */
	public boolean testAction(DS from, PAR parameter)
		throws StdExecutionException;
}