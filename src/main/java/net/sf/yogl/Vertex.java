package net.sf.yogl;

import java.util.Collection;

public abstract class Vertex<VC extends Vertex<VC, TC>, TC extends Edge<TC, VC>>{

	/** Depending on the graph implementation, entries may not be deleted. They are
     * just market as 'free'.
     */
    private boolean freeEntry = false;
    
    /** Is used by traversal algorithms to indicate the number
     * of times the vertex has been visited. This is used for
     * loop detection
     */
    private int visitCounts = 0;

    /** As edges can be only in one direction, this vertex has no direct tracking information of all incoming edges.
     * To help / optimize some methods, the graph implementation may use the following
     */
    private int incomingEdges = 0;
    
    /** setter method
     * @param freeEntry true means that vertex is in a free pool
     */
    public void setFreeEntry(boolean freeEntry){
        this.freeEntry = freeEntry;
    }
    
    /** getter method
     * @return true if the vertex is on the free pool.
     */
    public boolean isFreeEntry(){
        return freeEntry;
    }
    
    /** setter method
     * @param visitCounts new userValue indicating the number of times
     *                   has been traversed.
     */
    public void setVisitCounts(int visitCounts){
        this.visitCounts = visitCounts;
    }
    
    /** getter method
     * @return the number of times the vertex has been visited.
     */
    public int getVisitsCount(){
        return visitCounts;
    }
    
    /** modifier method.
     * postcondition the vertex has been traversed once more.
     */
    public int incVisitCounts(){
        return ++visitCounts;
    }
    
    public void clearVisitsCount(){
    	this.visitCounts = 0;
    }
    
    public int incrementIncomingEdges () {
    	return ++this.incomingEdges;
    }
    
    public int decrementIncomingEdges () {
    	return --this.incomingEdges;
    }
    
    /**
     * The underlying implementation of the collection is left to the type of the graph.
     * @return All edges from this vertex. If the collection is 0, this is a 'sink' vertex
     */
    public abstract Collection<TC> getOutgoingEdges();
    
    public abstract void tryAddEdge(TC edge);
    
}
