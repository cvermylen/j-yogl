   
package net.sf.yogl.adjacent.keyValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

public class KeyValueVertex <VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV >  extends Vertex<KeyValueVertex<VK, VV, EK, EV>, KeyValueEdge<VK, VV, EK, EV>>{
        
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
    public KeyValueVertex(VK key, VV userValue){
    	super();
    	this.key = key;
        this.userValue = userValue;
    }
    
    public KeyValueVertex<VK, VV, EK, EV> clone(){
        
        KeyValueVertex<VK, VV, EK, EV> cloned = new KeyValueVertex<VK, VV, EK, EV>(key, userValue);
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
    public boolean equals(KeyValueVertex<VK, VV, EK, EV> vertex){
        
        return this.compareTo(vertex) == 0;
    }
    
    public int compareTo(KeyValueVertex<VK, VV, EK, EV> v){
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
    public Collection<KeyValueEdge<VK, VV, EK, EV>> getEdgeTo(VK key){
        
        ArrayList<KeyValueEdge<VK, VV, EK, EV>> result = new ArrayList<>();
        Iterator<KeyValueEdge<VK, VV, EK, EV>> iter = outgoingEdges.values().iterator();
        while(iter.hasNext()){
            KeyValueEdge<VK, VV, EK, EV> edge = iter.next();
            if(edge.getNextVertexKey().equals(key)){
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
    public Collection<KeyValueEdge<VK, VV, EK, EV>> getNeighbors(){
    	ArrayList<KeyValueEdge<VK, VV, EK, EV>> result = new ArrayList<>(outgoingEdges.values());
        return result;
    }
    
    @Override
	public Collection<KeyValueEdge<VK, VV, EK, EV>> getOutgoingEdges() {
		return outgoingEdges.values();
	}
    
    public VK getNextVertexKey(EK edgeKey){
    	KeyValueEdge<VK, VV, EK, EV> edge = getEdgeByKey(edgeKey);
    	return edge.getNextVertexKey();
    }
    
    public KeyValueEdge<VK, VV, EK, EV> getEdgeByKey(EK edgeKey){
    	return (KeyValueEdge<VK, VV, EK, EV>)outgoingEdges.get(edgeKey);
    }
    
    /** This method insert a new edge between 'this' vertex
     * and the vertex pointed to by the edge.
     * @param edge contains the destination vertex
     * @exception GraphCorruptedException if 'edge' cannot
     *            be inserted in the neighbors list.
     */
    public void tryAddEdge(KeyValueEdge<VK, VV, EK, EV> newEdge) {
        if (newEdge != null){
        	outgoingEdges.addLast(newEdge.getEdgeKey(), newEdge);
        }
    }
    
    /** This method insert a new edge between 'this' vertex
     * and the vertex pointed to by the edge.
     *  The edge is placed at the head of the list
     * @param edge contains the destination vertex
     * @exception InvalidVertexException if 'edge' cannot
     *            be inserted in the neighbors list.
     */
    public void tryAddEdgeFirst(KeyValueEdge<VK, VV, EK, EV> newEdge) {
        if (newEdge != null){
            outgoingEdges.addFirst(newEdge.getEdgeKey(), newEdge);
        }
    }
    /** Removes from the neighbors list the edge given as parameter
     * @param edge to be removed
     */
    public void removeEdge(KeyValueEdge<VK, VV, EK, EV> edge){
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
