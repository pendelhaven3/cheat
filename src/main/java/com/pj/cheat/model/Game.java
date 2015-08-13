package com.pj.cheat.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import com.pj.cheat.model.Card.Suit;
import com.pj.cheat.model.Card.Value;

public class Game {

	private List<Player> players = new ArrayList<>();
	private Deck deck = new Deck();
	private Deque<Player> turnOrder = new ArrayDeque<>();
	private List<Card> pile = new ArrayList<>();
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public void start() {
		distributeCardsToPlayers();
		arrangePlayerCards();
		arrangePlayerTurnOrder();
		
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
	}

	private void processTurn(Turn turn) {
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

	private void arrangePlayerCards() {
		for (Player player : players) {
			player.arrangeCards();
		}
	}

	private void distributeCardsToPlayers() {
		while (deck.hasCards()) {
			for (Player player : players) {
				player.addCard(deck.getCard());
			}
		}
	}

	private class Turn {
		
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
	
	public static void main(String[] args) {
		Game game = new Game();
		game.addPlayer(new Player("Human"));
		game.addPlayer(new Player("Timmy"));
		game.addPlayer(new Player("Branston"));
		game.addPlayer(new Player("Chuffer Bob"));
		
		game.start();
		
		for (Player player : game.getPlayers()) {
			System.out.println(player.getName() + "'s hand");
			System.out.println("---------------");
			for (Card card : player.getCards()) {
				System.out.println(card);
			}
			System.out.println();
		}
	}
	
}