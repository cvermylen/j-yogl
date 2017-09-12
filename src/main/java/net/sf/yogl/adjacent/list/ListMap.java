/* Copyright (C) 2003 Symphonix
   
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.
   
   You should have received a copy of the GNU Library General Public
   License along with this library; if not, write to the Free
   Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
   MA 02111-1307, USA */

package net.sf.yogl.adjacent.list;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import net.sf.yogl.adjacent.list.AdjListEdge;

public class ListMap<VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> extends AbstractMap<EK, AdjListEdge<VK, EK, EV>> {

	private LinkedList<EK> list = new LinkedList<EK>();
	private HashMap<EK, AdjListEdge<VK, EK, EV>> map = new HashMap<>();
	  
	/* (non-Javadoc)
	 * @see java.util.AbstractMap#entrySet()
	 */
	public Set<Map.Entry<EK, AdjListEdge<VK, EK, EV>>> entrySet() {
		return map.entrySet();
	}

	public void addFirst(EK key, AdjListEdge<VK, EK, EV> value){
		map.put(key, value);
		list.addFirst(key);
	}
	
	public void addLast(EK key, AdjListEdge<VK, EK, EV> value){
		map.put(key, value);
		list.addLast(key);
	}
	
	public void clear(){
		list.clear();
		map.clear();
	}
	
	public AdjListEdge<VK, EK, EV> get(EK key){
		return map.get(key);
	}
	
	public EK getFirst(){
		return list.getFirst();
	}
	
	public EK getLast(){
		return list.getLast();
	}
	
	public AdjListEdge<VK, EK, EV> put(EK key, AdjListEdge<VK, EK, EV> value){
		addLast(key, value);
		return value;
	}
	
	public AdjListEdge<VK, EK, EV> remove(EK key){
		AdjListEdge<VK, EK, EV> result = map.get(key);
		list.remove(key);
		map.remove(key);
		return result;
	}
	
	public int size(){
		return list.size();
	}
	
	public Collection<AdjListEdge<VK, EK, EV>> values(){
		return map.values();
	}
	
	public Object clone(){
		ListMap<VK, VV, EK, EV> result = new ListMap<VK, VV, EK, EV>();
		result.list = (LinkedList<EK>)this.list.clone();
		result.map = (HashMap)this.map.clone();
		return result;
	}
}
