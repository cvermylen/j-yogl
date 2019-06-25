package net.sf.yogl.adjacent.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import net.sf.yogl.exceptions.NodeNotFoundException;

public class AdjListVertexTest {

	@Test
	public void shouldNotCreateVertexWithEmptyValue() {
		assertThrows(IllegalArgumentException.class, () ->
		{new AdjListVertex<Object, Object>(null);}
		);
	}

	@Test
	public void shouldInitializeToDefaultValues() {
		AdjListVertex<String, String>v = new AdjListVertex<>("string");
		
		assertEquals(0, v.getVisitsCount());
		assertEquals(0, v.getOutgoingEdges().size());
		assertEquals("string", v.getUserValue());
		assertFalse(v.isFreeEntry());
	}
	
	@Test
	public void shouldRecordTheLink() throws NodeNotFoundException {
		AdjListVertex<String, String>v = new AdjListVertex<>("string");
		AdjListVertex<String, String> target = new AdjListVertex<>("target");
		AdjListEdge<String, String>aLink = new AdjListEdge<>("aLink", target);
		v.addLinkLast(aLink);
		AdjListEdge<String, String>atTheEnd = new AdjListEdge<>("atTheEnd", target);
		v.addLinkLast(atTheEnd);
		AdjListEdge<String, String>theFirstOne = new AdjListEdge<>("theFirstOne", target);
		v.addLinkFirst(theFirstOne);
		
		assertNotNull(aLink);
		assertNotNull(atTheEnd);
		assertNotNull(theFirstOne);
		assertEquals("aLink", v.getOutgoingEdges().get(1).getUserValue());
		assertEquals("atTheEnd", v.getOutgoingEdges().get(2).getUserValue());
		assertEquals("theFirstOne", v.getOutgoingEdges().get(0).getUserValue());
	}
	
	@Test
	public void shouldRemoveTheEdgeByObjectAddress() throws NodeNotFoundException {
		AdjListVertex<String, String>v = new AdjListVertex<>("string");
		AdjListVertex<String, String> target = new AdjListVertex<>("target");
		AdjListEdge<String, String>aLink = new AdjListEdge<>("aLink", target);
		v.addLinkLast(aLink);
		AdjListEdge<String, String>atTheEnd = new AdjListEdge<>("atTheEnd", target);
		v.addLinkLast(atTheEnd);
		AdjListEdge<String, String>theFirstOne = new AdjListEdge<>("theFirstOne", target);
		v.addLinkFirst(theFirstOne);
		
		v.removeEdge(aLink);
		
		assertEquals(2, v.getOutgoingEdges().size());
		assertEquals(atTheEnd, v.getOutgoingEdges().get(1));
		assertEquals(theFirstOne, v.getOutgoingEdges().get(0));
	}
	
	@Test
	public void shouldRemoveTheEdgeAtAPosition() throws NodeNotFoundException {
		AdjListVertex<String, String>v = new AdjListVertex<>("string");
		AdjListVertex<String, String> target = new AdjListVertex<>("target");
		AdjListEdge<String, String>aLink = new AdjListEdge<>("aLink", target);
		v.addLinkLast(aLink);
		AdjListEdge<String, String>atTheEnd = new AdjListEdge<>("atTheEnd", target);
		v.addLinkLast(atTheEnd);
		AdjListEdge<String, String>theFirstOne = new AdjListEdge<>("theFirstOne", target);
		v.addLinkFirst(theFirstOne);
		
		v.removeEdgeAt(1);
		
		assertEquals(2, v.getOutgoingEdges().size());
		assertEquals(atTheEnd, v.getOutgoingEdges().get(1));
		assertEquals(theFirstOne, v.getOutgoingEdges().get(0));
	}
}
