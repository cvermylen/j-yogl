package net.sf.yogl.std;

import net.sf.yogl.uniqueElements.*;

public abstract class Transition <EK extends Comparable<EK>, VK extends Comparable<VK>, TS extends TransitionIntf<EK, VK, TS, DS, PAR>, DS extends StateIntf<VK, EK, DS, TS, PAR>, PAR>
		extends UniqueEdge <TS, DS, EK, VK> implements TransitionIntf<EK, VK, TS, DS, PAR> {

	public Transition(EK key) {
		super(key);
	}

}
