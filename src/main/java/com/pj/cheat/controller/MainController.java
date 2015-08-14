package com.pj.cheat.controller;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.pj.cheat.gui.CardPane;
import com.pj.cheat.gui.CardPanesHolder;
import com.pj.cheat.gui.ShowDialog;
import com.pj.cheat.model.Opponent;
import com.pj.cheat.model.Card;
import com.pj.cheat.model.Game;
import com.pj.cheat.model.Player;
import com.pj.cheat.model.Turn;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
	@FXML private HBox playControlsPane;
	@FXML private VBox challengeControlsPane;
	@FXML private Button accuseButton;
	@FXML private Button letItSlideButton;
	
	private Game game = new Game();
	private CardPanesHolder cardPanesHolder = new CardPanesHolder();
	private ResourceBundle messages = ResourceBundle.getBundle("messages");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setControlPanesToFreeUpSpaceWhenNotVisible();
		
		game.addPlayer(new Player("Human"));
		game.addPlayer(new Opponent("Timmy"));
		game.addPlayer(new Opponent("Branston"));
		game.addPlayer(new Opponent("Chuffer Bob"));
		
		game.start();
		
		updateNumberOfCardsTexts();
		updateHumanPlayerCardsDisplay();
		updateActionText();
		updateActionControl();
	}

	private void setControlPanesToFreeUpSpaceWhenNotVisible() {
		playControlsPane.managedProperty().bind(playControlsPane.visibleProperty());
		challengeControlsPane.managedProperty().bind(challengeControlsPane.visibleProperty());
	}

	private static final String ACCUSE_BUTTON_TEXT = "Accuse {0} of cheating!";
	private static final String LET_IT_SLIDE_BUTTON_TEXT = "Let {0} slide";
	
	private void updateActionControl() {
		if (game.isHumanPlayerTurn()) {
			playControlsPane.setVisible(true);
			challengeControlsPane.setVisible(false);
			if (game.isNewRound()) {
				cardValueComboBox.setItems(FXCollections.observableArrayList(Card.Value.values()));
			} else {
				cardValueComboBox.setItems(FXCollections.observableArrayList(game.getAllowedCardValues()));
			}
		} else {
			playControlsPane.setVisible(false);
			challengeControlsPane.setVisible(true);
			accuseButton.setText(MessageFormat.format(ACCUSE_BUTTON_TEXT, game.getTurnPlayer().getName()));
			letItSlideButton.setText(MessageFormat.format(LET_IT_SLIDE_BUTTON_TEXT, game.getTurnPlayer().getName()));
		}
	}

	private void updateActionText() {
		if (game.isHumanPlayerTurn()) {
			if (game.isNewRound()) {
				actionText.setText("You can play cards of any value this turn!");
			}
		} else {
			actionText.setText(game.getCurrentTurnDescription());
		}
	}

	private static final String HUMAN_PLAYER_CARDS_IN_HAND_TEXT = "YOUR HAND: ({0}) cards";
	private static final String AI_PLAYER_CARDS_IN_HAND_TEXT = "has {0} cards";
	private static final String CARDS_IN_PILE_TEXT = "The Pile has {0} cards in it";
	
	private void updateNumberOfCardsTexts() {
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
		updateHumanPlayerNumberOfCardsText();
		
		cardPanesHolder.clear();
		cardsFlowPane.getChildren().clear();
		
		List<Card> cards = game.getHumanPlayer().getCards();
		Collections.sort(cards, new CardDisplayComparator());
		
		List<CardPane> cardPanes = cards.stream()
				.map(card -> new CardPane(card, cardPanesHolder)).collect(Collectors.toList());
		cardsFlowPane.getChildren().addAll(cardPanes);		
	}
	
	@FXML
	public void play(ActionEvent event) {
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
		
		// TODO: turn challenge by AI
		
		if (game.getHumanPlayer().hasNoMoreCards()) {
			displayHumanPlayerWinMessage();
			return;
		} else {
			updateHumanPlayerCardsDisplay();
			updateCardsInPileText();
			takeNextPlayerTurn();
		}
	}

	private void displayHumanPlayerWinMessage() {
		ShowDialog.info(messages.getString("HUMAN_WINNER"));
	}

	private void takeNextPlayerTurn() {
		Player player = game.getNextTurnPlayer();
		if (player instanceof Opponent) {
			game.processTurn(((Opponent)player).takeTurn(game));
			
			updateActionText();
			updateCardsInOpponentHandText(player);
			updateCardsInPileText();
		}
		updateActionControl();
	}

	private void updateCardsInOpponentHandText(Player player) {
		int cardsInHand = player.getCards().size();
		Text text = null;
		if (player.getNumber() == 2) {
			text = player2CardsInHandText;
		} else if (player.getNumber() == 3) {
			text = player3CardsInHandText;
		} else if (player.getNumber() == 4) {
			text = player4CardsInHandText;
		}
		text.setText(MessageFormat.format(AI_PLAYER_CARDS_IN_HAND_TEXT, cardsInHand));
	}

	private boolean hasNoCardValueSelected() {
		return cardValueComboBox.getValue() == null;
	}

	private boolean hasNoCardsSelected() {
		return cardPanesHolder.hasNoSelectedCards();
	}

	@FXML
	public void challenge(ActionEvent event) {
		Player turnPlayer = game.getTurnPlayer();
		if (game.challengeCurrentTurn(game.getHumanPlayer())) {
			String message = messages.getString("HUMAN_CHALLENGER_AI_IS_CHEATING");
			ShowDialog.info(MessageFormat.format(message, turnPlayer.getName()));
			updateCardsInOpponentHandText(turnPlayer);
		} else {
			String message = messages.getString("HUMAN_CHALLENGER_AI_IS_NOT_CHEATING");
			ShowDialog.info(MessageFormat.format(message, turnPlayer.getName()));
			if (turnPlayer.hasNoMoreCards()) {
				displayAIPlayerWinMessage(turnPlayer);
				return;
			}
			updateHumanPlayerCardsDisplay();
		}
		
		takeNextPlayerTurn();
	}
	
	@FXML
	public void letItSlide(ActionEvent event) {
		if (game.getTurnPlayer().hasNoMoreCards()) {
			displayAIPlayerWinMessage(game.getTurnPlayer());
			return;
		}
		
		takeNextPlayerTurn();
	}
	
	private void displayAIPlayerWinMessage(Player player) {
		String message = messages.getString("AI_WINNER");
		ShowDialog.info(MessageFormat.format(message, player.getName()));
	}

	private class CardDisplayComparator implements Comparator<Card> {

		@Override
		public int compare(Card o1, Card o2) {
			int result = o1.getValue().compareTo(o2.getValue());
			if (result == 0) {
				result = o1.getSuit().compareTo(o2.getSuit());
			}
			return result;
		}
		
	}
	
}