   
package net.sf.yogl.adjacent.keyMap;

import java.util.LinkedList;

import net.sf.yogl.adjacent.list.AdjListEdge;

/** Class used to manage the path of vertices followed during
 * the traversal. Object are pushed on a stack with the number of adjacent
 * neighbors. Each time an object is popped from the traversal stack, the
 * top object of the path stack is decremented. When  the count value of the top
 * object reaches 0, the top object is removed.
 */

public class AdjKeyPath<VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV>{
	
	/** Reference to the vertex' node
	 */
	private VK nodeKey = null;
	
	private AdjKeyEdge<VK,VV, EK,EV> popedValue = null;
	
	/** Reference to links exiting from this vertex
	 *  Order in this stack is extremely important.
	 *  It MUST follow the same order as the one in the
	 *  vStack.
	 */
	private LinkedList<AdjKeyEdge<VK, VV, EK,EV>> outgoingEdges = null;
	
	public AdjKeyPath(VK nodeKey, AdjKeyEdge<VK,VV, EK,EV>[]eArray){
		this.nodeKey = nodeKey;
		this.outgoingEdges = new LinkedList<>();
		//outgoingEdges.push(new AdjKeyEdge<VK,VV, EK,EV>(null, null, null));
		for (int i=eArray.length-1; i>= 0; i--){
			outgoingEdges.push(eArray[i]);
		}
	}

	/** dec consumes the next edge from the current
	 *  path node.
	 *  @return the edge used to access to the next
	 *  node
	 */
	public AdjKeyEdge<VK,VV, EK,EV> old_dec(){
		
		AdjKeyEdge<VK,VV, EK,EV> usedEdge = outgoingEdges.pop();
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
	public VK getNodeKey(){
		return nodeKey;
	}
	
	/** getter method
	 *  @return the current edge
	 */
	public AdjKeyEdge<VK, VV, EK,EV> old_getEdge(){
		AdjKeyEdge<VK,VV, EK,EV> edge = null;
		
		if(!outgoingEdges.isEmpty()){
			if(outgoingEdges.size() > 1){ //last element is empty string
				edge = outgoingEdges.peek();
			}
		}
		return edge;
	}
	
	public AdjKeyEdge<VK, VV, EK,EV> old_getPopedValue(){
		
		return popedValue;
	}
	
	public String toString(){
		
		String result = "Path(Node("+nodeKey+")PopedValue("+popedValue+"))";
		return result;
	}
	
	public AdjKeyPath<VK, VV, EK, EV> clone(){
		AdjKeyPath<VK, VV, EK, EV>result = this.clone();
		result.nodeKey = this.nodeKey;
		result.popedValue = this.popedValue;
		result.outgoingEdges = this.outgoingEdges.stream().map(e -> e.clone()).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
		return result;
	}
}