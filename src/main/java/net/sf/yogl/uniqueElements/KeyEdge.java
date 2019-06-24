package net.sf.yogl.uniqueElements;


public class KeyEdge <EK extends Comparable<EK>, VK extends Comparable<VK>, TS extends KeyEdge<EK, VK, TS, DS>, DS extends KeyVertex<VK, EK, DS, TS>>
extends UniqueEdge <TS, DS, EK, VK>{

	public KeyEdge(EK key) {
		super(key);
		// TODO Auto-generated constructor stub
	}

}
