package FairSmokers;

import java.util.concurrent.ThreadLocalRandom;
import se.his.iit.it325g.common.AndrewsProcess;

public class Agent implements Runnable {
	private int tobacco = 0; //How many of each type of smoker that has smoked
	private int paper = 0;
	private int matches = 0;

	
	@Override
	public void run(){	
		while (true) {
			int[] smokChanList = new int[2];
			smokChanList = GlobalState.queChan.receive();
			switch(smokChanList[1]){
				case 0:
					GlobalState.smokChans.get(smokChanList[0]).send(smokChanList[1]); //send paper and matches
					tobacco++;
					break;
				case 1:
					GlobalState.smokChans.get(smokChanList[0]).send(smokChanList[1]); //send tobacco and matches
					paper++;
					break;
				case 2: 
					GlobalState.smokChans.get(smokChanList[0]).send(smokChanList[1]); //send tobacco and paper
					matches++;
					break;
			}
		}
	}
	
	public void getState(){
		System.out.println("A - tobacco:" + tobacco + " paper:" + paper
				+ " matches:" + matches);
	}
	
	public void doThings(){
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom
				.current().nextInt(500, 1000));	
	}
}