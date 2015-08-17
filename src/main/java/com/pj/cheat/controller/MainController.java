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
import com.pj.cheat.model.AiPlayer;
import com.pj.cheat.model.Card;
import com.pj.cheat.model.Event;
import com.pj.cheat.model.Game;
import com.pj.cheat.model.Level2AiPlayer;
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
	@FXML private Text player2NameText;
	@FXML private Text player3NameText;
	@FXML private Text player4NameText;
	@FXML private Text turnText;
	
	private Game game = new Game();
	private CardPanesHolder cardPanesHolder = new CardPanesHolder();
	private ResourceBundle messages = ResourceBundle.getBundle("messages");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setControlPanesToFreeUpSpaceWhenNotVisible();
		
		game.addPlayer(new Player("You"));
		game.addPlayer(new Level2AiPlayer("Timmy the Tuskaninny"));
		game.addPlayer(new Level2AiPlayer("Branston the Eyrie"));
		game.addPlayer(new Level2AiPlayer("Chuffer Bob the Meerka"));
		
		game.start();
		
		for (AiPlayer aiPlayer : game.getAiPlayers()) {
			Collections.sort(aiPlayer.getCards(), new CardDisplayComparator());
			System.out.println(aiPlayer.getName());
			System.out.println(aiPlayer.getCards());
			System.out.println();
		}
		
		updatePlayerNameTexts();
		updateNumberOfCardsTexts();
		updateHumanPlayerCardsDisplay();
		updateActionText();
		updateActionControl();
		updateTurnText();
	}

	private void updateTurnText() {
		turnText.setText("Turn: " + game.getTurnPlayer().getName());
	}

	private void updatePlayerNameTexts() {
		player2NameText.setText(game.getPlayers().get(1).getName());
		player3NameText.setText(game.getPlayers().get(2).getName());
		player4NameText.setText(game.getPlayers().get(3).getName());
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
			cardValueComboBox.setValue(null);
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
		
		updateHumanPlayerCardsDisplay();
		updateCardsInPileText();
		
		processAiChallengersForHumanTurn();
		
		if (game.getHumanPlayer().hasNoMoreCards()) {
			displayHumanPlayerWinMessage();
			return;
		} else {
			takeNextPlayerTurn();
		}
	}

	private void processAiChallengersForHumanTurn() {
		if (game.hasCurrentTurnAIChallengers()) {
			Player challenger = game.getRandomCurrentTurnAIChallenger();
			if (game.challengeCurrentTurn(challenger)) {
				String message = messages.getString("AI_CHALLENGER_HUMAN_IS_CHEATING");
				ShowDialog.info(MessageFormat.format(message, challenger.getName()));
				updateHumanPlayerCardsDisplay();
			} else {
				String message = messages.getString("AI_CHALLENGER_HUMAN_IS_NOT_CHEATING");
				ShowDialog.info(MessageFormat.format(message, challenger.getName()));
				updateCardsInOpponentHandText(challenger);
			}
			updateCardsInPileText();
			game.notifyAiPlayers(Event.END_ROUND);
		}
	}

	private void displayHumanPlayerWinMessage() {
		ShowDialog.info(messages.getString("HUMAN_WINNER"));
	}

	private void takeNextPlayerTurn() {
		game.notifyAiPlayers(Event.END_TURN);
		
		Player player = game.getNextTurnPlayer();
		if (player instanceof AiPlayer) {
			game.processTurn(((AiPlayer)player).play(game));
			
			updateCardsInOpponentHandText(player);
			updateCardsInPileText();
		}
		updateActionText();
		updateActionControl();
		updateTurnText();
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
				displayAiPlayerWinMessage(turnPlayer);
				return;
			}
			updateHumanPlayerCardsDisplay();
		}
		
		game.notifyAiPlayers(Event.END_ROUND);
		takeNextPlayerTurn();
	}
	
	@FXML
	public void letItSlide(ActionEvent event) {
		processAiChallengersForAiTurn();
		
		if (game.getTurnPlayer().hasNoMoreCards()) {
			displayAiPlayerWinMessage(game.getTurnPlayer());
			return;
		}
		
		takeNextPlayerTurn();
	}
	
	private void processAiChallengersForAiTurn() {
		if (game.hasCurrentTurnAIChallengers()) {
			Player turnPlayer = game.getTurnPlayer();
			Player challenger = game.getRandomCurrentTurnAIChallenger();
			if (game.challengeCurrentTurn(challenger)) {
				String message = messages.getString("AI_CHALLENGER_AI_IS_CHEATING");
				ShowDialog.info(MessageFormat.format(message, challenger.getName(), turnPlayer.getName()));
				updateCardsInOpponentHandText(turnPlayer);
			} else {
				String message = messages.getString("AI_CHALLENGER_AI_IS_NOT_CHEATING");
				ShowDialog.info(MessageFormat.format(message, challenger.getName(), turnPlayer.getName()));
				updateCardsInOpponentHandText(challenger);
			}
			updateCardsInPileText();
			game.notifyAiPlayers(Event.END_ROUND);
		}
	}

	private void displayAiPlayerWinMessage(Player player) {
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