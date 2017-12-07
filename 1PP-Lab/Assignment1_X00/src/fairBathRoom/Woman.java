package fairBathRoom;
import java.util.concurrent.ThreadLocalRandom;

import se.his.iit.it325g.common.AndrewsProcess;

public class Woman implements Runnable {

	boolean inQ = false;
	
	@Override
	public void run() {
		GlobalState.mutex.P();
		GlobalState.womQ.add(this);
		GlobalState.mutex.V();
		
		while (true){				//poop loop
			GlobalState.mutex.P();
			if ((GlobalState.men > 0 || GlobalState.menTurn) && !inQ){
				GlobalState.delayedWom++;
				GlobalState.mutex.V();
				GlobalState.semWom.P();
			}
			GlobalState.increaseTurn();
			if (GlobalState.wom >= 4 || GlobalState.womQ.peek() != this){	
				if (inQ){
					inQ = false;
					GlobalState.quedWom--;
				}
				GlobalState.wom++;
				GlobalState.womQ.poll(); //remove from queue
				GlobalState.signal(); //release to next wom
				
				doThings(); //poop
				System.out.println(getState());
				
				GlobalState.mutex.P();
				GlobalState.wom--;
				GlobalState.womQ.add(this); //re-enter queue
				GlobalState.signal();
				
				doThings(); //eat a burger to make more poop
			} else {
				if (!inQ){
					GlobalState.quedWom++;
					inQ = true;
				}
				GlobalState.mutex.V();
			}
		}
	}

	public static String getState(){ //printout the global state
		return "W " + AndrewsProcess.currentAndrewsProcessId()
				+ "  in CS, state: nm:" + GlobalState.men +
				" nw:" + GlobalState.wom +
				" dm:" + GlobalState.delayedMen +
				" dw:" + GlobalState.delayedWom;
	}
	private void doThings(){ //represents processes from staying in a state for a while
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom.current().nextInt(100, 500));
	}
}