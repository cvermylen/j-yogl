package net.sf.yogl.adjacent.key;

import net.sf.yogl.exceptions.NodeNotFoundException;

public class KeyEdge <EK extends Comparable<EK>, VK extends Comparable<VK>, TS extends KeyEdge<EK, VK, TS, DS>, DS extends KeyVertex<VK, EK, DS, TS>>
extends InternalKeyEdge <TS, DS, EK, VK>{

	public KeyEdge(EK key, DS toVertex) throws NodeNotFoundException {
		super(key, toVertex);
		// TODO Auto-generated constructor stub
	}

}
