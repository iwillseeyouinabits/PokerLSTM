package com.codebind;

import java.io.IOException;

public class GetDataThreaded extends Thread{
	
	int file;
	LSTM_Bot brain1;
	LSTM_Bot brain2;
	
	public GetDataThreaded(int file, LSTM_Bot brain1, LSTM_Bot brain2) {
		this.file = file;
		this.brain1 = brain1;
		this.brain2 = brain2;
	}
	
	public void run() {
		 try {
			new UnitPoker().playGame(brain1, brain2, file);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
