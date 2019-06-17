   
package net.sf.yogl.types;

import java.util.Collection;

import net.sf.yogl.adjacent.list.AdjListGraph;
import net.sf.yogl.exceptions.GraphException;

/** Simple graph which does not contains any data. Nodes and links
 *  are just identified by keys.
 */
public final class NoDataGraph<VK extends Comparable<VK>, EK extends Comparable<EK>> extends AdjListGraph<VK, Object, EK, Object> {

	public void addNode(VK nodeKey) throws GraphException{
		super.addNode(nodeKey, "");
	}
	
	public void addNode(VK nodeKey, Object nodeValue)throws GraphException{
		super.addNode(nodeKey, "");
	}
	public Collection nodesValues(){
		return null;
	}
	
	public void tryAddNode(VK nodeKey) throws GraphException{
		super.tryAddNode(nodeKey, null);
	}
}
