/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

import java.util.*;
import java.util.List;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	
	GameState.Player playerTurn = GameState.Player.P1;
	
	boolean aiPlaying = true;
	
	boolean aiVsAi = false;
	
	Ai ai = new Ai(GameState.Player.P2, 1);
	Ai aiPartner = new Ai(GameState.Player.P1, 1);
	
	List<Integer> dice = new ArrayList<Integer>();
	List<Integer> curDice = new ArrayList<Integer>();
	List<Integer> lastDice = new ArrayList<Integer>();

	//private List<Integer> test1 = new ArrayList<Integer>();
	//private List<Integer> test2 = new ArrayList<Integer>();
	
	
	//the game state we will be using
	private GameState gameState;
	
    // the board pieces
	
	private MoveablePiece[][] playerPieces = new MoveablePiece[2][15];
	
    //private MoveablePiece[] player1Pieces = new MoveablePiece[15];
    //private MoveablePiece[] player2Pieces = new MoveablePiece[15];

    public boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int COURT_WIDTH = 1000;
    public static final int COURT_HEIGHT = 600;
    
  //temp test piece will be removed ------------------------------------------------------------------------------------------------------------
    //private MoveablePiece testPiece = new MoveablePiece(COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
    //private MoveablePiece testPiece2 = new MoveablePiece(COURT_WIDTH, COURT_HEIGHT, Color.BLACK);

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;

    public GameCourt(JLabel status) {
    	
    	//sets a basic value for dice
    	dice.add(0);
    	dice.add(0);
    	
    	//testing space
    	/*
    	List<List<Move>> moves = new ArrayList<List<Move>>();
    	List<Move> move1 = new ArrayList<Move>();
    	move1.add(new Move(8, 3));
    	move1.add(new Move(6, 3));
    	moves.add(move1);
    	List<Move> move2 = new ArrayList<Move>();
    	move2.add(new Move(6, 3));
    	move2.add(new Move(8, 3));
    	
    	System.out.println("BIG TEST -------------------------------");
    	
    	System.out.println("contains: " + moves.contains(move2));
    	
    	System.out.println("contains permutation: " + GameState.containsMovePermutation(moves, move2));
    	*/
    	
    	//sets up the game state
    	
    	System.out.println("setting up");
    	
    	//for testing the bearing off
    	/*test1.add(24);
    	test1.add(24);
    	test1.add(23);
    	test1.add(23);
    	test1.add(23);
    	test1.add(22);
    	test1.add(22);
    	test1.add(22);
    	test1.add(21);
    	test1.add(21);
    	test1.add(20);
    	test1.add(20);
    	test1.add(19);
    	test1.add(19);
    	test1.add(19);
    	
    	test2.add(1);
    	test2.add(1);
    	test2.add(2);
    	test2.add(2);
    	test2.add(2);
    	test2.add(3);
    	test2.add(3);
    	test2.add(4);
    	test2.add(4);
    	test2.add(4);
    	test2.add(5);
    	test2.add(5);
    	test2.add(6);
    	test2.add(6);
    	test2.add(6);*/
    	
    	
    	
    	
    	
    	//the game state we will be using
    	//gameState = new GameState(test1, test2);
    	//gameState = new GameState(null, null);
    	
    	
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.WHITE);
        
        Mouse mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        

        this.status = status;
    }
    
    
    public int getTriPosX(int triNum) {
    	int posX;
    	if (triNum <= 12) posX = 64 + (triNum * 64);
    	else posX = 64 + ((25 - triNum) * 64);
    	if (triNum > 6 && triNum < 19) {
    		posX += 64;
    	}
    	return posX;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
    	System.out.println();
    	System.out.println("reset");
    	//resets the game state
        gameState = new GameState(null, null); //----------------------------------------------------------------
        //System.out.println(GameState.getAllPossibleRolls());
        /*for(int[] i : GameState.getAllPossibleRolls()) {
        	for (int j : i) {
        		System.out.print(j + ", ");
        	}
        	System.out.println();
        }*/
       
        playing = true;
        status.setText("Player 1's turn");
        lastDice = new ArrayList<Integer>();
        lastDice.addAll(curDice);
        dice = GameState.getNewRoll();
        curDice = new ArrayList<Integer>();
        curDice.addAll(dice);
        //System.out.println("Player 1 moves:" + gameState.getAllPossibleMoves(GameState.Player.P1, dice).toString());
    	//System.out.println("Player 2 moves:" + gameState.getAllPossibleMoves(GameState.Player.P2, dice).toString());
        

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        
        resetPieces();
        
        //resets it back to player 1's turn
        playerTurn = GameState.Player.P1;
        
        
        //temp test piece -----------------------------------------
        //testPiece.setPos(25, 0);
        //testPiece2.setPos(25, 14);
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
        	
        	//if there is an ai playing it should make a move
        	if (aiPlaying && playerTurn == GameState.Player.P2) {
    			ai.doMove(gameState, dice);
    			resetPieces();
    			dice = new ArrayList<Integer>();
    		}else if (aiPlaying && aiVsAi && playerTurn == GameState.Player.P1) {
    			aiPartner.doMove(gameState, dice);
    			resetPieces();
    			dice = new ArrayList<Integer>();
    		}
        	
        	if (gameState.checkGameOver()) {
        		status.setText("Game Over");
        		System.out.println("Game Over");
        		playing = false;
        	}
        	
        	//switch the player if they have used up all their pieces
        	if (dice.size() == 0 && playing) {
        		System.out.println("Turn over");
        		lastDice = new ArrayList<Integer>();
                lastDice.addAll(curDice);
                dice = GameState.getNewRoll();
                curDice = new ArrayList<Integer>();
                curDice.addAll(dice);
        		if (playerTurn.equals(GameState.Player.P1)) {
        			playerTurn = GameState.Player.P2;
        			System.out.println("It is now Player 2's turn");
        			status.setText("Turn over! It is now Player 2's turn.");
        		}else {
        			playerTurn = GameState.Player.P1;
        			System.out.println("It is now Player 1's turn");
        			status.setText("Turn over! It is now Player 1's turn.");
        		}

        	}
        	
        	//checks to make sure a move is possible
        	if (playerTurn.equals(GameState.Player.P2) && playing) {
    			if (!gameState.possibleMove(playerTurn, dice)) {
    				System.out.println("no possible moves so player 1's turn again");
    				status.setText("Player 2 has no possible moves so it's player 1's turn again");
    				lastDice = new ArrayList<Integer>();
    		        lastDice.addAll(curDice);
    		        dice = GameState.getNewRoll();
    		        curDice = new ArrayList<Integer>();
    		        curDice.addAll(dice);
    				playerTurn = GameState.Player.P1;
    			} //else System.out.println("moves exist");
    		}
    		else {
    			if (!gameState.possibleMove(playerTurn, dice) && playing) {
    				System.out.println("no possible moves so player 2's turn again");
    				status.setText("Player 1 has no possible moves so it's player 2's turn again");
    				lastDice = new ArrayList<Integer>();
    		        lastDice.addAll(curDice);
    		        dice = GameState.getNewRoll();
    		        curDice = new ArrayList<Integer>();
    		        curDice.addAll(dice);
    				playerTurn = GameState.Player.P2;
    			} //else System.out.println("moves exist");
    		}
        	
        	// update the display
            repaint();
       	
            
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        try {
			g.drawImage(ImageIO.read(new File("files/board.png")), 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}

        
        for (MoveablePiece i : playerPieces[0]) {
        	i.draw(g);
        }
        
        for (MoveablePiece i : playerPieces[1]) {
        	i.draw(g);
        }
        
        //draws the current dice
        g.setColor(Color.BLACK);
        g.setFont(new Font("Verdana",1,24));
        g.drawString(diceToString(dice), 14, 312);
        
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Verdana",1,15));
        g.drawString("Last roll: ", 10, 274);
        g.drawString(diceToString(lastDice), 16, 290);
        
        //testPiece.draw(g);-------------------------------------------------------------------------------------------------------------------
        //g.setColor(Color.RED);
        //g.fillRect(14, 254, 89, 40);
        //this.testPiece.draw(g);
        //this.testPiece2.draw(g);
        
        
        //System.out.println(gameState.toString());
    }

    //responsible for getting a displayable version of the dice
    private String diceToString(List<Integer> dice) {

		String output = " ";
		for (Integer i : dice) {
			if (i != 0) output += i.toString() + ",";
		}
		
		return output.substring(0, output.length() - 1);
	}


	@Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
    private void resetPieces() {
    	ArrayList<Integer> count = new ArrayList<Integer>();
        
        for (int i = 0; i < 15; i ++) {
        	int newPos = gameState.getPlayer1().get(i);
        	playerPieces[0][i] = new MoveablePiece(COURT_WIDTH, COURT_HEIGHT, Color.WHITE);
        	playerPieces[0][i].setPos(newPos, GameState.count(count, newPos));
        	count.add(newPos);
        }
        
        count = new ArrayList<Integer>();
        
        for (int i = 0; i < 15; i ++) {
        	int newPos = gameState.getPlayer2().get(i);
        	playerPieces[1][i] = new MoveablePiece(COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
        	playerPieces[1][i].setPos(newPos, GameState.count(count, newPos));
        	count.add(newPos);
        }
    }
    
    
    
    
    
    
  //mouse class
    
    public class Mouse extends MouseAdapter implements MouseListener, MouseMotionListener {	
    	
    	
    	//modes
        
        int draggableIndex;
        int origin;
                
        //while dragging
        private class Dragging extends MouseAdapter implements MouseListener, MouseMotionListener {
        	
        	public void mouseReleased(MouseEvent e) {
        		int newer = getMousePlace(e);
        		if (playerTurn.equals(GameState.Player.P1) && gameState.checkValidMove(playerTurn, playerPieces[0][draggableIndex].getCurPos(), newer, dice)) {
        			status.setText("Good move player 1!");
	        		gameState.move(playerTurn, origin, newer, dice);
        		}else if (playerTurn.equals(GameState.Player.P2) && gameState.checkValidMove(playerTurn, playerPieces[1][draggableIndex].getCurPos(), newer, dice)) {
        			status.setText("Good move player 2!");
	        		gameState.move(playerTurn, origin, newer, dice);
        		}else {
        			status.setText("Invalid move!");
        		}
        		
        		MoveablePiece playerPiece;
        		if (playerTurn.equals(GameState.Player.P1)) {
        			playerPiece = playerPieces[0][draggableIndex];
        		}else playerPiece = playerPieces[1][draggableIndex];
        		
        		if (!gameState.checkValidMove(playerTurn,  playerPiece.getCurPos(), newer, dice)) {
        			status.setText("Move Not Allowed!");
        		}
        		
        		mode = new Waiting();
        		resetPieces();
        	}
        	
        	public void mouseDragged(MouseEvent e) {
        		if (playerTurn.equals(GameState.Player.P1)) {
	        		playerPieces[0][draggableIndex].setPx(e.getX() - (MoveablePiece.SIZE / 2));
	        		playerPieces[0][draggableIndex].setPy(e.getY() - (MoveablePiece.SIZE / 2));
        		}else if(playerTurn.equals(GameState.Player.P2)) {
        			playerPieces[1][draggableIndex].setPx(e.getX() - (MoveablePiece.SIZE / 2));
	        		playerPieces[1][draggableIndex].setPy(e.getY() - (MoveablePiece.SIZE / 2));
        		}
        		
        	}
        }
        
        //whenever not dragging
        private class Waiting extends MouseAdapter implements MouseListener, MouseMotionListener {
        	public void mousePressed(MouseEvent e) {
        		origin = getMousePlace(e);
        		
        		if (playerTurn.equals(GameState.Player.P1) && !gameState.getPlayer1().contains(origin)) {
        			System.out.println("No piece here");
        			status.setText("Invalid move!");
        			return;
        		} else if (playerTurn.equals(GameState.Player.P2) && !gameState.getPlayer2().contains(origin)) {
        			System.out.println("No piece here");
        			status.setText("Invalid move!");
        			return;
        		}
        		
        		if (playerTurn.equals(GameState.Player.P1)) draggableIndex = gameState.getPlayer1().indexOf(origin);
        		else draggableIndex = gameState.getPlayer2().indexOf(origin);
        		
        		mode = new Dragging();
        	}
        }
        
        MouseAdapter mode = new Waiting();
        
    	public void mouseDragged(MouseEvent e) {
    		//System.out.println("mouse dragging");
    		
    		if (playing) mode.mouseDragged(e);
    	}
    	
    	public void mousePressed(MouseEvent e) {
    		if (playing) {
	    		int place = getMousePlace(e);
	    		//System.out.println("mouse press regestered at " + place);
	    		
	    		
	    		if (place == -1 || place == 25) {
	    			System.out.println("Invalid location");
	    			status.setText("Invalid move!");
	    			return;
	    		}
	    		
	    		mode.mousePressed(e);
    		}
    	}
    	
    	public void mouseReleased(MouseEvent e) {
    		if (playing) {
	    		int place = getMousePlace(e);
	    		
	    		//System.out.println("mouse release registered at " + place);
	    		
	    		
	    		if (place == -1 || place == 0) {
	    			System.out.println("Invalid location");
	    			status.setText("Invalid move!");
	    		}
	    		
	    		mode.mouseReleased(e);
    		}
    	}
    	
    	//returns which tile the mouse is over and returns -1 if not over anything
        public int getMousePlace(MouseEvent e) {
        	Point mousePos = e.getPoint();
        	
        	//mouse over 25
        	if (mousePos.x < 128) return 25;
        	
        	mousePos.x -= 128;
        	
        	//mouse over 0
        	if (mousePos.x > 408 && mousePos.x < 466) return 0;
        	
        	int position;
        	
        	if (mousePos.x > 0 && mousePos.x < 408) {
        		position = (mousePos.x / 68) + 1;
        		
        		if (mousePos.y < (COURT_HEIGHT / 2)) position = 25 - position;
        		return position;
        	}else if (mousePos.x > 466 && mousePos.x < 872) {
        		mousePos.x -= 466;
        		
        		position = (mousePos.x / 68) + 7;

        		if (mousePos.y < (COURT_HEIGHT / 2)) position = 25 - position;
        		return position;
        	}
        	
        	return -1;
        }
    }
    
}
