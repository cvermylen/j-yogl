package net.sf.yogl.exceptions;


public class DuplicateLinkException extends GraphException{

	private static final long serialVersionUID = 5871935564586436632L;

	private Object nodeKeyFrom;
	private Object link;
	private Object nodeKeyTo;
	
	/**
	 * 
	 */
	public DuplicateLinkException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DuplicateLinkException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public DuplicateLinkException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param s
	 */
	public DuplicateLinkException(String s) {
		super(s);
	}

	public String toString(){
		return "from:["+nodeKeyFrom+"] link:["+link+"] to:["+nodeKeyTo+"]";
	}
	/**
	 * @return
	 */
	public Object getLink() {
		return link;
	}

	/**
	 * @return
	 */
	public Object getNodeKeyFrom() {
		return nodeKeyFrom;
	}

	/**
	 * @return
	 */
	public Object getNodeKeyTo() {
		return nodeKeyTo;
	}

	/**
	 * @param object
	 */
	public void setLink(Object object) {
		link = object;
	}

	/**
	 * @param object
	 */
	public void setNodeKeyFrom(Object object) {
		nodeKeyFrom = object;
	}

	/**
	 * @param object
	 */
	public void setNodeKeyTo(Object object) {
		nodeKeyTo = object;
	}

}
