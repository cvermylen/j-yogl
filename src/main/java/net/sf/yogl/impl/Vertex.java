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

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.yogl.Node;
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

public class Vertex <VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> implements Node{
    /** Depending on the graph implementation, entries may not be deleted. They are
     * just market as 'free'.
     */
    private boolean freeEntry = false;
    
    /** Is used by traversal algorithms to indicate the number
     * of times the vertex has been visited. This is used for
     * loop detection
     */
    private int traversals = 0;
    
    /** contains the list of all adjacent vertices.
     */
    private ListMap<VK, VV, EK, EV> outgoingEdges = new ListMap<VK, VV, EK, EV>();
    
    /** Key must be comparable
     * 
     */
    VK key;
    
    /** Application class data. Refers to an object having the
     * 'equals' method defined.
     */
    VV userValue = null;
    
    /** ctor with initialisation
     * @param type associates a type to the contents ofthe vertex
     * @param userValue refers to the node object
     */
    public Vertex(VK key, VV userValue){
    	this.key = key;
        this.userValue = userValue;
    }
    
    public Vertex<VK, VV, EK, EV> clone(){
        
        Vertex<VK, VV, EK, EV> cloned = new Vertex<VK, VV, EK, EV>(key, userValue);
        cloned.freeEntry = freeEntry;
        cloned.traversals = traversals;
        cloned.outgoingEdges = outgoingEdges;
        return cloned;
    }
    /** compares the userValue objects of 'this' and 'v' if both
     * references are non-null. If either 'this' or 'v' is null
     * the function return 'false'
     * @param vertex the contents of the user defined node to
     *        be compared with
     * @return a boolean userValue indicating if the user defined node
     * matches or not.
     */
    public boolean equals(Vertex<VK, VV, EK, EV> vertex){
        boolean res = false;
        if( (userValue != null) && (vertex != null) ){
            res = userValue.equals(vertex.getUserValue());
        }
        return res;
    }
    
    /** setter method
     * @param freeEntry true means that vertex is in a free pool
     */
    public void setFreeEntry(boolean freeEntry){
        this.freeEntry = freeEntry;
    }
    
    /** getter method
     * @return true if the vertex is on the free pool.
     */
    public boolean isFree(){
        return freeEntry;
    }
    
    /** setter method
     * @param traversals new userValue indicating the number of times
     *                   has been traversed.
     */
    public void setTraversals(int traversals){
        this.traversals = traversals;
    }
    
    /** getter method
     * @return the number of times the vertex has been visited.
     */
    public int getTraversals(){
        return traversals;
    }
    
    /** modifier method.
     * postcondition the vertex has been traversed once more.
     */
    public int incTraversals(){
        return ++traversals;
    }
    
    /** getter method
     * @return the object userValue associated to the vertex.
     */
    public VV getUserValue(){
        return userValue;
    }
    
    /** Return the edge used to access the given vertex.
     *  @param to points to a vertex
     *  @return an edge or null if both vertex (this & to)
     *          are not connected.
     */
    public Edge<VK, EK, EV>[]getEdgeTo(VK key){
        
        ArrayList<Edge<VK, EK, EV>> result = new ArrayList<>();
        Iterator<Edge<VK, EK, EV>> iter = outgoingEdges.values().iterator();
        while(iter.hasNext()){
            Edge<VK, EK, EV> edge = iter.next();
            if(edge.getNextVertexKey().equals(key)){
                result.add(edge);
                break;
            }
        }
        if(result.size() == 0){
        	return null;
        }else{
        	return (Edge<VK, EK, EV>[])result.toArray(new Edge[result.size()]);
        }
    }
    
    /** get a stringified version of the vertex
     *  @return string representaiotn
     */
    public String toString(){
        
        return "traversals("+traversals+")userValue("+userValue.toString()+")"+
        " freeEntry("+freeEntry+")";
    }
    
    /** returns the number of edges going out from 'this'
     * @return the number of edges going out of this vertex
     */
    public int getCountEdges(){
        int num = 0;
        if(outgoingEdges != null){
            num = outgoingEdges.size();
        }
        return num;
    }
    
    /** getter method
     * Be careful to the following:
     *  - do not use this method if there is any kind
     *    of concurrent access to the list
     *  - do not modify directly this list (by adding or
     *    removing elements
     * @return the list of outgoing edges
     */
    public Edge<VK, EK, EV>[]getNeighbors(){
    	ArrayList<Edge<VK, EK, EV>> result = new ArrayList<>(outgoingEdges.values());
        return (Edge<VK, EK, EV>[])result.toArray(new Edge[outgoingEdges.size()]);
    }
    
    public VK getNextVertexKey(EK edgeKey){
    	Edge<VK, EK, EV> edge = getEdge(edgeKey);
    	return edge.getNextVertexKey();
    }
    
    public Edge<VK, EK, EV> getEdge(EK edgeKey){
    	return (Edge<VK, EK, EV>)outgoingEdges.get(edgeKey);
    }
    
    /** This method insert a new edge between 'this' vertex
     * and the vertex pointed to by the edge.
     * @param edge contains the destination vertex
     * @exception GraphCorruptedException if 'edge' cannot
     *            be inserted in the neighbors list.
     */
    public void addEdgeLast(Edge<VK, EK, EV> newEdge)throws GraphCorruptedException{
        if (newEdge != null){
            if (outgoingEdges != null){
                outgoingEdges.addLast(newEdge.getEdgeKey(), newEdge);
            }else{
                throw new GraphCorruptedException(
                "Vertex identified by "+userValue.toString()+
                " has a null neighbors list");
            }
        }
    }
    
    /** This method insert a new edge between 'this' vertex
     * and the vertex pointed to by the edge.
     *  The edge is placed at the head of the list
     * @param edge contains the destination vertex
     * @exception InvalidVertexException if 'edge' cannot
     *            be inserted in the neighbors list.
     */
    public void addEdgeFirst(Edge<VK, EK, EV> newEdge)throws GraphCorruptedException{
        if (newEdge != null){
            if (outgoingEdges != null){
                outgoingEdges.addFirst(newEdge.getEdgeKey(), newEdge);
            }else{
                throw new GraphCorruptedException(
                "Vertex identified by "+userValue.toString()+
                " has a null neighbors list");
            }
        }
    }
    /** Removes from the neighbors list the edge given as parameter
     * @param edge to be removed
     */
    public void removeEdge(Edge<VK, EK, EV> edge){
        if (edge != null){
            outgoingEdges.remove(edge.getEdgeKey());
        }
    }
    
    public void removeEdgeByKey(EK edgeKey){
    	if (edgeKey != null){
            outgoingEdges.remove(edgeKey);
        }
    }
    
    /** Removes all outgoing edges from the vertex. No successor will
     * be accessible after this operation.
     * Precondition: vertex exists
     * Postcondition: edges list is empty
     */
    public void clearEdges(){
        outgoingEdges.clear();
    }
    
    public VK getKey(){
    	return key;
    }
}
