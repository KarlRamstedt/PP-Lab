package bathRoom;
import java.util.concurrent.ThreadLocalRandom;

import se.his.iit.it325g.common.AndrewsProcess;

public class Woman implements Runnable {

	@Override
	public void run() {
		while (true) {
			// fill functionality here
			
			GlobalState.mutex.P();
			if(GlobalState.numberOfMenInCS > 0){
				GlobalState.numberOfDelayedWomen++;
				
				GlobalState.mutex.V();
				GlobalState.womSem.P();
			}
			GlobalState.numberOfWomenInCS++;
			GlobalState.signal();
			
			doThings();
			System.out.println(getState());
			
			GlobalState.mutex.P();
			GlobalState.numberOfWomenInCS--;
			GlobalState.signal();	
		
			doThings();

		}
	}

	// printout on the global state
	public static String getState() {
		return "W " + AndrewsProcess.currentAndrewsProcessId()
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
