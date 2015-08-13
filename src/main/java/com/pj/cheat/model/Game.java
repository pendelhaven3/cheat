package com.pj.cheat.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Game {

	private List<Player> players = new ArrayList<>();
	private Deck deck = new Deck();
	private Deque<Player> turnOrder = new ArrayDeque<>();
	private List<Card> pile = new ArrayList<>();
	private Turn currentTurn;
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public void start() {
		distributeCardsToPlayers();
		getHumanPlayer().arrangeCards();
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

	private Player getTurnPlayer() {
		Player player = turnOrder.removeFirst();
		turnOrder.addLast(player);
		return player;
	}

	private boolean hasWinner() {
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
	
}