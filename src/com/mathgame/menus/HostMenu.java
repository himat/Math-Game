package com.mathgame.menus;

import javax.swing.*;

import com.mathgame.guicomponents.GameButton;
import com.mathgame.math.MathGame;
import com.mathgame.network.Game;
import com.mathgame.network.GameManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The HostMenu class represents the multiplayer menu for hosting new games.
 * <p>
 * The menu fills the entire window and contains options for the # of players, type of game, scoring method, # of rounds (up to 5 for now), and difficulty.
 * Once a game is created, the host goes to the game screen, but the game does not start unless enough opponents join
 * @author David Schildkraut, Roland Fong
 */
public class HostMenu extends JPanel implements ActionListener {

	//TODO Link variables from menu to other variables (i.e. difficulty & number type variables)
	//TODO Work on actions to put choice into var 
	//TODO Get user input for name of game & create the location to do that
	
	private static final long serialVersionUID = -5507870440809320516L;
	
	MultiMenu multiMenu;
	
	int players; // # of players (currently 2)
	int rounds; // # of rounds (1-5)
	String type; // number type (Fraction, Decimal, Integer)
	String scoring; // scoring (Complexity, Speed, Mix)
	String diff; // difficulty (easy, Medium, HARD)
	
	static final String BACKGROUND_FILE = "/images/background2.png";
	
	private static ImageIcon background;
	
	static {
		background = new ImageIcon(OptionMenu.class.getResource(BACKGROUND_FILE));
	}
	
	private ButtonGroup diffGroup; // Easy, Medium, Hard
	private ButtonGroup scoringGroup; // Complexity, Speed, Mix
	private ArrayList<JCheckBox> types; // Integer, Decimal, Fraction (To be added: Negative, Exponents, Log)
	private ArrayList<JRadioButton> diffs;
	private ArrayList<JRadioButton> scorings;
	
	//TODO Make these strings in MathGame class for use in all classes
	private String[] typeNames = {"Integer", "Decimal", "Fraction"};
	private String[] diffNames = {"Easy", "Medium", "Hard"};
	private String[] scoringNames = {"Complexity", "Speed", "Mix"}; // Mixed scoring is a combination of speed and complexity
	
	private JSpinner roundsSpinner; // Displaying number of rounds
	// private JSpinner playersSpinner; // Displaying number of players
	private SpinnerNumberModel roundsModel;
	private SpinnerNumberModel playersModel;
	
	private Map<String, JToggleButton> buttonMap; // Associate buttons with their names for easy locating
	
	// JPanel playerPanel;
	private JPanel scoringPanel;
	private JPanel roundPanel;
	private JPanel typePanel;
	private JPanel diffPanel;
	
	// private JLabel playersLabel;
	private JLabel scoringLabel;
	private JLabel typeLabel;
	private JLabel roundLabel;
	private JLabel diffLabel;
	
	private GameButton cancel;
	private GameButton finish;
	
	private GridBagConstraints gbc;

	public HostMenu() {
		
		this.setLayout(new GridBagLayout());
		multiMenu = (MultiMenu)(MathGame.getMenu(MathGame.Menu.MULTIMENU));
		//TODO Use typemanager?
		
		// Set size
		Dimension size = getPreferredSize();
		size.width = MathGame.getAppWidth();
		size.height = MathGame.getAppHeight();
		setPreferredSize(size);
		
		gbc = new GridBagConstraints();
		
		// playersLabel = new JLabel("# Players:");
		scoringLabel = new JLabel("Scoring:");
		typeLabel = new JLabel("Number Type:");
		roundLabel = new JLabel("# Rounds:");
		diffLabel = new JLabel("Difficulty:");
		
		finish = new GameButton("Finish");
		finish.addActionListener(this);
		
		cancel = new GameButton("Cancel");
		cancel.addActionListener(this);
		
		// Button creation
		buttonMap = new HashMap<String, JToggleButton>();
		
		// initPlayerPanel();
		initTypePanel();
		initDiffPanel();
		initRoundPanel();
		initScoringPanel();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		// add(playerPanel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(typePanel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(diffPanel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(roundPanel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(scoringPanel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		add(finish, gbc);
		gbc.gridx = 2;
		gbc.gridy = 2;
		add(cancel, gbc);
		
		// Default values
		types.get(0).setSelected(true);
		diffs.get(0).setSelected(true);
		scorings.get(0).setSelected(true);
	}
	
	// Player count is being restricted to 2
	/*private void initPlayerPanel() {
		playerPanel = new JPanel();
		playersModel = new SpinnerNumberModel(2, 2, 6, 1); // 2 to 6 players, default 2
		playersSpinner = new JSpinner(playersModel);
		playersSpinner.setFont(MathGame.eurostile24);
		playersLabel = new JLabel("# Players:");
		playersLabel.setFont(MathGame.eurostile24);
		playerPanel.add(playersLabel);
		playerPanel.add(playersSpinner);
	}*/
	
