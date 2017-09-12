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
   
package net.sf.yogl.samples;

import java.util.HashMap;

import net.sf.yogl.adjacent.list.AdjListGraph;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.types.NoDataGraph;

public class ReadMeFirst {

	public static void main(String[] args) throws GraphException {
		NoDataGraph ndg = new NoDataGraph(/*new AdjListGraph(new HashMap())*/);
		ndg.addNode("1");
		ndg.addNode("2");
		ndg.addNode("3");
		ndg.addLinkFirst("1", "2", "A");
		ndg.addLinkFirst("2", "3", "B");
		ndg.addLinkFirst("1", "3", "C");
		System.out.println("A simple graph containing the nodes 1, 2 and 3, where 1 is the root"); 
		System.out.println(ndg);
	}
}
