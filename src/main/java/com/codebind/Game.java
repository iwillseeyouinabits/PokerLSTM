package com.codebind;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Game {

	Player player1, player2;

	public Game(Player player1, Player player2) {
//		this.player1 = player1;
//		this.player2 = player2;
	}

	public double[] getNextBet(int round, Player player, Player p2, double pot, double betToCall, ArrayList<int[]> hand,
			boolean prnt) {
		double bet = player.makeBet(pot, betToCall, round, hand.toArray(new int[hand.size()][]), p2);
		if (bet < betToCall) {
			if (prnt)
				System.out.println(player + " - Folds " + betToCall + " " + bet);
			p2.addToBankroll(pot);
			return new double[] { 0, 0 };
		} else if (bet > betToCall && (p2.getBankroll() > bet - betToCall)) {
			if (prnt)
				System.out.println((player) + " - Raise " + (bet));
			return new double[] { bet, 2 };
		} else if (bet == betToCall || (player.getBankroll() + bet == 0)) {
			if (prnt)
				System.out.println((player) + " - Call");
			return new double[] { bet, 1 };
		} else if (p2.getBankroll() <= bet - betToCall && bet > betToCall) {
			player.addToBankroll(bet - betToCall - p2.getBankroll());
			if (prnt)
				System.out.println((player) + " - Pushes All In with " + (p2.getBankroll() + betToCall));
			return new double[] { p2.getBankroll() + betToCall, 2 };
		} else {
			System.out.println("Money On Table");
			return new double[0];
		}
	}

	public double[] getBets2(int round, boolean prnt, ArrayList<int[]> hand1, ArrayList<int[]> hand2, Player pl1,
			Player pl2, double pot) {
		double betToCall = 0;
//		if (round == 0) {
//			betToCall = 10;
//		}
		double raiseItr = 0;
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			boolean flop = i % 2 == 0;
			ArrayList<int[]> hand;
			Player player, p2;
			if (flop) {
				player = pl1;
				p2 = pl2;
				hand = hand1;
			} else {
				player = pl2;
				p2 = pl1;
				hand = hand2;
			}

			double[] play = new double[] { 0, 0 };
			if (raiseItr <= 3) {
				play = getNextBet(round, player, p2, pot, betToCall, hand, prnt);
			} else {
				p2.addToBankroll(pot);
				if (prnt)
					System.out.println(player + " - Folds; loss of " + pot);
			}
			double bet = play[0];
			int fcr = (int) play[1];
			pot += bet;
			if (fcr == 1 && i > 0)
				return new double[] { pot, 1 };
			else if (fcr == 2)
				betToCall = bet - betToCall;
			else if (fcr == 0)
				return new double[] { pot, 0 };
			raiseItr++;
		}
		return new double[0];
	}

	public ArrayList[] playGame(boolean flip, double blinds, boolean prnt) throws FileNotFoundException {
		Deck deck = new Deck();
		int round = 0;
		double pot = 0;
		if (flip) {
			pot += player1.takeFromBankroll(blinds);
			pot += player2.takeFromBankroll(2 * blinds);
		} else {
			pot += player1.takeFromBankroll(2 * blinds);
			pot += player2.takeFromBankroll(blinds);
		}
		ArrayList<int[]> pocket1 = deck.drawNCards(2);
		ArrayList<int[]> pocket2 = deck.drawNCards(2);
		ArrayList<int[]> share = new ArrayList<int[]>();
		for (round = 0; round < 4; round++) {
			if (prnt)
				System.out.println("Round " + (round));
			if (round == 1)
				share.addAll(deck.drawNCards(3));
			else if (round > 1)
				share.add(deck.drawCard());
			double[] getBets;
			ArrayList<int[]> hand1 = (ArrayList<int[]>) pocket1.clone();
			hand1.addAll(share);
			ArrayList<int[]> hand2 = (ArrayList<int[]>) pocket2.clone();
			hand2.addAll(share);
			if (flip)
				getBets = getBets2(round, prnt, hand1, hand2, player1, player2, pot);
			else
				getBets = getBets2(round, prnt, hand2, hand1, player2, player1, pot);
			if (getBets[1] == 0) {
				pocket1.addAll(share);
				pocket2.addAll(share);
				return new ArrayList[] { hand1, hand2 };
			}
			pot = getBets[0];
		}
		ArrayList<int[]> hand1 = (ArrayList<int[]>) pocket1.clone();
		hand1.addAll(share);
		ArrayList<int[]> hand2 = (ArrayList<int[]>) pocket2.clone();
		hand2.addAll(share);

		double rankP1 = new Rank(hand1.toArray(new int[hand1.size()][])).getRank();
		double rankP2 = new Rank(hand2.toArray(new int[hand1.size()][])).getRank();
		if (rankP1 == rankP2) {
			if (prnt)
				System.out.println("Tie");
			player1.addToBankroll((int) (pot / 2));
			player2.addToBankroll((int) (pot / 2));
		} else if (rankP1 > rankP2) {
			if (prnt) {
				System.out.println("Player " + (player1) + " Wins " + (pot));
//				time.sleep(2);
				new GUI().printHand(hand1.toArray(new int[hand1.size()][]));
				new GUI().printWinningHand((int) rankP1);
//				time.sleep(7)
			}
			player1.addToBankroll(pot);
		} else {
			if (prnt) {
				System.out.println("Player " + (player2) + " Wins " + (pot));
//				time.sleep(2)
				new GUI().printHand(hand2.toArray(new int[hand2.size()][]));
				new GUI().printWinningHand((int) rankP2);
//				time.sleep(7)
			}
			player2.addToBankroll(pot);
		}
		return new ArrayList[] { hand1, hand2 };
	}

	public boolean playNGames(int N, boolean prnt) throws FileNotFoundException {
		double rate1 = 1;
		double rate2 = 1;
		double den1 = 1;
		double den2 = 1;
		for (int i = 0; i < N; i++) {
			double bank1 = player1.getBankroll();
			double bank2 = player2.getBankroll();
			if (prnt) {
				System.out.println();
				System.out.println("Hand__________________: " + (i) + " -- " + player1 + " || " + player2);
				System.out.println();
			}
			ArrayList[] hands = playGame(i % 2 == 0, 10, prnt);

			int[][] hand1 = (int[][]) hands[0].toArray(new int[hands[0].size()][]);
			int[][] hand2 = (int[][]) hands[1].toArray(new int[hands[1].size()][]);

			if (new Rank(hand1).getRank() != new Rank(hand2).getRank() && player1.getBankroll() > bank1)
				rate1 += 1;
			if (new Rank(hand1).getRank() != new Rank(hand2).getRank() && player2.getBankroll() > bank2)
				rate2 += 1;
			if (new Rank(hand1).getRank() > new Rank(hand2).getRank())
				den1 += 1;
			if (new Rank(hand1).getRank() < new Rank(hand2).getRank())
				den2 += 1;

			if (player1.getBankroll() <= 0 || player2.getBankroll() <= 0) {
				if (den1 + den2 > 0 && prnt) {
					System.out.println("How good is " + (player1) + " : " + (rate1 / den1));
					System.out.println("How good is " + (player2) + " : " + (rate2 / den2));
				}
				if (player1.getBankroll() > player2.getBankroll())
					return true;
				else
					return false;
			}
		}
		if (player1.getBankroll() > player2.getBankroll())
			return true;
		else
			return false;
	}

	public double getWinRate(String brain1, String brain2, int numGames, int N, boolean prnt) throws IOException {
		double num = 0;
		double den = 0;
		for (int i = 0; i < numGames; i++) {
			player1 = new Player(2000, "p1", new LSTM_Bot(new File(brain1), 7, 6));
			player2 = new Player(2000, "p2", new LSTM_Bot(new File(brain2), 7, 6));
			if (this.playNGames(N, prnt)) {
				num++;
			}
			den++;
			System.out.println(player1);
			System.out.println("rate: " + num / den);
		}
		return num / den;
	}
}
