package com.codebind;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException, IOException, InterruptedException {
		LSTM_Bot brain1 = new LSTM_Bot(3, 10);
		LSTM_Bot brain2 = new LSTM_Bot(3, 10);
		genData(brain1, brain2);
		brain1.modelToFile(0);
		brain1.train(1000);
		brain1.modelToFile(1);
	}
	
	public static void genData(LSTM_Bot brain1, LSTM_Bot brain2) {
		long start = new Date().getTime();
		GetDataThreaded[] threads = new GetDataThreaded[20];
		for (int i = 0; i < 1000; i++) {
			int itr = 0;
			while(true) {
				itr++;
				if(threads[itr%threads.length] == null || !threads[itr%threads.length].isAlive()) {
					threads[itr%threads.length] = new GetDataThreaded(i, brain1, brain2);
					threads[itr%threads.length].start();
					break;
				}
			}
		}
		System.out.print(((new Date().getTime()-start)/1000)/60);
	}

}
