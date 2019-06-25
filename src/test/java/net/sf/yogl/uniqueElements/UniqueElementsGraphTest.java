package net.sf.yogl.uniqueElements;

import org.junit.jupiter.api.Test;

import net.sf.yogl.adjacent.key.KeyEdge;
import net.sf.yogl.adjacent.key.KeyVertex;
import net.sf.yogl.exceptions.NodeNotFoundException;

public class UniqueElementsGraphTest {

	class StringUniqueVertex extends KeyVertex<String, String, StringUniqueVertex, StringUniqueEdge>{

		public StringUniqueVertex(String key) {
			super(key);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	class StringUniqueEdge extends KeyEdge<String, String, StringUniqueEdge, StringUniqueVertex>{

		public StringUniqueEdge(String key, StringUniqueVertex v) throws NodeNotFoundException {
			super(key, v);
			// TODO Auto-generated constructor stub
		}
		
	}
	@Test
	public void shouldCreateVertexAndEdge() {
		StringUniqueVertex v = new StringUniqueVertex("1");
		
	}
}
