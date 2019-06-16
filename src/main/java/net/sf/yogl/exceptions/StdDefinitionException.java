   
package net.sf.yogl.exceptions;

/**
 * Describes an error detected during the validation of the STD. The STD
 * cannot be used
 */
public class StdDefinitionException extends GraphException {

	private static final long serialVersionUID = -2049423510053088502L;

	/**
	 * 
	 */
	public StdDefinitionException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StdDefinitionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public StdDefinitionException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param s
	 */
	public StdDefinitionException(String s) {
		super(s);
	}

}
