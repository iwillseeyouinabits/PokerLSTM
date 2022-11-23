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
//		LSTM_Bot brain1 = new LSTM_Bot(numInputs, numOutputs);
//		LSTM_Bot brain2 = new LSTM_Bot(new File("brain" + 1), numInputs, numOutputs);
//		brain1.train(1900, 0, 0, 2);
//		brain1.modelToFile(777);
//		brain1.validate(1900, 1999, 0, 2);
//		brain2.validate(1900, 1999, 0, 2);
		new UnitPokerGame(false).playNGames(1000, "brain777", "brain0", numInputs, numOutputs);
//		double rate1 = new Game(new Player(2000, "p1", brain1), new Player(2000, "p2", brain2)).getWinRate("brain333", "brain0", 100, 50, false);	
//		double rate2 = new Game(new Player(2000, "p2", brain2), new Player(2000, "p1", brain1)).getWinRate("brain0", "brain333", 100, 250, false);
//		System.out.println(rate1 + " !!!!");
//		System.out.println(rate2 + " !!!!");
	}
}
