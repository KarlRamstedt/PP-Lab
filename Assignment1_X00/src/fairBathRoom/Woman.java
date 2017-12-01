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
			// fill functionality here
			
			GlobalState.mutex.P();
			
			if(GlobalState.numberOfMenInCS > 0){
				GlobalState.numberOfDelayedWomen++;
				if(!GlobalState.womQ.contains(this))
					GlobalState.womQ.add(this);
				GlobalState.mutex.V();
				GlobalState.womSem.P();
			}
			
			if(GlobalState.numberOfWomenInCS < 4){
				if (TooManyWomen){
					TooManyWomen = false;
					GlobalState.numberOfWaitingWomen--;
				}
				GlobalState.numberOfWomenInCS++;
				GlobalState.signal();
			
				doThings();
				System.out.println(getState());
				GlobalState.mutex.P();
				GlobalState.numberOfWomenInCS--;
				GlobalState.signal();
				
				doThings();
			} else {
				if (TooManyWomen == false){
					GlobalState.numberOfWaitingWomen++;
					TooManyWomen = true;
				}
				GlobalState.mutex.V();
			}
		}
	}

	public static String getState(){ // printout on the global state
		return "W " + AndrewsProcess.currentAndrewsProcessId()
				+ "  in CS, state: nm:" + GlobalState.numberOfMenInCS +
				" nw:" + GlobalState.numberOfWomenInCS +
				" dm:" + GlobalState.numberOfDelayedMen +
				" wm:" + GlobalState.numberOfWaitingMen +
				" dw:" + GlobalState.numberOfDelayedWomen +
				" ww:" + GlobalState.numberOfWaitingWomen;
	}

	// represents that processes are staying in a state for a while
	private void doThings() {
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom.current()
				.nextInt(100, 500));
	}
}