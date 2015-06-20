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

import java.util.Stack;

import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.types.VertexType;

/** Basic iterator to implement state transition diagrams. The iterator is
 *  positioned on the 'current' vertex and perform a 'transition' to the
 *  next vertex. The transition is piloted by the 'edge value'.
 *  Concurrency: this iterator does not modify the graph data. Many iterators
 *  of this type can run concurrently. Items can be inserted into the graph while
 *  the iterator is being used, but, ideally, no item should be removed.
 */
class EdgeIterator{
    
    /** Current graph the iterator is pointing to
     */
    private ImplementationGraph graph = null;
    
    /** 'top' contains the 'current' vertex
     */
    private Stack path = null;
    
    EdgeIterator(ImplementationGraph graph) throws NodeNotFoundException, GraphCorruptedException{
        
        path = new Stack();
        if(graph != null){
            //this.start = start;
            Object[]startNodes = graph.getNodesKeys(VertexType.START);
            if(startNodes.length > 1){
                path.push(graph.findVertexByKey(startNodes[0]));
            }
        }
    }
    
    /** Ctor: we need the starting position.
     *  When no starting vertex is given, traversal will start with
     *  the first entry in 'vertices'
     */
    public EdgeIterator(ImplementationGraph graph, Object startingNodeKey)
    throws GraphException{
        
        path = new Stack();
        if( (graph != null) && (startingNodeKey != null) ){
            this.graph = graph;
            if(graph.existsNode(startingNodeKey)){
            
                path.push(graph.findVertexByKey(startingNodeKey));
            }
        }
    }
    
    /** 'sVertex' will be considered as the new starting vertex for the
     *  next traversal.
     */
    public void reset(Object startingNodeKey)throws GraphException{
        
        if(graph.existsNode(startingNodeKey)){
            path.clear();
            path.push(graph.findVertexByKey(startingNodeKey));
        }else{
            throw new GraphCorruptedException("node does not exists");
        }
    }
    
    /** perform a transition from the 'current' vertex to the 'next'
     *  vertex by following the connection labelled by 'edge'.
     */
    public void transition(Object edge)throws GraphException{
        
        Vertex peek = (Vertex)path.peek();
        Object nextKey = peek.getNextVertexKey(edge);
		if(nextKey == null)
			throw new GraphCorruptedException("Edge does not exists");
        path.push(graph.findVertexByKey(nextKey));
    }
    
    /** Returns a (possibly empty) list with all possible transitions
     */
    public Object[] getTransition()throws GraphException{
        
        return graph.getOutgoingLinksKeys(path.peek());
    }
    
    /** Returns the value of the 'current' vertex
     */
    public Object key(){
        
        Object result = null;
        if(path.size() > 1)
            result = path.peek();
        return result;
    }
    
    /** goes back to the previous 'current' vertex. Returns false when
     *  the iterator is back to the starting vertex.
     */
    public boolean backtrack(){
        
        boolean result = false;
        if(path.size() > 0){
            path.pop();
            result = true;
        }
        return result;
    }
    
    /** return non-null if the requested transition is possible.
     */
    public Object testTransition(Object edgeValue)throws GraphException{
        
        Object result = null;
        Vertex peek = (Vertex)path.peek();
        Edge candidate = peek.getEdge(edgeValue);
        if(candidate != null){
        	result = candidate.getUserValue();
        }
        return result;
    }
}