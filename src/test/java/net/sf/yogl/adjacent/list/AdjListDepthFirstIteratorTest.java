package net.sf.yogl.adjacent.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.sf.yogl.DepthFirstIterator;

public class AdjListDepthFirstIteratorTest {

	@Test
	public void emptyGraphShouldNotBreakIterator() {
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
	
	@Test
	public void nextShouldReturnRootNodesInOrder() {
		AdjListGraph<String, String> graph = new AdjListGraph<>();
		graph.addRootNode("root1");
		graph.addRootNode("root2");
		graph.addRootNode("root3");
		
		DepthFirstIterator df = graph.depthFirstIterator(0);
		
		assertTrue(df.hasNext());
		assertEquals("root1", df.next());
		
		assertTrue(df.hasNext());
		assertEquals("root2", df.next());
		
		assertTrue(df.hasNext());
		assertEquals("root3", df.next());
		
		assertFalse(df.hasNext());
	}
}
