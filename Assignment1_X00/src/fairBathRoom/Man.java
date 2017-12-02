package fairBathRoom;
import java.util.concurrent.ThreadLocalRandom;

import se.his.iit.it325g.common.AndrewsProcess;

public class Man implements Runnable {

	@Override
	public void run() {
		GlobalState.mutex.P();
		GlobalState.menQ.add(this);
		GlobalState.mutex.V();
		
		while (true){				//poop loop
			GlobalState.mutex.P();
			if (GlobalState.wom > 0 || GlobalState.men >= 4 /*|| GlobalState.menQ.peek() != this*/ || !GlobalState.menTurn){ 
				GlobalState.delayedMen++;
				GlobalState.mutex.V();
				GlobalState.semMen.P();
			}
			GlobalState.men++;
			GlobalState.menQ.poll(); //remove from queue
			GlobalState.increaseTurn();
			GlobalState.signal(); //release to next man
			
			doThings(); //poop
			System.out.println(getState());
			
			GlobalState.mutex.P();
			GlobalState.men--;
			GlobalState.menQ.add(this); //re-enter queue
			GlobalState.signal();
			
			doThings(); //eat a burger to make more poop
		}
	}

	public static String getState(){ //printout the global state
		return "M " + AndrewsProcess.currentAndrewsProcessId()
				+ "  in CS, state: nm:" + GlobalState.men +
				" nw:" + GlobalState.wom +
				" dm:" + GlobalState.delayedMen +
				" dw:" + GlobalState.delayedWom;
	}
	private void doThings(){ //represents that processes are staying in a state for a while
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom.current().nextInt(100, 500));
	}
}