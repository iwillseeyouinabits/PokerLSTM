package com.codebind;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.tongfei.progressbar.ProgressBar;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException, IOException, InterruptedException {
//		runForGen(100, 500, 5);
		runForGen(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
	}

	public static void runForGen(int numGen, int numData, int numThreads)
			throws CloneNotSupportedException, IOException, InterruptedException {
		LSTM_Bot brain1 = new LSTM_Bot(3, 10);
		LSTM_Bot brain2 = new LSTM_Bot(3, 10);
		boolean getData = true;
		for (int i = 0; i < numGen; i++) {
			if (getData) {
				genData(brain1, brain2, numData, numThreads);
			} else {
				getData = true;
			}
			try {
				brain1.train(numData);
				brain1.modelToFile(i);
				LSTM_Bot tempBrain = brain1.getCopy();
				brain1 = brain2.getCopy();
				brain2 = tempBrain;
			} catch (Exception e) {
				getData = false;
				i--;
			}
		}
	}

	public static void genData(LSTM_Bot brain1, LSTM_Bot brain2, int numData, int numTreadsRunConcurently)
			throws CloneNotSupportedException {
		long start = new Date().getTime();
		ProgressBar pb = new ProgressBar("Generate Data Progress", numData);
		ExecutorService executor = Executors.newFixedThreadPool(numTreadsRunConcurently);
		for (int i = 0; i < numData; i++) {
			executor.execute(new GetDataThreaded(i, brain1.getCopy(), brain2.getCopy(), pb));
		}
		while(pb.getCurrent() != numData) {
		}
		pb.close();
//		System.out.print(((new Date().getTime() - start) / 1000) / 60);
	}

}
