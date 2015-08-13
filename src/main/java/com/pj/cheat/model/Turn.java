package com.pj.cheat.model;

import java.util.List;

public class Turn {

	private Player player;
	private Card.Value declaredValue;
	private List<Card> actualCards;
	
	/**
	 * 
	 * @param player 
	 * @param declaredValue Declared value of cards
	 * @param actualCards Actual cards played
	 */
	public Turn(Player player, Card.Value declaredValue, List<Card> actualCards) {
		this.player = player;
		this.declaredValue = declaredValue;
		this.actualCards = actualCards;
	}

	public boolean isPlayerCheating() {
		for (Card card : actualCards) {
			if (!card.getValue().equals(declaredValue)) {
				return true;
			}
		}
		return false;
	}

	public Player getPlayer() {
		return player;
	}
	
	public List<Card> getActualCards() {
		return actualCards;
	}
	
}
