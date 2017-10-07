package net.sf.yogl;

import java.util.List;

public abstract class Vertex<E extends Edge> {

	/** Depending on the graph implementation, entries may not be deleted. They are
     * just market as 'free'.
     */
    private boolean freeEntry = false;
    
    /** Is used by traversal algorithms to indicate the number
     * of times the vertex has been visited. This is used for
     * loop detection
     */
    private int visitCounts = 0;

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
    
    public abstract List<E> getOutgoingEdges();
    
 
}
