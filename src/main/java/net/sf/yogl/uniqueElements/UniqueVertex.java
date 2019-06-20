package net.sf.yogl.uniqueElements;

import java.util.Collection;
import java.util.HashMap;
import net.sf.yogl.Vertex;

public class UniqueVertex<VK extends Comparable<VK>, EK extends Comparable<EK>> extends Vertex<UniqueEdge<EK, VK>>{

	private VK key;
	
	private HashMap<EK, UniqueEdge<EK, VK>> edges= new HashMap<>();

	public UniqueVertex(VK key) {
		this.key = key;
	}
	
	@Override
	public Collection<UniqueEdge<EK, VK>> getOutgoingEdges() {
		return edges.values();
	}

	@Override
	public void tryAddEdgeFirst(UniqueEdge<EK, VK> edge) {
		if (!edges.containsKey(edge.getKey())) {
			edges.put(edge.getKey(), edge);
		}
	}

	@Override
	public void tryAddEdgeLast(UniqueEdge<EK, VK> edge) {
		if (!edges.containsKey(edge.getKey())) {
			edges.put(edge.getKey(), edge);
		}
	}
	
	public VK getKey() {
		return key;
	}
}