package com.pj.cheat.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {

	private int number;
	private String name;
	private List<Card> cards = new ArrayList<>();
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void addCard(Card card) {
		cards.add(card);
	}
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void removeCards(List<Card> cardsToBeRemoved) {
		int removed = 0;
		for (Iterator<Card> iter = cards.listIterator(); iter.hasNext(); ) {
			Card card = iter.next();
			if (cardsToBeRemoved.contains(card)) {
				iter.remove();
				removed++;
			}
		}
		if (cardsToBeRemoved.size() != removed) {
			throw new RuntimeException("Played a card not in player's hand");
		}
	}

	public boolean hasNoMoreCards() {
		return cards.isEmpty();
	}
	
}