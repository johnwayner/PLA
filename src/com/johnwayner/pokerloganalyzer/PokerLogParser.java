package com.johnwayner.pokerloganalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.johnwayner.pokerloganalyzer.HandAction.Action;

public class PokerLogParser {
	public static void parseFile(File f, List<PokerHand> hands, List<Player> players) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			PokerHand hand;
			int handNum = 1;
			do {
				hand = parseHand(reader, players);
				if(null != hand) {
					hands.add(hand);
				} else {
					System.out.println(f.getName() + " : failed to load hand number: " + handNum);
					while(!reader.readLine().isEmpty());//consume the rest of this hand.
				}
				handNum++;
			} while(true);
		} catch (NotALogFileException e) {
			System.out.println("Ignoring file: " + f.getAbsolutePath());
		} catch (EndOfFileException e) {
			//ignore.
		} catch (Exception e) {
			//No biggie... probably just a file we don't understand.
//			System.out.println("Ignoring file: " + f.getAbsolutePath());
			e.printStackTrace();
		}
	}
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private static final Pattern headerLinePattern =
		Pattern.compile(".*(PokerStars Game .*?):  ?(.*?) - (20.*) CT.*");
	private static final Pattern tableLinePattern = 
		Pattern.compile("Table '(.*?)' (\\d+)-max Seat #(\\d+) is the button");
	private static final Pattern seatLinePattern =
		Pattern.compile("Seat (\\d+): (.*?) \\(\\$?(.*) in chips\\).*");
	private static final Pattern blindLinePattern =
		Pattern.compile("(.*?): posts (big|small) blind \\$?(.*).*");
	private static final Pattern dealtCardsLinePattern =
		Pattern.compile("Dealt to (.*?) \\[(..) (..)\\]");
	private static final Pattern summaryLinePattern =
		Pattern.compile("\\*\\*\\* SUMMARY.*");
	
	
	private static PokerHand parseHand(BufferedReader reader, List<Player> players) throws Exception {
		PokerHand hand = new PokerHand();
		
		//read any empty lines
		String line;		
		do {
			line = reader.readLine();
		} while((null!=line) && (line.isEmpty()));	
		if(null == line) {
			throw new EndOfFileException();
		}
		
		Matcher matcher;
		//Hand header
		matcher = headerLinePattern.matcher(line);
		
		if(!matcher.matches()) {
			throw new NotALogFileException();
		}
		
		hand.setId(matcher.group(1));
		hand.setDescription(matcher.group(2));
		hand.setDate(dateFormat.parse(matcher.group(3)));
		
		do {
			line = reader.readLine();
			
			matcher = tableLinePattern.matcher(line);
			if(matcher.matches()) {
				hand.setTableName(matcher.group(1));
				hand.setMaxSeats(Integer.parseInt(matcher.group(2)));
				hand.setButtonSeat(Integer.parseInt(matcher.group(3)));
				continue;
			}
			
			matcher = seatLinePattern.matcher(line);
			if(matcher.matches()) {
				Seat seat = new Seat(Integer.parseInt(matcher.group(1)), findPlayer(matcher.group(2), players), 
					new BigDecimal(matcher.group(3)));
				hand.getSeats().add(seat);
				continue;
			}
			
			matcher = blindLinePattern.matcher(line);
			if(matcher.matches()) {
				Seat s = hand.getSeatForPlayerName(matcher.group(1));
				if(matcher.group(2).equals("big")) {
					s.setBigBlind(true);
				} else {
					s.setSmallBlind(true);
				}
				continue;
			}
			
			matcher = dealtCardsLinePattern.matcher(line);
			if(matcher.matches()) {
				Seat s = hand.getSeatForPlayerName(matcher.group(1));
				s.setCards(new String[] { matcher.group(2), matcher.group(3)});
				continue;
			}
			
			//check actions
			HandAction ha = null;
			for(Action a : Action.values()) {
				ha = a.getHandAction(line, hand);
				if(null != ha) {
					hand.getActions().add(ha);
					break;
				}
			}
			if(null != ha) {
				continue;
			}
			
			
			matcher = summaryLinePattern.matcher(line);
			if(matcher.matches()) {
				//read all the lines until blank to build summary.
				do {
					line = reader.readLine();
					if(!line.isEmpty()) {
						hand.addSummaryLine(line);
					}
				} while(!line.isEmpty());
			}
			
			System.out.println("Didn't match: " + line);
		} while(!line.isEmpty());
		
		
		//Sanity Check stuff.
		if(hand.getSeats().size() < 1) {
			return null;
		}
		
		return hand;
	}
	
	private static Player findPlayer(String playerName, List<Player> players) {
		for(Player p : players) {
			if(p.getName().equals(playerName)) {
				return p;
			}
		}
		Player p =  new Player(playerName);
		players.add(p);
		return p;
	}
	
	public static class NotALogFileException extends Exception {

		public NotALogFileException() {
			super();
		}
	}
	
	public static class EndOfFileException extends Exception {
		public EndOfFileException() {
			super();
		}
	}
}
