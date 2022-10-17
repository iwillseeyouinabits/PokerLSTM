package com.codebind;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.tongfei.progressbar.ProgressBar;

public class Runner {

	public void run(String[] args) throws CloneNotSupportedException, IOException, InterruptedException {
		if (args.length == 0) {
			runForGen(100, 500, 5, 0, 0);
		} else {
			runForGen(
					Integer.parseInt(args[0]), //numGen
					Integer.parseInt(args[1]), //numData
					Integer.parseInt(args[2]), //numThreads
					Integer.parseInt(args[3]), //genStart
					Integer.parseInt(args[4]) //gameOn
					);
		}
	}

	public void runForGen(int numGen, int numData, int numThreads, int genStart, int gameOn)
			throws CloneNotSupportedException, IOException, InterruptedException {
		LSTM_Bot brain1;
		LSTM_Bot brain2;
		if (genStart == 0) {
			brain1 = new LSTM_Bot(7, 10);
			brain2 = new LSTM_Bot(7, 10);
		} else if (genStart == 1) {
			brain1 = new LSTM_Bot(7, 10);
			brain2 = new LSTM_Bot(new File("brain" + genStart), 7, 10);
		} else {
			brain1 = new LSTM_Bot(new File("brain" + (genStart - 1)), 7, 10);
			brain2 = new LSTM_Bot(new File("brain" + genStart), 7, 10);
		}
		boolean getData = true;
		for (int i = gameOn; i < numGen; i++) {
			if (getData) {
				genData(brain1, brain2, numData, numThreads, i);
			} else {
				getData = true;
			}
			try {
				brain1.train(numData, i);
				brain1.modelToFile(i + 1);
				LSTM_Bot tempBrain = brain1.getCopy();
				brain1 = brain2.getCopy();
				brain2 = tempBrain;
			} catch (Exception e) {
				e.printStackTrace();
				getData = false;
				i--;
			}
		}
	}

	public void genData(LSTM_Bot brain1, LSTM_Bot brain2, int numData, int numTreadsRunConcurently, int gen)
			throws CloneNotSupportedException {
		long start = new Date().getTime();
		ProgressBar pb = new ProgressBar("Generate Data Progress", numData);
		ExecutorService executor = Executors.newWorkStealingPool(numTreadsRunConcurently);
		for (int i = 0; i < numData; i++) {
			executor.execute(new GetDataThreaded(i, gen, brain1.getCopy(), brain2.getCopy(), pb));
		}
		while (pb.getCurrent() != numData) {
		}
		pb.close();
	}

}
