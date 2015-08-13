package com.pj.cheat.controller;

import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.pj.cheat.gui.CardPane;
import com.pj.cheat.gui.CardPanesHolder;
import com.pj.cheat.gui.ShowDialog;
import com.pj.cheat.model.AIPlayer;
import com.pj.cheat.model.Card;
import com.pj.cheat.model.Game;
import com.pj.cheat.model.Player;
import com.pj.cheat.model.Turn;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class MainController implements Initializable {

	@FXML private FlowPane cardsFlowPane;
	@FXML private Text cardsInPileText;
	@FXML private Text actionText;
	@FXML private Text cardsInHandText;
	@FXML private Text player2CardsInHandText;
	@FXML private Text player3CardsInHandText;
	@FXML private Text player4CardsInHandText;
	@FXML private ComboBox<Card.Value> cardValueComboBox;
	
	private Game game = new Game();
	private CardPanesHolder cardPanesHolder = new CardPanesHolder();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		game.addPlayer(new Player("Human"));
		game.addPlayer(new AIPlayer("Timmy"));
		game.addPlayer(new AIPlayer("Branston"));
		game.addPlayer(new AIPlayer("Chuffer Bob"));
		
		game.start();
		
		updateNumberOfCardsTexts();
		updateHumanPlayerCardsDisplay();
		updateActionText();
		updateActionControl();
	}

	private void updateActionControl() {
		if (game.isHumanPlayerTurn()) {
			if (game.isNewRound()) {
				cardValueComboBox.setItems(FXCollections.observableArrayList(Card.Value.values()));
			}
		}
	}

	private void updateActionText() {
		if (game.isHumanPlayerTurn()) {
			if (game.isNewRound()) {
				actionText.setText("You can play cards of any value this turn!");
			}
		}
	}

	private static final String HUMAN_PLAYER_CARDS_IN_HAND_TEXT = "YOUR HAND: ({0}) cards";
	private static final String AI_PLAYER_CARDS_IN_HAND_TEXT = "has {0} cards";
	private static final String CARDS_IN_PILE_TEXT = "The Pile has {0} cards in it";
	
	private void updateNumberOfCardsTexts() {
		updateHumanPlayerNumberOfCardsText();
		
		int player2CardsInHand = game.getPlayers().get(1).getCards().size();
		player2CardsInHandText.setText(MessageFormat.format(AI_PLAYER_CARDS_IN_HAND_TEXT, player2CardsInHand));
		
		int player3CardsInHand = game.getPlayers().get(2).getCards().size();
		player3CardsInHandText.setText(MessageFormat.format(AI_PLAYER_CARDS_IN_HAND_TEXT, player3CardsInHand));
		
		int player4CardsInHand = game.getPlayers().get(3).getCards().size();
		player4CardsInHandText.setText(MessageFormat.format(AI_PLAYER_CARDS_IN_HAND_TEXT, player4CardsInHand));
		
		int cardsInPile = game.getPile().size();
		cardsInPileText.setText(MessageFormat.format(CARDS_IN_PILE_TEXT, cardsInPile));
		
		updateCardsInPileText();
	}

	private void updateHumanPlayerNumberOfCardsText() {
		int cardsInHand = game.getHumanPlayer().getCards().size();
		cardsInHandText.setText(MessageFormat.format(HUMAN_PLAYER_CARDS_IN_HAND_TEXT, cardsInHand));
	}
	
	private void updateCardsInPileText() {
		int cardsInPile = game.getPile().size();
		cardsInPileText.setText(MessageFormat.format(CARDS_IN_PILE_TEXT, cardsInPile));
	}

	private void updateHumanPlayerCardsDisplay() {
		cardPanesHolder.clear();
		cardsFlowPane.getChildren().clear();
		
		List<Card> cards = game.getHumanPlayer().getCards();
		List<CardPane> cardPanes = cards.stream()
				.map(card -> new CardPane(card, cardPanesHolder)).collect(Collectors.toList());
		cardsFlowPane.getChildren().addAll(cardPanes);		
	}
	
	@FXML
	public void playSelectedCards(ActionEvent event) {
		if (hasNoCardValueSelected()) {
			ShowDialog.error("Please select a card value to declare");
			return;
		}
		
		if (hasNoCardsSelected()) {
			ShowDialog.error("Please select at least one card to play");
			return;
		}
		
		Turn turn = new Turn(
				game.getHumanPlayer(),
				cardValueComboBox.getValue(),
				cardPanesHolder.getSelectedCards());
		game.processTurn(turn);
		
		updateHumanPlayerCardsDisplay();
		updateHumanPlayerNumberOfCardsText();
		updateCardsInPileText();
	}

	private boolean hasNoCardValueSelected() {
		return cardValueComboBox.getValue() == null;
	}

	private boolean hasNoCardsSelected() {
		return cardPanesHolder.hasNoSelectedCards();
	}

}