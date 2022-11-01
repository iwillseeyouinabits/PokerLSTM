package com.codebind;

import java.util.ArrayList;

public class Player {

	double bankroll;
	String name;
	LSTM_Bot lstm;
	ArrayList<Double> bets, ranksHand, ranksTot, round, minBets, pots;

	public Player(double bankroll, String name, LSTM_Bot w) {
		this.bankroll = bankroll;
		this.name = name;
		this.lstm = w;
		this.bets = new ArrayList<Double>();
		this.ranksHand = new ArrayList<Double>();
		this.ranksTot = new ArrayList<Double>();
		this.round = new ArrayList<Double>();
		this.minBets = new ArrayList<Double>();
		this.pots = new ArrayList<Double>();
		this.bets.add(0.0);
		this.ranksHand.add(0.0);
		this.ranksTot.add(0.0);
		this.round.add(0.0);
		this.minBets.add(0.0);
		this.pots.add(0.0);
	}

	public Player(double bankroll, String name, LSTM_Bot w, ArrayList<Double> bets, ArrayList<Double> ranksTot,
			ArrayList<Double> ranksHand, ArrayList<Double> round, ArrayList<Double> minBets, ArrayList<Double> pots) {
		this.bankroll = bankroll;
		this.name = name;
		this.lstm = w;
		this.bets = bets;
		this.ranksTot = ranksTot;
		this.ranksHand = ranksHand;
		this.round = round;
		this.minBets = minBets;
		this.pots = pots;
	}

	public void reset(int bankroll) {
		this.bankroll = bankroll;
		this.bets = new ArrayList<Double>();
		this.ranksHand = new ArrayList<Double>();
		this.ranksTot = new ArrayList<Double>();
		this.round = new ArrayList<Double>();
		this.minBets = new ArrayList<Double>();
		this.pots = new ArrayList<Double>();
		this.bets.add(0.0);
		this.ranksHand.add(0.0);
		this.ranksTot.add(0.0);
		this.round.add(0.0);
		this.minBets.add(0.0);
		this.pots.add(0.0);
		this.lstm.reset();
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
		return new Player(bankroll, String.valueOf(name), lstm.getCopy(), (ArrayList<Double>) bets.clone(),
				(ArrayList<Double>) ranksTot.clone(), (ArrayList<Double>) ranksHand.clone(),
				(ArrayList<Double>) round.clone(), (ArrayList<Double>) minBets.clone(),
				(ArrayList<Double>) pots.clone());
	}

	public String toString() {
		return name + ": " + bankroll;
	}

	public ArrayList<Double> getBets() {
		return bets;
	}

	public ArrayList<Double> getRanksHand() {
		return ranksHand;
	}

	public ArrayList<Double> getRanksTot() {
		return ranksTot;
	}

	public ArrayList<Double> getRound() {
		return round;
	}

	public ArrayList<Double> getMinBets() {
		return minBets;
	}

	public ArrayList<Double> getPots() {
		return pots;
	}

	public float[] getInputs(Player p2) {
		return new Data().getData(this, p2);
	}

	public double filterBet(double pot, double minbet, double bet, double minraise) {
		if (bet > pot && bet <= bankroll && bet >= minbet + minraise)
			return bet;
		else if (bet <= pot && bet <= bankroll && bet >= minbet + minraise)
			return bet;
		else if (bet <= pot && bet <= bankroll && bet < minbet + minraise && bet >= minbet)
			return minbet;
		else if (bet >= bankroll)
			return bankroll;
		else
			return 0;
	}

	public double[] getBetOptions(double pot, double minbet) {
		double[] bets = new double[6];
		bets[0] = 0;
		bets[1] = minbet;
		for (int i = 2; i < 6; i++) {
			bets[i] = (Math.max(Math.min(this.getBankroll()-minbet, pot-minbet),0)*Math.pow(i/5.0, 2))+minbet;
		}
		for (int i = 0; i < bets.length; i++) {
			bets[i] = filterBet(pot, minbet, Math.floor(bets[i]), 1);
		}
		return bets;
	}

	public double makeBet(double pot, double minbet, double round, int[][] hand, Player player2) {
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
//		System.out.println();
//		System.out.println(maxInd);
//		System.out.println();
		double bet = 0;
		if (maxInd == 1)
			bet = minbet;
		else if (maxInd >= 2) {
			bet = (Math.max(Math.min(this.getBankroll()-minbet, pot-minbet),0)*Math.pow(maxInd/5.0, 2))+minbet;
		}

//		System.out.println();
//		System.out.println(bet + " " + minbet + " " + pot);
//		System.out.println();
		bet = filterBet(pot, minbet, Math.floor(bet), 1);

//		System.out.println();
//		System.out.println(bet);
//		System.out.println();
		this.round.add(round);
		bets.add(bet);
		minBets.add(minbet);
		pots.add(pot);
		ranksTot.add(new Rank(hand).getRank());
		ranksHand.add(new Rank(new int[][] { hand[hand.length - 1], hand[hand.length - 2] }).getRank());
		bankroll -= bet;
		return bet;
	}

	public double makeBet(double pot, double minbet, double round, int[][] hand, Player player2, double betIn) {
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
		double bet = filterBet(pot, minbet, Math.floor(betIn), 1);
		this.round.add(round);
		minBets.add(minbet);
		pots.add(pot);
		bets.add(bet);
		ranksTot.add(new Rank(hand).getRank());
		ranksHand.add(new Rank(new int[][] { hand[hand.length - 1], hand[hand.length - 2] }).getRank());
		bankroll -= bet;
		return bet;
	}
}
