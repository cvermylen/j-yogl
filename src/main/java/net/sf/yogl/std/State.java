package net.sf.yogl.std;

import net.sf.yogl.uniqueElements.*;

public class State <VK extends Comparable<VK>, EK extends Comparable<EK>> extends UniqueVertex <StateIntf<VK, EK>, TransitionIntf<EK, VK>, VK, EK> {

	public State(VK key) {
		super(key);
	}

}
