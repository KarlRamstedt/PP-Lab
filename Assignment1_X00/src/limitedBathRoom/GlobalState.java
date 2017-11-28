package limitedBathRoom;

import java.security.KeyStore.Entry;
import java.util.ArrayList;

import se.his.iit.it325g.common.AndrewsProcess;
import se.his.iit.it325g.common.AndrewsSemaphore;

/*
 * author: <C1, Erik, Björn, Mattias, Karl>
 */

public class GlobalState {
	/* insert necessary semaphores here! */
	public static AndrewsSemaphore mutex = new AndrewsSemaphore(1);
	public static AndrewsSemaphore mutex2 = new AndrewsSemaphore(2);
	public static AndrewsSemaphore menSem = new AndrewsSemaphore(0);
	public static AndrewsSemaphore womSem = new AndrewsSemaphore(0);
	public static AndrewsSemaphore menLimit = new AndrewsSemaphore(4);
	public static AndrewsSemaphore womLimit = new AndrewsSemaphore(4);

	// adjusts the number of total processes
	public volatile static int totalNumberOfWoman = 8;
	public volatile static int totalNumberOfMan = 8;
	// the number of people in the bathroom
	public volatile static int numberOfWomenInCS = 0;
	public volatile static int numberOfMenInCS = 0;
	// the number of people staying in the line
	public volatile static int numberOfDelayedMen = 0;
	public volatile static int numberOfDelayedWomen = 0;

	// The main is only present to start the processes
	public static void main(String argv[]) {
		// contains man type processes
		ArrayList<AndrewsProcess> men = new ArrayList<>();
		// contains woman type processes
		ArrayList<AndrewsProcess> women = new ArrayList<>();

		for (int i = 0; i < GlobalState.totalNumberOfMan; i++) {
			AndrewsProcess man = new AndrewsProcess(new Man());
			man.start();
			men.add(man);
		}

		/*for (int i = 0; i < GlobalState.totalNumberOfWoman; i++) {
			AndrewsProcess woman = new AndrewsProcess(new Woman());
			woman.start();
			women.add(woman);
		}*/
	}
	
	public static void signal(){
		if(numberOfWomenInCS == 0 && numberOfDelayedMen > 0){
			numberOfDelayedMen--;
			menSem.V();
		}
		if(numberOfMenInCS == 0 && numberOfDelayedWomen > 0){
			numberOfDelayedWomen--;
			womSem.V();
		}
		if((numberOfWomenInCS > 0 || numberOfDelayedMen == 0) && (numberOfMenInCS > 0  || numberOfDelayedWomen == 0))
			mutex.V();
	}
}
