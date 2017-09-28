package net.sf.yogl.adjacent.list;

import static org.junit.Assert.*;

import org.junit.Test;

public class LinearEdgesIteratorTest {

	private LinearEdgesIterator fixture;
	
	@Test
	public void initShouldRegisterOutgoingEdgesForASingleVertex() {
		AdjListVertex<Object, Object> o = new AdjListVertex<>("a");
		o.addLinkLast("l");
		
		fixture = new LinearEdgesIterator(o);
		
		assertTrue(fixture.hasModeEdges());
		fixture.nextEdge();
		assertFalse(fixture.hasModeEdges());
	}

}
