package FairSmokers;

import java.util.concurrent.ThreadLocalRandom;

import se.his.iit.it325g.common.AndrewsProcess;
import se.his.iit.it325g.common.AsynchronousChan;

public class Smoker implements Runnable {
	private int smokd = 0;
	private int id;
	private int type;
	private AsynchronousChan<Integer> myChan;
	
	public Smoker(int _id, int _type, AsynchronousChan<Integer> _myChan){
		id = _id;
		type = _type;
		myChan = _myChan;
	}
	@Override
	public void run(){
		//Tell agent type
		int[] temp = {id, type};
		
		while (true){
			GlobalState.queChan.send(temp); // Tell agent we want to smoke
			myChan.receive();
			GlobalState.tableChan.send(id); //message agent that blaze has begun
			doThings(); //420 blazeit
			smokd++;
			System.out.println("S" + AndrewsProcess.currentRelativeToTypeAndrewsProcessId()	+ " Type " + type + " Smokd " + smokd);
			doThings(); //While not smoking
		}
	}

	public void doThings() {
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom.current()
				.nextInt(500, 1000));
	}
}
