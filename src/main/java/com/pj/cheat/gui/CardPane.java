package com.pj.cheat.gui;

import com.pj.cheat.model.Card;
import com.pj.cheat.util.CardImages;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class CardPane extends StackPane {

	private Card card;
	private Region selectedEffectPane;
	
	public CardPane(Card card, CardPanesHolder holder) {
		this.card = card;
		holder.add(this);
		
		ImageView imageView = new ImageView(CardImages.getImage(card));
		imageView.getStyleClass().add("card");
	    getChildren().add(imageView);
		
		selectedEffectPane = new Region();
	    selectedEffectPane.setPrefSize(70.0, 90.0); // size of card image
	    selectedEffectPane.getStyleClass().add("cardSelected");
	    selectedEffectPane.setVisible(false);
	    getChildren().addAll(selectedEffectPane);
		
	    setOnMouseClicked(e -> {
	    	if (isSelected()) {
	    		selectedEffectPane.setVisible(false);
	    	} else if (!holder.hasMaximumSelectedCards()) { 
				selectedEffectPane.setVisible(true);
	    	}
	    });
	}

	public boolean isSelected() {
		return selectedEffectPane.isVisible();
	}

	public Card getCard() {
		return card;
	}
	
}