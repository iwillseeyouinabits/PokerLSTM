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
//		Player p1 = new Player(2000, "p1", new LSTM_Bot(new File("brain" + 4), numInputs, numOutputs));
//		Player p2 = new Player(2000, "p2", new LSTM_Bot(new File("brain" + 3), numInputs, numOutputs));
//		new Game(p1, p2).getWinRate(500, 500, true);
	}
}
