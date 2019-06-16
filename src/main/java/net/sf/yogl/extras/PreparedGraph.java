   
package net.sf.yogl.extras;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.yogl.adjacent.keyMap.ComparableKeysGraph;
import net.sf.yogl.exceptions.DuplicateLinkException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.impl.DuplicateVertexException;

/** Utility class that help to create a new graph. Inserting nodes in a graph
 *  requires to known for each node if it has predecessors or successors or
 *  both. With this class, the graph is created in two steps. First nodes
 *  and links are stored in a local structure, and when all of them are present,
 *  the graph is populated. Local structures are used to determine the position
 *  of each node in the graph.
 */
public class PreparedGraph extends java.lang.Object {
    
    private HashSet heads = new HashSet();
    
    private HashSet tails = new HashSet();
    
    private LinkedList links = new LinkedList();
    
    private LinkedHashMap nodes = new LinkedHashMap();
    
    /** Structure to store relations
     */
    private class Rel{
        Object from;
        Object to;
        Object link;
        Rel(Object from, Object to, Object link){
            this.from = from;
            this.to = to;
            this.link = link;
        }
        
        public boolean equals(Object o){
            boolean result = false;
            if(o instanceof Rel){
                Rel rel = (Rel)o;
                result = this.from.equals(rel.from) && this.to.equals(rel.to);
            }
            return result;
        }
    }
    
    /** Creates new PrepareInsert */
    public PreparedGraph() {
    }
    
    /** Creates a new node if it does not already exists. If it does,
     *  the previous reference is lost.
     */
    public void addNode(Object nodeKey, Object nodeValue) {
        nodes.put(nodeKey, nodeValue);
    }

    public Map getNodes(){
        return this.nodes;
    }
    
    public List getLinks(){
        return this.links;
    }
    
    /** CReates a new node if it does not already exists. The duplicate
     *  existence is detected by the 'equals' method.
     *  @param node is the new node to be inserted in the graph
     *  @exception DuplicateVertexException if the node is already present
     */
    public void tryAddNode(java.lang.Object nodeKey, Object nodeValue)
    throws DuplicateVertexException {
        if(!nodes.containsKey(nodeKey)){
            nodes.put(nodeKey, nodeValue);
        }
    }
    
    public void addLink(java.lang.Object nodeKeyFrom,java.lang.Object nodeKeyTo, Object link) {
        links.add(new Rel(nodeKeyFrom, nodeKeyTo, link));
        heads.add(nodeKeyFrom);
        tails.add(nodeKeyTo);
    }
    
    public void tryAddLink(java.lang.Object nodeKeyFrom,java.lang.Object nodeKeyTo, Object link)
    throws DuplicateLinkException {
        Rel rel = new Rel(nodeKeyFrom, nodeKeyTo, link);
        if(links.contains(rel)){
        	DuplicateLinkException e = new DuplicateLinkException();
        	e.setNodeKeyFrom(nodeKeyFrom);
        	e.setNodeKeyTo(nodeKeyTo);
        	e.setLink(link);
            throw new DuplicateLinkException();
        }
        links.add(rel);
        heads.add(nodeKeyFrom);
        tails.add(nodeKeyTo);
    }
    
    public void populateGraph(ComparableKeysGraph graph) throws GraphException {
        cleanUpHeadsAndTails();
        Iterator nodesIter = nodes.entrySet().iterator();
        while(nodesIter.hasNext()){
            Map.Entry node = (Map.Entry)nodesIter.next();
            graph.addNode((Comparable)node.getKey(), node.getValue());
        }
        
        Iterator linksIter = links.iterator();
        while(linksIter.hasNext()){
            Rel rel = (Rel)linksIter.next();
            graph.addLinkLast((Comparable)rel.from, (Comparable)rel.to, (Comparable)rel.link, null);
        }
    }
    
    private void cleanUpHeadsAndTails(){
        Iterator toIter = tails.iterator();
        while(toIter.hasNext()){
            heads.remove(toIter.next());
        }
        Iterator fromIter = heads.iterator();
        while(fromIter.hasNext()){
            tails.remove(fromIter.next());
        }
        //Nodes without relations are standalone
        LinkedHashMap nodesClone = (LinkedHashMap)nodes.clone();
        Iterator linksIter = links.iterator();
        while(linksIter.hasNext()){
            Rel rel = (Rel)linksIter.next();
            nodesClone.remove(rel.from);
            nodesClone.remove(rel.to);
        }
        Iterator remainder = nodesClone.keySet().iterator();
        while(remainder.hasNext()){
            Object nodeKey = remainder.next();
            heads.add(nodeKey);
            tails.add(nodeKey);
        }
    }
}
