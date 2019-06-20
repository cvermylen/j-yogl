   
package net.sf.yogl.samples;

import java.util.HashMap;
import java.util.Map;

import net.sf.yogl.adjacent.keyMap.AdjKeyGraph;
import net.sf.yogl.adjacent.keyMap.AdjKeyVertex;
import net.sf.yogl.exceptions.StdExecutionException;
import net.sf.yogl.samples.Std.MyObject;
import net.sf.yogl.samples.Std.MyState;
import net.sf.yogl.samples.Std.MyTransition;
import net.sf.yogl.std.State;
import net.sf.yogl.std.StateTransitionDiagram;
import net.sf.yogl.std.Transition;

/** Example with StateTransitionDiagram
 */
public class Std {

	class MyObject{
		private String state;
		public String getState() {
			return state;
		}

		public void setState(String string) {
			state = string;
		}

	}
	class MyState extends AdjKeyVertex<String, String, String, String> implements State<String, String> {
		private String stateValue;
		private Map<String, MyTransition> transitions = new HashMap<String, MyTransition>();
		
		public MyState (String key, String value){
			super(key);
			this.stateValue = value;
		}
		
		public boolean onEntry(Transition<String, String> using, MyObject parameter) throws StdExecutionException {
			parameter.setState(stateValue);
			return true;
		}
		public void setStateValue(String stateValue) {
			this.stateValue = stateValue;
		}
		public String toString(){
			return stateValue;
		}
		/**
		 * @return
		 */
		public String getStateValue() {
			return stateValue;
		}
		@Override
		public Transition<String, String> getOutgoingEdge(String nextTransitionKey) {
			return transitions.get(nextTransitionKey);
		}

	}

	class MyTransition extends Transition<String, String>{
		
		public MyTransition(String key) {
			super(key);
		}

		public boolean doAction(MyState from, MyObject parameter, MyState to)
		throws StdExecutionException {
			return true;
		}


	}
	
	public static void main(String[] args) throws Exception {
		String[][]stdDes ={
			{"1", "Root"},
			{"2", "Second State"},
			{"3", "Third State"},
			{"4", "Final State"},
			{"1", "2", "key12"},
			{"1", "3", "key13"},
			{"2", "3", "key23"},
			{"3", "2", "key32"},
			{"2", "4", "key24"}
		};
		AdjKeyGraph<String, Object, String, Object> ga = new AdjKeyGraph<>();
		StateTransitionDiagram<String, String> std = new StateTransitionDiagram<String, String>(ga);
		for(int i=0; i < stdDes.length; i++){
			if(stdDes[i].length == 2){
				MyState state = std.new MyState(stdDes[i][0], stdDes[i][1]);
				
				ga.tryAddVertex(state, i == 0);
			}else if(stdDes[i].length == 3){
				MyTransition transition = std.new MyTransition();
				transition.setKey(stdDes[i][2]);
				ga.addLinkLast(stdDes[i][0], stdDes[i][1], stdDes[i][2], transition);
			}
		}
		MyObject myObject = std.new MyObject();
		StateTransitionDiagram diag = new StateTransitionDiagram(ga);
		diag.init(myObject);
		System.out.println("Object is in state:"+myObject.getState());	
		boolean b = diag.doActiveTransition("key13", myObject);
		System.out.println("Object is now in state:"+myObject.getState());
		b = diag.isTransition("key12");
		System.out.println("Object is still in state:"+myObject.getState());
		System.out.println("The returned value of the transition was:"+b);
		System.out.println("The getTransitionKeys just looks if the transition is possible:"+diag.getTransitionKeys());
	}
}
