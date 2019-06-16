package net.sf.yogl.exceptions;


public class GraphCorruptedException extends GraphException{

	private static final long serialVersionUID = -2624276347651343600L;

	/**
	 * 
	 */
	public GraphCorruptedException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public GraphCorruptedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public GraphCorruptedException(Throwable arg0) {
		super(arg0);
	}

	public GraphCorruptedException(String s){
		super(s);
	}
}
