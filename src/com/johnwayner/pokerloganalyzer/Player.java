package com.johnwayner.pokerloganalyzer;

import java.util.ArrayList;
import java.util.List;


public class Player implements Comparable {
	private String name;
	private List<PokerHand> hands = new ArrayList<PokerHand>();
	
	
	public Player(String name) {
		super();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PokerHand> getHands() {
		return hands;
	}
	public void setHands(List<PokerHand> hands) {
		this.hands = hands;
	}
	
	@Override
	public String toString() {
		return name;
	}
	@Override
	public int compareTo(Object arg0) {
		return this.name.toLowerCase().compareTo(((Player)arg0).getName().toLowerCase());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(null == obj) {
			return false;
		}
		if(obj instanceof Player) {
			return ((Player) obj).getName().equals(this.getName());
		} else {
			return obj.equals(this);
		}
	}
}
