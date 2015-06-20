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
   
package net.sf.yogl.types;
/**
 * The VertexType is used to set up the type of each vertex regarding the
 * graph. From a graph perspective, we distinguish 3 types of nodes:
 * entry points, end nodes and between them intermediates nodes.
 */

public class VertexType {
	/** intermediate vertex. Has predecessors AND successors */
	public static final int NONE = 1;

	/** starting vertex in the graph. Has successors only, no predessors */
	public static final int START = 2;

	/** indicates a final(terminating) vertex. Has predecessors only,
	 * no successors */
	public static final int END = 4;

	/** If the graph contains only one vertex, this latter is a start & end
	 *  node
	 */
	public static final int STARTEND = 8;
}
