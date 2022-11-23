package com.codebind;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	ArrayList<int[]> deck = new ArrayList<int[]>();

	
	public Deck() {
		newDeck();
	}
	
	public Deck(ArrayList<int[]> deck) {
		this.deck = deck;
	}

	public void newDeck() {
		deck = new ArrayList<int[]>();
		for (int suit = 0; suit < 4; suit++)
			for (int rank = 0; rank < 13; rank++)
				deck.add(new int[] {suit, rank});
		Collections.shuffle(deck);
	}
	
	public void shufle() {
		Collections.shuffle(deck);
	}

	public int[] drawCard() {
		int[] card = deck.remove(0);
		return card;
	}
	
	public ArrayList<int[]> drawNCards(int N) {
		ArrayList<int[]> out = new ArrayList<int[]>();
		for (int i = 0; i < N; i++)
			out.add(drawCard());
		return out;
	}

	public int[] removeCard(int[] card) {
		deck.remove(card);
		return card;
	}
	
	public Deck getCopy() {
		return new Deck((ArrayList<int[]>) this.deck.clone());
	}

	public float playNHands(int N, int[][] hand) {
		double num = 0, den = 0;
		ArrayList<int[]> share = new ArrayList<int[]>();
		for (int[] card : hand) {
			this.removeCard(card);
			share.add(card);
		}
		int[][] pocket1 = new int[2][];
		int[][] pocket2 = new int[2][];
		pocket1[0] = share.remove(share.size()-1);
		pocket1[1] = share.remove(share.size()-1);
		ArrayList<int[]> oldShare = (ArrayList<int[]>) share.clone();
		for(int i = 0; i < N; i++) {
			share = (ArrayList<int[]>) oldShare.clone();
			Deck d = new Deck((ArrayList<int[]>) deck.clone());
			d.shufle();
			pocket2[0] = d.drawCard();
			pocket2[1] = d.drawCard();
			for (; share.size() < 5;) {
				share.add(d.drawCard());
			}
			double rank1 = new Rank(new int[][] {pocket1[0], pocket1[1], share.get(0), share.get(1), share.get(2), share.get(3), share.get(4)}).getRank();
			double rank2 = new Rank(new int[][] {pocket2[0], pocket2[1], share.get(0), share.get(1), share.get(2), share.get(3), share.get(4)}).getRank();
			if (rank1 > rank2)
				num++;
			den++;
		}
		return (float) (num/den);
	}
	
}
