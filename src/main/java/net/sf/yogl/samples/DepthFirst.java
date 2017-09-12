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
import net.sf.yogl.extras.GraphUtil;
import net.sf.yogl.types.NoDataGraph;


public class DepthFirst {

	public static void main(String[] args) throws Exception {
		NoDataGraph ndg = new NoDataGraph(/*new AdjListGraph(new HashMap())*/);
		ndg.addNode("1");
		ndg.addNode("2");
		ndg.addNode("3");
		ndg.addNode("4");
		ndg.addNode("5");
		ndg.addNode("6");
		ndg.addNode("7");
		ndg.addLinkFirst("1", "2", "A");
		//ndg.addLinkFirst("2", "3", "B");
		ndg.addLinkFirst("1", "3", "C");
		ndg.addLinkFirst("3", "4", "D");
		ndg.addLinkFirst("2", "5", "E");
		ndg.addLinkFirst("5", "6", "F");
		ndg.addLinkFirst("5", "7", "G");
		ndg.addLinkFirst("7", "4", "H");
		String s = GraphUtil.depthFirstToString(ndg, 1);
		System.out.println(s);
	}
}
