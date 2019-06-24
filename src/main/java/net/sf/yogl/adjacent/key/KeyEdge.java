package net.sf.yogl.adjacent.key;


public class KeyEdge <EK extends Comparable<EK>, VK extends Comparable<VK>, TS extends KeyEdge<EK, VK, TS, DS>, DS extends KeyVertex<VK, EK, DS, TS>>
extends InternalKeyEdge <TS, DS, EK, VK>{

	public KeyEdge(EK key, DS toVertex) {
		super(key, toVertex);
		// TODO Auto-generated constructor stub
	}

}
