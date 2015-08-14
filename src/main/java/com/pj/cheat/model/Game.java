package com.pj.cheat.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import com.pj.cheat.model.Card.Value;

public class Game {

	private List<Player> players = new ArrayList<>();
	private Deck deck = new Deck();
	private Deque<Player> turnOrder = new ArrayDeque<>();
	private List<Card> pile = new ArrayList<>();
	private Turn currentTurn;
	private Turn previousTurn;
	
	public void addPlayer(Player player) {
		player.setNumber(players.size() + 1);
		players.add(player);
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public void start() {
		distributeCardsToPlayers();
		arrangePlayerTurnOrder();
		
		/*
		while (!hasWinner()) {
			Player player = getTurnPlayer();
			
			// player turn
			Turn turn = new Turn(player, Card.Value.ACE, Arrays.asList(new Card(Suit.CLUB, Value.ACE)));
			processTurn(turn);
			
			// turn challenged
			if (turn.isPlayerCheating()) {
				turn.getPlayer().getCards().addAll(pile);
			} else {
				// player making challenge gets the cards in the pile
			}
			pile.clear();
		}
		*/
	}

	public void processTurn(Turn turn) {
		currentTurn = turn;
		turn.getPlayer().removeCards(turn.getActualCards());
		pile.addAll(turn.getActualCards());
	}

	public Player getTurnPlayer() {
		return turnOrder.peekFirst();
	}

	public boolean hasWinner() {
		for (Player player : players) {
			if (player.hasNoMoreCards()) {
				return true;
			}
		}
		return false;
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
		if (currentTurn == null) {
			return Arrays.asList(Card.Value.values());
		} else {
			return currentTurn.getDeclaredValue().getSelfAndAdjacentValues();
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
		turnOrder.addLast(turnOrder.removeFirst());
		return turnOrder.peekFirst();
	}
	
}