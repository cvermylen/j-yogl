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
   
package net.sf.yogl.adjacent.keyMap;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.yogl.Vertex;
import net.sf.yogl.Edge;
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

public class AdjKeyVertex <VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV >  extends Vertex<AdjKeyEdge>{
        
    /** contains the list of all adjacent vertices.
     */
    private KeyMap<VK, VV, EK, EV> outgoingEdges = new KeyMap<VK, VV, EK, EV>();
    
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
    public AdjKeyVertex(VK key, VV userValue){
    	super();
    	this.key = key;
        this.userValue = userValue;
    }
    
    public AdjKeyVertex<VK, VV, EK, EV> clone(){
        
        AdjKeyVertex<VK, VV, EK, EV> cloned = new AdjKeyVertex<VK, VV, EK, EV>(key, userValue);
        cloned.setFreeEntry(isFreeEntry());
        cloned.setVisitCounts(getVisitsCount());
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
    public boolean equals(AdjKeyVertex<VK, VV, EK, EV> vertex){
        
        return this.compareTo(vertex) == 0;
    }
    
    public int compareTo(AdjKeyVertex<VK, VV, EK, EV> v){
    	return this.key.compareTo(v.getKey());
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
    public AdjKeyEdge<VK, EK, EV>[]getEdgeTo(VK key){
        
        ArrayList<AdjKeyEdge<VK, EK, EV>> result = new ArrayList<>();
        Iterator<AdjKeyEdge<VK, EK, EV>> iter = outgoingEdges.values().iterator();
        while(iter.hasNext()){
            AdjKeyEdge<VK, EK, EV> edge = iter.next();
            if(edge.getNextVertexKey().equals(key)){
                result.add(edge);
                break;
            }
        }
        if(result.size() == 0){
        	return null;
        }else{
        	return (AdjKeyEdge<VK, EK, EV>[])result.toArray(new AdjKeyEdge[result.size()]);
        }
    }
    
    /** get a stringified version of the vertex
     *  @return string representaiotn
     */
    public String toString(){
        
        return "traversals("+getVisitsCount()+")userValue("+userValue.toString()+")"+
        " freeEntry("+isFreeEntry()+")";
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
    public AdjKeyEdge<VK, EK, EV>[]getNeighbors(){
    	ArrayList<AdjKeyEdge<VK, EK, EV>> result = new ArrayList<>(outgoingEdges.values());
        return (AdjKeyEdge<VK, EK, EV>[])result.toArray(new AdjKeyEdge[outgoingEdges.size()]);
    }
    
    public VK getNextVertexKey(EK edgeKey){
    	AdjKeyEdge<VK, EK, EV> edge = getEdge(edgeKey);
    	return edge.getNextVertexKey();
    }
    
    public AdjKeyEdge<VK, EK, EV> getEdge(EK edgeKey){
    	return (AdjKeyEdge<VK, EK, EV>)outgoingEdges.get(edgeKey);
    }
    
    /** This method insert a new edge between 'this' vertex
     * and the vertex pointed to by the edge.
     * @param edge contains the destination vertex
     * @exception GraphCorruptedException if 'edge' cannot
     *            be inserted in the neighbors list.
     */
    public void addEdgeLast(AdjKeyEdge<VK, EK, EV> newEdge)throws GraphCorruptedException{
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
    public void addEdgeFirst(AdjKeyEdge<VK, EK, EV> newEdge)throws GraphCorruptedException{
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
    public void removeEdge(AdjKeyEdge<VK, EK, EV> edge){
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
