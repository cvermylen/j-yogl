package net.sf.yogl.uniqueElements;

import org.junit.jupiter.api.Test;

public class UniqueElementsGraphTest {

	class StringUniqueVertex extends KeyVertex<String, String, StringUniqueVertex, StringUniqueEdge>{

		public StringUniqueVertex(String key) {
			super(key);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	class StringUniqueEdge extends KeyEdge<String, String, StringUniqueEdge, StringUniqueVertex>{

		public StringUniqueEdge(String key) {
			super(key);
			// TODO Auto-generated constructor stub
		}
		
	}
	@Test
	public void shouldCreateVertexAndEdge() {
		StringUniqueVertex v = new StringUniqueVertex("1");
		
	}
}
