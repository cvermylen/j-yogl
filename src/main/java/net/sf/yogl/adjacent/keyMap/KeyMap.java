
package net.sf.yogl.adjacent.keyMap;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class KeyMap<VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> extends AbstractMap<EK, AdjKeyEdge<VK, VV, EK, EV>> {

	private LinkedList<EK> list = new LinkedList<EK>();
	private HashMap<EK, AdjKeyEdge<VK, VV, EK, EV>> map = new HashMap<>();
	  
	/* (non-Javadoc)
	 * @see java.util.AbstractMap#entrySet()
	 */
	public Set<Map.Entry<EK, AdjKeyEdge<VK, VV, EK, EV>>> entrySet() {
		return map.entrySet();
	}

	public void addFirst(EK key, AdjKeyEdge<VK, VV, EK, EV> value){
		map.put(key, value);
		list.addFirst(key);
	}
	
	public void addLast(EK key, AdjKeyEdge<VK, VV, EK, EV> value){
		map.put(key, value);
		list.addLast(key);
	}
	
	public void clear(){
		list.clear();
		map.clear();
	}
	
	public AdjKeyEdge<VK, VV, EK, EV> get(EK key){
		return map.get(key);
	}
	
	public EK getFirst(){
		return list.getFirst();
	}
	
	public EK getLast(){
		return list.getLast();
	}
	
	public AdjKeyEdge<VK, VV, EK, EV> put(EK key, AdjKeyEdge<VK, VV, EK, EV> value){
		addLast(key, value);
		return value;
	}
	
	public AdjKeyEdge<VK, VV, EK, EV> remove(EK key){
		AdjKeyEdge<VK, VV, EK, EV> result = map.get(key);
		list.remove(key);
		map.remove(key);
		return result;
	}
	
	public int size(){
		return list.size();
	}
	
	public Collection<AdjKeyEdge<VK, VV, EK, EV>> values(){
		return map.values();
	}
	
	public Object clone(){
		KeyMap<VK, VV, EK, EV> result = new KeyMap<VK, VV, EK, EV>();
		result.list = (LinkedList<EK>)this.list.clone();
		result.map = (HashMap)this.map.clone();
		return result;
	}
}
