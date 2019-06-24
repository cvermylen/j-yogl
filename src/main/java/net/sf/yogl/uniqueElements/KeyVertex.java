package net.sf.yogl.uniqueElements;


public class KeyVertex <VK extends Comparable<VK>, EK extends Comparable<EK>, DS extends KeyVertex<VK, EK, DS, TS>, TS extends KeyEdge<EK, VK, TS, DS>>
extends UniqueVertex <DS, TS, VK, EK>{

	public KeyVertex(VK key) {
		super(key);
		// TODO Auto-generated constructor stub
	}

}
