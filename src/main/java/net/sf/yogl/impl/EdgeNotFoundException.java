   
package net.sf.yogl.impl;

import net.sf.yogl.exceptions.GraphException;

public class EdgeNotFoundException extends GraphException{
	/**
	 * Constructor for EdgeNotFoundException.
	 */
	public EdgeNotFoundException() {
		super();
	}

	/**
	 * Constructor for EdgeNotFoundException.
	 * @param arg0
	 * @param arg1
	 */
	public EdgeNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor for EdgeNotFoundException.
	 * @param arg0
	 */
	public EdgeNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public EdgeNotFoundException(String s){
		super(s);
	}
}
