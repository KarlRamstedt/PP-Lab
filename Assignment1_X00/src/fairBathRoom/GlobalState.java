package fairBathRoom;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.*;
import se.his.iit.it325g.common.AndrewsProcess;
import se.his.iit.it325g.common.AndrewsSemaphore;

// author: <C1, Erik, Björn, Mattias, Karl>

public class GlobalState {
	public static AndrewsSemaphore mutex = new AndrewsSemaphore(1);
	public static AndrewsSemaphore semMen = new AndrewsSemaphore(0);
	public static AndrewsSemaphore semWom = new AndrewsSemaphore(0);

	// number of total processes
	public volatile static int totalWom = 6;
	public volatile static int totalMen = 6;
	// number of people in the bathroom
	public volatile static int wom = 0;
	public volatile static int men = 0;
	// number of people staying in the line
	public volatile static int delayedMen = 0;
	public volatile static int delayedWom = 0;
	// for fairness
	public volatile static int quedMen = 0;
	public volatile static int quedWom = 0;
	public volatile static int curTurn = 0;
	public volatile static int maxTurns = 5;
	public volatile static boolean menTurn = true;
	public volatile static Queue<Man> menQ = new LinkedList<Man>();
	public volatile static Queue<Woman> womQ = new LinkedList<Woman>();

	public static void main(String argv[]){ // main just starts the other processes
		ArrayList<AndrewsProcess> men = new ArrayList<>(); // contains man type processes
		ArrayList<AndrewsProcess> women = new ArrayList<>(); // contains woman type processes

		for (int i = 0; i < GlobalState.totalMen; i++) {
			AndrewsProcess man = new AndrewsProcess(new Man());
			man.start();
			men.add(man);
		}
		for (int i = 0; i < GlobalState.totalWom; i++) {
			AndrewsProcess woman = new AndrewsProcess(new Woman());
			woman.start();
			women.add(woman);
		}
	}
	public static void increaseTurn(){
		curTurn++;
		if(curTurn >= maxTurns){
			curTurn = 0;
			menTurn = !menTurn;
		}
	}
	public static void signal(){
		if(wom == 0 && delayedMen > 0 && menTurn){
			delayedMen--;
			semMen.V();
		} else if (men == 0 && delayedWom > 0 && !menTurn){
			delayedWom--;
			semWom.V();
		} else
			mutex.V();
	}
}