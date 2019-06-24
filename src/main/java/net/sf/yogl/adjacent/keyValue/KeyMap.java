
package net.sf.yogl.adjacent.keyValue;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KeyMap<VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> extends AbstractMap<EK, KeyValueEdge<VK, VV, EK, EV>> {

	private LinkedList<EK> list = new LinkedList<EK>();
	private HashMap<EK, KeyValueEdge<VK, VV, EK, EV>> map = new HashMap<>();
	  
	/* (non-Javadoc)
	 * @see java.util.AbstractMap#entrySet()
	 */
	public Set<Map.Entry<EK, KeyValueEdge<VK, VV, EK, EV>>> entrySet() {
		return map.entrySet();
	}

	public void addFirst(EK key, KeyValueEdge<VK, VV, EK, EV> value){
		map.put(key, value);
		list.addFirst(key);
	}
	
	public void addLast(EK key, KeyValueEdge<VK, VV, EK, EV> value){
		map.put(key, value);
		list.addLast(key);
	}
	
	public void clear(){
		list.clear();
		map.clear();
	}
	
	public KeyValueEdge<VK, VV, EK, EV> get(EK key){
		return map.get(key);
	}
	
	public EK getFirst(){
		return list.getFirst();
	}
	
	public EK getLast(){
		return list.getLast();
	}
	
	public KeyValueEdge<VK, VV, EK, EV> put(EK key, KeyValueEdge<VK, VV, EK, EV> value){
		addLast(key, value);
		return value;
	}
	
	public KeyValueEdge<VK, VV, EK, EV> remove(EK key){
		KeyValueEdge<VK, VV, EK, EV> result = map.get(key);
		list.remove(key);
		map.remove(key);
		return result;
	}
	
	public int size(){
		return list.size();
	}
	
	public Collection<KeyValueEdge<VK, VV, EK, EV>> values(){
		return map.values();
	}
	
	public Object clone(){
		KeyMap<VK, VV, EK, EV> result = new KeyMap<VK, VV, EK, EV>();
		result.list = this.list.stream().collect(Collectors.toCollection(LinkedList<EK>::new));
		result.map = this.map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, HashMap::new));
		return result;
	}
}
