package com.codebind;

import java.io.IOException;

import me.tongfei.progressbar.ProgressBar;

public class GetDataThreaded implements Runnable {

	int file;
	LSTM_Bot brain1;
	LSTM_Bot brain2;
	ProgressBar pb;

	public GetDataThreaded(int file, LSTM_Bot brain1, LSTM_Bot brain2, ProgressBar pb) {
		this.file = file;
		this.brain1 = brain1;
		this.brain2 = brain2;
		this.pb = pb;
	}

	public void run() {
		try {
			new UnitPoker().playGame(brain1, brain2, file);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pb.step();
	}
}
