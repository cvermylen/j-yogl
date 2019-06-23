   
package net.sf.yogl.samples;

import net.sf.yogl.exceptions.StdExecutionException;
import net.sf.yogl.std.State;
import net.sf.yogl.std.StateTransitionDiagram;
import net.sf.yogl.std.Transition;

/** Example with StateTransitionDiagram
 */
class MyParameter {
	private String state;
	public String getState() {
		return state;
	}

	public void setState(String string) {
		state = string;
	}

}
class MyState extends State<String, String, MyState, MyTransition, MyParameter> {
	
	public MyState (String key){
		super(key);
	}
	
	public boolean onEntry(MyTransition using, MyParameter parameter) throws StdExecutionException {
		return true;
	}



	@Override
	public boolean onEntry(MyState comingFrom, MyTransition using, MyParameter parameter) throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reEntryAfterBacktrack(MyState from, MyTransition using, MyParameter parameter, Exception e)
			throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkBeforeExit(MyState navigatingTo, MyTransition using, MyParameter parameter)
			throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onExit(MyState navigatingTo, MyTransition using, MyParameter parameter)
			throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exitAfterBacktrack(MyState navigatingTo, MyTransition using, MyParameter parameter)
			throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkBeforeEntry(MyState comingFrom, MyTransition using, MyParameter parameter)
			throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}
}


class MyTransition extends Transition<String, String, MyTransition, MyState, MyParameter>{
	
	public MyTransition(String key) {
		super(key);
	}

	@Override
	public boolean doTransition(MyState from, MyParameter parameter) throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean actionAfterBacktrack(MyState from, MyParameter parameter, Exception e)
			throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean testAction(MyState from, MyParameter parameter) throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

}
public class Std {

	
	
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
		StateTransitionDiagram<String, String, MyState, MyTransition, MyParameter> std = new StateTransitionDiagram<>();
		for(int i=0; i < stdDes.length; i++){
			MyState[] stateArr = new MyState[4];
			if(stdDes[i].length == 2){
				stateArr[i] = new MyState(stdDes[i][0]);
				std.tryAddVertex(stateArr[i], i == 0);
			}else if(stdDes[i].length == 3){
				MyTransition transition = new MyTransition(stdDes[i][2]);
				transition.setToVertex(std.findVertexByKey(stdDes[1][1]));
				stateArr[i].tryAddEdgeLast(transition);
			}
		}
		MyParameter myObject = new MyParameter();
		MyState startingState = std.getCurrentState();
		System.out.println("Object is in state:"+myObject.getState());
		MyTransition transition = startingState.getOutgoingEdges().stream().findFirst().get();
		boolean b = std.doActiveTransition(transition, myObject);
		System.out.println("Object is now in state:"+myObject.getState());
		System.out.println("Object is still in state:"+myObject.getState());
		System.out.println("The returned value of the transition was:"+b);
		System.out.println("The getTransitionKeys just looks if the transition is possible:"+std.getTransitions());
	}

}
