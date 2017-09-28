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

import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;

import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;

/** Basic iterator to implement state transition diagrams. The iterator is
 *  positioned on the 'current' vertex and perform a 'transition' to the
 *  next vertex. The transition is piloted by the 'edge value'.
 *  Concurrency: this iterator does not modify the graph data. Many iterators
 *  of this type can run concurrently. Items can be inserted into the graph while
 *  the iterator is being used, but, ideally, no item should be removed.
 */
class EdgeIterator<VV, EV>{
    
    /** Current graph the iterator is pointing to
     */
    private AdjListGraph<VV, EV> graph = null;
    
    /** 'top' contains the 'current' vertex
     */
    private Stack<AdjListVertex<VV, EV>> nodesStack = null;
    private LinkedList<AdjListEdge<VV, EV>> edgesStack = new LinkedList<>();
    private LinkedList<AdjListEdge<VV, EV>> backStack = new LinkedList<>();
    
    EdgeIterator(AdjListGraph<VV,EV> graph) throws NodeNotFoundException, GraphCorruptedException{
        reset();
        
    }
    
    /** 'sVertex' will be considered as the new starting vertex for the
     *  next traversal.
     */
    public void reset()throws GraphException{
        
    	nodesStack = new Stack<>();
        if(graph != null){
            Collection<AdjListVertex<VV, EV>>startNodes = graph.getRoots();
            if(startNodes.size() > 0){
                nodesStack.push(startNodes.iterator().next());
                stackEdges();
            }
        }
    }
    
    private void stackEdges(){
    	AdjListVertex<VV, EV> current = nodesStack.pop();
    	edgesStack.addAll(current.getOutgoingEdges());
    }
    
    /** perform a transition from the 'current' vertex to the 'next'
     *  vertex by following the connection labelled by 'edge'.
     */
    public AdjListEdge<VV, EV> transition()throws GraphException{
        
    	AdjListEdge<VV, EV> result = null;
    	if(!(edgesStack.isEmpty() & nodesStack.isEmpty())){
    		if(edgesStack.isEmpty()){
    			this.stackEdges();
    		}
    		result = edgesStack.poll();
    		backStack.push(result);
    	}
    	return result;
    }
    
    /** goes back to the previous 'current' vertex. Returns false when
     *  the iterator is back to the starting vertex.
     */
    public AdjListEdge<VV, EV> backtrack(){
        
    	AdjListEdge<VV, EV> result = null;
        if(backStack.size() > 0){
        	result = backStack.poll();
        }
        return result;
    }
    
}