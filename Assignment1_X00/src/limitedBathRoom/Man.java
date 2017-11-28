package limitedBathRoom;
import java.util.concurrent.ThreadLocalRandom;

import se.his.iit.it325g.common.AndrewsProcess;

public class Man implements Runnable {

	@Override
	public void run() {
		while (true) {
			// fill functionality here
			
			GlobalState.mutex.P();
			if(GlobalState.numberOfWomenInCS > 0){
				GlobalState.numberOfDelayedMen++;
				
				GlobalState.mutex.V();
				GlobalState.menSem.P();
			}
			GlobalState.numberOfMenInCS++;
			GlobalState.signal();
			//cs
			doThings();
			System.out.println(getState());
			GlobalState.mutex.P();
			GlobalState.numberOfMenInCS--;
			GlobalState.signal();	
		
			doThings();
		}
	}

	// printout on the global state
	public static String getState() {
		return "M " + AndrewsProcess.currentAndrewsProcessId()
				+ "  in CS, state: nm:" + GlobalState.numberOfMenInCS + " nw:"
				+ GlobalState.numberOfWomenInCS + " dm:"
				+ GlobalState.numberOfDelayedMen + " dw:"
				+ GlobalState.numberOfDelayedWomen;
	}

	// represents that processes are staying in a state for a while
	private void doThings() {
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom.current()
				.nextInt(100, 500));
	}
}