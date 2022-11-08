package com.codebind;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UnitPoker {
	
	boolean prnt = false;
	
	UnitPoker() {
		
	}
	
	UnitPoker(boolean prnt) {
		this.prnt = prnt;
	}

	public double[] getNextBet(int round, Player player, Player p2, double pot, double betToCall, int[][] hand,
			boolean updatePlayer, int raiseItr, double bet) {
		if (updatePlayer)
			bet = player.makeBet(pot, betToCall, round, hand, p2, bet);
		if (bet < betToCall) {
			if (player.getBankroll() == 0 && bet == 0) {
				return new double[] { 0, 1 };
			} else {
				if (updatePlayer) {
					player.addToBankroll(bet);
					p2.addToBankroll(pot);
				}
				return new double[] { 0, 0 };
			}
		} else if (bet > betToCall && !(p2.getBankroll() <= bet - betToCall)) {
			return new double[] { bet, 2 };
		} else if (bet == betToCall || (player.getBankroll() + bet == 0)) {
			return new double[] { bet, 1 };
		} else if (p2.getBankroll() <= bet - betToCall && bet > betToCall) {
			if (updatePlayer)
				player.addToBankroll(bet - (p2.getBankroll() + betToCall));
			return new double[] { p2.getBankroll() + betToCall, 2 };
		} else {
			System.out.println("Money On Table");
			return null;
		}
	}

	public double[] getNextBet(int round, Player player, Player p2, double pot, double betToCall, int[][] hand,
			boolean prnt, int raiseItr) {
		double bet = player.makeBet(pot, betToCall, round, hand, p2);
		if (bet < betToCall) {
			if (player.getBankroll() == 0 && bet == 0) {
				return new double[] { 0 };
			} else {
				player.addToBankroll(bet);
				p2.addToBankroll(pot);
				return new double[] { 0 };
			}
		} else if (bet > betToCall && !(p2.getBankroll() <= bet - betToCall)) {
			return new double[] { bet };
		} else if (bet == betToCall || (player.getBankroll() + bet == 0)) {
			return new double[] { bet };
		} else if (p2.getBankroll() <= bet - betToCall && bet > betToCall) {
			player.addToBankroll(bet - (p2.getBankroll() + betToCall));
			return new double[] { p2.getBankroll() + betToCall };
		} else {
			System.out.println("Money On Table");
			return null;
		}
	}

	public void findWinner(Player p1, Player p2, int[][] hand1, int[][] hand2, int[][] share, double pot)
			throws Exception {
		int[][] totHand1 = new int[hand1.length + share.length][];
		int[][] totHand2 = new int[hand1.length + share.length][];
		for (int i = 0; i < share.length; i++) {
			totHand1[i] = share[i];
			totHand2[i] = share[i];
		}
		for (int i = 0; i < hand1.length; i++) {
			totHand1[share.length + i] = hand1[i];
			totHand2[share.length + i] = hand2[i];
		}
		if (share.length != 5) {
			System.out.println("FAILURE");
			TimeUnit.SECONDS.sleep(10000000);
		}
		double rankP1 = new Rank(totHand1).getRank();
		double rankP2 = new Rank(totHand2).getRank();
		if (rankP1 == rankP2) {
//			System.out.println("tie");
			p1.addToBankroll(Math.floor(pot / 2));
			p2.addToBankroll(Math.floor(pot / 2));
		} else if (rankP1 > rankP2) {
//			System.out.println("p1");
			p1.addToBankroll(pot);
		} else {
//			System.out.println("p2");
			p2.addToBankroll(pot);
		}
	}

	public void startGame(Player p1, Player p2) {
		p1.takeFromBankroll(10);
		p2.takeFromBankroll(10);
	}

	public int[][] dealCards(Deck deck, int round) {
		if (round == 0)
			return deck.drawNCards(3).toArray(new int[3][]);
		else
			return deck.drawNCards(1).toArray(new int[1][]);
	}

	public int[][] copyHand(int[][] hand) {
		int[][] newHand = new int[hand.length][2];
		for (int i = 0; i < hand.length; i++) {
			newHand[i][0] = hand[i][0];
			newHand[i][1] = hand[i][1];
		}
		return newHand;
	}

	public ArrayList<Object[]> nextUnit(Player p1, Player p2, int[][] share, int[][] hand1, int[][] hand2, Deck deck,
			double pot, double minbet1,  double minbet2, boolean callable, boolean flip, int round, int raisItr, int dataFile,
			ArrayList<float[][]> histData) throws Exception {
		int[][] totHand1 = new int[hand1.length + share.length][];
		int[][] totHand2 = new int[hand1.length + share.length][];

		for (int i = 0; i < share.length; i++) {
			totHand1[i] = share[i];
			totHand2[i] = share[i];
		}
		for (int i = 0; i < hand1.length; i++) {
			totHand1[share.length + i] = hand1[i];
			totHand2[share.length + i] = hand2[i];
		}
		double[] plays;
		int maxPlaysPerHand = 2;

		if (flip && raisItr <= maxPlaysPerHand) {
			plays = p1.getBetOptions(pot, minbet1);
		} else if (flip)
			plays = new double[] { p1.getBetOptions(pot, minbet1)[0], p1.getBetOptions(pot, minbet1)[1] };
		else
			plays = getNextBet(round, p2, p1, pot, minbet2, totHand2, false, raisItr);
//		else
//			plays = new double[] { getNextBet(round, p2, p1, pot, minbet2, totHand2, true, raisItr, minbet2)[0] };

		ArrayList<Object[]> out = new ArrayList<Object[]>();
		Player oldp1 = p1.getCopy();
		Player oldp2 = p2.getCopy();
		double oldpot = pot;
		int oldround = round;
		double oldminbet1 = minbet1;
		double oldminbet2 = minbet2;
		int oldraisitr = raisItr;
		ArrayList<float[][]> oldhistData = (ArrayList<float[][]>) histData.clone();
		for (int i = 0; i < plays.length; i++) {
			double play = plays[i];
			p1 = oldp1.getCopy();
			p2 = oldp2.getCopy();
			pot = oldpot;
			minbet1 = oldminbet1;
			minbet2 = oldminbet2;
			round = oldround;
			raisItr = oldraisitr;
			histData = (ArrayList<float[][]>) oldhistData.clone();
			boolean dealCards = false;
			double[] playOut;

			if (plays.length > 1) {
				Player p1copy = p1.getCopy();
				Player p2copy = p2.getCopy();

				float[] input = p1copy.commitForwardStep(pot, minbet1, round, totHand1, p2copy);
				float[] output = new float[100];
				output[i] = 1;
				histData.add(new float[][] { input, output });
			}

			if (plays.length == 1 && flip) {
				playOut = getNextBet(round, p1, p2, pot, minbet1, totHand1, false, raisItr, play);
			} else if (plays.length == 1) {
				playOut = getNextBet(round, p2, p1, pot, minbet2, totHand2, false, raisItr, play);
			} else {
				playOut = getNextBet(round, p1, p2, pot, minbet1, totHand1, true, raisItr, play);
			}
			double bet = playOut[0];
			int fcr = (int) playOut[1];
			pot += bet;
			boolean cont = true;
			int tempRound = round;

			if (fcr == 0) {
				callable = false;
				cont = false;
			} else if (fcr == 1 && !callable) {
				callable = true;
			} else if (fcr == 1 && callable && round < 3) {
				callable = false;
				dealCards = true;
				round += 1;
				minbet1 = 0;
				minbet2 = 0;
			} else if (fcr == 1 && callable && round == 3) {
				cont = false;
				callable = false;
				findWinner(p1, p2, hand1, hand2, share, pot);
			} else if (fcr == 2) {
				callable = true;
				if (flip)
					minbet2 = bet-minbet1;
				else
					minbet1 = bet-minbet2;
			} else {
				System.out.println("Wrong Move Made");
			}

			if (round == tempRound)
				raisItr += 1;
			else
				raisItr = 0;

			if (cont) {
				if (!dealCards)
					out.add(new Object[] { p1.getCopy(), p2.getCopy(), this.copyHand(share), this.copyHand(hand1),
							this.copyHand(hand2), deck.getCopy(), pot, minbet1, minbet2, callable, !flip, round, raisItr,
							dataFile, histData });
				else {
					Deck tempDeck = ((Deck) deck).getCopy();
					int[][] newCards = dealCards(tempDeck, round - 1);
					int[][] newShare = new int[newCards.length + share.length][];
					for (int k = 0; k < share.length; k++) {
						newShare[k] = share[k];
					}
					for (int k = 0; k < newCards.length; k++) {
						newShare[k + share.length] = newCards[k];
					}
					out.add(new Object[] { p1.getCopy(), p2.getCopy(), this.copyHand(newShare), this.copyHand(hand1),
							this.copyHand(hand2), tempDeck, pot, minbet1,  minbet2, callable, !flip, round, raisItr, dataFile,
							histData.clone() });
				}
			} else {
				out.add(new Object[] { p1.getBankroll(), p1.getCopy(), p2.getCopy(), histData.clone() });
			}
		}
		return out;
	}

	public Object[] playHand(Player p1, Player p2, boolean flip, int dataFile) throws Exception {
		Deck deck = new Deck();
		int round = 0;
		double pot = 0;
		if (flip) {
			pot += p1.takeFromBankroll(10);
			pot += p2.takeFromBankroll(20);
		} else {
			pot += p1.takeFromBankroll(20);
			pot += p2.takeFromBankroll(10);
		}
		int[][] hand1 = (int[][]) deck.drawNCards(2).toArray(new int[2][]);
		int[][] hand2 = (int[][]) deck.drawNCards(2).toArray(new int[2][]);
		int[][] share = new int[0][];
		double minbet = 0;
		boolean callable = false;
		ArrayList<Object[]> argArray = new ArrayList<Object[]>();
		argArray.add(new Object[] { p1, p2, share, hand1, hand2, deck, pot, minbet,  minbet, callable, flip, round, 0, dataFile,
				new ArrayList<double[][]>() });
		ArrayList<Object[]> output = new ArrayList<Object[]>();
		while (argArray.size() > 0) {
			Object[] args = argArray.remove(0);
			if (args.length == 15) {
				ArrayList<Object[]> nextUnit = nextUnit((Player) args[0], (Player) args[1], (int[][]) args[2],
						(int[][]) args[3], (int[][]) args[4], (Deck) args[5], (Double) args[6], (Double) args[7], (Double) args[8],
						(Boolean) args[9], (Boolean) args[10], (Integer) args[11], (Integer) args[12],
						(Integer) args[13], (ArrayList<float[][]>) args[14]);
				for (Object[] n : nextUnit) {
					argArray.add(0, n);
				}
			} else {
				output.add(args);
			}
		}
		double maxBank = -1;
		Object[] bestOut = new Object[3];
		for (int i = 0; i < output.size(); i++) {
			Object[] out = output.get(i);
			if ((Double) out[0] > maxBank) {
				maxBank = (Double) out[0];
				bestOut = out;
			}
		}
		return bestOut;
	}

	public ArrayList<float[][]> playGame(LSTM_Bot w1, LSTM_Bot w2, int botItr, int dataFile, int gen) throws Exception {
		Player p1 = new Player(2000, "p1", w1);
		Player p2 = new Player(2000, "p2", w2);
		ArrayList<float[][]> data = new ArrayList<float[][]>();
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			Object[] out = playHand(p1, p2, i % 2 == 0, dataFile);
			p1 = (Player) out[1];
			p2 = (Player) out[2];
			if (prnt) {
				System.out.println(p1 + " " + p2);
				System.out.println();
				System.out.println(p1 + " ____________");
				for (double in : p1.bets) {
					System.out.print(in + " ");
				}
				System.out.println();
				System.out.println(p2 + " ____________");
				for (double in : p2.bets) {
					System.out.print(in + " ");
				}
				System.out.println();
				System.out.println("____________");
				System.out.println(p2 + " ____________");
				for (float[] ins : p2.lstm.x) {
					System.out.println();
					for (float in : ins) {
						System.out.print(in + " ");
					}
				}
				System.out.println();
				System.out.println("================================");
			}
			for (float[][] datum : (ArrayList<float[][]>) out[3]) {
				data.add(datum);
			}
			if (p1.getBankroll() <= 0 || p2.getBankroll() <= 0) {
				File parDir = new File(botItr + "/" + gen + "/data_inputs_" + dataFile + ".csv").getParentFile();
				if (parDir != null) {
					parDir.mkdirs();
				}
				FileWriter fwIn = new FileWriter(botItr + "/" + gen + "/data_inputs_" + dataFile + ".csv");
				FileWriter fwOut = new FileWriter(botItr + "/" + gen + "/data_outputs_" + dataFile + ".csv");
				for (float[][] datum : data) {

					String line = "";
					for (float in : datum[0]) {
						line += (in + ",");
					}
					fwIn.write(line.substring(0, line.length() - 1) + "\n");
					line = "";
//					for(float in : datum[1]) {
//						line += ((int)(in) + ",");
//					}
//					fwOut.write(line.substring(0, line.length()-1) + "\n");

					float maxOut = 0;
					int maxInd = 0;
					for (int k = 0; k < datum[1].length; k++) {
						if (datum[1][k] > maxOut) {
							maxOut = datum[1][k];
							maxInd = k;
						}
					}
					fwOut.write(maxInd + "\n");
				}
				fwIn.close();
				fwOut.close();
				return data;
			}
		}
		return data;
	}
}
