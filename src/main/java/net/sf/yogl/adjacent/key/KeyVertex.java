package net.sf.yogl.adjacent.key;


public class KeyVertex <VK extends Comparable<VK>, EK extends Comparable<EK>, DS extends KeyVertex<VK, EK, DS, TS>, TS extends KeyEdge<EK, VK, TS, DS>>
extends InternalKeyVertex <DS, TS, VK, EK>{

	public KeyVertex(VK key) {
		super(key);
		// TODO Auto-generated constructor stub
	}

}
