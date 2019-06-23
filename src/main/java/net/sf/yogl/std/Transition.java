package net.sf.yogl.std;

import net.sf.yogl.uniqueElements.*;

public class Transition <EK extends Comparable<EK>, VK extends Comparable<VK>> extends UniqueEdge <TransitionIntf<EK, VK>, StateIntf<VK, EK>, EK, VK>  {

	public Transition(EK key) {
		super(key);
	}

}
