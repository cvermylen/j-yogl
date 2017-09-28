/* Copyright (C) 2003 Symphonic
   
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

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import net.sf.yogl.DepthFirstIterator;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;

/** Given a specific graph, this iterator returns the next vertex in a
 * depth first order. Iterators cannot be concurrent on the same graph.
 * This limitation is due to the local variables in vertices and edges
 * being modified during the traversal.
 */

public final class AdjListDepthFirstIterator<VV, EV> extends DepthFirstIterator<VV, AdjListVertex<VV, EV>, EV, AdjListEdge<VV, EV>>{
    
    /** Last edge used to access current vertex
     */
    private AdjListEdge<VV, EV> lastUsedEdge =  null;
    
    /** The 'pathStack' contains the path followed from the starting node
     * to the current node, this one excluded.
     * Objects on this stack are of type 'Path'
     */
    private LinkedList<LinearEdgesIterator> pathStack = new LinkedList<>();
    
    /** 'leaf' indicates if the pointed vertex is a leaf or not.
     */
    private boolean leaf;
    
    /**
     *  @return the next user object in first order
     * @exception NoSuchElementException if the iterator is empty
     */
    public VV next()
    throws NoSuchElementException{
        if(currentVertex == null) throw new NoSuchElementException();
        VV result = currentVertex.getUserValue();
        getNext();
        return result;
    }
    
    /** This method determines if the current vertex can be or not traversed.
     * The condition is given by 2 parameters and 1 computed value.
     * maxTraversals indicates the maximum number of times the same
     * vertex can be visited by the algorithm. This max value
     * is tested each time a vertex is visited.
     * dontMindTraversal is set by the user to skip the above test.
     */
    private boolean canTraverse()
    throws GraphException{
        
        if(currentVertex == null){
            throw new GraphException("canTraverse: vertex is null");
        }
		return (currentVertex.getVisitsCount() <= maxCycling);
    }
    
    /** The ctor builds the iterator on the graph given as parameter.
     * This iterator works directly on the graph elements, not on a copy
     * of it.
     * @param graph is a valid, possibly decorated, concrete graph.
     */
    AdjListDepthFirstIterator(AdjListGraph<VV,EV> graph)
    throws GraphException{
        
    	super(graph.getRoots());
    }
    
    /** This method is part of the Iterator interface. It is not
     * implemented here since we consider that many iterators
     * may run concurrently on the same graph
     */
    public void remove()
    throws UnsupportedOperationException, IllegalStateException{
        
        throw new UnsupportedOperationException("");
    }
    
    @Override
    /** @return the edge used to access the current node
     */
    public EV usedLink(){
        return lastUsedEdge.getUserValue();
        
    }
}