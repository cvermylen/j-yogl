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
   
package net.sf.yogl.types;

import java.util.Collection;

import net.sf.yogl.GraphAdapter;
import net.sf.yogl.exceptions.GraphException;

/** Simple graph which does not contains any data. Nodes and links
 *  are just identified by keys.
 */
public final class NoDataGraph<VK extends Comparable<VK>, EK extends Comparable<EK>> extends GraphAdapter<VK, Object, EK, Object> {

	public void addNode(VK nodeKey) throws GraphException{
		super.addNode(nodeKey, "");
	}
	
	public void addNode(VK nodeKey, Object nodeValue)throws GraphException{
		super.addNode(nodeKey, "");
	}
	public Collection nodesValues(){
		return null;
	}
	
	public void tryAddNode(VK nodeKey) throws GraphException{
		super.tryAddNode(nodeKey, null);
	}
}
