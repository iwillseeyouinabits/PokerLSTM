package com.codebind;

import java.io.IOException;

import me.tongfei.progressbar.ProgressBar;

public class GetDataThreaded implements Runnable {

	int file;
	int gen;
	int botItr;
	LSTM_Bot brain1;
	LSTM_Bot brain2;
	ProgressBar pb;

	public GetDataThreaded(int file,  int gen,  int botItr, LSTM_Bot brain1, LSTM_Bot brain2, ProgressBar pb) {
		this.file = file;
		this.brain1 = brain1;
		this.brain2 = brain2;
		this.pb = pb;
		this.gen = gen;
		this.botItr = botItr;
	}

	public void run() {
			try {
//				System.out.println("start");
				new UnitPokerRecur().playGame(brain1, brain2, botItr, file, gen);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		pb.step();
	}
}
