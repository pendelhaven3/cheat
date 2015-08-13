package com.pj.cheat.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Player {

	private String name;
	private List<Card> cards = new ArrayList<>();
	
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

	public void arrangeCards() {
		Collections.sort(cards, new Comparator<Card>() {

			@Override
			public int compare(Card o1, Card o2) {
				int result = o1.getValue().compareTo(o2.getValue());
				if (result == 0) {
					result = o1.getSuit().compareTo(o2.getSuit());
				}
				return result;
			}
		});
	}

	public void removeCards(List<Card> cardsToBeRemoved) {
		for (Card cardToBeRemoved : cardsToBeRemoved) {
			for (Iterator<Card> iter = cards.listIterator(); iter.hasNext(); ) {
				Card card = iter.next();
				if (card.equals(cardToBeRemoved)) {
					iter.remove();
					continue;
				}
				throw new RuntimeException("Played a card not in player's hand");
			}
		}
	}

	public boolean hasNoMoreCards() {
		return cards.isEmpty();
	}
	
}