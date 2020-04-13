import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")	
public class GameCourt extends JPanel {
	// the state of the game logic
	private Tile[][] board;
	
	//tells us what to draw
	private int state = 2;
	
	//stores high score names and their score
	private Map<String, Integer> names;

	public boolean playing = false; // whether the game is running
	private int time = 200;
	private JLabel status; // Current status text, i.e. "Running..."

	private ScoreMaker highscores;

	// Game constants
	public static final int COURT_WIDTH = 400;
	public static final int COURT_HEIGHT = 400;

	// Update intervals for timers, in milliseconds
	public static final int INTERVAL1 = 100;
	public static final int INTERVAL2 = 1000;

	public GameCourt(JLabel status) throws IOException {
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		//sets up the high score map by reading a file
		highscores = ScoreMaker.makeFromFile("files/Scores.txt");
	    names = new TreeMap<String, Integer>();
	    List<Integer> nums = highscores.getTimes();
	    List<String> names2 = highscores.getNames();
	    for (int i = 0; i < nums.size(); i++) {
	    	names.put(names2.get(i), nums.get(i));
	    }

		// The timer is an object which triggers an action periodically with the given
		// INTERVAL. 
		Timer timer1 = new Timer(INTERVAL1, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick1();
			}
		});
		Timer timer2 = new Timer(INTERVAL2, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					tick2();
				} catch (IOException e1) {

				}
			}
		});
		timer1.start();
		timer2.start();

		// Enable keyboard focus on the court area.
		setFocusable(true);

		//Mouse listener responds to mouse clicks within the board
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int ex = e.getX();
				int ey = e.getY();
				
				//brings from intro to the game
				if (state == 2) {
					state = 0;
				} else {
					for (int i = 0; i < COURT_WIDTH / 20; i++) {
						for (int j = 0; j < COURT_HEIGHT / 20; j++) {
							if (ex >= i * 20 && ex < (i + 1) * 20 && ey >= j * 20 && ey < (j + 1) * 20) {
								//You lose if you click on a mine
								if (board[i][j].getIsMine()) {
									status.setText("Game Over!");
									playing = false;
									showAll();
									repaint();
								} else {
									//show the specific tile clicked
									show(board[i][j]);
									
									//This is what happens when you win
									if (won() && playing) {
										try {
											playing = false;
											String s = JOptionPane.showInputDialog("Please enter a name");
											if (s != null) {
												highscores.write(time, "files/Scores.txt", s);
												names.put(s, time);
												showAll();
												status.setText("You Won :D!!!");
												repaint();
											}
										} catch (IOException e2) {

										}
									}
								}
							}
						}
					}
				}
			}

		});

		this.status = status;
	}
	
	
	//draws the initial board by filling in each square with a bomb or a numbered tile
	public void drawBoard() {
		placeMines(35);
		for (int i = 0; i < COURT_WIDTH / 20; i++) {
			for (int j = 0; j < COURT_HEIGHT / 20; j++) {
				if (board[i][j] == null) {
					int mines = 0;
					for (int ii = i - 1; ii <= i + 1; ii++) {
						for (int jj = j - 1; jj <= j + 1; jj++) {
							if (ii >= 0 && jj >= 0 && ii < COURT_WIDTH / 20 && jj < COURT_HEIGHT / 20) {
								try {
									if (board[ii][jj].getIsMine()) {
										mines++;
									}
								} catch (NullPointerException e) {
								}
							}
						}
					}
					board[i][j] = new NumberTile(i * 20, j * 20, COURT_WIDTH, COURT_HEIGHT, mines);
				}
			}
		}
	}

	//randomly places a certain number of mines within the board (this is done first)
	public void placeMines(int num) {
		int max = num;
		int change = num;
		for (int i = 0; i < COURT_WIDTH / 20; i++) {
			for (int j = 0; j < COURT_HEIGHT / 20; j++) {
				if (Math.random() > 0.9 && change > 0 && board[i][j] == null) {
					board[i][j] = new Mine(i * 20, j * 20, COURT_WIDTH, COURT_HEIGHT);
					change--;
				}
			}
		}
		if (change > 0) {
			placeMines(max - change);
		}
	}

	//Recursive function: shows the tile clicked and recursively calls the function on the tiles around it
	public void show(Tile t) {
		t.setWasClicked(true);
		if (((NumberTile) t).getNumMines() == 0) {
			for (int i = (t.getPx() / 20) - 1; i <= (t.getPx() / 20) + 1; i++) {
				for (int j = (t.getPy() / 20) - 1; j <= (t.getPy() / 20) + 1; j++) {
					if (i >= 0 && j >= 0 && i < COURT_WIDTH / 20 && j < COURT_HEIGHT / 20
							&& !board[i][j].getWasClicked()) {
						show(board[i][j]);
					}
				}
			}
		}

	}

	//Shows the whole board when the game is over
	public void showAll() {
		for (int i = 0; i < COURT_WIDTH / 20; i++) {
			for (int j = 0; j < COURT_HEIGHT / 20; j++) {
				board[i][j].setWasClicked(true);
			}
		}
	}

	//Win condition
	public boolean won() {
		for (int i = 0; i < COURT_WIDTH / 20; i++) {
			for (int j = 0; j < COURT_HEIGHT / 20; j++) {
				if ((!board[i][j].getIsMine()) && !(board[i][j].getWasClicked())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() throws IOException {
		this.board = new Tile[COURT_WIDTH / 20][COURT_HEIGHT / 20];
		drawBoard();
		highscores = ScoreMaker.makeFromFile("files/Scores.txt");
		this.time = 200;
		

		playing = true;
		status.setText("");

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	/**
	 * These methods called every time the timers defined in the constructor
	 * trigger.
	 */
	
	//constantly updates the boaard
	void tick1() {
		if (playing) {
			repaint();
		}
	}

	//ticks down time in one second intervals
	void tick2() throws IOException {
		if (playing && state == 0) {
			this.time--;
		}
		repaint();
		if (this.time == 0) {
			status.setText("You ran out of time my guy :/");
			playing = false;
			showAll();
			repaint();
		}
	}

	//Getters
	public int getTime() {
		return this.time;
	}

	public boolean getPlaying() {
		return this.playing;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Case for the game and board itself
		if (state == 0) {
			for (int i = 0; i < COURT_WIDTH / 20; i++) {
				for (int j = 0; j < COURT_HEIGHT / 20; j++) {
					if (board[i][j].getWasClicked()) {
						board[i][j].draw1(g);
					} else {
						board[i][j].draw2(g);
					}
				}
			}
		} else if (state == 1) { //Case for the high score table
			List<Integer> times = highscores.getTimes();
			List<String> names1 = highscores.getNames();
			Collections.sort(times);
			Collections.reverse(times);
			int num = 0;
			int height = 40;
			g.drawString("High Scores: ", 70, height);
			String scores = "";
			List<String> hasSeen = new LinkedList<String>();

			for (int i : times) {
				for (String s : names1) {
					if (num < 20 && names.get(s) == i && !hasSeen.contains(s)) {
						 hasSeen.add(s);
					     scores += s + "-> " + i + " ";
					     g.drawString(scores, 160, height);
					     height += 20;
						 num++;
						 scores = "";
					}
				}
			}
		} else if (state == 2) { //Case for the introduction 
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, 400, 400);
			g.setColor(Color.BLACK);
			g.drawString("Hi there!", 10, 50);
			g.drawString("This is a standard game of Minesweeper. For those who don't ", 10, 80);
			g.drawString("know, minesweeper is a game that consists of a board of ", 10, 100);
			g.drawString("tiles, some of which are safe, and some of which are ", 10, 120);
			g.drawString("dangerous bombs D:", 10, 140);
			g.drawString("To play the game, you have to click on a tile on the ", 10, 170);
			g.drawString("board. If you click on a bomb, you lose. Otherwise, ", 10, 190);
			g.drawString("the tile's number will be revealed. This number ", 10, 210);
			g.drawString("tells you how many bombs surround that tile. If the tile ", 10, 230);
			g.drawString("is empty it will recursively show you the states around it.", 10, 250);
			g.drawString("To win the game, you have to reveal all non-bomb tiles. ", 10, 280);
			g.drawString("Be careful, there is a timer and once it reaches 0, you lose!", 10, 310);
			g.drawString("Good luck and Have fun!", 10, 340);
			g.drawString("To continue to the game, click anywhere on the screen", 10, 370);
		}
	}

	//Change the state of the game (board, high scores, introduction)
	public void setState(int i) {
		if (i >= 0 && i <= 2) {
			this.state = i;
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}

}
