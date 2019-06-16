   
package net.sf.yogl.extras;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import net.sf.yogl.Graph;
import net.sf.yogl.adjacent.list.AdjListGraphTest;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.impl.ImplementationGraph;

/** A simple graphSpecifier creates a graph based on a definition extracted
 *  from an ASCII file.
 *  The format defined for this definition file is:
 *  fromNodeIdentification trnsitionLinkIdentification toNodeIdentification
 *  These 3 items are placed on a single line an are blank-separated.
 *  <LI>Example:
 *  <LI>Consider the definition file having the following content:
 *  <LI> type	nodeSource isConnectedTo destNode
 *  <LI><LI>
 *  <LI>A valid GraphAdapter will be constructed provided the user supplies
 *  this piece of code:
 *  <LI>SimpleGraphSpecifier s = new SimpleGraphSpecifier();
 *  <LI>s.defineNode("nodeSource", new SomeClassNode(...));
 *  <LI>s.defineNode("destNode", new AnotherClassNode(...));
 *  <LI>s.defineLink("isConnectedTo", new SomeClassLink(...));
 *  <LI>AbstractGraph graph = s.map2Graph("d:\toto");
 */
//TODO FIXME
public class SimpleGraphSpecifier {

	/** The 2 hashtables maintains a mapping between the string used
	  *  in the definition file and the instance of the object.
	  */
	HashMap nodesDefinition = new HashMap();
	HashMap linksDefinition = new HashMap();

	boolean defineLinkFromFile = false;

	/** Construct an empty specifier
	 */
	public SimpleGraphSpecifier() {

	}

	/** Creates a mapping between a logical name and an object instance representing
	 *  a link in the resulting graph.
	 *  @param linkName identifies the node in the definition file
	 *  @param link is the instance that will match the previous name in the graph
	 *  @exception NullPointerException if one of the above parameter is null
	 */
	public void defineLink(String linkName, Object link) {

		if (linkName == null) {
			throw new NullPointerException(
				"SimpleGraphSpecifier.defineLink "
					+ "parameter 'linkName' is null");
		}
		if (link == null) {
			throw new NullPointerException(
				"SimpleGraphSpecifier.defineLink "
					+ "parameter 'link' is null");
		}
		linksDefinition.put(linkName, link);
	}

	public void defineLinkFromFile(boolean defineLinkFromFile) {

		this.defineLinkFromFile = defineLinkFromFile;
	}

	/** Creates a mapping between a logical name and an object instance representing
	 *  a node in the resulting graph.
	 *  @param nodeName identifies the node in the definition file
	 *  @param node is the instance that will match the previous name in the graph
	 *  @exception NullPointerException if one of the above parameter is null
	 */
	public void defineNode(String nodeName, Object node) {

		if (nodeName == null) {
			throw new NullPointerException(
				"SimpleGraphSpecifier.defineNode "
					+ "parameter 'nodeName' is null");
		}
		if (node == null) {
			throw new NullPointerException(
				"SimpleGraphSpecifier.defineNode "
					+ "parameter 'node' is null");
		}
		nodesDefinition.put(nodeName, node);
	}

	/** Read the definition of the graph and map the nodes defined previously.
	 *  All elements referenced by this definition file must have been
	 *  defined previously.
	 *  @param definitionFile: full path to a valid graph definition file
	 *  @exception InvalidVertexException if one of the nodes referenced in the
	 *             definition file has not been previously set up using the
	 *             'defineNode' method.
	 *  @exception InvalidEdgeException if one of the links referenced in the
	 *             definition file has not been previously set up using the
	 *             'defineLink' method
	 */
	public Graph map2Graph(String definitionFile)
		throws IOException, GraphException {

		if (definitionFile == null) {
			throw new NullPointerException(
				"SimpleGraphSpecifier.map2Graph: "
					+ "parameter 'definitionFile' is null");
		}
		File file = new File(definitionFile);
		Graph graph = new AdjListGraphTest();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		while (reader.ready()) {
			String s = reader.readLine();
			StringTokenizer tokenizer = new StringTokenizer(s);
			String type = tokenizer.nextToken();
			String fromNodeId = tokenizer.nextToken();
			String linkId = tokenizer.nextToken();
			Object link = null;
			if (defineLinkFromFile == true) {
				if (!linkId.equals("null")) {
					link = new String(linkId);
				}
			} else {
				link = linksDefinition.get(linkId);
			}
			String toNodeId = tokenizer.nextToken();
			Object from = nodesDefinition.get(fromNodeId);
			Object to = nodesDefinition.get(toNodeId);

//			graph.addNode(fromNodeId, from);
//			graph.tryAddNode(toNodeId, to);
//			graph.addLinkLast((Comparable)fromNodeId, (Comparable)toNodeId, (Comparable)link, null);
		}
		return graph;
	}

	public Graph map2GraphArray(String[][] definitions)
		throws GraphException {

		Graph graph = new AdjListGraphTest();
		for (int i = 0; i < definitions.length; i++) {
			String type = definitions[i][0];
			String fromNodeId = definitions[i][1];
			String linkId = definitions[i][2];
			Object link = null;
			if (defineLinkFromFile == true) {
				if (linkId.equals("nulll")) {
					link = new String(linkId);
				}
			} else {
				link = linksDefinition.get(linkId);
			}
			String toNodeId = definitions[i][3];
			Object from = nodesDefinition.get(fromNodeId);
			Object to = nodesDefinition.get(toNodeId);

//			graph.tryAddNode(fromNodeId, from);
//			graph.tryAddNode(toNodeId, to);
//			graph.addLinkLast((Comparable)fromNodeId, (Comparable)toNodeId, (Comparable)link, null);
		}
		return graph;
	}
}
