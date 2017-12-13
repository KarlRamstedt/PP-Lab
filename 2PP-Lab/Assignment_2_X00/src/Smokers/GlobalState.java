package Smokers;

import java.util.ArrayList;

import se.his.iit.it325g.common.AndrewsProcess;
import se.his.iit.it325g.common.AsynchronousChan;

public class GlobalState {
	private final static int numberOfSmokers = 1000;
	
	public volatile static AsynchronousChan<Integer> smokChan1 = new AsynchronousChan<Integer>();
	public volatile static AsynchronousChan<Integer> smokChan2 = new AsynchronousChan<Integer>();
	public volatile static AsynchronousChan<Integer> smokChan3 = new AsynchronousChan<Integer>();
	public volatile static AsynchronousChan<Integer> agentChan = new AsynchronousChan<Integer>();
	
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