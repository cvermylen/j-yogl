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
   
package net.sf.yogl.extras;

import net.sf.yogl.ComparableKeysGraph;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.types.NoDataGraph;

public class GraphBuilderArray {

	/** Utility method to create a simple graph containing no data.
	 *  The method uses the information in the 'srcData' to fill in
	 *  the graph.
	 * @param srcData is an array where each line describes a relation
	 *        between 2 nodes.
	 *        The element at [i][0] contains the source node id
	 *        The element at [i][1] contains the destination node id
	 *        The element at [i][2] contains the link id
	 * @param destGraph on entry, the graph must be empty.
	 * @throws GraphException
	 */
	public static void buildNoDataGraph(
		Object[][] srcData,
		NoDataGraph destGraph)
		throws GraphException {
		if (destGraph.getNodeCount() > 0) {
			throw new GraphCorruptedException("The graph must be empty");
		}
		PreparedGraph pg = new PreparedGraph();
		for (int i = 0; i < srcData.length; i++) {
			pg.tryAddNode(srcData[i][0], null);
			pg.tryAddNode(srcData[i][1], null);
		}
		for (int i = 0; i < srcData.length; i++) {
			pg.tryAddLink(srcData[i][0], srcData[i][1], srcData[i][2]);
		}
		pg.populateGraph(destGraph);
	}

	/** Insert the content of the array into the graph.
	 * 
	 * @param srcData contains the information to build the graph. Each line
	 *        describes a node OR a link. If the line contains 2 elements, then
	 *        we have a node; a line with 4 elements describes a link.
	 *        The node line:
	 *        	Element at[i][0] contains the node id
	 *          Element at[i][1] contains the node data
	 *        The link line:
	 *          Element at[i][0] contains the source node id
	 *          Element at[i][1] contains the destination node id
	 *          Element at[i][2] contains the link id
	 *          Element at[i][3] contains the link data.
	 * @param destGraph
	 * @throws GraphException
	 */
	public static void buildGraph(Object[][] srcData, ComparableKeysGraph destGraph) throws GraphException {
		if (destGraph.getNodeCount() > 0) {
			throw new GraphCorruptedException("The graph must be empty");
		}
		PreparedGraph pg = new PreparedGraph();
		for (int i = 0; i < srcData.length; i++) {
			if(srcData[i].length == 2){
				pg.tryAddNode(srcData[i][0], srcData[i][1]);
			}else if(srcData[i].length == 4){
				pg.tryAddLink(srcData[i][0], srcData[i][1],srcData[i][2]/*,srcData[i][3]*/);
			}else{
				throw new GraphCorruptedException("Cannot use graph description: bad data at line:"+i);
			}
		}
		pg.populateGraph(destGraph);
	}
}
