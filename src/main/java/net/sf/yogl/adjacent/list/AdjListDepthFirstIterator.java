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

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.yogl.DepthFirstIterator;
import net.sf.yogl.exceptions.GraphException;

/** Given a specific graph, this iterator returns the next vertex in a
 * depth first order. Iterators cannot be concurrent on the same graph.
 * This limitation is due to the local variables in vertices and edges
 * being modified during the traversal.
 */

public final class AdjListDepthFirstIterator<VV, EV> extends DepthFirstIterator<AdjListVertex<VV, EV>, AdjListEdge<VV, EV>> implements Iterator<VV>{
    
    /**
     *  @return the next user object in first order
     * @exception NoSuchElementException if the iterator is empty
     */
    public VV next()
    throws NoSuchElementException{
        if(currentVertex == null) throw new NoSuchElementException();
        moveToNextVertex();
        VV result = currentVertex.getUserValue();
        return result;
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
}