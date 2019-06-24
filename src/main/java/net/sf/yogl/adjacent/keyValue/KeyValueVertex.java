   
package net.sf.yogl.adjacent.keyValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

public class KeyValueVertex <VK extends Comparable<VK>, VV, EV >  extends Vertex<KeyValueVertex<VK, VV, EV>, ValueEdge<VK, VV, EV>>{
        
    /** contains the list of all adjacent vertices.
     */
//    private KeyMap<VK, VV, EK, EV> outgoingEdges = new KeyMap<VK, VV, EK, EV>();
    private List<ValueEdge<VK, VV, EV>> outgoingEdges = new ArrayList<>();
    
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
    public KeyValueVertex(VK key, VV userValue){
    	super();
    	this.key = key;
        this.userValue = userValue;
    }
    
    public KeyValueVertex<VK, VV, EV> clone(){
        
        KeyValueVertex<VK, VV, EV> cloned = new KeyValueVertex<VK, VV, EV>(key, userValue);
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
    public boolean equals(KeyValueVertex<VK, VV, EV> vertex){
        
        return this.compareTo(vertex) == 0;
    }
    
    public int compareTo(KeyValueVertex<VK, VV, EV> v){
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
    public Collection<ValueEdge<VK, VV, EV>> getEdgeTo(VK key){
        
        ArrayList<ValueEdge<VK, VV, EV>> result = new ArrayList<>();
        Iterator<ValueEdge<VK, VV, EV>> iter = outgoingEdges.iterator();
        while(iter.hasNext()){
            ValueEdge<VK, VV, EV> edge = iter.next();
            if(edge.getToVertex().equals(key)){
                result.add(edge);
                break;
            }
        }
        if(result.size() == 0){
        	return null;
        }else{
        	return result;
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
    public Collection<ValueEdge<VK, VV, EV>> getOutgoingEdges(){
    	ArrayList<ValueEdge<VK, VV, EV>> result = new ArrayList<>(outgoingEdges);
        return result;
    }
    
    /** This method insert a new edge between 'this' vertex
     * and the vertex pointed to by the edge.
     * @param edge contains the destination vertex
     * @exception GraphCorruptedException if 'edge' cannot
     *            be inserted in the neighbors list.
     */
    public void tryAddEdge(ValueEdge<VK, VV, EV> newEdge) {
        if (newEdge != null){
        	outgoingEdges.add(newEdge);
        }
    }
    
    /** This method insert a new edge between 'this' vertex
     * and the vertex pointed to by the edge.
     *  The edge is placed at the head of the list
     * @param edge contains the destination vertex
     * @exception InvalidVertexException if 'edge' cannot
     *            be inserted in the neighbors list.
     */
    public void tryAddEdgeFirst(ValueEdge<VK, VV, EV> newEdge) {
        if (newEdge != null){
            outgoingEdges.add(0, newEdge);
        }
    }
    /** Removes from the neighbors list the edge given as parameter
     * @param edge to be removed
     */
    public void removeEdge(ValueEdge<VK, VV, EV> edge){
        if (edge != null){
            outgoingEdges.remove(edge);
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
