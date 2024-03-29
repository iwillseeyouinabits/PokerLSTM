package com.codebind;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.tongfei.progressbar.ProgressBar;

public class Runner {

	int numInputs = 8;
	int numOutputs = 5;

	public void run(String[] args) throws CloneNotSupportedException, IOException, InterruptedException {
		if (args.length == 0) {
			runForGen(100, 5100, 20, 5000, 4, true, 5);
		} else {
			runForGen(Integer.parseInt(args[0]), // numGen
					Integer.parseInt(args[1]), // numData
					Integer.parseInt(args[2]), // numThreads
					Integer.parseInt(args[3]), // gameStart
					Integer.parseInt(args[4]), // botInd
					Boolean.parseBoolean(args[5]), // getData
					Integer.parseInt(args[6]) // botNum
			);
		}
	}

	public LSTM_Bot[] getBrains(int numBrains, int genStart) throws IOException {
		LSTM_Bot[] brains = new LSTM_Bot[numBrains];
		int brainsAdded = 0;
		for (int i = genStart - 1; i >= 0 && brainsAdded < brains.length; i--) {
			System.out.println("brain" + i);
			brains[brainsAdded] = new LSTM_Bot(new File("brain" + i), numInputs, numOutputs);
			brainsAdded++;
		}
		for (; brainsAdded < brains.length; brainsAdded++) {
			System.out.println("New Brain");
			brains[brainsAdded] = new LSTM_Bot(numInputs, numOutputs);
		}
		System.out.println("");
		return brains;
	}

	public LSTM_Bot[] flip(LSTM_Bot[] brains) {
		LSTM_Bot[] tempBrains = new LSTM_Bot[brains.length];
		for (int i = 0; i < brains.length; i++) {
			tempBrains[i] = brains[brains.length - 1 - i];
		}
		brains = tempBrains;
		return tempBrains;
	}

	public void runForGen(int numGen, int numData, int numThreads, int gameStart, int botInd, boolean getData,
			int numBots) throws CloneNotSupportedException, IOException, InterruptedException {
		for (; botInd < numBots; botInd++) {
			LSTM_Bot[] brains = this.getBrains(botInd + 1, botInd);
			brains = this.flip(brains);
			if (getData) {
				genData(brains, numData, numThreads, 0, gameStart, botInd);
			} else {
				getData = true;
			}
			brains[0].train(numData, 0, botInd);
			brains[0].modelToFile(0 + botInd);
			gameStart = 0;

		}
	}

	public void genData(LSTM_Bot[] brains, int numData, int numTreadsRunConcurently, int gen, int gameStart, int botItr)
			throws CloneNotSupportedException {
		long start = new Date().getTime();
		ProgressBar pb = new ProgressBar("Generate Data Progress: gen - " + gen, (numData - gameStart) * (1));
		ExecutorService executor = Executors.newWorkStealingPool(numTreadsRunConcurently);
		for (int i = gameStart; i < numData; i++) {
			executor.execute(new GetDataThreaded(i, gen, botItr, brains[0].getCopy(), brains[brains.length-1].getCopy(), pb));
		}
		while (pb.getCurrent() != (numData - gameStart) * (1)) {
		}
		pb.close();
	}
}
