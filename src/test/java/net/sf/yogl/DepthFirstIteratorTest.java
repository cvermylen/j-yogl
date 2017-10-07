package net.sf.yogl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

import net.sf.yogl.exceptions.GraphException;

public class DepthFirstIteratorTest {

	class V extends Vertex<E>{
		List<E>el = new LinkedList<>();
		String value;
		
		@Override
		public List<E> getOutgoingEdges() {
			return el;
		}
	};
	
	class E extends Edge<V> {
		V v;
		String value;
		
		@Override
		public V getOutgoingVertex() {
			return v;
		}
	}
	
	class DFImpl extends DepthFirstIterator<V, E>{

		public DFImpl(Collection<V> roots) throws GraphException {
			super(roots);
		}
	}
	
	@Test
	public void internalLinearEdgeShouldReturnOneEdge() {
		V o = new V();
		E e = createEdge("e");
		o.getOutgoingEdges().add(e);
		
		DepthFirstIterator<V, E>df = new DFImpl(Arrays.asList(o));
		
		assertEquals(1, df.vStack.size());
		assertTrue(df.vStack.get(0).hasMoreEdges());
		E testedValue = df.vStack.get(0).nextEdge();
		assertFalse(df.vStack.get(0).hasMoreEdges());
		assertNull(testedValue.v);
		assertEquals("e", testedValue.value);
	}

	@Test(expected=NoSuchElementException.class)
	public void callingNextOnEmptyEdgeStackShouldThrowException() {
		V o = new V();
		
		DepthFirstIterator<V, E>df = new DFImpl(Arrays.asList(o));
		
		assertEquals(1, df.vStack.size());
		assertFalse(df.vStack.get(0).hasMoreEdges());
		df.vStack.get(0).nextEdge();
	}
	
	@Test
	public void internalLinearEdgeShouldReturn5EdgesInInsertionOrder() {
		V root = new V();
		E e1 = createEdge("e1");
		root.getOutgoingEdges().add(e1);
		E e2 = createEdge("e2");
		root.getOutgoingEdges().add(e2);
		E e3 = createEdge("e3");
		root.getOutgoingEdges().add(e3);
		E e4 = createEdge("e4");
		root.getOutgoingEdges().add(e4);
		E e5 = createEdge("e5");
		root.getOutgoingEdges().add(e5);
		
		DepthFirstIterator<V, E>df = new DFImpl(Arrays.asList(root));
		
		assertEquals(1, df.vStack.size());
		assertTrue(df.vStack.get(0).hasMoreEdges());
		E testedValue1 = df.vStack.get(0).nextEdge();
		assertTrue(df.vStack.get(0).hasMoreEdges());
		assertEquals("e1", testedValue1.value);
		E testedValue2 = df.vStack.get(0).nextEdge();
		assertTrue(df.vStack.get(0).hasMoreEdges());
		assertEquals("e2", testedValue2.value);
		E testedValue3 = df.vStack.get(0).nextEdge();
		assertTrue(df.vStack.get(0).hasMoreEdges());
		assertEquals("e3", testedValue3.value);
		E testedValue4 = df.vStack.get(0).nextEdge();
		assertTrue(df.vStack.get(0).hasMoreEdges());
		assertEquals("e4", testedValue4.value);
		E testedValue5 = df.vStack.get(0).nextEdge();
		assertEquals("e5", testedValue5.value);
		assertFalse(df.vStack.get(0).hasMoreEdges());
	}
	
	@Test
	public void moveToNextVertexShouldNotBreakOnEmptyGraph() {
		DepthFirstIterator<V, E>df = new DFImpl(new LinkedList<>());
		
		assertNull(df.currentVertex);
		df.moveToNextVertex();
		assertNull(df.currentVertex);
	}
	
	@Test
	public void firstCallToMoveToNextVertexShouldChangeCurrentVertex() {
		V root = new V();
		E e1 = createEdge("e1");
		root.getOutgoingEdges().add(e1);
		
		DepthFirstIterator<V, E>df = new DFImpl(Arrays.asList(root));
		
		assertNull(df.currentVertex);
		df.moveToNextVertex();
		assertNotNull(df.currentVertex);
		assertEquals(root, df.currentVertex);
	}

	@Test
	public void moveToNextVertexOnEmptyEdgeListShouldNotBreak() {
		V root = new V();
		
		DepthFirstIterator<V, E>df = new DFImpl(Arrays.asList(root));
		
		df.moveToNextVertex();
		assertNotNull(df.currentVertex);
		assertEquals(root, df.currentVertex);
	}
	
	@Test
	public void moveToNextVertexShouldReturnRootNodesInOrder() {
		V root1 = createVertex("root1");
		V root2 = createVertex("root2");
		V root3 = createVertex("root3");
		
		DepthFirstIterator<V, E>df = new DFImpl(Arrays.asList(root1, root2, root3));
		
		df.moveToNextVertex();
		assertNotNull(df.currentVertex);
		assertEquals(root1, df.currentVertex);
		
		df.moveToNextVertex();
		assertNotNull(df.currentVertex);
		assertEquals(root2, df.currentVertex);
		
		df.moveToNextVertex();
		assertNotNull(df.currentVertex);
		assertEquals(root3, df.currentVertex);
	}
	
	private V createVertex(String value) {
		V vertex = new V();
		vertex.value = value;
		return vertex;
	}
	
	private E createEdge(String value) {
		E e1 = new E();
		e1.value = value;
		return e1;
	}
}
