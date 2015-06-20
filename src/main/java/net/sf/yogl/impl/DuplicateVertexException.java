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
 * Each vertex whithin the graph must be unique. The vertex identifier is returned by the 'hash' method
 * (inherited from Object).This exception is thrown when the hash function of two vertices in the graph returns
 * the same value.
 */
public class DuplicateVertexException extends GraphException{
	private Vertex vertex;
	
	/**
	 * Constructor for DuplicateVertexException.
	 */
	public DuplicateVertexException() {
		super();
	}

	/**
	 * Constructor for DuplicateVertexException.
	 * @param arg0
	 * @param arg1
	 */
	public DuplicateVertexException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor for DuplicateVertexException.
	 * @param arg0
	 */
	public DuplicateVertexException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * Constructor for DuplicateVertexException.
	 * @param s
	 */
	public DuplicateVertexException(String s) {
		super(s);
	}

	public DuplicateVertexException(Vertex vertex){
		this.vertex = vertex;
	}
}
