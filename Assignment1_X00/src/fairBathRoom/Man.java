package fairBathRoom;
import java.util.concurrent.ThreadLocalRandom;

import se.his.iit.it325g.common.AndrewsProcess;

public class Man implements Runnable {

	boolean TooManyMen = false;
	
	@Override
	public void run() {
		GlobalState.mutex.P();
		GlobalState.menQ.add(this);
		GlobalState.mutex.V();
		
		while (true) {
			GlobalState.mutex.P();
			if(GlobalState.numberOfWomenInCS > 0){
				GlobalState.numberOfDelayedMen++;
				if(!GlobalState.menQ.contains(this))
					GlobalState.menQ.add(this);
				GlobalState.mutex.V();
				GlobalState.menSem.P();
			}
			while(GlobalState.menQ.peek() != this)
				doThings();
			if(GlobalState.numberOfMenInCS < 4 /*&& GlobalState.menQ.peek() == this*/ && GlobalState.mansTurn){
				GlobalState.menQ.poll();
				GlobalState.numberOfMenInCS++;
				GlobalState.increaseTurn();
				GlobalState.signal();
				
				doThings();
				System.out.println(getState());
				GlobalState.mutex.P();
				GlobalState.numberOfMenInCS--;
				GlobalState.signal();	
			//	System.out.println("Man queue size: " + GlobalState.menQ.size());
				//System.out.println("Woman queue size: " + GlobalState.womQ.size());
				doThings();
			} else {
				GlobalState.mutex.V();
			}
			System.out.println("M" + AndrewsProcess.currentAndrewsProcessId() + " is out.");
		}
	}

	public static String getState(){ // printout on the global state
		return "M " + AndrewsProcess.currentAndrewsProcessId()
				+ "  in CS, state: nm:" + GlobalState.numberOfMenInCS +
				" nw:" + GlobalState.numberOfWomenInCS +
				" dm:" + GlobalState.numberOfDelayedMen +
				" dw:" + GlobalState.numberOfDelayedWomen;
	}

	// represents that processes are staying in a state for a while
	private void doThings() {
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom.current()
				.nextInt(100, 500));
	}
}