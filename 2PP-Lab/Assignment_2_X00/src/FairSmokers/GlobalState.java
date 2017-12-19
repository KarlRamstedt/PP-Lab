package FairSmokers;

import java.util.ArrayList;

import se.his.iit.it325g.common.AndrewsProcess;
import se.his.iit.it325g.common.AsynchronousChan;

public class GlobalState {
	public final static int numberOfSmokers = 2;
	
	public volatile static ArrayList<AsynchronousChan<Integer>> smokChans = new ArrayList<AsynchronousChan<Integer>>();
	public volatile static AsynchronousChan<int[]> queChan = new AsynchronousChan<int[]>();
	public volatile static AsynchronousChan<Integer> tableChan = new AsynchronousChan<Integer>();
	
	public static void main(String[] args) {
		
		ArrayList<AndrewsProcess> smokers = new ArrayList<>();
		
		AndrewsProcess agent = new AndrewsProcess(new Agent());
		agent.start();
		
		for(int  i = 0; i < GlobalState.numberOfSmokers; i++){
			if (i < 2){
				smokChans.add(new AsynchronousChan<Integer>());
				AndrewsProcess smoker = new AndrewsProcess(new Smoker(i, 0, smokChans.get(i)));
				smoker.start();
				smokers.add(smoker);
<<<<<<< HEAD
			}
			else if (i < 20){
=======
			} else if (i < 20){
>>>>>>> 8850de7ce468290457bc48b85188e12d01c83fb4
				smokChans.add(new AsynchronousChan<Integer>());
				AndrewsProcess smoker = new AndrewsProcess(new Smoker(i, 1, smokChans.get(i)));
				smoker.start();
				smokers.add(smoker);
			} else {
				smokChans.add(new AsynchronousChan<Integer>());
				AndrewsProcess smoker = new AndrewsProcess(new Smoker(i, 2, smokChans.get(i)));
				smoker.start();
				smokers.add(smoker);
			}
		}
	}
}