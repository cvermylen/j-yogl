   
package net.sf.yogl.impl;

/** This class is used by AllowDupNodes to allow the user to introduce nodes
 * having the same contents. The basic idea is that a unique key is generated
 * inside the proxy class.
 * REFACTOR: thsi should extends a Vertex
 */

final public class NodeContainer{
	/** user object
	 */
	private Object userObject;

	/** unique identifier
	 */
	private int key;

	/**
	 */
	public NodeContainer(int key, Object userObject){
		this.key = key;
		this.userObject = userObject;
	}

	public boolean equals(Object anotherNodeContainer){
		NodeContainer container = (NodeContainer) anotherNodeContainer;
		boolean res = false;

		if(container !=  null){
			res = this.key == container.key;
		}

		return res;
	}

	public String toString(){
		return("NodeContainer("+key+")");
	}

	public Object clone(){
		NodeContainer n = new NodeContainer(key, userObject);
		return n;
	}

	public Object getValue(){
		return userObject;
	}
}
