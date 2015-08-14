package com.pj.cheat.model;

import java.util.List;
import java.util.stream.Collectors;

import com.pj.cheat.model.Card.Value;
import com.pj.cheat.util.RandomUtil;

public class Opponent extends Player {

	public Opponent(String name) {
		super(name);
	}

	public Turn takeTurn(Game game) {
		List<Card.Value> playableCardValues = getPlayableCardValues(game);
		if (!playableCardValues.isEmpty()) {
			Card.Value valueToPlay = RandomUtil.getRandomElement(playableCardValues);
			return new Turn(this, valueToPlay, getCardsWithValue(valueToPlay));
		} else {
			List<Card.Value> availableCardValues = getAvailableCardValues();
			Card.Value valueToPlay = RandomUtil.getRandomElement(availableCardValues);
			Card.Value valueToDeclare = RandomUtil.getRandomElement(game.getAllowedCardValues());
			return new Turn(this, valueToDeclare, getCardsWithValue(valueToPlay));
		}
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
	
}