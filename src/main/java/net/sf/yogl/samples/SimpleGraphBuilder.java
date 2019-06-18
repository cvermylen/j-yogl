   
package net.sf.yogl.samples;

import java.util.HashMap;

import net.sf.yogl.adjacent.keyMap.AdjKeyGraph;
import net.sf.yogl.adjacent.keyMap.AdjKeyVertex;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.extras.GraphBuilderArray;
import net.sf.yogl.types.NoDataGraph;

public class SimpleGraphBuilder {

	public static void main(String[] args) throws GraphException {
		NoDataGraph ndg = new NoDataGraph();
		String[][]data = {
			{"1", "2", "A"},
			{"1", "3", "B"},
			{"2", "3", "C"}
		};
		GraphBuilderArray.buildNoDataGraph(data, ndg);
		System.out.println(ndg);
		
		System.out.println("\nSecond graph\n-----------");
		AdjKeyGraph<String, String, String, String> alg = new AdjKeyGraph<>(new HashMap<String, AdjKeyVertex<String, String, String, String>>());
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
