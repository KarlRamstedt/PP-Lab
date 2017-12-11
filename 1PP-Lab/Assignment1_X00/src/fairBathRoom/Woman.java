package fairBathRoom;
import java.util.concurrent.ThreadLocalRandom;

import se.his.iit.it325g.common.AndrewsProcess;

public class Woman implements Runnable {
	
	boolean TooManyWomen = false;
	
	@Override
	public void run() {
		GlobalState.mutex.P();
		GlobalState.womQ.add(this);
		GlobalState.mutex.V();
		
		while (true) {
			
			if(!GlobalState.womQ.contains(this)) {
				GlobalState.womQ.add(this);
			}
			
			GlobalState.mutex.P();
			
			if(GlobalState.numberOfMenInCS > 0) {
				GlobalState.numberOfDelayedWomen++;

				GlobalState.mutex.V();
				GlobalState.womSem.P();
			}
			GlobalState.mutex.V();
			
			while(GlobalState.womQ.peek() != this) {
				doThings();
			}
			
			GlobalState.mutex.P();
			
			if(GlobalState.numberOfWomenInCS < 4 && !GlobalState.mansTurn){
				
				GlobalState.increaseTurn();
				GlobalState.womQ.poll();
				GlobalState.numberOfWomenInCS++;
				System.out.println(getState());
				
				GlobalState.mutex.V();
		
				doThings();
		
				GlobalState.mutex.P();
				GlobalState.numberOfWomenInCS--;
				GlobalState.signal();
				
				doThings();
				
			} else {
				GlobalState.signal();
			}
		}
	}

	public static String getState(){ // printout on the global state
		return "W " + AndrewsProcess.currentAndrewsProcessId()
				+ "  in CS, state: nm:" + GlobalState.numberOfMenInCS +
				" nw:" + GlobalState.numberOfWomenInCS +
				" dm:" + GlobalState.numberOfDelayedMen +
				" dw:" + GlobalState.numberOfDelayedWomen;
	}

	// represents that processes are staying in a state for a while
	private void doThings() {
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom.current().nextInt(100, 500));
	}
}