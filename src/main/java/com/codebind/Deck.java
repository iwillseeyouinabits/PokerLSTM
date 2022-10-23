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

	public int[] drawCard() {
		return deck.remove(0);
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

}
