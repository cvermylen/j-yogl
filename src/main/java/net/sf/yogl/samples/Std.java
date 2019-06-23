   
package net.sf.yogl.samples;

import net.sf.yogl.adjacent.keyMap.AdjKeyGraph;
import net.sf.yogl.exceptions.StdExecutionException;
import net.sf.yogl.samples.Std.MyObject;
import net.sf.yogl.std.State;
import net.sf.yogl.std.StateIntf;
import net.sf.yogl.std.StateTransitionDiagram;
import net.sf.yogl.std.Transition;
import net.sf.yogl.std.TransitionIntf;

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
	class MyState extends State<String, String> {
		
		public MyState (String key){
			super(key);
		}
		
		public boolean onEntry(TransitionIntf<String, String> using, MyObject parameter) throws StdExecutionException {
			return true;
		}

		@Override
		public <SP> boolean checkBeforeEntry(StateIntf<String, String> comingFrom, TransitionIntf<String, String> using,
				SP parameter) throws StdExecutionException {
			// TODO Auto-generated method stub
			return false;
		}

		
		public <MyObject> boolean onEntry(StateIntf<String, String> comingFrom, TransitionIntf<String, String> using, MyObject parameter)
				throws StdExecutionException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public <SP> boolean reEntryAfterBacktrack(StateIntf<String, String> from, TransitionIntf<String, String> using,
				SP parameter, Exception e) throws StdExecutionException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public <SP> boolean checkBeforeExit(StateIntf<String, String> navigatingTo, TransitionIntf<String, String> using,
				SP parameter) throws StdExecutionException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public <SP> boolean onExit(StateIntf<String, String> navigatingTo, TransitionIntf<String, String> using, SP parameter)
				throws StdExecutionException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public <SP> boolean exitAfterBacktrack(StateIntf<String, String> navigatingTo, TransitionIntf<String, String> using,
				SP parameter) throws StdExecutionException {
			// TODO Auto-generated method stub
			return false;
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


		@Override
		public <SP> boolean doTransition(StateIntf<String, String> from, SP parameter, StateIntf<String, String> to)
				throws StdExecutionException {
			// TODO Auto-generated method stub
			return false;
		}


		@Override
		public <SP> boolean actionAfterBacktrack(StateIntf<String, String> from, SP parameter, StateIntf<String, String> to,
				Exception e) throws StdExecutionException {
			// TODO Auto-generated method stub
			return false;
		}


		@Override
		public <SP> boolean testAction(StateIntf<String, String> from, SP parameter, StateIntf<String, String> to)
				throws StdExecutionException {
			// TODO Auto-generated method stub
			return false;
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
				MyState state = new MyState(stdDes[i][0]);
				
				ga.tryAddVertex(state, i == 0);
			}else if(stdDes[i].length == 3){
				MyTransition transition = new MyTransition(stdDes[i][2]);
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
