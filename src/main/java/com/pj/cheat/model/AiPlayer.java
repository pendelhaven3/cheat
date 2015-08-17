package com.pj.cheat.model;

public abstract class AiPlayer extends Player {

	protected static final int MAX_NUMBER_OF_SAME_VALUE_CARDS = 4;
	
	public AiPlayer(String name) {
		super(name, true);
	}

	public abstract Turn play(Game game);
	
	public abstract boolean willChallenge(Turn turn);

	public abstract void processEvent(Game game, Event event);
	
}