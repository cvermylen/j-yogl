   
package net.sf.yogl.samples;

import java.util.HashMap;

import net.sf.yogl.adjacent.list.AdjListGraphTest;
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
