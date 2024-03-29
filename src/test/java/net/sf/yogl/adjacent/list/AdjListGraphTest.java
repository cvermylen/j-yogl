package net.sf.yogl.adjacent.list;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.sf.yogl.exceptions.NodeNotFoundException;

class VertexTest extends AdjListVertex<String, String>{
	
}

class EdgeTest extends AdjListEdge<String, String> {

	public EdgeTest(String userValue, AdjListVertex<String, String> toVertex) throws NodeNotFoundException {
		super(userValue, toVertex);
	}
	
}

class GraphTest extends AdjListGraph<String, String>{}

public class AdjListGraphTest {

	@Test
	public void graphShouldBotBeEmpty(){
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String> v = new AdjListVertex<>("1");
		ndg.addRootVertex(v);
		
		assertFalse(ndg.isEmpty());
	}
	
	@Test
	public void getRootsShouldReturnAllRootNodes() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String> v1 = new AdjListVertex<>("1");
		ndg.addRootVertex(v1);
		AdjListVertex<String, String> v2 = new AdjListVertex<>("2");
		ndg.addRootVertex(v2);
		AdjListVertex<String, String> va = new AdjListVertex<>("A");
		ndg.addRootVertex(va);
		
		assertEquals(3, ndg.getRoots().size());
	}
	
	@Test
	public void graphShouldAllowDuplicateNodes() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String> v1 = new AdjListVertex<>("1");
		ndg.addRootVertex(v1);
		AdjListVertex<String, String> v2 = new AdjListVertex<>("2");
		ndg.addRootVertex(v2);
		AdjListVertex<String, String> v3 = new AdjListVertex<>("3");
		ndg.addRootVertex(v3);
		
		assertEquals(3, ndg.getRoots().size());
	}
	
	@Test
	public void clearAllVisitCountsShouldClearRootNodes() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String> v1 = new AdjListVertex<>("1");
		AdjListVertex<String, String>one = ndg.addRootVertex(v1);
		one.incVisitCounts();
		AdjListVertex<String, String> v2 = new AdjListVertex<>("2");
		AdjListVertex<String, String>two = ndg.addRootVertex(v2);
		two.incVisitCounts();
		AdjListVertex<String, String> va = new AdjListVertex<>("A");
		AdjListVertex<String, String>a = ndg.addRootVertex(va);
		a.incVisitCounts();
		a.incVisitCounts();
		
		ndg.clearAllVisitCounts();
		
		assertEquals(0, one.getVisitsCount());
		assertEquals(0, two.getVisitsCount());
		assertEquals(0, a.getVisitsCount());
	}
	
	@Test
	public void clearAllVisitCountsShouldClearVerticesAndEdges() throws NodeNotFoundException {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String> v1 = new AdjListVertex<>("1");
		AdjListVertex<String, String> va = new AdjListVertex<>("A");
		AdjListVertex<String, String> vb = new AdjListVertex<>("B");
		AdjListVertex<String, String> vc = new AdjListVertex<>("C");
		AdjListVertex<String, String>root = ndg.addRootVertex(v1);
		root.incVisitCounts();
		AdjListEdge<String, String>a = new AdjListEdge<String, String>("ev", va);
		root.addEdgeLast(a);
		a.incVisitCounts();
		AdjListVertex<String, String>two = a.setNextNode("2");
		a.setNextVertex(two);
		two.incVisitCounts();
		AdjListEdge<String, String>b = new AdjListEdge<>("ew", vb);
		two.addEdgeLast(b);
		b.incVisitCounts();
		AdjListVertex<String, String>three = b.setNextNode("3");
		b.setNextVertex(three);
		three.incVisitCounts();
		AdjListEdge<String, String>c = new AdjListEdge<>("ez", vc);
		root.addEdgeLast(c);
		c.setNextVertex(three);
		c.incVisitCounts();
		
		ndg.clearAllVisitCounts();
		
		assertEquals(0, root.getVisitsCount());
		assertEquals(0, two.getVisitsCount());
		assertEquals(0, three.getVisitsCount());
		assertEquals(0, a.getVisitsCount());
		assertEquals(0, b.getVisitsCount());
		assertEquals(0, c.getVisitsCount());
	}
	
	@Test
	public void nodesShouldBePrintedInOrder() throws NodeNotFoundException {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String> v1 = new AdjListVertex<>("1");
		AdjListVertex<String, String>root = ndg.addRootVertex(v1);
		AdjListVertex<String, String> va = new AdjListVertex<>("A");
		AdjListVertex<String, String> vb = new AdjListVertex<>("B");
		AdjListVertex<String, String> vc = new AdjListVertex<>("C");
		AdjListEdge<String, String>a = new AdjListEdge<>("ev", va);
		root.addEdgeLast(a);
		AdjListVertex<String, String>two = a.setNextNode("2");
		a.setNextVertex(two);
		AdjListEdge<String, String>b = new AdjListEdge<>("ew", vb);
		two.addEdgeLast(b);
		AdjListVertex<String, String>three = b.setNextNode("3");
		b.setNextVertex(three);
		AdjListEdge<String, String>c = new AdjListEdge<>("ez", vc);
		root.addEdgeLast(c);
		c.setNextVertex(three);
		System.out.println("A simple graph containing the nodes 1, 2 and 3, where 1 is the root"); 
		System.out.println(ndg);
	}

	@Test
	public void shouldProcessNodesAndLinks() throws NodeNotFoundException {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String> v1 = new AdjListVertex<>("1");
		AdjListVertex<String, String>root = ndg.addRootVertex(v1);
		AdjListVertex<String, String> va = new AdjListVertex<>("A");
		AdjListVertex<String, String> vb = new AdjListVertex<>("B");
		AdjListVertex<String, String> vc = new AdjListVertex<>("C");
		AdjListEdge<String, String>a = new AdjListEdge<>("ev", va);
		root.addEdgeLast(a);
		AdjListVertex<String, String>two = a.setNextNode("2");
		a.setNextVertex(two);
		AdjListEdge<String, String>b = new AdjListEdge<>("ew", vb);
		two.addEdgeLast(b);
		AdjListVertex<String, String>three = b.setNextNode("3");
		b.setNextVertex(three);
		AdjListEdge<String, String>c = new AdjListEdge<>("ez", vc);
		root.addEdgeLast(c);
		c.setNextVertex(three);
		StringBuilder nodeValues = new StringBuilder();
		StringBuilder linkValues = new StringBuilder();
		
		ndg.traverse((nodeValue)->{nodeValues.append(nodeValue);}, (linkValue)->{linkValues.append(linkValue);});
		
		assertEquals("123", nodeValues.toString());
		assertEquals("ABC", linkValues.toString());
	}
	
	@Test
	public void emptyGraphShouldReturnOutDegreeOfZero() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		
		int outDegree = ndg.getMaxOutDegree();
		
		assertEquals(0, outDegree);
	}
	
	@Test
	public void graphWithNoEdgeShouldReturnOutDegreeOfZero(){
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String> v1 = new AdjListVertex<>("1");
		ndg.addRootVertex(v1);
		AdjListVertex<String, String> v2 = new AdjListVertex<>("2");
		ndg.addRootVertex(v2);
		AdjListVertex<String, String> v3 = new AdjListVertex<>("3");
		ndg.addRootVertex(v3);
		
		int outDegree = ndg.getMaxOutDegree();
		
		assertEquals(0, outDegree);
	}
	
	@Test
	public void twoEdgesToSameVertexShouldReturnOutDegreeOfTwo() throws NodeNotFoundException {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String> v1 = new AdjListVertex<>("1");
		AdjListVertex<String, String>root = ndg.addRootVertex(v1);
		AdjListVertex<String, String> va = new AdjListVertex<>("A");
		AdjListVertex<String, String> vb = new AdjListVertex<>("B");
		AdjListVertex<String, String> vc = new AdjListVertex<>("C");
		AdjListEdge<String, String>a = new AdjListEdge<>("ev", va);
		root.addEdgeLast(a);
		AdjListVertex<String, String>two = a.setNextNode("2");
		a.setNextVertex(two);
		AdjListEdge<String, String>b = new AdjListEdge<>("ew", vb);
		two.addEdgeLast(b);
		AdjListVertex<String, String>three = b.setNextNode("3");
		b.setNextVertex(three);
		AdjListEdge<String, String>c = new AdjListEdge<>("ez", vc);
		root.addEdgeLast(c);
		c.setNextVertex(three);
		
		int outDegree = ndg.getMaxOutDegree();
		
		assertEquals(2, outDegree);
	}
	
	@Test
	public void countingOutDegreeShouldClearVisitCounters() throws NodeNotFoundException {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String> v1 = new AdjListVertex<>("1");
		AdjListVertex<String, String>root = ndg.addRootVertex(v1);
		AdjListVertex<String, String> va = new AdjListVertex<>("A");
		AdjListVertex<String, String> vb = new AdjListVertex<>("B");
		AdjListVertex<String, String> vc = new AdjListVertex<>("C");
		AdjListEdge<String, String>a = new AdjListEdge<>("ev", va);
		root.addEdgeLast(a);
		AdjListVertex<String, String>two = a.setNextNode("2");
		a.setNextVertex(two);
		AdjListEdge<String, String>b = new AdjListEdge<>("ew", vb);
		two.addEdgeLast(b);
		AdjListVertex<String, String>three = b.setNextNode("3");
		b.setNextVertex(three);
		AdjListEdge<String, String>c = new AdjListEdge<>("ez", vc);
		root.addEdgeLast(c);
		c.setNextVertex(three);
		
		ndg.getMaxOutDegree();
		
		assertEquals(0, root.getVisitsCount());
		assertEquals(0, two.getVisitsCount());
		assertEquals(0, three.getVisitsCount());
		assertEquals(0, a.getVisitsCount());
		assertEquals(0, b.getVisitsCount());
		assertEquals(0, c.getVisitsCount());
	}
}
