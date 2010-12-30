package com.johnwayner.pokerloganalyzer;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PokerLogAnalyzerApp {

	private JFrame frmPokerLogAnalyzer;
	private PokerLogDatabase db = null;
	private JMenuItem mntmFindByName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PokerLogAnalyzerApp window = new PokerLogAnalyzerApp();
					window.frmPokerLogAnalyzer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PokerLogAnalyzerApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPokerLogAnalyzer = new JFrame();
		frmPokerLogAnalyzer.setTitle("Poker Log Analyzer");
		frmPokerLogAnalyzer.setBounds(100, 100, 450, 300);
		frmPokerLogAnalyzer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmPokerLogAnalyzer.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoadFiles = new JMenuItem("Load Files");
		mntmLoadFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(chooser.showDialog(frmPokerLogAnalyzer, "Choose directory that contains log files...") == JFileChooser.APPROVE_OPTION) {
					db = new PokerLogDatabase(chooser.getSelectedFile());
					mntmFindByName.setEnabled(db.isLoaded());
					
					int handActionCount = 0;
					for(PokerHand hand : db.getHands()) {
						handActionCount += hand.getActions().size();
					}
					
					System.out.println("DB has " + db.getHands().size() + " hands and " +
							handActionCount + " hand actions.");
				}
			}
		});
		mntmLoadFiles.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK));
		mnFile.add(mntmLoadFiles);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_MASK));
		mnFile.add(mntmQuit);
		
		JMenu mnQuery = new JMenu("Query");
		menuBar.add(mnQuery);
		
		mntmFindByName = new JMenuItem("Blinds played...");
		mntmFindByName.setEnabled(false);
		mntmFindByName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(null == db) {
					return;
				}
				
				Collections.<Player>sort(db.getPlayers());
				
				Player player = (Player)JOptionPane.showInputDialog(
						frmPokerLogAnalyzer,
						"Choose player:",
						"Choose player",
						JOptionPane.PLAIN_MESSAGE,
						(Icon)null,
						db.getPlayers().toArray(),
						db.getPlayers().get(0));
				
				List<PokerHand> hands = db.getHandsForPlayer(player);
				int handsPlayed = 0;
				int bigBlindsPlayed = 0;
				int smallBlindsPlayed = 0;
				
				for(PokerHand h : hands) {
					for(HandAction ac : h.getActions()) {
						Player acPlayer = ac.getPlayer();
						if(null != acPlayer) {
							if(ac.getPlayer().equals(player)) {
								switch(ac.getAction()) {
								case CALL:
								case CALL_ALL_IN:
								case RAISE:
								case RAISE_ALL_IN:
								case BET:
								case BET_ALL_IN:
									//they played.
									handsPlayed++;
									Seat s = h.getSeatForPlayerName(player.getName());
									if(s.isBigBlind()) {
										bigBlindsPlayed++;
									} else if(s.isSmallBlind()) {
										smallBlindsPlayed++;
									}
								}
							}
						}
					}
				}
				
				JOptionPane.showMessageDialog(frmPokerLogAnalyzer,
					    player.getName() + " played " + 
					    		(handsPlayed==0?
					    			"no hands!":
					    			handsPlayed + " hands.\n" +
					    			bigBlindsPlayed + " as big blind. (" + ((bigBlindsPlayed*100)/handsPlayed) + "%)\n" +
					    			smallBlindsPlayed + " as small blind. (" + ((smallBlindsPlayed*100)/handsPlayed) + "%)\n" +
					    			(bigBlindsPlayed+smallBlindsPlayed) + " as any blind. (" + (((bigBlindsPlayed+smallBlindsPlayed)*100)/handsPlayed) + "%)"), 
					    "Blind Stats", 0);

				
			}
		});
		mntmFindByName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.ALT_MASK));
		mnQuery.add(mntmFindByName);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.setEnabled(false);
		mntmHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_MASK));
		mnHelp.add(mntmHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setEnabled(false);
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_MASK));
		mnHelp.add(mntmAbout);
		frmPokerLogAnalyzer.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		initDataBindings();
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	protected void initDataBindings() {
	}
}
