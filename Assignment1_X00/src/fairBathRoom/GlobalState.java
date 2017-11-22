package fairBathRoom;

import java.util.ArrayList;

import se.his.iit.it325g.common.AndrewsProcess;
import se.his.iit.it325g.common.AndrewsSemaphore;

/*
 * author: <group_ID, group members>
 */

public class GlobalState {
	/* insert necessary semaphores here! */

	// adjusts the number of total processes
	public volatile static int totalNumberOfWoman = 2;
	public volatile static int totalNumberOfMan = 2;
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

		for (int i = 0; i < GlobalState.totalNumberOfWoman; i++) {
			AndrewsProcess woman = new AndrewsProcess(new Woman());
			woman.start();
			women.add(woman);
		}
	}
}