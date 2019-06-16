   
package net.sf.yogl.exceptions;


public class LinkNotFoundException extends GraphException {
	
	private static final long serialVersionUID = -3224750154817333454L;

	private Object link;
	
	/**
	 * 
	 */
	public LinkNotFoundException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public LinkNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public LinkNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public LinkNotFoundException(String s){
		super(s);
	}

	/**
	 * @return
	 */
	public Object getLink() {
		return link;
	}

	/**
	 * @param object
	 */
	public void setLink(Object object) {
		link = object;
	}

}
