package FairSmokers;

import java.util.ArrayList;

import se.his.iit.it325g.common.AndrewsProcess;
import se.his.iit.it325g.common.AsynchronousChan;

public class GlobalState {
	private final static int numberOfSmokers = 6;
	
	public static void main(String[] args) {
		
		ArrayList<AndrewsProcess> smokers = new ArrayList<>();
		
		AndrewsProcess agent = new AndrewsProcess(new Agent());
		agent.start();
		
		for(int  i = 0; i < GlobalState.numberOfSmokers; i++) {
			AndrewsProcess smoker = new AndrewsProcess(new Smoker());
			smoker.start();
			smokers.add(smoker);
		}
	}
}
