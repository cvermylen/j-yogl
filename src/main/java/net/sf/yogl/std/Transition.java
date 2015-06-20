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
   
package net.sf.yogl.std;

import net.sf.yogl.exceptions.StdExecutionException;

/** The interface can be implemented by links in the State Transition diagram.
 *  When a link is TRAVERSED, the doAction is triggered.
 *  Before the link can be traversed, the 'testAction' can be triggered.
 *  In a STD, a node that does not implements this interface is a 'nil' node.
 */
public abstract class Transition {

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
	public boolean doAction(
		Object thisTransitionKey,
		State from,
		Object parameter,
		State to)
		throws StdExecutionException {
		return true;
	}

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
	public boolean actionAfterBacktrack(
		Object thisTransitionKey,
		State from,
		Object parameter,
		State to,
		Exception e)
		throws StdExecutionException {
		return true;
	}

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
	public boolean testAction(
		Object thisTransitionKey,
		State from,
		Object parameter,
		State to)
		throws StdExecutionException {
		return true;
	}
}