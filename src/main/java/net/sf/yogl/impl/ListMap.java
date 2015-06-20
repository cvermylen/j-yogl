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

package net.sf.yogl.impl;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class ListMap extends AbstractMap {

	private LinkedList list = new LinkedList();
	private HashMap map = new HashMap();
	  
	/* (non-Javadoc)
	 * @see java.util.AbstractMap#entrySet()
	 */
	public Set entrySet() {
		return map.entrySet();
	}

	public void addFirst(Object key, Object value){
		map.put(key, value);
		list.addFirst(key);
	}
	
	public void addLast(Object key, Object value){
		map.put(key, value);
		list.addLast(key);
	}
	
	public void clear(){
		list.clear();
		map.clear();
	}
	
	public Object get(Object key){
		return map.get(key);
	}
	
	public Object getFirst(){
		return list.getFirst();
	}
	
	public Object getLast(){
		return list.getLast();
	}
	
	public Object put(Object key, Object value){
		addLast(key, value);
		return value;
	}
	
	public Object remove(Object key){
		Object result = map.get(key);
		list.remove(key);
		map.remove(key);
		return result;
	}
	
	public int size(){
		return list.size();
	}
	
	public Collection values(){
		return map.values();
	}
	
	public Object clone(){
		ListMap result = new ListMap();
		result.list = (LinkedList)this.list.clone();
		result.map = (HashMap)this.map.clone();
		return result;
	}
}
