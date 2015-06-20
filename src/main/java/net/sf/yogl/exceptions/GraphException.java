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
   
package net.sf.yogl.exceptions;

import net.sf.yogl.AbstractGraph;

/**
 * 'GraphException' is the root class for all exceptions thrown by graph methods. The purpose of this class
 * is just to make more easy the handling of exceptions by the graph user.
 */
public class GraphException extends Exception {

	private static final long serialVersionUID = 8963356757643065040L;

	private AbstractGraph graph;
	
	/**
	 * Constructor for GraphException.
	 */
	public GraphException() {
		super();
	}

	/**
	 * Constructor for GraphException.
	 * @param arg0
	 * @param arg1
	 */
	public GraphException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor for GraphException.
	 * @param arg0
	 */
	public GraphException(Throwable arg0) {
		super(arg0);
	}

	public GraphException(String s){
		super(s);
	}
	/**
	 * @return
	 */
	public AbstractGraph getGraph() {
		return graph;
	}

	/**
	 * @param graph
	 */
	public void setGraph(AbstractGraph graph) {
		this.graph = graph;
	}

}
