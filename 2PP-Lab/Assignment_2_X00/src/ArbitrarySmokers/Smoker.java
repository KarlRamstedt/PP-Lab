package ArbitrarySmokers;

import java.util.concurrent.ThreadLocalRandom;

import se.his.iit.it325g.common.AndrewsProcess;

public class Smoker implements Runnable {
	private int tobacco = 0;
	private int paper = 0;
	private int matches = 0;

	@Override
	public void run() {
		while (true) {

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
