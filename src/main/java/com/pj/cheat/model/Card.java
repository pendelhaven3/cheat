package com.pj.cheat.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Card {

	public enum Suit {
		
		CLUB, SPADE, HEART, DIAMOND;
	
	}
	
	public enum Value {

		ACE("Ace"), 
		TWO("2"), 
		THREE("3"), 
		FOUR("4"), 
		FIVE("5"), 
		SIX("6"), 
		SEVEN("7"), 
		EIGHT("8"), 
		NINE("9"), 
		TEN("10"), 
		JACK("Jack"), 
		QUEEN("Queen"), 
		KING("King");
		
		private static final List<Value> LIST = Arrays.asList(Value.values());
		
		private String description;
		
		private Value(String description) {
			this.description = description;
		}
		
		@Override
		public String toString() {	
			return description;
		}
		
		public List<Value> getSelfAndAdjacentValues() {
			List<Value> toReturn = new ArrayList<>(3);
			toReturn.add(this);
			
			int index = LIST.indexOf(this);
			if (index == 0) {
				toReturn.add(0, LIST.get(12));
				toReturn.add(LIST.get(index + 1));
			} else if (index == 12) {
				toReturn.add(0, LIST.get(11));
				toReturn.add(LIST.get(0));
			} else {
				toReturn.add(0, LIST.get(index - 1));
				toReturn.add(LIST.get(index + 1));
			}
			
			return toReturn;
		}
		
		public String getDescription() {
			return description;
		}
		
	}
	
	private Suit suit;
	private Value value;
	
	public Card(Suit suit, Value value) {
		this.suit = suit;
		this.value = value;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public Value getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(17); // longest is 17-char "Queen of Diamonds"
		
		switch (value) {
		case ACE:
			sb.append("Ace of ");
			break;
		case TWO:
			sb.append("Two ");
			break;
		case THREE:
			sb.append("Three ");
			break;
		case FOUR:
			sb.append("Four ");
			break;
		case FIVE:
			sb.append("Five ");
			break;
		case SIX:
			sb.append("Six ");
			break;
		case SEVEN:
			sb.append("Seven ");
			break;
		case EIGHT:
			sb.append("Eight ");
			break;
		case NINE:
			sb.append("Nine ");
			break;
		case TEN:
			sb.append("Ten ");
			break;
		case JACK:
			sb.append("Jack of ");
			break;
		case QUEEN:
			sb.append("Queen of ");
			break;
		case KING:
			sb.append("King of ");
			break;
		}
		
		switch (suit) {
		case CLUB:
			sb.append("Clubs");
			break;
		case SPADE:
			sb.append("Spades");
			break;
		case HEART:
			sb.append("Hearts");
			break;
		case DIAMOND:
			sb.append("Diamonds");
			break;
		}
		
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(suit)
				.append(value)
				.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Card other = (Card) obj;
		return new EqualsBuilder()
				.append(suit, other.getSuit())
				.append(value, other.getValue())
				.isEquals();
	}
	
}