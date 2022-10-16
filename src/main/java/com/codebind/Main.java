package com.codebind;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import me.tongfei.progressbar.ProgressBar;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException, IOException, InterruptedException {
//		runForGen(100, 1000);
		runForGen(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
	}

	public static void runForGen(int numGen, int numData, int numThreads) throws CloneNotSupportedException, IOException, InterruptedException {
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
				e.printStackTrace();
				getData = false;
				i--;
			}
		}
	}

	public static void genData(LSTM_Bot brain1, LSTM_Bot brain2, int numData, int numTreadsRunConcurently)
			throws CloneNotSupportedException {
		long start = new Date().getTime();
		GetDataThreaded[] threads = new GetDataThreaded[numTreadsRunConcurently];
		ProgressBar pb = new ProgressBar("Generate Data Progress", numData);
		for (int i = 0; i < numData; i++) {
			int itr = 0;
			try {
				while (true) {
					itr++;
					if (itr == threads.length) {
						itr = 0;
					}
					if (threads[itr % threads.length] == null || !threads[itr % threads.length].isAlive()) {
						threads[itr % threads.length] = new GetDataThreaded(i, brain1.getCopy(), brain2.getCopy());
						threads[itr % threads.length].start();
						pb.step();
						break;
					}
				}
			} catch (Exception e) {
				System.out.println(i + " " + itr + " " + (itr % threads.length) + " "
						+ (((new Date().getTime() - start) / 1000.0) / 60.0));
				System.out.println("__________");
				i--;
			}
		}
		pb.close();
		System.out.print(((new Date().getTime() - start) / 1000) / 60);
	}

}
