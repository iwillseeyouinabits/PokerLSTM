package com.codebind;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.tongfei.progressbar.ProgressBar;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException, IOException, InterruptedException {
		new Runner().run(args);
		
//		int numInputs = 7;
//		int numOutputs = 6;
//		LSTM_Bot brain1 = new LSTM_Bot(new File("brain" + 333), numInputs, numOutputs);
//		LSTM_Bot brain2 = new LSTM_Bot(new File("brain" + 0), numInputs, numOutputs);
////		brain1.validate(0, 29999, 0, 0);
//		brain1.train(29999, 18000, 0, 0);
//		brain1.modelToFile(333);
//		Player p1 = new Player(2000, "p1", brain1);
//		Player p2 = new Player(2000, "p2", brain2);
//		new Game(p1, p2).getWinRate(1000, Integer.MAX_VALUE, false);
	}
}
