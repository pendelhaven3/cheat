package com.pj.cheat.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.pj.cheat.model.Card.Value;
import com.pj.cheat.util.RandomUtil;

public class Level2AiPlayer extends AiPlayer {

	private List<Turn> roundTurns = new ArrayList<>();
	
	public Level2AiPlayer(String name) {
		super(name);
	}

	public Turn play(Game game) {
		List<Card.Value> playableCardValues = getPlayableCardValues(game);
		if (!playableCardValues.isEmpty()) {
			Card.Value valueToPlay = RandomUtil.getRandomElement(playableCardValues);
			return new Turn(this, valueToPlay, getCardsWithValue(valueToPlay));
		} else {
			return new Turn(this, 
					getRandomAllowedCardValue(game), 
					Arrays.asList(getRandomAvailableCard()));
		}
	}

	private Card getRandomAvailableCard() {
		return RandomUtil.getRandomElement(getCards());
	}

	private Value getRandomAllowedCardValue(Game game) {
		return RandomUtil.getRandomElement(game.getAllowedCardValues());
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
		int totalCardsWithSameValueOnHand = getCardsWithValue(turn.getDeclaredValue()).size();
		int totalCardsWithSameValueThisRound = getTotalNumberOfCardsWithValueThisRound(turn.getDeclaredValue());
		System.out.println(turn.getDeclaredValue());
		System.out.println("hark: " + totalCardsWithSameValueThisRound);
		
		if (totalCardsWithSameValueOnHand + totalCardsWithSameValueThisRound 
				+ turn.getActualCards().size() > MAX_NUMBER_OF_SAME_VALUE_CARDS) {
			return true;
		}
		
		return false;
	}

	private int getTotalNumberOfCardsWithValueThisRound(Value value) {
		int total = 0;
		for (Turn turn : roundTurns) {
			if (turn.getDeclaredValue() == value) {
				total += turn.getActualCards().size();
			}
		}
		return total;
	}

	@Override
	public void processEvent(Game game, Event event) {
		switch (event) {
		case END_TURN:
			roundTurns.add(game.getCurrentTurn());
			break;
		case END_ROUND:
			roundTurns.clear();
			break;
		}
	}

}