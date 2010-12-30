package com.johnwayner.pokerloganalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PokerLogDatabase {
	private List<PokerHand> hands = new ArrayList<PokerHand>();
	private List<Player> players = new ArrayList<Player>();	
	
	public PokerLogDatabase(File directoryToLoadFrom) {
		for(File file : directoryToLoadFrom.listFiles()) {
			if(file.isFile() && file.canRead()) {
				PokerLogParser.parseFile(file, hands, players);
			}
		}
	}

	public List<PokerHand> getHands() {
		return hands;
	}

	public void setHands(List<PokerHand> hands) {
		this.hands = hands;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public boolean isLoaded() {
		return hands.size() > 0;
	}

	public List<PokerHand> getHandsForPlayer(Player player) {
		List<PokerHand> playerHands = new ArrayList<PokerHand>();
		for(PokerHand h : this.hands) {
			if(null != h.getSeatForPlayerName(player.getName())) {
				playerHands.add(h);
			}
		}
		return playerHands;
	}
}
