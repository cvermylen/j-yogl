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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.sf.yogl.Vertex;
import net.sf.yogl.exceptions.GraphCorruptedException;
/**
 * The Vertex describes the node of the graph. It is a container for a
 * user defined object.
 *  - the 'value' field refers to this object
 *  - it also refers to a list with all outgoing edges.
 * Some additional status information is also contained:
 *  - an integer indicating the number of times this node has been visited
 *    by an algorithm
 *  - a boolean indicating if the vertex is free or not
 *  V = user value Object
 */

public class AdjListVertex <VV, EV> extends Vertex {
        
    private ArrayList<AdjListEdge<VV, EV>> outgoingEdges = new ArrayList<>();
    
    VV userValue = null;
    
    public AdjListVertex(VV userValue){
    	super();
    	if(userValue == null) throw new IllegalArgumentException("Null paramter not allowed");
        this.userValue = userValue;
    }
    
    public AdjListVertex<VV, EV> clone(){
        
        AdjListVertex<VV, EV> cloned = new AdjListVertex<VV, EV>(userValue);
        cloned.setFreeEntry(isFreeEntry());
        cloned.setVisitCounts(getVisitsCount());
        cloned.outgoingEdges = outgoingEdges;
        return cloned;
    }
    
    public VV getUserValue(){
        return userValue;
    }
    
    public List<AdjListEdge<VV, EV>> getOutgoingEdges(){
        
    	return this.outgoingEdges;
    }
    
    public AdjListEdge<VV, EV> addLinkLast(EV value) {
    	return this.addEdgeLast(new AdjListEdge<>(value));
    }
    
    public AdjListEdge<VV, EV> addEdgeLast(AdjListEdge<VV, EV> newEdge)throws GraphCorruptedException{
        if (newEdge != null){
        	outgoingEdges.add(newEdge);
        }
        return newEdge;
    }
    
    public AdjListEdge<VV, EV> addLinkFirst(EV value){
    	return this.addEdgeFirst(new AdjListEdge<>(value));
    }
    
    public AdjListEdge<VV, EV> addEdgeFirst(AdjListEdge<VV, EV> newEdge)throws GraphCorruptedException{
        if (newEdge != null){
        	outgoingEdges.add(0, newEdge);
        }
        return newEdge;
    }

    public void removeEdge(AdjListEdge<VV, EV> edge){
        if (edge != null){
            outgoingEdges.remove(edge);
        }
    }
    
    public void removeEdgeAt(int pos) {
    	outgoingEdges.remove(pos);
    }
    
    public void removeIf(Predicate<AdjListEdge<VV, EV>> p){
    	outgoingEdges.removeIf(p);
    }
    
    public void clearEdges(){
        outgoingEdges.clear();
    }
    
    public AdjListVertex<VV, EV> deepCopy() {
    	AdjListVertex<VV, EV>result = this.clone();
    	
    	return result;
    }
}
