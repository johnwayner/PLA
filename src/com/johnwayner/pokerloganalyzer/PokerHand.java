package com.johnwayner.pokerloganalyzer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PokerHand {
	private String id;
	private String description;
	private Date date;
	private String tableName;
	private int maxSeats;
	private int buttonSeat;
	private List<Seat> seats = new ArrayList<Seat>();
	private List<HandAction> actions = new ArrayList<HandAction>();
	private List<String> summary = new ArrayList<String>();
	
	public PokerHand() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getMaxSeats() {
		return maxSeats;
	}

	public void setMaxSeats(int maxSeats) {
		this.maxSeats = maxSeats;
	}

	public int getButtonSeat() {
		return buttonSeat;
	}

	public void setButtonSeat(int buttonSeat) {
		this.buttonSeat = buttonSeat;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public List<HandAction> getActions() {
		return actions;
	}

	public void setActions(List<HandAction> actions) {
		this.actions = actions;
	}
	
	public void addSummaryLine(String line) {
		this.summary.add(line);
	}
	
	public Seat getSeatForPlayerName(String name) {
		for(Seat s : seats) {
			if(s.getPlayer().getName().equals(name)) {
				return s;
			}
		}
		return null;
	}
}
