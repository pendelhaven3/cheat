package com.pj.cheat.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import com.pj.cheat.model.Card.Value;
import com.pj.cheat.util.RandomUtil;

public class Game {

	private List<Player> players = new ArrayList<>();
	private List<AiPlayer> aiPlayers = new ArrayList<>();
	private Deck deck = new Deck();
	private Deque<Player> turnOrder = new ArrayDeque<>();
	private List<Card> pile = new ArrayList<>();
	private Turn currentTurn;
	private Turn previousTurn;
	
	public void addPlayer(Player player) {
		player.setNumber(players.size() + 1);
		players.add(player);
		if (player.isAi()) {
			aiPlayers.add((AiPlayer)player);
		}
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public List<AiPlayer> getAiPlayers() {
		return aiPlayers;
	}
	
	public void start() {
		distributeCardsToPlayers();
		arrangePlayerTurnOrder();
	}

	public void processTurn(Turn turn) {
		currentTurn = turn;
		turn.getPlayer().removeCards(turn.getActualCards());
		pile.addAll(turn.getActualCards());
	}

	/**
	 * Returns the Player of the current turn;
	 * 
	 * @return
	 */
	public Player getTurnPlayer() {
		return turnOrder.peekFirst();
	}

	private void arrangePlayerTurnOrder() {
		turnOrder.addAll(players);
	}

	private void distributeCardsToPlayers() {
		while (deck.hasCards()) {
			for (Player player : players) {
				player.addCard(deck.getCard());
			}
		}
	}

	public Player getHumanPlayer() {
		return players.get(0);
	}
	
	
	public List<Card> getPile() {
		return pile;
	}
	
	public boolean isHumanPlayerTurn() {
		return getHumanPlayer().equals(turnOrder.peekFirst());
	}

	public boolean isNewRound() {
		return pile.isEmpty();
	}

	public List<Value> getAllowedCardValues() {
		if (isNewRound()) {
			return Arrays.asList(Card.Value.values());
		} else {
			return previousTurn.getDeclaredValue().getSelfAndAdjacentValues();
		}
	}

	public String getCurrentTurnDescription() {
		return currentTurn.getDescription();
	}

	/**
	 * 
	 * @param challenger
	 * @return true if challenge successful (e.g. opponent IS cheating)
	 */
	public boolean challengeCurrentTurn(Player challenger) {
		boolean cheating = currentTurn.isPlayerCheating();
		if (cheating) {
			currentTurn.getPlayer().getCards().addAll(pile);
		} else {
			challenger.getCards().addAll(pile);
		}
		pile.clear();
		return cheating;
	}

	public Player getNextTurnPlayer() {
		previousTurn = currentTurn;
		turnOrder.addLast(turnOrder.removeFirst());
		return turnOrder.peekFirst();
	}

	private List<Player> getCurrentTurnAIChallengers() {
		return aiPlayers.stream()
				.filter(aiPlayer -> aiPlayer != getTurnPlayer() && aiPlayer.willChallenge(currentTurn))
				.collect(Collectors.toList());
	}
	
	public boolean hasCurrentTurnAIChallengers() {
		return !getCurrentTurnAIChallengers().isEmpty();
	}

	public Player getRandomCurrentTurnAIChallenger() {
		return RandomUtil.getRandomElement(getCurrentTurnAIChallengers());
	}

	public void notifyAiPlayers(Event event) {
		aiPlayers.stream().forEach(aiPlayer -> aiPlayer.processEvent(this, event));
	}

	public Turn getCurrentTurn() {
		return currentTurn;
	}
	
}