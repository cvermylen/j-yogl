package net.sf.yogl.std;

import net.sf.yogl.exceptions.StdExecutionException;
import net.sf.yogl.uniqueElements.UniqueEdge;
import net.sf.yogl.uniqueElements.UniqueVertex;

class G<SK extends Comparable<SK>,TK extends Comparable<TK>> extends Graph <X extends UniqueVertex<SK, TK> &State<SK, TK>, Y extends UniqueEdge<TK, SK> &Transition<TK, SK>> {
}

class TestComposite2<SK extends Comparable<SK>,TK extends Comparable<TK>, 
					X extends UniqueVertex<SK, TK> &State<SK, TK>, Y extends UniqueEdge<TK, SK> &Transition<TK, SK>> {

}

class MyTest {
	TestComposite2<String, String, MyState, MyTransition> ops;
}
/*
TestComposite<MyState, MyTransition>
*/
class MyTransition extends UniqueEdge<String, String> implements Transition<String, String>{

	MyTransition() {
		super(null);
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

class MyState extends UniqueVertex<String, String> implements State<String, String>{

	MyState(){
		super(null);
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
