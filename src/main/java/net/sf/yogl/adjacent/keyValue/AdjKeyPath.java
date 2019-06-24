   
package net.sf.yogl.adjacent.keyValue;

import java.util.LinkedList;

/** Class used to manage the path of vertices followed during
 * the traversal. Object are pushed on a stack with the number of adjacent
 * neighbors. Each time an object is popped from the traversal stack, the
 * top object of the path stack is decremented. When  the count value of the top
 * object reaches 0, the top object is removed.
 */

public class AdjKeyPath<VERTEX_KEY extends Comparable<VERTEX_KEY>, VERTEX_VALUE, EDGE_VALUE>{
	
	/** Reference to the vertex' node
	 */
	private VERTEX_KEY nodeKey = null;
	
	private ValueEdge<VERTEX_KEY,VERTEX_VALUE, EDGE_VALUE> popedValue = null;
	
	/** Reference to links exiting from this vertex
	 *  Order in this stack is extremely important.
	 *  It MUST follow the same order as the one in the
	 *  vStack.
	 */
	private LinkedList<ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>> outgoingEdges = null;
	
	public AdjKeyPath(VERTEX_KEY nodeKey, ValueEdge<VERTEX_KEY,VERTEX_VALUE, EDGE_VALUE>[]eArray){
		this.nodeKey = nodeKey;
		this.outgoingEdges = new LinkedList<>();
		//outgoingEdges.push(new AdjKeyEdge<VERTEX_KEY,VERTEX_VALUE, EK,EDGE_VALUE>(null, null, null));
		for (int i=eArray.length-1; i>= 0; i--){
			outgoingEdges.push(eArray[i]);
		}
	}

	/** dec consumes the next edge from the current
	 *  path node.
	 *  @return the edge used to access to the next
	 *  node
	 */
	public ValueEdge<VERTEX_KEY,VERTEX_VALUE, EDGE_VALUE> old_dec(){
		
		ValueEdge<VERTEX_KEY,VERTEX_VALUE, EDGE_VALUE> usedEdge = outgoingEdges.pop();
		popedValue = usedEdge;
		return usedEdge;
	}

	/** getter method
	 * @return the count number
	 */
	public int getCount(){
		return outgoingEdges.size() - 1;
	}

	/** getter method
	 * @return the vertex index
	 */
	public VERTEX_KEY getNodeKey(){
		return nodeKey;
	}
	
	/** getter method
	 *  @return the current edge
	 */
	public ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> old_getEdge(){
		ValueEdge<VERTEX_KEY,VERTEX_VALUE, EDGE_VALUE> edge = null;
		
		if(!outgoingEdges.isEmpty()){
			if(outgoingEdges.size() > 1){ //last element is empty string
				edge = outgoingEdges.peek();
			}
		}
		return edge;
	}
	
	public ValueEdge<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> old_getPopedValue(){
		
		return popedValue;
	}
	
	public String toString(){
		
		String result = "Path(Node("+nodeKey+")PopedValue("+popedValue+"))";
		return result;
	}
	
	public AdjKeyPath<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE> clone(){
		AdjKeyPath<VERTEX_KEY, VERTEX_VALUE, EDGE_VALUE>result = this.clone();
		result.nodeKey = this.nodeKey;
		result.popedValue = this.popedValue;
		result.outgoingEdges = this.outgoingEdges.stream().map(e -> e.clone()).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
		return result;
	}
}