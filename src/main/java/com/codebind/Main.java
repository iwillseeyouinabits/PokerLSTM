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
		
		int numInputs = 8;
		int numOutputs = 5;
//		LSTM_Bot brain1 = new LSTM_Bot(new File("brain" + 888), numInputs, numOutputs);
//		LSTM_Bot brain2 = new LSTM_Bot(new File("brain" + 3), numInputs, numOutputs);
//		brain1.train(new int[][] {{1, 3000}, {2, 2000}, {3, 5000}, {4, 5000}});
//		brain1.modelToFile(888);
//		brain1.validate(4999, 5099, 0, 4);
//		brain2.validate(4999, 5099, 0, 4);
		new UnitPokerGame(true).playNGamesHuman(5, "brain888", numInputs, numOutputs);
//		new Game(new Player(2000, "p1", brain1), new Player(2000, "p2", brain2)).getWinRateHuman("brain888", 1, Integer.MAX_VALUE, true);	
	}
}