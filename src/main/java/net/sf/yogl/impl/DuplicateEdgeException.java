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
   
package net.sf.yogl.impl;

import net.sf.yogl.exceptions.GraphException;

/**
 * Each edge is uniquely identified by the 'from' vertex and the return value of its 'hash' function.
 * This mean that 2 edges having the same 'from' vertex may not have the same 'hash' return value.
 */
public class DuplicateEdgeException extends GraphException{
	
	private Vertex pred;
	private Edge edge;
	
	/**
	 * Constructor for DuplicateLinkException.
	 */
	public DuplicateEdgeException(Vertex pred, Edge edge) {
		super();
		this.pred = pred;
		this.edge = edge;
	}

	/**
	 * Constructor for DuplicateEdgeException.
	 */
	public DuplicateEdgeException() {
		super();
	}

	/**
	 * Constructor for DuplicateEdgeException.
	 * @param arg0
	 * @param arg1
	 */
	public DuplicateEdgeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor for DuplicateEdgeException.
	 * @param arg0
	 */
	public DuplicateEdgeException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * Constructor for DuplicateEdgeException.
	 * @param s
	 */
	public DuplicateEdgeException(String s) {
		super(s);
	}

	/**
	 * Constructor for DuplicateLinkException.
	 * @param arg0
	 */
	public DuplicateEdgeException(Vertex pred, Edge edge, Throwable arg0) {
		super(arg0);
		this.pred = pred;
		this.edge = edge;
	}

	public String toString(){
		return edge.toString();
	}
}
