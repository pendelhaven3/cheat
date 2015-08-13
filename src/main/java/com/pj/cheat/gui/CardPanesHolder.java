package com.pj.cheat.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pj.cheat.model.Card;

public class CardPanesHolder {

	private static final int MAX_SELECTED_CARDS = 4;
	
	private List<CardPane> cardPanes = new ArrayList<>();
	
	public void add(CardPane cardPane) {
		cardPanes.add(cardPane);
	}
	
	public boolean hasMaximumSelectedCards() {
		return getTotalSelectedCards() == MAX_SELECTED_CARDS;
	}
	
	private int getTotalSelectedCards() {
		return (int)cardPanes.stream().filter(cardPane -> cardPane.isSelected()).count();
	}
	
	public boolean hasNoSelectedCards() {
		return getTotalSelectedCards() == 0;
	}

	public List<Card> getSelectedCards() {
		return cardPanes.stream().filter(cardPane -> cardPane.isSelected())
				.map(cardPane -> cardPane.getCard()).collect(Collectors.toList());
	}

	public void clear() {
		cardPanes.clear();
	}
	
}