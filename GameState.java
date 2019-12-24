import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

//A class that stores the state of the game and provides functions in order to change the state of the game
public class GameState {
	
	boolean debug = false;
	
	enum Player {P1, P2};
	
	private List<Integer> p1 = new ArrayList<Integer>();
	private List<Integer> p2 = new ArrayList<Integer>();
	private static Random rand = new Random();
	

	//game state given a set of starting positions
	public GameState(List<Integer> p1, List<Integer> p2) {
		
		//if the the game state is started will null then use default starting values
		if (p1 == null || p2 == null) {
			List<Integer> startP1 = new ArrayList<Integer>();
			List<Integer> startP2 = new ArrayList<Integer>();
			for (int i : new int[] {1, 1, 12, 12, 12, 12, 12, 17, 17, 17, 19, 19, 19, 19, 19}) {
				startP1.add(i);
			} 
			for (int i : new int[] {24, 24, 13, 13, 13, 13, 13, 8, 8, 8, 6, 6, 6, 6, 6}) {
				startP2.add(i);
			} 
			
			this.p1 = startP1;
			this.p2 = startP2;
			return;
			
		}
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public static List<Integer> getNewRoll() {
		
		List<Integer>curDice = new ArrayList<Integer>();
		
		curDice.add(rand.nextInt(6) + 1);
		curDice.add(rand.nextInt(6) + 1);
		
		if (curDice.get(0).equals(curDice.get(1))) {
			curDice.addAll(curDice);
		}
		
		System.out.println("new roll: " + curDice.toString());
		return curDice;
	}
	
	
	public boolean validState() {
		
		//checks that there are the appropriate number of pieces
		if (p1.size() != 15 || p2.size() != 15) return false;
		
		//checks that all the pieces are at a valid location
		for (int i : p1) {
			if (i < 0 || i > 25) return false;
		}
		for (int i : p2) {
			if (i < 0 || i > 25) return false;
		}
		
		//checks that no p1 pieces are at the same location as p2 pieces
		for (int i : p1) {
			if (p2.contains(i)) return false;
		}
		
		
		
		return true;
	}
	
	//checks to see if the game is done
	public boolean checkGameOver() {
		for (int i : p1) {
			if (i != 25) {
				for (int j : p2) {
					if (j != 25) return false;
				}
				return true;
			}
		}
		
		return true;
		
	}
	
	public static int count(List<Integer> list, int index) {
		int count = 0;
		for (Integer i : list) {
			if (i.equals(index)) count ++;
		}
		return count;
	}
	
	@SuppressWarnings("deprecation")
	private int find(List<Integer> list, int index) {
		return list.indexOf(new Integer(index));
	}
	
	public boolean checkValidMove(Player player, int from, int to, List<Integer> dice) {
		//checks the range of the from and to
		if (from < 0 || from > 24 || to <= 0) return false;
		//if (debug) System.out.println("Passed range");
		
		//checks that the player has a piece in the appropriate position
		if (player == Player.P1 && !p1.contains(from)) return false;
		if (player == Player.P2 && !p2.contains(from)) return false;
		//if (debug) System.out.println("Passed piece exists");
		
		//checks that if player 2 is taking off pieces it is allowed
		//System.out.println("got command from: " + from + "  to:  " + to);
		if (player == Player.P2 && to == 25 && isInBearOff(player) && containsGreater(dice, from)) {
			return true;
		}
		//if (debug) System.out.println("Passed dice contains roll for player 2 taking pieces off the board");
		
		//checks that if player 1 is taking off pieces it is allowed
		if (player == Player.P1 && to == 25 && isInBearOff(player) && containsGreater(dice, 25 - from)) {
			return true;
		}
		//if (debug) System.out.println("Passed dice contains roll for player 1 taking pieces off the board");
		
		//checks that the position that the player is trying to move to is not blocked
		if (player == Player.P1) {
			if (count(p2, to) >= 2) {
				return false;
			}
		} else {
			if (count(p1, to) >= 2) {
				return false;
			}
		}
		//if (debug) System.out.println("Passed place to move is not blocked by other player");
		
		//checks that the player is only taking off pieces if they are allowed to
		if (!this.isInBearOff(player) && to >= 25) return false;
		//if (debug) System.out.println("Passed player is only taking off pieces if they are allowed to");
		
		//checks that if a player has a piece off the board they are putting that piece back on
		if (player == Player.P1 && p1.contains(0) && from != 0) return false;
		if (player == Player.P2 && p2.contains(0) && from != 0) return false;
		//if (debug) System.out.println("Passed player is putting pieces onto the board if they have to");
		
		//checks that if player 2 is taking of pieces it is allowed
		
		if (player == Player.P2 && from == 0 && dice.contains(25 - to)) {
			return true;
		}
		//if (debug) System.out.println("Passed player2 is only taking off pieces if it is allowed to");
		
		//checks that the dice are not all used up
		
		boolean available = false;
		for (int i : dice) {
			if (i != 0) {
				available = true;
				break;
			}
		}
		
		if (!available) return false;
		//if (debug) System.out.println("Passed dice are not all used up");
		
		//checks that the player is only doing moves such that the dice will allow
		int change = -1;
		if (player == Player.P1) change = 1;
		
		if (!dice.contains(change * (to - from))) return false;
		
		//if (debug) System.out.println("Passed payer has dice necessary for move");
		
		return true;
	}
	
	
	private boolean containsGreater(List<Integer> dice, int search) {
		//System.out.println("Looking for greater than " + search);
		if (search >= 7) {
			//System.out.println("does not contain greater");
			return false;
			
		}
		if (dice.contains(search)) return true;
		return containsGreater(dice, search + 1);
	}
	@SuppressWarnings("deprecation")
	private void removeGreater(List<Integer> dice, int search) {
		//System.out.println("trying to find greater than " + search);
		if (search >= 7) {
			throw new IllegalArgumentException();
		}
		if (dice.contains(search)) {
			//System.out.println("removing " + search);
			dice.remove(new Integer(search));
			return;
		}
		removeGreater(dice, search + 1);
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void move(Player player, int from, int to, List<Integer> dice) {
		if (!checkValidMove(player, from, to, dice)) {
			System.out.println("error has been found with move from: " + from + " to: " + to);
			System.out.println("player's turn = " + player);
			System.out.println("dice: " + dice);
			System.out.println("board state: " + this.toString());
			
			throw new IllegalArgumentException();
		}
		int change = -1;
		if (player == Player.P1) change = 1;
		
		
		
		//removes the dice roll so you can only do it once
		
		//deals with the dice removal in the case where player 2 is putting pieces back onto the board
		if (from == 0 && player == Player.P2) {
			dice.remove(new Integer(25 - to));
		}else if(to != 25) {
			dice.remove(new Integer(change * (to - from)));
			//System.out.println("just removed " + change * (to - from));
		}
		
		
		//deals with the dice removal in the case where player 2 is taking pieces off the board
		if (player == Player.P2 && to == 25) {
			removeGreater(dice, from);
		}
		
		//deals with the dice removal when player 1 is taking pieces off the board
		if (player == Player.P1 && to == 25) {
			removeGreater(dice, 25 - from);
		}
		
		//deals with taking the opponents pieces
		if (player.equals(Player.P1) && to != 25) {
			if (p2.contains(to)) {
				p2.set(p2.indexOf(to), 0);
			}
		}else if (player.equals(Player.P2) && to != 25) {
			if (p1.contains(to)) {
				p1.set(p1.indexOf(to), 0);
			}
		}
		
		//checks for regular move
		if (player == Player.P1) {
			p1.set(find(p1, from), to);
			return;
		}
		if (player == Player.P2) {
			p2.set(find(p2, from), to);
			return;
		}
		
		throw new IllegalArgumentException();
	}
	
	
	public boolean equals(GameState other) {
		return this.p1.equals(other.p1) && this.p2.equals(other.p2);
	}
	
	public GameState switchPlayers() {
		List<Integer> newP1 = new LinkedList<Integer>();
		List<Integer> newP2 = new LinkedList<Integer>();
		for (Integer i : p1) {
			newP1.add(25 - i);
		}
		for (Integer j : p2) {
			newP2.add(25 - j);
		}
		return new GameState(newP2, newP1);
	}
	
	public boolean isInBearOff(Player player) {
		
		if (player == Player.P1) {
			
			if (p1.contains(0)) return false;
			
			for (int i : p1) {
				if (i < 19) return false;
			}
		}else {
			
			if (p2.contains(0)) return false;
			
			for (int i : p2) {
				if (i > 6 && i != 25) return false;
			}
		}
		return true;
	}
	
	//returns weather there are any possible moves
	/*public boolean possibleMove(Player player) {
		if (player == Player.P1 && p1.contains(0)) {
			return !(count(p2, 1) >= 2 && count(p2, 2) >= 2 && count(p2, 3) >= 2 && count(p2, 4) >= 2 && count(p2, 5) >= 2 && count(p2, 6) >= 2);
		}else if (player == Player.P2 && p2.contains(0)) {
			return !(count(p1, 24) >= 2 && count(p1, 23) >= 2 && count(p1, 22) >= 2 && count(p1, 21) >= 2 && count(p1, 20) >= 2 && count(p1, 19) >= 2);
		}else return true;
	}*/
	
	public boolean possibleMove(Player player, List<Integer> dice) {
		
		if ((player == Player.P1 && p1.contains(0)) || (player == Player.P2 && p2.contains(0))) {
			
			if (player == Player.P1) {
				if (count(p2, dice.get(0)) >= 2 && (dice.size() == 1 || count(p2, dice.get(1)) >= 2)) {
					return false;
				}
			} else if (player == Player.P2) {
				if (count(p1, 25 - dice.get(0)) >= 2 && (dice.size() == 1 || count(p1, 25 - dice.get(1)) >= 2)) {
					return false;
				}
			}
			
			return true;
		} else {
			
			if (debug) System.out.println("No pieces off the board");
			
			int change = -1;
			
			if (player == Player.P1) change = 1;
			
			//checks every position
			for (int place : getPositions(player)) {
				//checks for all the dice rolls
				for (int roll : dice) {
					if (player == Player.P1) {
						if (count(p2, place + (change * roll))  < 2 && place + (change * roll) < 25 && 
								place + (change * roll) > 0) return true;
					}else if (count(p1, place + (change * roll))  < 2 && place + (change * roll) < 25 && 
							place + (change * roll) > 0) return true;
					
					//checks in the case where it is in bear off
					if (isInBearOff(player)) {
						if (this.checkValidMove(player, place, 25, dice)) return true;
					}
				}
			}
		}
		
		if (debug) System.out.println("No possible moves");
		
		return false;
	}

	
	//returns all possible moves that a given player could make given a certain dice roll
	public List<List<Move>> getAllPossibleMoves(Player player, List<Integer> dice) {
		
		if (debug) System.out.println("Looking for all possible moves for player " + player);
		if (debug) System.out.println("Looking for all possible moves with dice " + dice);
		
		List<List<Move>> moves = new ArrayList<List<Move>>();
		if (!possibleMove(player, dice)) {
			if (debug) System.out.println("No possible moves!");
			return moves;
		}
		
		
		
		
		if(dice.size() != 4) {
			if (debug)System.out.println("Checking dice 1 and then dice 2");
			//gets all the possible moves where you do the first dice move first
			List<Move> firstMove = getPossibleMoves(player, dice.get(0));		
			for (Move i : firstMove) {
								
				
				if (debug)System.out.println("Checking first move " + i.toString());
				List<Integer> diceCollection = new ArrayList<Integer>();
				diceCollection.add(dice.get(0));
				diceCollection.add(dice.get(0));
				
				GameState newState = doMove(player, i.from, i.to, diceCollection);
				
				//in the case where there are no more possible moves
				if (!newState.possibleMove(player, diceCollection)) {
					if (debug) System.out.println("no more possible moves");
					List<Move> move = new ArrayList<Move>();
					move.add(i);
					if (!moves.contains(move) && !containsMovePermutation(moves, move)) {
						//found new state
						moves.add(reOrder(move));
					}
				} else {
				
					for (Move j : newState.getPossibleMoves(player, dice.get(1))) {
						if (debug) System.out.println("Checking second move " + j.toString());
						List<Move> move = new ArrayList<Move>();
						move.add(i);
						move.add(j);
						
						/*List<Integer> diceCollection2 = new ArrayList<Integer>();
						diceCollection2.add(dice.get(1));
						
						System.out.println();
						System.out.println(finalState);
						diceCollection2.add(dice.get(1));
						diceCollection2.add(dice.get(1));
						System.out.println(newState.doMove(player, j.from, j.to, diceCollection2));
						System.out.println(finalState.equals(newState.doMove(player, j.from, j.to, diceCollection2)));
						System.out.println();*/
						
						if (!moves.contains(move) && !containsMovePermutation(moves, move)) {
							//found new state
							moves.add(reOrder(move));
						}
					}
				}
			}
			
			//gets all the possible moves where you do the second dice move first
			if (debug)System.out.println("Checking dice 2 and then dice 1");
			
			//gets all the possible moves where you do the first dice move first
			List<Move> firstMove2 = getPossibleMoves(player, dice.get(1));		
			for (Move i : firstMove2) {
				if (debug) System.out.println("Checking first move " + i.toString());
				List<Integer> diceCollection = new ArrayList<Integer>();
				diceCollection.add(dice.get(1));
				diceCollection.add(dice.get(0));
				GameState newState = doMove(player, i.from, i.to, diceCollection);
				
				//in the case where there are no more possible moves
				if (!newState.possibleMove(player, diceCollection)) {
					if (debug) System.out.println("no more possible moves");
					List<Move> move = new ArrayList<Move>();
					move.add(i);
					if (!moves.contains(move) && !containsMovePermutation(moves, move)) {
						//found new state
						moves.add(reOrder(move));
					}
				} else {
					
					for (Move j : newState.getPossibleMoves(player, dice.get(0))) {
						if (debug)System.out.println("Checking second move " + j.toString());
						List<Move> move = new ArrayList<Move>();
						move.add(i);
						move.add(j);

						if (!moves.contains(move) && !containsMovePermutation(moves, move)) {
							//found new state
							moves.add(reOrder(move));
						}
					}
				}
			}
			
			
		} else {
			if (debug) System.out.println("dealing with the case of a double roll");
			
			List<Integer> subDice = new ArrayList<Integer>();
			subDice.add(dice.get(0));
			subDice.add(dice.get(0));
			
			List<List<Move>> firstMoves = getAllPossibleMoves(player, subDice);
			
			
			
			//finds all possible moves doing 2 of the first dice rolls
			for (List<Move> i : firstMoves) {
				
				//deals with the case that there is only one first move
				
				if (i.size() == 1) {
					if (!moves.contains(i) && !containsMovePermutation(moves, i)) {
						//found new state
						moves.add(i);
					}
				} else {
				
					List<Integer> diceCollection = new ArrayList<Integer>();
					diceCollection.add(dice.get(0));
					diceCollection.add(dice.get(0));
					diceCollection.add(dice.get(0));
					GameState newState = doMove(player, i.get(0).from, i.get(0).to, diceCollection);
					newState = newState.doMove(player, i.get(1).from, i.get(1).to, diceCollection);
					
					//in the case where there are no more possible moves
					if (!newState.possibleMove(player, diceCollection)) {
						if (debug) System.out.println("no more possible moves");
						List<Move> move = new ArrayList<Move>();
						move.addAll(i);
						if (!moves.contains(move) && !containsMovePermutation(moves, move)) {
							//found new state
							moves.add(reOrder(move));
						}
					} else {
					
						List<Integer> subDice2 = new ArrayList<Integer>();
						subDice2.add(dice.get(0));
						subDice2.add(dice.get(0));
						
						//finds all possible moves for second dice rolls given first dice rolls
						List<List<Move>> secondMoves = newState.getAllPossibleMoves(player, subDice2);
						for (List<Move> j : secondMoves) {
							
							List<Integer> subDice3 = new ArrayList<Integer>();
							subDice3.add(dice.get(0));
							subDice3.add(dice.get(0));
							
							
							GameState finalState = newState.doMove(player, j.get(0).from, j.get(0).to, subDice3);
							if (j.size() > 1) finalState = finalState.doMove(player, j.get(1).from, j.get(1).to, subDice3);
							
							List<Move> finalMove = new ArrayList<Move>();
							
							finalMove.addAll(i);
							finalMove.addAll(j);
							
							if (!moves.contains(finalMove) && !containsMovePermutation(moves, finalMove)) {
								//found new state
								moves.add(reOrder(finalMove));
							}
						}
					}
				}
				
			}
			
		}
		
		
		return moves;
	}
	
	
	//reorders the moves so that if a piece is being taken off then that happens at the end of the turn
	List<Move> reOrder(List<Move> move) {
		if (move.size() != 2) return move;
		
		if(move.get(0).to == 25) {
			List<Move> returner = new ArrayList<Move>();
			
			returner.add(move.get(1));
			returner.add(move.get(0));
			
			return returner;			
		}
		
		return move;
	}

	public static boolean containsMovePermutation(List<List<Move>> moves, List<Move> search) {
		for (List<Move> i : moves) {
			boolean contains = true;
			
			for(Move j : search) {
				if (!i.contains(j)) {
					contains = false;
					break;
				}
			}
			
			if (contains) return true;
			
		}
		return false;
	}
	
	//gets all the possible moves using one dice number
	public List<Move> getPossibleMoves(Player player, int dice) {
		
		List<Integer> positions = getPositions(player);
		
		int change = -1;
		if (player == Player.P1) change = 1;
		

		List<Move> moves = new ArrayList<Move>();
		
		List<Integer> diceSingleton = new ArrayList<Integer>();
		diceSingleton.add(dice);
		
		if (player == Player.P1 && p1.contains(0)) {
			if (checkValidMove(player, 0, dice, diceSingleton)) {
				moves.add(new Move(0, dice));
				return moves;
			}
		}
		
		
		if (player == Player.P2 && p2.contains(0)) {
			if (checkValidMove(player, 0, 25 - dice, diceSingleton)) {
				moves.add(new Move(0, 25 - dice));
				return moves;
			}
		}
		
		
		
		
		//for every position
		for (int i : positions) {
			
			//puts the dice in a collection for the other functions
			List<Integer> diceCollection = new ArrayList<Integer>();
			diceCollection.add(dice);
			
			//check if the dice means you can move from every position
			if (checkValidMove(player, i, i + (change * dice), diceCollection)) {
				if (debug) System.out.println("found new move " + (new Move(i, i + (change * dice)).toString()));
				moves.add(new Move(i, i + (change * dice)));
			}
			
			diceCollection.add(dice);
			
			//checks if you can take pieces off
			if (isInBearOff(player) && checkValidMove(player, i, 25, diceCollection) 
					&& !moves.contains(new Move(i, 25))) {
				if (debug) System.out.println("found new move " + (new Move(i, i + (change * dice)).toString()));
				moves.add(new Move(i, 25));
			}
			
		}
		
		return moves;
	}


	//Returns all the positions where a specific player has pieces
	public List<Integer> getPositions(Player player){
		List<Integer> returner = new ArrayList<Integer>();
		
		for (int i = 0; i <= 24; i ++) {
			if (player == Player.P1) {
				if (p1.contains(i)) returner.add(i);
			}else if (p2.contains(i)) returner.add(i);
		}
		
		return returner;
	}
	
	
	//creates a new gameState with a particular move done
	public GameState doMove(Player player, int from, int to, List<Integer> dice) {
		List<Integer> newP1 = new ArrayList<Integer>();
		List<Integer> newP2 = new ArrayList<Integer>();
		
		newP1.addAll(p1);
		newP2.addAll(p2);
		
		GameState gameState = new GameState(newP1, newP2);
		gameState.move(player, from, to, dice);
		return gameState;
	}
	
	//returns all possible rolls 
	public static int[][] getAllPossibleRolls() {
		//the roll can have up to 4 values (it has 4 values if a double is rolled) and there are 21 different possible rolls
		int[][] returner = new int[21][2];
		int count = 0;
		for (int i = 1; i <= 6; i++) {
			for (int j = i; j <= 6; j++) {
				returner[count][0] = i;
				returner[count][1] = j;
				if (i == j) {
					returner[count] = new int[] {i, i, i, i};
				}else {
				}
				count ++;
			}
		}
		
		return returner;
	}
	//get functions
	
	public List<Integer> getPlayer1(){
		return p1;
	}
	
	public List<Integer> getPlayer2(){
		return p2;
	}
	
	public String toString() {
		return "Player 1:  " + p1.toString() + "\nPlayer 2:  " + p2.toString();
	}
	
	
	public int getPlayerScore(Player player) {
		int total = 0;
		Iterator<Integer> i1 = p1.iterator();
		Iterator<Integer> i2 = p2.iterator();
		while (i1.hasNext()) {
			int store = i1.next();
			if (!isInBearOff(player) && store > 18) {
				total += 19;
			}else total += store;
		}
		
		while (i2.hasNext()) {
			int store = i2.next();
			if (store != 25 && store != 0)
				if(!isInBearOff(player) && store <= 6) {
					total -= 19;
				}else total -= 25 - store;
			else total -= store;
		}
	
		if (player == Player.P2) total *= -1;
		
		return total;
	}
	
	
}
