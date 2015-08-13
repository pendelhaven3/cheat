package com.pj.cheat.util;

import java.util.HashMap;
import java.util.Map;

import com.pj.cheat.model.Card;

import javafx.scene.image.Image;

public class CardImages {

	private static final Map<Card, Image> map = new HashMap<>();
	
	static {
		for (Card.Suit suit : Card.Suit.values()) {
			for (Card.Value value : Card.Value.values()) {
				Card card = new Card(suit, value);
				map.put(card, new Image("/images/cards/" + getImageFilename(card)));
			}
		}
	}
	
	private static String getImageFilename(Card card) {
		StringBuilder sb = new StringBuilder();
		switch (card.getValue()) {
		case ACE: 
			sb.append("1"); break;
		case TWO: 
			sb.append("2"); break;
		case THREE: 
			sb.append("3"); break;
		case FOUR: 
			sb.append("4"); break;
		case FIVE: 
			sb.append("5"); break;
		case SIX: 
			sb.append("6"); break;
		case SEVEN: 
			sb.append("7"); break;
		case EIGHT: 
			sb.append("8"); break;
		case NINE: 
			sb.append("9"); break;
		case TEN: 
			sb.append("10"); break;
		case JACK: 
			sb.append("11"); break;
		case QUEEN: 
			sb.append("12"); break;
		case KING: 
			sb.append("13"); break;
		}
		
		sb.append("_");
		
		switch (card.getSuit()) {
		case CLUB:
			sb.append("clubs"); break;
		case SPADE:
			sb.append("spades"); break;
		case HEART:
			sb.append("hearts"); break;
		case DIAMOND:
			sb.append("diamonds"); break;
		}
		
		sb.append(".gif");
		
		return sb.toString();
	}
	
	public static Image getImage(Card card) {
		return map.get(card);
	}

}