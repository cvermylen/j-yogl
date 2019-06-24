   
package net.sf.yogl.samples;

import java.util.HashMap;

import net.sf.yogl.adjacent.keyValue.KeyValueGraph;
import net.sf.yogl.adjacent.keyValue.KeyValueVertex;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.types.NoDataGraph;
import net.sf.yogl.utils.GraphBuilderArray;

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
		KeyValueGraph<String, String, String> alg = new KeyValueGraph<>();
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
