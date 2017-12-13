package Smokers;

import java.util.concurrent.ThreadLocalRandom;
import se.his.iit.it325g.common.AndrewsProcess;

public class Agent implements Runnable { // tobacco = 0, paper = 1, matches = 2 (ID numbers)
	@Override
	public void run(){
		while (true){
			SendASenseOfPrideAndAccomplishment(3); //RANDOM PREVENTS FAIRNESS
			GlobalState.agentChan.receive();
			//FAIRNESS NOT POSSIBLE SEQUENTIALLY WHEN SERVER/CLIENT, DO TIME-BASED
			}
	}
	
	void SendASenseOfPrideAndAccomplishment(int max){ //Max = number of processes
		int randomNum = ThreadLocalRandom.current().nextInt(0, max);
		switch (randomNum){
		case 0: //tobacco
			randomNum = ThreadLocalRandom.current().nextInt(1, max); //max - 1 is max number returned from function
			if (randomNum == 1){
				GlobalState.smokChan3.send(1); //tobacco + paper, missing matches
			} else
				GlobalState.smokChan2.send(1); //tobacco + matches, missing paper
			break;
		case 1: //paper
			randomNum = ThreadLocalRandom.current().nextInt(1, max);
			if (randomNum == 1){
				GlobalState.smokChan3.send(1); //tobacco + paper, missing matches
			} else
				GlobalState.smokChan1.send(1); //matches + paper, missing tobacco
			break;
		case 2: //matches
			randomNum = ThreadLocalRandom.current().nextInt(1, max);
			if (randomNum == 1){
				GlobalState.smokChan2.send(1); //tobacco + matches, missing paper
			} else
				GlobalState.smokChan1.send(1); //matches + paper, missing tobacco
			break;
		}
	}
	
	public void doThings(){
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom
				.current().nextInt(500, 1000));	
	}
	
//	public void getState(){
//		System.out.println("A - tobacco:" + tobacco + " paper:" + paper
//				+ " matches:" + matches);
//	}
}