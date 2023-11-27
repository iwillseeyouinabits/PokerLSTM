package com.codebind;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PlayerReal extends Player {

	double bankroll;
	String name;
	LSTM_Bot lstm;
	ArrayList<Double> bets, ranksHand, ranksTot, round, minBets, pots;
	ArrayList<int[][]> hands = new ArrayList<int[][]>();


	public PlayerReal(double bankroll, String name) {
		super(bankroll, name, new LSTM_Bot(100, 100));
		this.bankroll = bankroll;
		this.name = name;
		this.bets = new ArrayList<Double>();
		this.ranksHand = new ArrayList<Double>();
		this.ranksTot = new ArrayList<Double>();
		this.round = new ArrayList<Double>();
		this.minBets = new ArrayList<Double>();
		this.pots = new ArrayList<Double>();
		this.hands = new ArrayList<int[][]>();
		this.bets.add(0.0);
		this.ranksHand.add(0.0);
		this.ranksTot.add(0.0);
		this.round.add(0.0);
		this.minBets.add(0.0);
		this.pots.add(0.0);
	}
	


	public PlayerReal(double bankroll, String name, LSTM_Bot w, ArrayList<Double> bets, ArrayList<Double> ranksTot,
			ArrayList<Double> ranksHand, ArrayList<Double> round, ArrayList<Double> minBets, ArrayList<Double> pots, ArrayList<int[][]> hands) {
		super(bankroll, name, new LSTM_Bot(100, 100));
		this.bankroll = bankroll;
		this.name = name;
		this.lstm = w;
		this.bets = bets;
		this.ranksTot = ranksTot;
		this.ranksHand = ranksHand;
		this.round = round;
		this.minBets = minBets;
		this.pots = pots;
		this.hands = hands;
	}


	public void reset(int bankroll) {
		this.bankroll = bankroll;
		this.bets = new ArrayList<Double>();
		this.ranksHand = new ArrayList<Double>();
		this.ranksTot = new ArrayList<Double>();
		this.round = new ArrayList<Double>();
		this.minBets = new ArrayList<Double>();
		this.pots = new ArrayList<Double>();
		this.hands = new ArrayList<int[][]>();
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

	public ArrayList<int[][]> getHands() {
		return hands;
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
		double[] bets = new double[5];
		bets[0] = 0;
		bets[1] = minbet;
		for (int i = 2; i <= 4; i++) {
			bets[i] = (Math.max(Math.min(getBankroll()-minbet, (pot*1.2)-minbet),0)*Math.pow(i/4.0, 3))+minbet;
		}
		for (int i = 0; i < bets.length; i++) {
			bets[i] = filterBet(pot, minbet, Math.floor(bets[i]), 1);
		}
		return bets;
	}
	
	public float[] commitForwardStep(double pot, double minbet, double round, int[][] hand, Player player2) {
		this.round.add(round);
		minBets.add(minbet);
		pots.add(pot);
		ranksTot.add(new Rank(hand).getRank());
		ranksHand.add(new Rank(new int[][] { hand[hand.length - 1], hand[hand.length - 2] }).getRank());
		hands.add(hand);
		return getInputs(player2);
		
	}
	
	


	public PlayerReal getCopy() throws CloneNotSupportedException {
		return new PlayerReal(bankroll, String.valueOf(name), lstm, (ArrayList<Double>) bets.clone(),
				(ArrayList<Double>) ranksTot.clone(), (ArrayList<Double>) ranksHand.clone(),
				(ArrayList<Double>) round.clone(), (ArrayList<Double>) minBets.clone(),
				(ArrayList<Double>) pots.clone(), (ArrayList<int[][]>) hands.clone());
	}
	
	public double makeBet(double pot, double minbet, double round, int[][] hand, Player player2) {
		this.round.add(round);
		minBets.add(minbet);
		pots.add(pot);
		ranksTot.add(new Rank(hand).getRank());
		ranksHand.add(new Rank(new int[][] { hand[hand.length - 1], hand[hand.length - 2] }).getRank());
		hands.add(hand);
		try {
			int[][] handPrint = new int[hand.length][];
			for(int i = 0; i < hand.length; i++) {
				handPrint[i] = hand[i];
			}
			new GUI().printHand(handPrint);
		} catch (FileNotFoundException e) {}
		System.out.println("Pot: " + pot);
		System.out.println("Your Bankroll: " + bankroll);
		System.out.println("Robot Bankroll: " + player2.getBankroll());
		for(int i = 0; i < 5; i++) {
			double bet = 0;
			if (i == 1) {
				bet = minbet;
			} else if (i >= 2) {
				bet = (Math.max(Math.min(getBankroll()-minbet, (pot*1.2)-minbet),0)*Math.pow(i/4.0, 3))+minbet;;
			}
			bet = filterBet(pot, minbet, Math.floor(bet), 1);
			System.out.println("Option " + i + ": " + bet);
		}
		int maxInd = new Scanner(System.in).nextInt();
		double bet = 0;
		if (maxInd == 1)
			bet = minbet;
		else if (maxInd >= 2) {
			bet = (Math.max(Math.min(getBankroll()-minbet, (pot*1.2)-minbet),0)*Math.pow(maxInd/4.0, 3))+minbet;;
		}
		bet = filterBet(pot, minbet, Math.floor(bet), 1);
		
		bets.add(bet);
		
		bankroll -= bet;
		return bet;
	}

	public double makeBet(double pot, double minbet, double round, int[][] hand, PlayerReal player2, double betIn) {
		this.round.add(round);
		minBets.add(minbet);
		pots.add(pot);
		ranksTot.add(new Rank(hand).getRank());
		ranksHand.add(new Rank(new int[][] { hand[hand.length - 1], hand[hand.length - 2] }).getRank());
		hands.add(hand);
		
		double bet = filterBet(pot, minbet, Math.floor(betIn), 1);
		
		bets.add(bet);
		
		
		bankroll -= bet;
		return bet;
	}
}
