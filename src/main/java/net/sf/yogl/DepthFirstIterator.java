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
   
package net.sf.yogl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.impl.Edge;
import net.sf.yogl.impl.ImplementationGraph;
import net.sf.yogl.impl.Path;
import net.sf.yogl.types.VertexType;

/** Given a specific graph, this iterator returns the next vertex in a
 * depth first order. Iterators cannot be concurrent on the same graph.
 * This limitation is due to the local variables in vertices and edges
 * being modified during the traversal.
 */

public final class DepthFirstIterator implements Iterator{
    
    /** Current vertex 'pointed' by the iterator
     */
    protected Object current = null;
    
    /** Last edge used to access current vertex
     */
    private Edge lastUsedEdge =  null;
    
    /** 'graph' refers to the graph currently being traversed.
     */
    private ImplementationGraph graph = null;
    
    /** The vStack contains all nodes in the order they are visited.
     * This stack is used to simulate the recursivity.
     * The Objects contained in the stack will be of type 'Vertex'
     */
    protected Stack vStack = new Stack();
    
    /** The 'pathStack' contains the path followed from the starting node
     * to the current node, this one excluded.
     * Objects on this stack are of type 'Path'
     */
    private Stack pathStack = new Stack();
    
    /** Maximum number of times a node can be visited
     */
    private int maxCycling;
    
    /** 'leaf' indicates if the pointed vertex is a leaf or not.
     */
    private boolean leaf;
    
    /** The init() method iterates on all existing vertices to find
     * out the 'starting' vertices. The traversal variable is also set
     */
    private void init() throws GraphCorruptedException{
        if(graph == null){
            throw new NullPointerException(
            "graph is null in init()");
        }
        Object[]keys = graph.getNodesKeys(VertexType.START | VertexType.STARTEND);
        if((keys == null) || (keys.length == 0)){
			return;
	    }
	    for(int i=0; i < keys.length; i++){
	    	vStack.push(keys[i]);
	    }
    }
    
    /** The 'getNext' iterate to the next element in pre-order and put it
     * in local variable 'vertex'
     * pre-condition: 'graph' and 'vStack' initialized
     * post-condition: result is in 'vertex', vStack', vertex contents
     *	and 'pathStack' are altered.
     */
    private boolean getNext()
    throws GraphException{
        
        if (vStack.isEmpty() == true){
            current = null;
            return false;
        }
        
        //get the first selectable vertex
        do{
            current = vStack.pop();
            //update the pathStack
            while((pathStack.size() > 0) &&
            (pathPeek().getCount() <= 0)){
                
                pathPop();
            }
            
            if (pathStack.size() > 0){
                lastUsedEdge = pathPeek().dec();
            }else{
                lastUsedEdge = null;
            }
        }while((!vStack.isEmpty())&&(!canTraverse()));
        
        if (canTraverse()){
            //the next node to be visited is referenced
            // by 'vertex'
            graph.findVertexByKey(current).incTraversals();
            //put all adjacent neighbors on traversal stack
            if(graph.findVertexByKey(current).getNeighbors().length > 0){
                // returned vertex has some successors
                Edge[]eArray = graph.findVertexByKey(current).getNeighbors();
                // loop from end to begin
                for (int i=eArray.length-1; i>= 0; i--){
                    //vStack.push(graph.getVertex(((Edge)eArray[i]).getVertex()));
                    Object nextKey = eArray[i].getNextVertexKey();
                    vStack.push(nextKey);
                }
                
                Path path = new Path(current, graph.findVertexByKey(current).getNeighbors());
                pathStack.push(path);
                leaf = false;
            }else{
                leaf = true;
            }
            return true;
        }else{
            return false;
        }
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
        
        if(current == null){
            throw new GraphException("canTraverse: vertex is null");
        }
		return (graph.findVertexByKey(current).getTraversals() <= maxCycling);
    }
    
    /** The ctor builds the iterator on the graph given as parameter.
     * This iterator works directly on the graph elements, not on a copy
     * of it.
     * @param graph is a valid, possibly decorated, concrete graph.
     */
    DepthFirstIterator(ImplementationGraph graph)
    throws GraphException{
        
        if(graph == null){
            throw new NullPointerException("graph reference is null");
        }
        this.graph = graph;
        
        init();
    }
    