	/**
	 * Initialize the type panel, allowing players to choose the type of game
	 */
	private void initTypePanel() {
		types = new ArrayList<JCheckBox>();
		for(String s : typeNames) {
			types.add(new JCheckBox(s));
		}
		typePanel = new JPanel();
		typeLabel = new JLabel("Number Type:");
		typeLabel.setFont(MathGame.eurostile24);
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.PAGE_AXIS));
		typePanel.add(typeLabel);
		typePanel.setOpaque(false);
		for(int i = 0; i < types.size(); i++) {
			typePanel.add(types.get(i));
			buttonMap.put(typeNames[i], types.get(i));
			types.get(i).setActionCommand(typeNames[i]);
			types.get(i).setOpaque(false);
			// types.get(i).addActionListener(this);
		}
	}
	
	/**
	 * Initialize the difficulty panel, allowing players to choose the difficulty of the game
	 */
	private void initDiffPanel() {
		diffs = new ArrayList<JRadioButton>();
		for(String s : diffNames) {
			diffs.add(new JRadioButton(s));
		}
		diffPanel = new JPanel();
		diffGroup = new ButtonGroup();
		diffLabel = new JLabel("Difficulty:");
		diffLabel.setFont(MathGame.eurostile24);;
		diffPanel.setLayout(new BoxLayout(diffPanel, BoxLayout.PAGE_AXIS));
		diffPanel.add(diffLabel);
		diffPanel.setOpaque(false);
		for(int i = 0; i < diffs.size(); i++) {
			diffGroup.add(diffs.get(i));
			diffPanel.add(diffs.get(i));
			buttonMap.put(diffNames[i], diffs.get(i));
			diffs.get(i).setActionCommand(diffNames[i]);
			diffs.get(i).setOpaque(false);
			// diffs.get(i).addActionListener(this);
		}
	}
	
	/**
	 * Initialize the round panel, allowing players to choose the number of rounds in the game
	 */
	private void initRoundPanel() {
		roundPanel = new JPanel();
		// Choose from 1 to 5 rounds, with a default of 3 rounds
		roundsModel = new SpinnerNumberModel(3, 1, 5, 1);
		roundsSpinner = new JSpinner(roundsModel);
		roundsSpinner.setFont(MathGame.eurostile24);
		roundLabel = new JLabel("# Rounds:");
		roundLabel.setFont(MathGame.eurostile24);
		roundPanel.add(roundLabel);
		roundPanel.add(roundsSpinner);
	}
	
	/**
	 * Initialize the scoring panel, allowing players to choose the scoring method of the game
	 */
	private void initScoringPanel()	{
		scorings = new ArrayList<JRadioButton>();
		for(String s : scoringNames) {
			scorings.add(new JRadioButton(s));
		}
		scoringPanel = new JPanel();
		scoringGroup = new ButtonGroup();
		scoringPanel.setLayout(new BoxLayout(scoringPanel, BoxLayout.PAGE_AXIS));
		scoringLabel = new JLabel("Scoring:");
		scoringLabel.setFont(MathGame.eurostile24);
		scoringPanel.add(scoringLabel);
		scoringPanel.setOpaque(false);
		for(int i = 0; i < scorings.size(); i++) {
			scoringGroup.add(scorings.get(i));
			scoringPanel.add(scorings.get(i));
			buttonMap.put(scoringNames[i], scorings.get(i));
			scorings.get(i).setActionCommand(scoringNames[i]);
			scorings.get(i).setOpaque(false);
			// scorings.get(i).addActionListener(this);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == finish)	{
			addGame();
			MathGame.getCardPanel().hideCards(); // Hide cards until next player joins
			MathGame.showMenu(MathGame.Menu.GAME); // Go to the game (but should it wait?)
			Thread waitForPlayer = new Thread()	{
					public void run() {
						while(!MathGame.getGameManager().gameFilled()) {
							System.out.println("waiting"); // Wait until the game is filled
						}
						MathGame.getCardPanel().showCards();
						MathGame.getSidePanel().startTimer(type);
						MathGame.getSidePanel().setUpMultiplayer();
						MathGame.getGameManager();
						
						//Get the names of the other players
						int numPlayers = MathGame.getGameManager().getGame().getNumberOfPlayers();
						for(int i=1; i<=numPlayers; i++)
						{							
							MathGame.getGameManager().getGame().addPlayer(GameManager.getMatchesAccess().getPlayerName(MathGame.getGameManager().getGame().getID(), i));
						}
					}
			};
			waitForPlayer.start();
			MathGame.getUser().setPlayerID(1);
		}
		else if(e.getSource() == cancel) {
			MathGame.showMenu(MathGame.Menu.MULTIMENU); // Return to the multiplayer menu
		}
	}
	
	/**
	 * Adds a new game
	 */
	public void addGame() {
		this.setVisible(false);
		// players = (Integer) playersSpinner.getModel().getValue();
		players = 2;
		rounds = (Integer) roundsSpinner.getModel().getValue();
		diff = diffGroup.getSelection().getActionCommand();
		scoring = scoringGroup.getSelection().getActionCommand();

		//TODO Set capability for multiple (instead of first one picked)
		if(buttonMap.get("Integer").isSelected()) {
			multiMenu.chooseInteger();
			type = "Integer";
		} else if(buttonMap.get("Decimal").isSelected()) {
			multiMenu.chooseDecimal();	
			type = "Decimal";
		} else if(buttonMap.get("Fraction").isSelected())	{
			multiMenu.chooseFraction();
			type = "Fraction";
		} else {
			// The default game type is "Integer" (for now)
			multiMenu.chooseInteger();
			type = "Integer";
		}
		
		// Etc.
		System.out.println("MULTIPLAYER GAME SPECS: "								
				+ "\n\tPLAYERS: "+players
				+ "\n\tROUNDS: "+rounds
				+ "\n\tDIFF: "+diff
				+ "\n\tSCORING: "+scoring
				+ "\n\tTYPE: "+type
				+ "\n\tNUMPLAYERS: "+players);
		
		multiMenu.addGame(new Game(-1, players, type, scoring, diff, rounds));
		
		MathGame.getTypeManager().setType(type);
		MathGame.getTypeManager().randomize();
		// FOR DEBUGGING PURPOSES ONLY: 
		MathGame.showMenu(MathGame.Menu.MULTIMENU);
		//TODO Go directly to game and make sure game waits for another player
		System.out.println("CREATED NEW GAME");
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.drawImage(background.getImage(), 0, 0, HostMenu.this);
	}

}
