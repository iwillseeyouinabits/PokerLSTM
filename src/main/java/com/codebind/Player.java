package com.codebind;
import java.util.ArrayList;

public class Player {
	
	double bankroll;
	String name;
	LSTM_Bot lstm;
	ArrayList<Double> bets, ranks;

	public Player (double bankroll, String name, LSTM_Bot w) {
		this.bankroll = bankroll;
		this.name = name;
		this.lstm = w;
		this.bets = new ArrayList<Double>();
		this.ranks = new ArrayList<Double>();
		bets.add(0.0);
		ranks.add(0.0);
	}

	public Player (double bankroll, String name, LSTM_Bot w, ArrayList<Double> bets, ArrayList<Double> ranks) {
		this.bankroll = bankroll;
		this.name = name;
		this.lstm = w;
		this.bets = bets;
		this.ranks = ranks;
	}


	public void reset(int bankroll) {
		this.bankroll = bankroll;
		this.bets = new ArrayList<Double>();
		this.ranks = new ArrayList<Double>();
	}

	public LSTM_Bot getBrain() {
		return lstm;
	}

	public void addToBankroll(double win) {
		this.bankroll += win;
	}

	public double takeFromBankroll(double take) {
		this.bankroll -= take;
		if (this.bankroll >= 0)
			return take;
		else {
			double dif = this.bankroll;
			this.bankroll = 0;
			return take + dif;
		}
	}

	public double getBankroll() {
		return this.bankroll;
	}

	public String getName() {
		return this.name;
	}

	public Player getCopy() throws CloneNotSupportedException {
		return new Player(bankroll, String.valueOf(name), lstm.getCopy(), (ArrayList<Double>) bets.clone(), (ArrayList<Double>) ranks.clone());
	}
	
	public String toString() {
		return  name + ": " + bankroll;
	}

	public ArrayList<Double> getBets() {
		return bets;
	}

	public ArrayList<Double> getRanks() {
		return ranks;
	}
	
	public float[] getInputs(Player p2) {
		return new Data().getData(this, p2);
	}

	public double filterBet(double pot, double minbet, double bet, double minraise) {
		if (bet > pot && bet <= bankroll && bet >= minbet+minraise)
			return bet;
		else if (bet <= pot && bet <= bankroll && bet >= minbet+minraise)
			return bet;
		else if (bet <= pot && bet <= bankroll && bet < minbet+minraise && bet >= minbet)
			return minbet;
		else if (bet >= bankroll)
			return bankroll;
		else
			return 0;
	}
	
	public double[] getBetOptions(double pot, double minbet) {
		double[] bets = new double[10];
		for (int i = 0; i < 9; i++) {
			int maxInd = i;
			double bet;
			if (maxInd == 0)
				bet = 0;
			else if (maxInd >= 6) {
				maxInd -= 5;
				bet = (Math.max(0, getBankroll()-pot)/4)*maxInd + pot;
			} else {
				maxInd -= 1;
				bet = Math.min(pot, getBankroll()-minbet)*(maxInd/5)+minbet;
			}
			bets[i] = bet;
		}
		for (int i = 0; i < bets.length; i++) {
			bets[i] = filterBet(pot, minbet, Math.floor(bets[i]), 10);
		}
		return bets;
	}

	public double makeBet(double pot, double minbet, int[][] hand, Player player2) {
		float[] inputs = getInputs(player2);
		float[] outputs = lstm.predict(inputs);
		int maxInd = -1;
		double maxOut = -1;
		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i] >= maxOut) {
				maxOut = outputs[i];
				maxInd = i;
			}
		}
		double bet;
		if (maxInd == 0)
			bet = 0;
		else if (maxInd >= 6) {
			maxInd -= 5;
			bet = (Math.max(0, getBankroll()-pot)/4)*maxInd + pot;
		}else {
			maxInd -= 1;
			bet = Math.min(pot, getBankroll()-minbet)*(maxInd/5)+minbet;
		}		
		bet = filterBet(pot, minbet, Math.floor(bet), 10);
		bets.add(bet);
		ranks.add(new Rank(hand).getRank());
		bankroll -= bet;
		return bet;
	}

	public double makeBet(double pot, double minbet, int[][] hand, Player player2, double betIn) {
		float[] inputs = getInputs(player2);
		float[] outputs = lstm.predict(inputs);
		int maxInd = -1;
		double maxOut = -1;
		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i] > maxOut) {
				maxOut = outputs[i];
				maxInd = i;
			}
		}
		double bet = filterBet(pot, minbet, Math.floor(betIn), 10);
		bets.add(bet);
		ranks.add(new Rank(hand).getRank());
		bankroll -= bet;
		return bet;
	}
}
