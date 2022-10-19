package com.codebind;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.tongfei.progressbar.ProgressBar;

public class Runner {
	
	int numInputs = 7;

	public void run(String[] args) throws CloneNotSupportedException, IOException, InterruptedException {
		if (args.length == 0) {
			runForGen(100, 2000, 5, 0, 0, true);
		} else {
			runForGen(
					Integer.parseInt(args[0]), //numGen
					Integer.parseInt(args[1]), //numData
					Integer.parseInt(args[2]), //numThreads
					Integer.parseInt(args[3]), //genStart
					Integer.parseInt(args[4]), //gameOn
					Boolean.parseBoolean(args[5]) //getData
					);
		}
	}

	public void runForGen(int numGen, int numData, int numThreads, int genStart, int gameOn, boolean getData)
			throws CloneNotSupportedException, IOException, InterruptedException {
		LSTM_Bot brain1;
		LSTM_Bot brain2;
		if (genStart == 0) {
			brain1 = new LSTM_Bot(numInputs, 10);
			brain2 = new LSTM_Bot(numInputs, 10);
		} else if (genStart == 1) {
			brain1 = new LSTM_Bot(numInputs, 10);
			brain2 = new LSTM_Bot(new File("brain" + (genStart - 1)), numInputs, 10);
		} else {
			brain1 = new LSTM_Bot(new File("brain" + (genStart - 2)), numInputs, 10);
			brain2 = new LSTM_Bot(new File("brain" + (genStart - 1)), numInputs, 10);
		}
		for (int i = genStart; i < numGen; i++) {
			if (getData) {
				genData(brain1, brain2, numData, numThreads, i, gameOn);
			} else {
				getData = true;
			}
			try {
				brain1.train(numData, i);
				brain1.modelToFile(i);
				LSTM_Bot tempBrain = brain1;
				brain1 = brain2;
				brain2 = tempBrain;
			} catch (Exception e) {
				e.printStackTrace();
				getData = false;
				i--;
			}
		}
	}

	public void genData(LSTM_Bot brain1, LSTM_Bot brain2, int numData, int numTreadsRunConcurently, int gen, int gameStart)
			throws CloneNotSupportedException {
		long start = new Date().getTime();
		ProgressBar pb = new ProgressBar("Generate Data Progress: gen - " + gen, numData-gameStart);
		ExecutorService executor = Executors.newWorkStealingPool(numTreadsRunConcurently);
		for (int i = gameStart; i < numData; i++) {
			executor.execute(new GetDataThreaded(i, gen, brain1.getCopy(), brain2.getCopy(), pb));
		}
		while (pb.getCurrent() != numData) {
		}
		pb.close();
	}

}
