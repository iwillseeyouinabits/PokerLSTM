package com.codebind;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.tongfei.progressbar.ProgressBar;

public class Main {

	public static void main(String[] args) throws Exception {
//		new Runner().run(args);
		
		int numInputs = 7;
		int numOutputs = 11;
		LSTM_Bot brain1 = new LSTM_Bot(new File("brain" + 333), numInputs, numOutputs);
		LSTM_Bot brain2 = new LSTM_Bot(new File("brain" + 0), numInputs, numOutputs);
//		brain1.train(80, 0, 0, 0);
//		brain1.modelToFile(333);
//		brain1.validate(0, 50, 0, 1);
//		brain2.validate(0, 50, 0, 1);
		new UnitPokerGame(false).playNGames(1000, brain1, brain2);
	}
}
