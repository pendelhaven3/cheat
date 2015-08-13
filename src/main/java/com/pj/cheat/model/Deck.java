package com.pj.cheat.model;

import java.util.Collections;
import java.util.Stack;

public class Deck {

	private final Stack<Card> cards = new Stack<>();

	public Deck() {
		addCards();
	}
	
	private void addCards() {
		for (Card.Suit suit : Card.Suit.values()) {
			for (Card.Value value : Card.Value.values()) {
				cards.push(new Card(suit, value));
			}
		}
		shuffle();
	}

	private void shuffle() {
		Collections.shuffle(cards);
	}

	public boolean hasCards() {
		return !cards.isEmpty();
	}
	
	public Card getCard() {
		return cards.pop();
	}

}