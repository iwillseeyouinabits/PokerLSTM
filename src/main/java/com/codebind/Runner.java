package com.codebind;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.tongfei.progressbar.ProgressBar;

public class Runner {

	int numInputs = 7;
	int numOutputs = 6;

	public void run(String[] args) throws CloneNotSupportedException, IOException, InterruptedException {
		if (args.length == 0) {
			runForGen(100, 2, 5, 0, 0, true, 3);
		} else {
			runForGen(Integer.parseInt(args[0]), // numGen
					Integer.parseInt(args[1]), // numData
					Integer.parseInt(args[2]), // numThreads
					Integer.parseInt(args[3]), // genStart
					Integer.parseInt(args[4]), // botInd
					Boolean.parseBoolean(args[5]), // getData
					Integer.parseInt(args[6]) // botNum
			);
		}
	}

	public LSTM_Bot[] getBrains(int numBrains, int genStart) {
		LSTM_Bot[] brains = new LSTM_Bot[numBrains];
		int brainsAdded = 0;
		for (int i = genStart - 1; i >= 0 && brainsAdded < brains.length; i--) {
			try {
				brains[brainsAdded] = new LSTM_Bot(new File("brain" + i), numInputs, numOutputs);
			} catch (IOException e) {
				e.printStackTrace();
			}
			brainsAdded++;
		}
		for (; brainsAdded < brains.length; brainsAdded++) {
			brains[brainsAdded] = new LSTM_Bot(numInputs, numOutputs);
		}
		return brains;
	}

	public LSTM_Bot[] rotate(LSTM_Bot[] brains) {
		LSTM_Bot b0 = brains[0];
		for (int i = 1; i < brains.length; i++) {
			brains[i - 1] = brains[i];
		}
		brains[brains.length - 1] = b0;
		return brains;
	}

	public void runForGen(int numGen, int numData, int numThreads, int genStart, int botInd, boolean getData,
			int numBots) throws CloneNotSupportedException, IOException, InterruptedException {
		for (int i = genStart; i < numGen; i += numBots) {
			for (botInd = 0; botInd < numBots; botInd++) {
				LSTM_Bot[] brains = this.getBrains(numBots, i+botInd);
				for (int ind = 0; ind < botInd; ind++) {
					brains = this.rotate(brains);
				}
				if (getData) {
					genData(brains, numData, numThreads, i, 0, botInd);
				} else {
					getData = true;
				}
				try {
					brains[0].train(numData, i, botInd);
					brains[0].modelToFile(i + botInd);
				} catch (Exception e) {
					e.printStackTrace();
					getData = false;
					botInd--;
				}
			}
		}
	}

	public void genData(LSTM_Bot[] brains, int numData, int numTreadsRunConcurently, int gen, int gameStart, int botItr)
			throws CloneNotSupportedException {
		long start = new Date().getTime();
		ProgressBar pb = new ProgressBar("Generate Data Progress: gen - " + gen,
				(numData - gameStart) * (brains.length - 1));
		ExecutorService executor = Executors.newWorkStealingPool(numTreadsRunConcurently);
		for (int bItr = 1; bItr < brains.length; bItr++) {
			for (int i = gameStart; i < numData; i++) {
				executor.execute(new GetDataThreaded(((bItr-1)*(numData-gameStart))+i, gen, botItr, brains[0].getCopy(), brains[bItr].getCopy(), pb));
			}
		}
		while (pb.getCurrent() != (numData - gameStart) * (brains.length - 1)) {
		}
		pb.close();
	}
}
