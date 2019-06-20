package net.sf.yogl.std;

import net.sf.yogl.Edge;
import net.sf.yogl.Vertex;
import net.sf.yogl.adjacent.keyMap.AdjKeyEdge;
import net.sf.yogl.adjacent.keyMap.AdjKeyVertex;
import net.sf.yogl.exceptions.StdExecutionException;

public class TestComposite<SK extends Comparable<SK>,TK extends Comparable<TK>, X extends Vertex<Edge> & State<SK, TK>, Y extends Edge<Vertex> & Transition<TK, SK>> {

}

class TestComposite2<X extends Vertex<Y> , Y extends Edge<X>> {

}

class MyTest {
	TestComposite<String, String, MyState, MyTransition> oops;
	TestComposite2<MyState, MyTransition> ops;
}
/*
TestComposite<MyState, MyTransition>
*/
class MyTransition extends AdjKeyEdge<String, Object, String, Object> implements Transition<String, String>{

	MyTransition() {
		super(null, null, null);
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <SP> boolean doTransition(State<String, String> from, SP parameter, State<String, String> to)
			throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <SP> boolean actionAfterBacktrack(State<String, String> from, SP parameter, State<String, String> to,
			Exception e) throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <SP> boolean testAction(State<String, String> from, SP parameter, State<String, String> to)
			throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}
	
}

class MyState extends AdjKeyVertex<String, Object, String, Object> implements State<String, String>{

	MyState(){
		super(null, null);
	}
	@Override
	public <SP> boolean checkBeforeEntry(State<String, String> comingFrom, Transition<String, String> using,
			SP parameter) throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <SP> boolean onEntry(State<String, String> comingFrom, Transition<String, String> using, SP parameter)
			throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <SP> boolean reEntryAfterBacktrack(State<String, String> from, Transition<String, String> using,
			SP parameter, Exception e) throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <SP> boolean checkBeforeExit(State<String, String> navigatingTo, Transition<String, String> using,
			SP parameter) throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <SP> boolean onExit(State<String, String> navigatingTo, Transition<String, String> using, SP parameter)
			throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <SP> boolean exitAfterBacktrack(State<String, String> navigatingTo, Transition<String, String> using,
			SP parameter) throws StdExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void tryAddEdgeFirst(Transition<String, String> edge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tryAddEdgeLast(Transition<String, String> edge) {
		// TODO Auto-generated method stub
		
	}}
