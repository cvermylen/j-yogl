   
package net.sf.yogl.exceptions;


public class DuplicateNodeException extends GraphException{

	private static final long serialVersionUID = 3668915150848910774L;

	private Object nodeKey;
	
	/**
	 * 
	 */
	public DuplicateNodeException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DuplicateNodeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public DuplicateNodeException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param s
	 */
	public DuplicateNodeException(String s) {
		super(s);
	}

	/**
	 * @return
	 */
	public Object getNodeKey() {
		return nodeKey;
	}

	/**
	 * @param object
	 */
	public void setNodeKey(Object object) {
		nodeKey = object;
	}

}
