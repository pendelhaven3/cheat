package com.pj.cheat.model;

import java.util.List;
import java.util.stream.Collectors;

import com.pj.cheat.model.Card.Value;
import com.pj.cheat.util.RandomUtil;

public class AiPlayer extends Player {

	private static final int MAX_NUMBER_OF_SAME_VALUE_CARDS = 4;
	
	public AiPlayer(String name) {
		super(name, true);
	}

	public Turn play(Game game) {
		List<Card.Value> playableCardValues = getPlayableCardValues(game);
		if (!playableCardValues.isEmpty()) {
			Card.Value valueToPlay = RandomUtil.getRandomElement(playableCardValues);
			return new Turn(this, valueToPlay, getCardsWithValue(valueToPlay));
		} else {
			return new Turn(this, 
					getRandomAllowedCardValue(game), 
					getRandomAvailableCards());
		}
	}

	private List<Card> getRandomAvailableCards() {
		return getCardsWithValue(getRandomAvailableCardValue());
	}

	private Value getRandomAllowedCardValue(Game game) {
		return RandomUtil.getRandomElement(game.getAllowedCardValues());
	}
	
	private Value getRandomAvailableCardValue() {
		return RandomUtil.getRandomElement(getAvailableCardValues());
	}

	private List<Value> getAvailableCardValues() {
		return getCards().stream().map(card -> card.getValue()).distinct().collect(Collectors.toList());
	}

	private List<Card> getCardsWithValue(Value value) {
		return getCards().stream().filter(card -> card.getValue() == value).collect(Collectors.toList());
	}

	private List<Value> getPlayableCardValues(Game game) {
		List<Card.Value> allowed = game.getAllowedCardValues();
		return allowed.stream().filter(value -> hasCardWithValue(value))
				.collect(Collectors.toList());
	}

	private boolean hasCardWithValue(Card.Value value) {
		return getCards().stream().anyMatch(card -> card.getValue() == value);
	}
	
	public boolean willChallenge(Turn turn) {
		if (hasCardWithValue(turn.getDeclaredValue())) {
			return getCardsWithValue(turn.getDeclaredValue()).size() + turn.getActualCards().size() > MAX_NUMBER_OF_SAME_VALUE_CARDS;
		}
		return false;
	}
	
}