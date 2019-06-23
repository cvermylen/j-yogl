package net.sf.yogl.adjacent.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

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
	public void shouldRecordTheLink() {
		AdjListVertex<String, String>v = new AdjListVertex<>("string");
		
		AdjListEdge<String, String>aLink = v.addLinkLast("aLink");
		AdjListEdge<String, String>atTheEnd = v.addLinkLast("atTheEnd");
		AdjListEdge<String, String>theFirstOne = v.addLinkFirst("theFirstOne");
		
		assertNotNull(aLink);
		assertNotNull(atTheEnd);
		assertNotNull(theFirstOne);
		assertEquals("aLink", v.getOutgoingEdges().get(1).getUserValue());
		assertEquals("atTheEnd", v.getOutgoingEdges().get(2).getUserValue());
		assertEquals("theFirstOne", v.getOutgoingEdges().get(0).getUserValue());
	}
	
	@Test
	public void shouldRemoveTheEdgeByObjectAddress() {
		AdjListVertex<String, String>v = new AdjListVertex<>("string");
		
		AdjListEdge<String, String>aLink = v.addLinkLast("aLink");
		AdjListEdge<String, String>atTheEnd = v.addLinkLast("atTheEnd");
		AdjListEdge<String, String>theFirstOne = v.addLinkFirst("theFirstOne");
		
		v.removeEdge(aLink);
		
		assertEquals(2, v.getOutgoingEdges().size());
		assertEquals(atTheEnd, v.getOutgoingEdges().get(1));
		assertEquals(theFirstOne, v.getOutgoingEdges().get(0));
	}
	
	@Test
	public void shouldRemoveTheEdgeAtAPosition() {
		AdjListVertex<String, String>v = new AdjListVertex<>("string");
		
		v.addLinkLast("aLink");
		AdjListEdge<String, String>atTheEnd = v.addLinkLast("atTheEnd");
		AdjListEdge<String, String>theFirstOne = v.addLinkFirst("theFirstOne");
		
		v.removeEdgeAt(1);
		
		assertEquals(2, v.getOutgoingEdges().size());
		assertEquals(atTheEnd, v.getOutgoingEdges().get(1));
		assertEquals(theFirstOne, v.getOutgoingEdges().get(0));
	}
}
