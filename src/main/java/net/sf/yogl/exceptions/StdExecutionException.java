   
package net.sf.yogl.exceptions;

/**
 * Reports an error occurred during a State or Transition callback.
 */
public class StdExecutionException extends GraphException {

	private static final long serialVersionUID = -8187330048924600230L;

	/**
	 * 
	 */
	public StdExecutionException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StdExecutionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public StdExecutionException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param s
	 */
	public StdExecutionException(String s) {
		super(s);
	}

}
