package com.codebind;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Rank {
	int[][] hand;
	
	public Rank(int[][] hand) {
		this.hand = hand;
	}

	public double getNHightCard(int n) {
		int[] rank = new int[13];
		for (int[] card : hand) {
			rank[card[1]] += 1;
		}
		ArrayList<Integer> ranks = new ArrayList<Integer>();
		for (int i = 0; i < 13; i++) {
			if(rank[i] > 0)
				ranks.add(i);
		}
		if (ranks.size() - n < 0)
			return 0;
		return ranks.get(ranks.size()-n);
	}

	public double isFlush() {
		int[] suits = new int[4];
		for (int[] card : hand)
			suits[card[0]] += 1;
		for (int suit : suits) {
			if (suit == 5) {
				int maxInd = 0;
				for (int[] card : hand) {
					if (card[0] == suit) {
						maxInd = Math.max(maxInd, card[1]);
					}
				}
				return maxInd/13;
			}
		}
		return -1;
	}

	public double isStreight() {
		int[] rank = new int[13];
		for(int[] card : hand) {
			rank[card[1]] += 1;
		}
		int maxInd = -1;
		for (int i = 0; i < 13-5; i++) {
			boolean streight = true;
			for (int j = 0; j < 5; j++) {
				if (rank[i+j] == 0) {
					streight = false;
					break;
				}
			}
			if (streight) {
				maxInd = Math.max(maxInd, i+4);
			}
		}
		if (maxInd < 0)
			return -1;
		else
			return maxInd/13;
	}

	public double[] numOfKind() {
		int[] rank = new int[13];
		for (int[] card : hand)
			rank[card[1]] += 1;
		for (int i = 0; i < 13; i++) {
			if (rank[i] == Arrays.stream(rank).max().getAsInt()) {
				return new double[] {Arrays.stream(rank).max().getAsInt(), i/13};
			}
		}
		return new double[] {-1, -1};
	}

	public double isTwoPair() {
		int[] rank = new int[13];
		for (int[] card : hand)
			rank[card[1]] += 1;
		int numPair = 0;
		for (int r : rank) {
			if (r == 2) {
				numPair += 1;
			}
		}
		if (numPair != 2) {
			return -1;
		} else {
			int maxInd = 0;
			for (int i = 0; i < 13; i++) {
				if (rank[i] == Arrays.stream(rank).max().getAsInt())
					return i/13;
			}
		}
		return -1;
	}
							
	public double isFullHouse() {
		int[] rank = new int[13];
		for (int[] card : hand) {
			rank[card[1]] += 1;
		}
		boolean has3 = false;
		boolean has2 = false;
		for (int r : rank) {
			if (r == 3) {
				has3 = true;
			}
			if (r == 2) {
				has2 = true;
			}
		}
		if (has2 && has3) {
			for (int i = 0; i < 13; i++) {
				if (rank[i] == 3) {
					return i/13;
				}
			}
		}
		return -1;
	}
	
	public double getRank() {
		if (isFlush() >= 0 && isStreight() >= 0)
			return 8 + isStreight();
		if (numOfKind()[0] == 4)
			return 7 + numOfKind()[1];
		if (isFullHouse() >= 0)
			return 6 + isFullHouse();
		if (isFlush() >= 0)
			return 5 + isFlush();
		if (isStreight() >= 0)
			return 4 + isStreight();
		if (numOfKind()[0] == 3)
			return 3 + numOfKind()[1] + (getNHightCard(1)/1000) + (getNHightCard(2)/20000);
		if (isTwoPair() >= 0)
			return 2 + isTwoPair() + (getNHightCard(1)/1000) + (getNHightCard(2)/20000);
		if (numOfKind()[0] == 2)
			return 1 + numOfKind()[1] + (getNHightCard(1)/1000) + (getNHightCard(2)/20000);
		return (getNHightCard(1)/13) + (getNHightCard(2)/1000);
	}
}
