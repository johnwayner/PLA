package com.johnwayner.pokerloganalyzer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandAction {
	public enum Action {
		BET("(.*?): bets \\$?(\\d+(\\.\\d+)?)$", true, true, false),
		BET_ALL_IN("(.*?): bets \\$?(.*?) and is all-in", true, true, false),
		RAISE("(.*?): raises \\$(.*) to \\$\\d+(\\.\\d+)?$", true, true, false),
		RAISE_ALL_IN("(.*?): raises \\$(.*) to .* and is all-in", true, true, false),
		FOLD("(.*?):.*folds.*", true, false, false),
		CALL("(.*?): calls \\$?(\\d+(\\.\\d+)?)$", true, true, false),
		CALL_ALL_IN("(.*?): calls \\$?(.*) and is all-in", true, true, false),
		FLOP("\\*\\*\\* FLOP.*\\[(..) (..) (..)\\]", false, false, true),
		TURN("\\*\\*\\* TURN.*\\[.. .. ..\\] \\[(..)\\]", false, false, true),
		RIVER("\\*\\*\\* RIVER.*\\[.. .. .. ..\\] \\[(..)\\]", false, false, true),
		LEAVE_TABLE("(.*?) leaves the table", true, false, false),
		SHOWS("(.*?): shows \\[(..) (..)\\].*", true, false, true),
		COLLECTED("(.*?) collected \\$(\\d+(\\.\\d+)?) from pot", true, true, false),
		CHECK("(.*?):.*checks.*", true, false, false);
		
		private Pattern pattern;
		private boolean hasAmount;
		private boolean hasCards;
		private boolean hasPlayer;
		private Action(String regex, boolean hasPlayer, boolean hasAmount, boolean hasCards) {
			this.pattern = Pattern.compile(regex);
			this.hasPlayer = hasPlayer;
			this.hasAmount = hasAmount;
			this.hasCards = hasCards;
		}
		
		public Matcher getMatcher(String input) {
			return pattern.matcher(input);
		}
		
		public HandAction getHandAction(String input, PokerHand hand) {
			Matcher matcher = getMatcher(input);
			if(matcher.matches()) {
				Player p = null;
				if(this.hasPlayer) {
					Seat s = hand.getSeatForPlayerName(matcher.group(1));
					if(null == s) {
						System.out.println(this + " cannot find seat for " + matcher.group(1));
						return null;
					}
					p = s.getPlayer();
				}
				
				HandAction ha = new HandAction(p, this);
				
				if(this.hasAmount) {
					try {
						ha.setAmount(new BigDecimal(matcher.group(2)));
					} catch (RuntimeException e) {
						System.out.println(this + " failed to parse: " + matcher.group(2));
						throw e;
					}
				} else if(this.hasCards) {
					for(int i=2;i<matcher.groupCount();i++) {
						ha.addCard(matcher.group(i));
					}
				}
				return ha;
				
			} else {
				return null;
			}
		}
	}
	
	private Player player; 
	private Action action;
	private BigDecimal amount;
	private List<String> cards = new ArrayList<String>();
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public List<String> getCards() {
		return cards;
	}
	public void setCards(List<String> cards) {
		this.cards = cards;
	}
	public void addCard(String card) {
		this.cards.add(card);
	}
	public HandAction(Player player, Action action) {
		super();
		this.player = player;
		this.action = action;
	}

	
	
}
