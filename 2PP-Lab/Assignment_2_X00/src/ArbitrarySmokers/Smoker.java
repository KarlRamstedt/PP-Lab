package ArbitrarySmokers;


import java.util.concurrent.ThreadLocalRandom;

import se.his.iit.it325g.common.AndrewsProcess;

public class Smoker implements Runnable {
	private int tobacco = 0;
	private int paper = 0;
	private int matches = 0;

	@Override
	public void run() {
		int type = AndrewsProcess.currentRelativeToTypeAndrewsProcessId()%3;
		while (true) {
			switch(type){
			case 0:
				GlobalState.smokChan1.receive();
				break;
			case 1:
				GlobalState.smokChan2.receive();
				break;
			case 2:
				GlobalState.smokChan3.receive();
				break;
			}
			GlobalState.agentChan.send(1); //message agent that blaze has begun
			doThings(); //420 blazeit
			System.out.println("S" + AndrewsProcess.currentRelativeToTypeAndrewsProcessId()	+ " type " + AndrewsProcess.currentRelativeToTypeAndrewsProcessId()%3);
		}
	}

	public void getState() {
		System.out.println("S"
				+ AndrewsProcess.currentRelativeToTypeAndrewsProcessId()
				+ " tobacco:" + tobacco + " paper:" + paper + " matches:"
				+ matches);	
	}

	public void doThings() {
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom.current()
				.nextInt(500, 1000));
	}
}

