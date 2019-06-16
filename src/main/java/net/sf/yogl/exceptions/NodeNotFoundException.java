   
package net.sf.yogl.exceptions;


/** The node key is not defined in the given graph. 
 */
public class NodeNotFoundException extends GraphException {
	
	private static final long serialVersionUID = -532784334454133700L;

	private Object nodeKey;
	
	/**
	 * 
	 */
	public NodeNotFoundException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public NodeNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public NodeNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public NodeNotFoundException(String s){
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
