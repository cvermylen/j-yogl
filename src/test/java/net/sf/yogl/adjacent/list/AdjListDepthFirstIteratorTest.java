package net.sf.yogl.adjacent.list;

import static org.junit.Assert.*;

import org.junit.Test;

public class AdjListDepthFirstIteratorTest {

	@Test
	public void emptyGraphShouldNotBerakIterator() {
		AdjListGraph<String, String> graph = new AdjListGraph<>();
		AdjListDepthFirstIterator<String, String> a = graph.depthFirstIterator(1);
		
		assertFalse(a.hasNext());
	}

	@Test
	public void rootNodesShouldBeReturnedInOrder() {
		AdjListGraph<String, String> graph = new AdjListGraph<>();
		graph.addRootNode("a");
		graph.addRootNode("4");
		graph.addRootNode("b");
		
		AdjListDepthFirstIterator<String, String> a = graph.depthFirstIterator(1);
		
		assertTrue(a.hasNext());
		assertEquals("a", a.next());
		assertTrue(a.hasNext());
		assertEquals("4", a.next());
		assertTrue(a.hasNext());
		assertEquals("b", a.next());
		assertFalse(a.hasNext());
	}
}
