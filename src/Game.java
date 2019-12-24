/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	
    public void run() {
    	
    	// NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("AWESOME BACKGAMMON");
        frame.setLocation(0, 0);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        
        //instruction window
    	
    	JFrame instr = new JFrame("Instructions");
    	instr.setLocation(250, 150);
    	
    	String rules = ("Welcome to backgammon \n\nThe rules are very simple:\n \nBackgammon Rules\n" + 
    			"Backgammon is a board game for two players. \nThere are 24 triangles on the board, alternate in color, called points. \nThe board is separated into four quadrants known as the player's home board and outer board, and their opponent's home board and outer board. \nHome and outer boards are separated from each other by the bar which is located in the middle of the bar. Each player has 15 checkers to play with.\n" + 
    			"\n" + 
    			"You will always go first and the AI will always go second\n" + 
    			"\n" + 
    			"Object of the game\n" + 
    			"You must move all of your checkers to your home board and then bear them off one by one. The first player to bear off all of their checkers wins the game.\n" + 
    			"\n" + 
    			"Checker movement\n" + 
    			"To begin your turn, you must first roll the dice to determine how many points (also known as pips) you can move your checkers. You can only move your checkers forward (counter-clockwise). \nThe following rules apply:\n" + 
    			"\n" + 
    			"You can move a checker only to an open point. A point is open when it is not occupied by two or more opposing checkers.\n" + 
    			"Each dice roll constitutes to a separate move. \nFor example, if you roll 3 and 6, you may move one checker 3 spaces to an open point and another 6 spaces to an open point, or move one checker a total of 9 spaces to an open point, \nonly if the intermediate point (either 3 or 6 spaces from the starting point) is also open.\n" + 
    			"If you roll a double (for example 6 and 6) you play the dice numbers twice. Doubles constitute to four moves.\n" + 
    			"You are required to use all moves of a roll if it is legally possible. If you cannot use all of your rolls, you must use the ones you can, skipping the others. \nIf you can make either of the two moves but not both, you are required to use the higher roll.\n" + 
    			"\nHitting and Entering\n" + 
    			"Blots are points which are occupied by only one checker. When an opposing checker lands on a blot, the blot is hit and moved to the bar. \nIf you have checkers on the bar, you must first enter those checkers before conducting any other moves.\n" + 
    			"\n" + 
    			"To enter a checker, you must move it to an open point in the opponent's home board, corresponding to one of your dice rolls. If both points are not open, you must skip your turn. \nIf you can enter only some of your checkers on the bar, you must enter as many as you can, and forfeit the remainder of your turn.\n" + 
    			"\n" + 
    			"Once you enter all of your checkers on the bar, you must use your remaining dice rolls on legal moves (if any) before finishing your turn.\n" + 
    			"\n" + 
    			"Bearing Off\n" + 
    			"Once you have moved all of your fifteen checkers into your home board, you can begin bearing them off. \nYou can bear off a checker by rolling a number which corresponds to the point on which the checker resides and then removing it from the bar. \nRolling 5 will allow you to bear off a checker at point 5 (which is the second from left to right on your home board).\n" + 
    			"\n" + 
    			"If you have no checkers on the point indicated by the roll, you must make a valid move using a checker on a higher-numbered point. \nIf you have no checkers on higher-numbered points, you can bear off a checker from a lower-numbered point. \nYou are not required to bear off in this case unless there are no more legal moves to make.\n" + 
    			"\n" + 
    			"You can only bear off if all of your remaining checkers are on your home board. \nIf one of your checkers is hit during bearing off, you must first enter that checker and move it back to your home board, before you can resume bearing off. \nThe first player to bear off all checkers is the winner of the game." +
    			"\n\n And that's it.  Enjoy the game!\n");

    	JTextArea text = new JTextArea();
		instr.add(text);
		text.setText(rules);
		text.setFont(new Font("Verdana",1,10));
    	
    	instr.pack();
    	instr.setVisible(true);
        

        // Start game
        court.reset();
    }


	/**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
