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
		return actualCards.stream().anyMatch(card -> card.getValue() != declaredValue);
	}

	public Player getPlayer() {
		return player;
	}
	
	public List<Card> getActualCards() {
		return actualCards;
	}
	
	public Card.Value getDeclaredValue() {
		return declaredValue;
	}

	public String getDescription() {
		return new StringBuilder()
				.append(player.getName())
				.append(" has played ")
				.append(actualCards.size())
				.append(" ")
				.append(getDeclaredValueDescription())
				.toString();
	}

	private String getDeclaredValueDescription() {
		StringBuilder sb = new StringBuilder();
		
		switch (declaredValue) {
		case ACE:
			sb.append("ace"); break;
		case TWO:
			sb.append("two"); break;
		case THREE:
			sb.append("three"); break;
		case FOUR:
			sb.append("four"); break;
		case FIVE:
			sb.append("five"); break;
		case SIX:
			sb.append("six"); break;
		case SEVEN:
			sb.append("seven"); break;
		case EIGHT:
			sb.append("eight"); break;
		case NINE:
			sb.append("nine"); break;
		case TEN:
			sb.append("ten"); break;
		case JACK:
			sb.append("jack"); break;
		case QUEEN:
			sb.append("queen"); break;
		case KING:
			sb.append("king"); break;
		}
		
		if (isMoreThanOneCardPlayed()) {
			if (declaredValue == Card.Value.SIX) {
				sb.append("e");
			}
			sb.append("s");
		}
		
		return sb.toString();
	}

	private boolean isMoreThanOneCardPlayed() {
		return actualCards.size() > 1;
	}
	
}