    /** This ctor accepts a fixed starting node
     *  @param graph is a valid, possibly decorated, concrete graph.
     *  @param startNode user-defined node in this graph
     */
    DepthFirstIterator(ImplementationGraph graph, Object startingNodeKey, int maxCycling)
    throws GraphException{
        
        if(graph == null){
            throw new NullPointerException("graph reference is null");
        }
        this.graph = graph;
        this.maxCycling = maxCycling;
        if(startingNodeKey == null){
            init();
        }else{
            graph.setAllVisitCounts(0);
            vStack.push(startingNodeKey);
        }
    }
    
    /** Used to iterate through the graph
     * @return true if the next() method will return something
     */
    public boolean hasNext(){
        
        return (!vStack.isEmpty());
    }
    
    /**
     * @return the path followed to access the current node
     * @throws NodeNotFoundException
     */
    private Path pathPeek() throws NodeNotFoundException{
    	return (Path)pathStack.peek();
    }
    
    private Path pathPop() throws NodeNotFoundException{
    	return (Path)pathStack.pop();
    }
    
    /**
     *  @return the next user object in first order
     * @exception NoSuchElementException if the iterator is empty
     */
    public Object next()
    throws NoSuchElementException{
        
        try{
            getNext();
            return current;
        }catch(GraphException e){
            throw new NoSuchElementException();
        }
    }
    
    /** This method is part of the Iterator interface. It is not
     * implemented here since we consider that many iterators
     * may run concurrently on the same graph
     */
    public void remove()
    throws UnsupportedOperationException, IllegalStateException{
        
        throw new UnsupportedOperationException("");
    }
    
    /** @return the list of all nodes defining the path between the current vertex
     * and the graph root
     */
    public Object[]nodePath(){
        
        ArrayList pathList = new ArrayList();
        Stack localStack = (Stack)pathStack.clone();
        while(!localStack.isEmpty()){
            Path path = (Path)localStack.pop();
            if(path.getPopedValue() != null){
                pathList.add(0, path.getNodeKey());
            }
        }
        return pathList.toArray();
    }
    
    /** @return the list of all links used to access the current vertex
     */
    public Object[]linkPath(){
        
        ArrayList pathList = new ArrayList();
        if(pathStack != null){
            Stack localStack = (Stack)pathStack.clone();
            if(!localStack.isEmpty()){
                Path top = (Path)localStack.pop();
                Edge topEdge = top.getPopedValue();
                if(topEdge != null){
                    pathList.add(0, topEdge.getEdgeKey());
                }
                while(!localStack.isEmpty()){
                    Path path = (Path)localStack.pop();
                    Edge edge = path.getPopedValue();
                    if(edge != null){
                        pathList.add(0, edge.getEdgeKey());
                    }
                }
            }
        }
        return pathList.toArray();
    }
    
    /** @return the edge used to access the current node
     */
    public Object usedLink(){
        
        Object result = null;
        if(lastUsedEdge != null){
           result = lastUsedEdge.getUserValue();
        }else{
            result = null;
        }
        return result;
        
    }
    
        /** A connection path is the list of elements that are used to
     *  link one node to another. Two nodes are connected if there is
     *  such a connection path between these two nodes.
     */
    public static final Object[]getConnectionPath(ImplementationGraph graph, 
    Object nodeKeyFrom, Object nodeKeyTo)
    throws GraphException{
        
        if(nodeKeyFrom == null)
            throw new NodeNotFoundException("Parameter 'from' is null");
        if(nodeKeyTo == null)
            throw new NodeNotFoundException("Parameter 'to' is null");
        DepthFirstIterator iter = new DepthFirstIterator(graph, nodeKeyFrom, 1);
        while(iter.hasNext()){
            if(nodeKeyTo.equals(iter.next())){
                return iter.nodePath();
            }
        }
        return null;
    }
    
    public final Object[]getConnectionPath(Object nodeKeyFrom, Object nodeKeyTo)
    throws GraphException{
        if(nodeKeyFrom == null)
            throw new NodeNotFoundException("Parameter 'from' is null");
        if(nodeKeyTo == null)
            throw new NodeNotFoundException("Parameter 'to' is null");
        DepthFirstIterator iter = new DepthFirstIterator(this.graph, nodeKeyFrom, 1);
        while(iter.hasNext()){
            if(nodeKeyTo.equals(iter.next())){
                return iter.nodePath();
            }
        }
        return null;
    }
}