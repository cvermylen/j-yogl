package net.sf.yogl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

public class DepthFirstIteratorTest {

	class V extends Vertex<V, E>{
		List<E>el = new LinkedList<>();
		String value;
		
		@Override
		public List<E> getOutgoingEdges() {
			return el;
		}

		@Override
		public void tryAddEdge(E edge) {
			// TODO Auto-generated method stub
			
		}
	};
	
	class E extends Edge<E, V> {
		V v;
		String value;
		
		@Override
		public V getToVertex() {
			return v;
		}
	}
	
	class DFImpl extends DepthFirstIterator<V, E>{

		public DFImpl(V superRoot, Collection<V> roots) {
			pushVertex(superRoot);
		}
		
		public DFImpl() {}
		
		public V next(){return null;}
	}
	
	@Test
	public void hasMoreNodesToVisitShouldBeFalseOnEmptyStack() throws Exception {
		DepthFirstIterator<V, E>df = new DFImpl();
		boolean r = Whitebox.invokeMethod(df, "hasMoreNodesToVisit");
		assertFalse(r);
	}
	
	@Test
	public void hasMoreNodesToVisitShouldBeFalseWithOneEmptyNode() throws Exception {
		V o = new V();
		DepthFirstIterator<V, E>df = new DFImpl(o, null);
		assertEquals(1, df.vStack.size());
		boolean r = Whitebox.invokeMethod(df, "hasMoreNodesToVisit");
		assertFalse(r);
	}
	
	@Test
	public void pushVertexShouldAddTheVertexAndAllEdges() {
		V o = new V();
		E e = createEdge("e");
		o.getOutgoingEdges().add(e);
		
		DepthFirstIterator<V, E>df = new DFImpl();
		df.pushVertex(o);
		
		assertEquals(1, df.vStack.size());
		assertTrue(df.vStack.get(0).hasMoreEdges());
	}
	
	@Test
	public void internalLinearEdgeShouldReturnOneEdge() {
		V o = new V();
		E e = createEdge("e");
		o.getOutgoingEdges().add(e);
		
		DepthFirstIterator<V, E>df = new DFImpl(o, Arrays.asList(o));
		
		assertEquals(1, df.vStack.size());
		assertTrue(df.vStack.get(0).hasMoreEdges());
		E testedValue = df.vStack.get(0).nextEdge();
		assertFalse(df.vStack.get(0).hasMoreEdges());
		assertNull(testedValue.v);
		assertEquals("e", testedValue.value);
	}

	@Test
	public void callingNextOnEmptyEdgeStackShouldThrowException() {
		V o = new V();
		
		DepthFirstIterator<V, E>df = new DFImpl(o, Arrays.asList(o));
		
		assertEquals(1, df.vStack.size());
		assertFalse(df.vStack.get(0).hasMoreEdges());
		assertThrows(NumberFormatException.class, () -> {
			df.vStack.get(0).nextEdge();
		  });
		
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
		
		DepthFirstIterator<V, E>df = new DFImpl(root, null);
		
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
		DepthFirstIterator<V, E>df = new DFImpl();
		
		assertNull(df.currentVertex);
		df.moveToNextVertex();
		assertNull(df.currentVertex);
	}
	
	@Test
	public void firstCallToMoveToNextVertexShouldChangeCurrentVertex() {
		V root = new V();
		E e1 = createEdge("e1");
		root.getOutgoingEdges().add(e1);
		
		DepthFirstIterator<V, E>df = new DFImpl(root, new LinkedList());
		
		assertNull(df.currentVertex);
		df.moveToNextVertex();
		assertNotNull(df.currentVertex);
		assertEquals(root, df.currentVertex);
	}

	@Test
	public void moveToNextVertexOnEmptyEdgeListShouldNotBreak() {
		V root = new V();
		
		DepthFirstIterator<V, E>df = new DFImpl(root, null);
		
		df.moveToNextVertex();
		assertNotNull(df.currentVertex);
		assertEquals(root, df.currentVertex);
	}
	
	@Test
	public void hasNextShouldReturnFalseOnEmptyGraph() {
		DepthFirstIterator<V, E>df = new DFImpl();
		
		boolean b = df.hasNext();
		
		assertFalse(b);
	}
	
	@Test
	public void hasNextShouldBeTrueWithOneEmptyNode() {
		V root = new V();
		DepthFirstIterator<V, E>df = new DFImpl(root, null);
		
		boolean b = df.hasNext();
		
		assertTrue(b);
	}
	
	private E createEdge(String value) {
		E e1 = new E();
		e1.value = value;
		return e1;
	}
}
