package com.johnwayner.pokerloganalyzer;

import java.math.BigDecimal;

public class Seat {
	private int number;
	private Player player;
	private BigDecimal startingChips;
	private String[] cards;
	private boolean isBigBlind = false;
	private boolean isSmallBlind = false;
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public BigDecimal getStartingChips() {
		return startingChips;
	}
	public void setStartingChips(BigDecimal startingChips) {
		this.startingChips = startingChips;
	}
	public Seat(int number, Player player, BigDecimal startingChips) {
		super();
		this.number = number;
		this.player = player;
		this.startingChips = startingChips;
	}
	public String[] getCards() {
		return cards;
	}
	public void setCards(String[] cards) {
		this.cards = cards;
	}
	public boolean isBigBlind() {
		return isBigBlind;
	}
	public void setBigBlind(boolean isBigBlind) {
		this.isBigBlind = isBigBlind;
	}
	public boolean isSmallBlind() {
		return isSmallBlind;
	}
	public void setSmallBlind(boolean isSmallBlind) {
		this.isSmallBlind = isSmallBlind;
	}
	
	
}
