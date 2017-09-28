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

import net.sf.yogl.adjacent.keyMap.GraphAdapter;
import net.sf.yogl.adjacent.list.AdjListGraphTest;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.extras.GraphBuilderArray;
import net.sf.yogl.types.NoDataGraph;

public class SimpleGraphBuilder {

	public static void main(String[] args) throws GraphException {
		NoDataGraph ndg = new NoDataGraph(/*new AdjListGraph(new HashMap())*/);
		String[][]data = {
			{"1", "2", "A"},
			{"1", "3", "B"},
			{"2", "3", "C"}
		};
		GraphBuilderArray.buildNoDataGraph(data, ndg);
		System.out.println(ndg);
		
		System.out.println("\nSecond graph\n-----------");
		GraphAdapter alg = new GraphAdapter(/*new AdjListGraph(new HashMap())*/);
		String[][]data2 = {
			{"1", "as the root node"},
			{"2", "intermediate node"},
			{"3", "intermediate node"},
			{"4", "end node"},
			{"1", "2", "A", "Link from 1 to 2"},
			{"1", "3", "B", "Link from 1 to 3"},
			{"2", "3", "C", "Link from 2 to 3"},
			{"3", "4", "D", "Link from 3 to 4"}
		};
		GraphBuilderArray.buildGraph(data2, alg);
		System.out.println(alg);
	}
}
