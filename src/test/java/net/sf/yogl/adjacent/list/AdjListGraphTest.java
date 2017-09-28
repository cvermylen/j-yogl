package net.sf.yogl.adjacent.list;

import static org.junit.Assert.*;

import org.junit.Test;

public class AdjListGraphTest {

	@Test
	public void graphShouldBotBeEmpty() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		
		ndg.addRootNode("1");
		
		assertFalse(ndg.isEmpty());
	}
	
	@Test
	public void getRootsShouldReturnAllRootNodes() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		
		ndg.addRootNode("1");
		ndg.addRootNode("2");
		ndg.addRootNode("A");
		
		assertEquals(3, ndg.getRoots().size());
	}
	
	@Test
	public void graphShouldAllowDuplicateNodes() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		
		ndg.addRootNode("1");
		ndg.addRootNode("1");
		ndg.addRootNode("1");
		
		assertEquals(3, ndg.getRoots().size());
	}
	
	@Test
	public void clearAllVisitCountsShouldClearRootNodes() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String>one = ndg.addRootNode("1");
		one.incVisitCounts();
		AdjListVertex<String, String>two = ndg.addRootNode("2");
		two.incVisitCounts();
		AdjListVertex<String, String>a = ndg.addRootNode("A");
		a.incVisitCounts();
		a.incVisitCounts();
		
		ndg.clearAllVisitCounts();
		
		assertEquals(0, one.getVisitsCount());
		assertEquals(0, two.getVisitsCount());
		assertEquals(0, a.getVisitsCount());
	}
	
	@Test
	public void clearAllVisitCountsShouldClearVerticesAndEdges() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String>root = ndg.addRootNode("1");
		root.incVisitCounts();
		AdjListEdge<String, String>a = root.addLinkLast("A");
		a.incVisitCounts();
		AdjListVertex<String, String>two = a.setNextNode("2");
		a.setNextVertex(two);
		two.incVisitCounts();
		AdjListEdge<String, String>b = two.addLinkLast("B");
		b.incVisitCounts();
		AdjListVertex<String, String>three = b.setNextNode("3");
		b.setNextVertex(three);
		three.incVisitCounts();
		AdjListEdge<String, String>c = root.addLinkLast("C");
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
	public void nodesShouldBePrintedInOrder() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String>root = ndg.addRootNode("1");
		AdjListEdge<String, String>a = root.addLinkLast("A");
		AdjListVertex<String, String>two = a.setNextNode("2");
		a.setNextVertex(two);
		AdjListEdge<String, String>b = two.addLinkLast("B");
		AdjListVertex<String, String>three = b.setNextNode("3");
		b.setNextVertex(three);
		AdjListEdge<String, String>c = root.addLinkLast("C");
		c.setNextVertex(three);
		System.out.println("A simple graph containing the nodes 1, 2 and 3, where 1 is the root"); 
		System.out.println(ndg);
	}

	@Test
	public void shouldProcessNodesAndLinks() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String>root = ndg.addRootNode("1");
		AdjListEdge<String, String>a = root.addLinkLast("A");
		AdjListVertex<String, String>two = a.setNextNode("2");
		a.setNextVertex(two);
		AdjListEdge<String, String>b = two.addLinkLast("B");
		AdjListVertex<String, String>three = b.setNextNode("3");
		b.setNextVertex(three);
		AdjListEdge<String, String>c = root.addLinkLast("C");
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
		ndg.addRootNode("1");
		ndg.addRootNode("2");
		ndg.addRootNode("3");
		
		int outDegree = ndg.getMaxOutDegree();
		
		assertEquals(0, outDegree);
	}
	
	@Test
	public void twoEdgesToSameVertexShouldReturnOutDegreeOfTwo() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String>root = ndg.addRootNode("1");
		AdjListEdge<String, String>a = root.addLinkLast("A");
		AdjListVertex<String, String>two = a.setNextNode("2");
		a.setNextVertex(two);
		AdjListEdge<String, String>b = two.addLinkLast("B");
		AdjListVertex<String, String>three = b.setNextNode("3");
		b.setNextVertex(three);
		AdjListEdge<String, String>c = root.addLinkLast("C");
		c.setNextVertex(three);
		
		int outDegree = ndg.getMaxOutDegree();
		
		assertEquals(2, outDegree);
	}
	
	@Test
	public void countingOutDegreeShouldClearVisitCounters() {
		AdjListGraph<String, String> ndg = new AdjListGraph<>();
		AdjListVertex<String, String>root = ndg.addRootNode("1");
		AdjListEdge<String, String>a = root.addLinkLast("A");
		AdjListVertex<String, String>two = a.setNextNode("2");
		a.setNextVertex(two);
		AdjListEdge<String, String>b = two.addLinkLast("B");
		AdjListVertex<String, String>three = b.setNextNode("3");
		b.setNextVertex(three);
		AdjListEdge<String, String>c = root.addLinkLast("C");
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
