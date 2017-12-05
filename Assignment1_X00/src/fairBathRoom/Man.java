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

			if(!GlobalState.menQ.contains(this)) {				// Om mannen ej finns i kön, lägg till.
				GlobalState.menQ.add(this);
			}
			
			GlobalState.mutex.P();								// Ber om tillgång till mutex.
			
			if(GlobalState.numberOfWomenInCS > 0) {				// Om kvinnor finns på toaletten.
				GlobalState.numberOfDelayedMen++;
				
				GlobalState.mutex.V();							// Lås upp mutex.
				GlobalState.menSem.P();							// Blockera män.
			}
			GlobalState.mutex.V();								// ?? Upplåsning mutex ??
			
			while(GlobalState.menQ.peek() != this) {			// Om inga kvinnor på toaletten, kolla vem som är först i kön.
				doThings();										// Vänta om ej först.
			}
			
			GlobalState.mutex.P();												// Ber om tillgång till mutex.
			
			if(GlobalState.numberOfMenInCS < 4 && GlobalState.mansTurn) {		// Den som är först i kön kollar om det är färre än 4 och männens tur.
				
				GlobalState.increaseTurn();										// Talar om att en av de fem tickets tagits av en man.
				GlobalState.menQ.poll();										// Första mannen i kön tas bort.
				GlobalState.numberOfMenInCS++;									// Antal män i CS + 1.
				System.out.println(getState());
				
				GlobalState.mutex.V();											// Släpper mutex.
				
				doThings();
				
				GlobalState.mutex.P();											// Ber om tillgång till mutex.
				GlobalState.numberOfMenInCS--;									// Antal män i CS - 1.
				GlobalState.signal();											// Släpper mutex, om inte män == 0.
			
				doThings();
				
			} else {
				GlobalState.signal();
			}
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
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom.current().nextInt(100, 500));
	}
}