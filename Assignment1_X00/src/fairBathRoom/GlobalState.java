package fairBathRoom;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.*;
import se.his.iit.it325g.common.AndrewsProcess;
import se.his.iit.it325g.common.AndrewsSemaphore;


/*
 * author: <C1, Erik, Björn, Mattias, Karl>
 */

public class GlobalState {
	/* insert necessary semaphores here! */
	public static AndrewsSemaphore mutex = new AndrewsSemaphore(1);
	public static AndrewsSemaphore menSem = new AndrewsSemaphore(0);
	public static AndrewsSemaphore womSem = new AndrewsSemaphore(0);

	// adjusts the number of total processes
	public volatile static int totalNumberOfWoman = 6;
	public volatile static int totalNumberOfMan = 0;
	// the number of people in the bathroom
	public volatile static int numberOfWomenInCS = 0;
	public volatile static int numberOfMenInCS = 0;
	// the number of people staying in the line
	public volatile static int numberOfDelayedMen = 0;
	public volatile static int numberOfDelayedWomen = 0;
	// no function, just indicates how many are waiting on others of same gender
	public volatile static int numberOfWaitingMen = 0;
	public volatile static int numberOfWaitingWomen = 0;
	
	public volatile static int maxTurns = 5;
	public volatile static int curTurn = 0;
	public volatile static boolean mansTurn = true;
	
	public volatile static Queue<Man> menQ = new LinkedList<Man>();
	public volatile static Queue<Woman> womQ = new LinkedList<Woman>();
	

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

		for (int i = 0; i < GlobalState.totalNumberOfWoman; i++) {
			AndrewsProcess woman = new AndrewsProcess(new Woman());
			woman.start();
			women.add(woman);
		}
	}
	

	
	public static void increaseTurn(){
		curTurn++;
		if(curTurn >= maxTurns){
			curTurn = 0;
			mansTurn = !mansTurn;
			
		}
	}
	//SIGNAL IS MAIN ISSUE - LOOK CLOSELY ON MUTEX, DOESN'T CONSIDER TURN
	public static void signal(){
		if(numberOfWomenInCS == 0 && numberOfDelayedMen > 0){
			numberOfDelayedMen--;
			menSem.V();
		} else if(numberOfMenInCS == 0 && numberOfDelayedWomen > 0){
			numberOfDelayedWomen--;
			womSem.V();
		} else
			mutex.V();
	}
}