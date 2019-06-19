   
package net.sf.yogl.samples;

import net.sf.yogl.adjacent.keyMap.GraphUtil;
import net.sf.yogl.types.NoDataGraph;


public class DepthFirst {

	public static void main(String[] args) throws Exception {
		NoDataGraph ndg = new NoDataGraph(/*new AdjListGraph(new HashMap())*/);
		ndg.tryAddNode("1", true);
		ndg.tryAddNode("2", false);
		ndg.tryAddNode("3", false);
		ndg.tryAddNode("4", false);
		ndg.tryAddNode("5", false);
		ndg.tryAddNode("6", false);
		ndg.tryAddNode("7", false);
		
		ndg.tryAddLinkFirst("1", "2", "A");
		//ndg.addLinkFirst("2", "3", "B");
		ndg.tryAddLinkFirst("1", "3", "C");
		ndg.tryAddLinkFirst("3", "4", "D");
		ndg.tryAddLinkFirst("2", "5", "E");
		ndg.tryAddLinkFirst("5", "6", "F");
		ndg.tryAddLinkFirst("5", "7", "G");
		ndg.tryAddLinkFirst("7", "4", "H");
		String s = GraphUtil.depthFirstToString(ndg, 1);
		System.out.println(s);
	}
}
