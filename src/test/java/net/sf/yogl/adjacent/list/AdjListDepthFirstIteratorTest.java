package net.sf.yogl.adjacent.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.iterators.DepthFirstIterator;

public class AdjListDepthFirstIteratorTest {

	@Test
	public void emptyGraphShouldNotBreakIterator() throws GraphException {
		AdjListGraph<String, String> graph = new AdjListGraph<>();
		DepthFirstIterator<AdjListVertex<String, String>, AdjListEdge<String, String>> a = graph.depthFirstIterator(null, 1);
		
		assertFalse(a.hasNext());
	}

	@Test
	public void rootNodesShouldBeReturnedInOrder() throws GraphException {
		AdjListGraph<String, String> graph = new AdjListGraph<>();
		AdjListVertex<String, String> a = new AdjListVertex<>("a");
		graph.addRootVertex(a);
		AdjListVertex<String, String> b = new AdjListVertex<>("4");
		graph.addRootVertex(b);
		AdjListVertex<String, String> c = new AdjListVertex<>("b");
		graph.addRootVertex(c);
		
		DepthFirstIterator<AdjListVertex<String, String>, AdjListEdge<String, String>> df = graph.depthFirstIterator(Arrays.asList(a, b, c), 1);
		
		assertTrue(df.hasNext());
		assertEquals("a", df.next());
		assertTrue(df.hasNext());
		assertEquals("4", df.next());
		assertTrue(df.hasNext());
		assertEquals("b", df.next());
		assertFalse(df.hasNext());
	}
	
	@Test
	public void nextShouldReturnRootNodesInOrder() throws GraphException {
		AdjListGraph<String, String> graph = new AdjListGraph<>();
		AdjListVertex<String, String> a = new AdjListVertex<>("root1");
		graph.addRootVertex(a);
		AdjListVertex<String, String> b = new AdjListVertex<>("root2");
		graph.addRootVertex(b);
		AdjListVertex<String, String> c = new AdjListVertex<>("root3");
		graph.addRootVertex(c);
		
		DepthFirstIterator<AdjListVertex<String, String>, AdjListEdge<String, String>> df = graph.depthFirstIterator(Arrays.asList(a, b, c), 0);
		
		assertTrue(df.hasNext());
		assertEquals("root1", df.next());
		
		assertTrue(df.hasNext());
		assertEquals("root2", df.next());
		
		assertTrue(df.hasNext());
		assertEquals("root3", df.next());
		
		assertFalse(df.hasNext());
	}
}
