package net.sf.yogl;

public abstract class Vertex {

	/** Depending on the graph implementation, entries may not be deleted. They are
     * just market as 'free'.
     */
    private boolean freeEntry = false;
    
    /** Is used by traversal algorithms to indicate the number
     * of times the vertex has been visited. This is used for
     * loop detection
     */
    private int traversals = 0;

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
    
}
