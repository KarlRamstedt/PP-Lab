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

			if(!GlobalState.menQ.contains(this)) {				// Om mannen ej finns i k�n, l�gg till.
				GlobalState.menQ.add(this);
			}
			
			GlobalState.mutex.P();								// Ber om tillg�ng till mutex.
			
			if(GlobalState.numberOfWomenInCS > 0) {				// Om kvinnor finns p� toaletten.
				GlobalState.numberOfDelayedMen++;
				
				GlobalState.mutex.V();							// L�s upp mutex.
				GlobalState.menSem.P();							// Blockera m�n.
			}
			GlobalState.mutex.V();								// ?? Uppl�sning mutex ??
			
			while(GlobalState.menQ.peek() != this) {			// Om inga kvinnor p� toaletten, kolla vem som �r f�rst i k�n.
				doThings();										// V�nta om ej f�rst.
			}
			
			GlobalState.mutex.P();												// Ber om tillg�ng till mutex.
			
			if(GlobalState.numberOfMenInCS < 4 && GlobalState.mansTurn) {		// Den som �r f�rst i k�n kollar om det �r f�rre �n 4 och m�nnens tur.
				
				GlobalState.increaseTurn();										// Talar om att en av de fem tickets tagits av en man.
				GlobalState.menQ.poll();										// F�rsta mannen i k�n tas bort.
				GlobalState.numberOfMenInCS++;									// Antal m�n i CS + 1.
				System.out.println(getState());
				
				GlobalState.mutex.V();											// Sl�pper mutex.
				
				doThings();
				
				GlobalState.mutex.P();											// Ber om tillg�ng till mutex.
				GlobalState.numberOfMenInCS--;									// Antal m�n i CS - 1.
				GlobalState.signal();											// Sl�pper mutex, om inte m�n == 0.
			
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