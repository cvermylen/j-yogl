package net.sf.yogl.std;

import net.sf.yogl.uniqueElements.*;

public abstract class State <VK extends Comparable<VK>, EK extends Comparable<EK>, DS extends StateIntf<VK, EK, DS, TS, PAR>, TS extends TransitionIntf<EK, VK, TS, DS, PAR>, PAR>
			extends UniqueVertex <DS, TS, VK, EK> 
			implements StateIntf<VK, EK, DS, TS, PAR>, UniqueVertexIntf <DS, TS, VK, EK>{

	public State(VK key) {
		super(key);
	}

	
}
