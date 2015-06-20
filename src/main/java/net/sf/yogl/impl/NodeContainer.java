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

/** This class is used by AllowDupNodes to allow the user to introduce nodes
 * having the same contents. The basic idea is that a unique key is generated
 * inside the proxy class.
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
