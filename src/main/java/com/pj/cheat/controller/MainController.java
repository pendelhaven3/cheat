package com.pj.cheat.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.pj.cheat.model.AIPlayer;
import com.pj.cheat.model.Card;
import com.pj.cheat.model.Game;
import com.pj.cheat.model.Player;
import com.pj.cheat.util.CardImages;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
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
	
	private Game game;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		game = new Game();
		game.addPlayer(new Player("Human"));
		game.addPlayer(new AIPlayer("Timmy"));
		game.addPlayer(new AIPlayer("Branston"));
		game.addPlayer(new AIPlayer("Chuffer Bob"));
		
		game.start();
		
		updateNumberOfCardsTexts();
		displayHumanPlayerCards();
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

	private void updateNumberOfCardsTexts() {
		int cardsInHand = game.getHumanPlayer().getCards().size();
		cardsInHandText.setText("YOUR HAND: (" + cardsInHand + ") cards");
		
		int player2CardsInHand = game.getPlayers().get(1).getCards().size();
		player2CardsInHandText.setText("has " + player2CardsInHand + " cards");
		
		int player3CardsInHand = game.getPlayers().get(2).getCards().size();
		player3CardsInHandText.setText("has " + player3CardsInHand + " cards");
		
		int player4CardsInHand = game.getPlayers().get(3).getCards().size();
		player4CardsInHandText.setText("has " + player4CardsInHand + " cards");
		
		int cardsInPile = game.getPile().size();
		cardsInPileText.setText("The Pile has " + cardsInPile + " cards in it");
	}

	private void displayHumanPlayerCards() {
		for (Card card : game.getHumanPlayer().getCards()) {
			ImageView imageView = new ImageView();
			imageView.setImage(CardImages.getImage(card));
			cardsFlowPane.getChildren().add(imageView);
		}
	}
	
}