/* Copyright (C) 2003 Symphonix
   
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.
   
   You should have received a copy of the GNU Library General Public
   License along with this library; if not, write to the Free
   Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
   MA 02111-1307, USA */
   
package net.sf.yogl.samples;

import java.util.HashMap;

import net.sf.yogl.AdjListGraph;
import net.sf.yogl.GraphAdapter;
import net.sf.yogl.StateTransitionDiagram;
import net.sf.yogl.exceptions.StdExecutionException;
import net.sf.yogl.std.State;
import net.sf.yogl.std.Transition;

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
	class MyState extends State {
		private String stateValue;

		public boolean onEntry(State from, Transition using, Object parameter) throws StdExecutionException {
			MyObject mo = (MyObject)parameter;
			mo.setState(stateValue);
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

	}

	class MyTransition extends Transition{
		private String key;
		
		public void setKey(String string) {
			key = string;
		}

		public boolean doAction(State from, Object parameter, State to)
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
		GraphAdapter ga = new GraphAdapter(new AdjListGraph(new HashMap()));
		Std std = new Std();
		for(int i=0; i < stdDes.length; i++){
			if(stdDes[i].length == 2){
				MyState state = std.new MyState();
				state.setStateValue(stdDes[i][1]);
				ga.addNode(stdDes[i][0], state);
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
