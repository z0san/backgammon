import java.util.ArrayList;
import java.util.List;

public class Ai {

	GameState.Player player;
	int depth;
	
	public Ai (GameState.Player player, int depth) {
		this.player = player;
		this.depth = depth;
	}
	
	public List<Move> getMove(GameState gameState, List<Integer> dice) {
		
		if (player == GameState.Player.P2) System.out.println("Player 2 moves:");
		else System.out.println("Player 1 moves:");
		
        for (List<Move> i : gameState.getAllPossibleMoves(player, dice)) {
        	System.out.println(i.toString());
        }
        
        System.out.println("Starting search for best move");
        long startTime = System.currentTimeMillis();
		
		List<List<Move>> possibleMoves = gameState.getAllPossibleMoves(player, dice);
		
		int useDepth;
		if (possibleMoves.size() > 30) useDepth = depth - 1;
		else useDepth = depth;
		
		//if there is only one possible move just do it
		if (possibleMoves.size() == 1) {
			return possibleMoves.get(0);
		}
		
		//call for minimizing player first
		//for all the possible moves does the best move
		
		double bestExpected = -999;
		List<Move> bestMove = null;
		for (List<Move> i : possibleMoves) {
			
			List<Integer> newDiceCollection = new ArrayList<Integer>();
			newDiceCollection.addAll(dice);
			
			GameState currLooking = gameState.doMove(player, i.get(0).from, i.get(0).to, newDiceCollection);
			if (i.size() > 1) currLooking = currLooking.doMove(player, i.get(1).from, i.get(1).to, newDiceCollection);
			
			if (i.size() == 4) {
				currLooking = currLooking.doMove(player, i.get(2).from, i.get(2).to, newDiceCollection);
				currLooking = currLooking.doMove(player, i.get(3).from, i.get(3).to, newDiceCollection);
			}
			
			//find the expected value for the particular move we are looking at
			double curExpected = findExpectedScore(currLooking, useDepth, switchPlayer(player), false);
			//if we have found a new best move
			if (curExpected > bestExpected) {
				bestExpected = curExpected;
				bestMove = new ArrayList<Move>();
				bestMove.addAll(i);
			}
		}
		
		System.out.println("Finnished searching for best move and took:  " + (System.currentTimeMillis() - startTime)
				+ "  milli seconds");
		return bestMove;
		
	}
	
	public void doMove(GameState gameState, List<Integer> dice) {
		
		System.out.println("trying to do move with dice " + dice);
		
		
		
		List<Move> move = getMove(gameState, dice);
		
		//if there are no possible moves then simply do nothing
		
		if (!gameState.possibleMove(player, dice) || move == null) {
			System.out.println("No possible moves for the ai to take");
			return;
		}
		
		System.out.println("Ai now doing move: " + move);
		for (Move i : move) {
			gameState.move(player, i.from, i.to, dice);
		}
	}
	
	private double findExpectedScore (GameState gameState, int depth, GameState.Player player, boolean maximizingPlayer) {
		if (depth == 0 || gameState.checkGameOver()) {
			return gameState.getPlayerScore(player);
		}
		
		//in the case we are trying to maximize our expected value
		if (maximizingPlayer) {
			
			//total counter for expected value
			double total = 0;
			
			//for every possible roll
			for (int[] roll : GameState.getAllPossibleRolls()) {
				
				double max = -999;
				
				//converts the roll into a collection
				List<Integer> rollCollection = new ArrayList<Integer>();
				for (int dice : roll) {
					rollCollection.add(dice);
				}
				
				//for every possible move we are looking for the greatest expected value
				for (List<Move> move : gameState.getAllPossibleMoves(player, rollCollection)) {
					//does all the moves in the given move
					
					//creates a new dice collection for the doMove function
					List<Integer> newDiceCollection = new ArrayList<Integer>();
					newDiceCollection.addAll(rollCollection);
					
					GameState newGameState = gameState.doMove(player, move.get(0).from, move.get(0).to, newDiceCollection);
					
					//in the case where there is only one possible move
					if(move.size() > 1) {
						newGameState = newGameState.doMove(player, move.get(1).from, move.get(1).to, newDiceCollection);
					}
					//if there are more than 2 moves to do
					if(move.size() == 4) {
						newGameState = newGameState.doMove(player, move.get(2).from, move.get(2).to, newDiceCollection);
						newGameState = newGameState.doMove(player, move.get(3).from, move.get(3).to, newDiceCollection);
					}
					
					double expectedScore = findExpectedScore(newGameState, depth - 1, switchPlayer(player), !maximizingPlayer);
					max = max(max, expectedScore);
					
				}
				
				total += getRollProb(rollCollection) * max;
				
			}
			
			return total;
			
		}else {

			//total counter for expected value
			double total = 0;
			
			//for every possible roll
			for (int[] roll : GameState.getAllPossibleRolls()) {
				
				double min = 999;
				
				//converts the roll into a collection
				List<Integer> rollCollection = new ArrayList<Integer>();
				for (int dice : roll) {
					rollCollection.add(dice);
				}
				
				//for every possible move we are looking for the greatest expected value
				for (List<Move> move : gameState.getAllPossibleMoves(player, rollCollection)) {
					//does all the moves in the given move
					
					//creates a new dice collection for the doMove function
					List<Integer> newDiceCollection = new ArrayList<Integer>();
					newDiceCollection.addAll(rollCollection);
					
					GameState newGameState = gameState.doMove(player, move.get(0).from, move.get(0).to, newDiceCollection);
					if(move.size() > 1) newGameState = newGameState.doMove(player, move.get(1).from, move.get(1).to, newDiceCollection);
					
					//if there are more than 2 moves to do
					if(move.size() == 4) {
						newGameState = newGameState.doMove(player, move.get(2).from, move.get(2).to, newDiceCollection);
						newGameState = newGameState.doMove(player, move.get(3).from, move.get(3).to, newDiceCollection);
					}
					
					double expectedScore = findExpectedScore(newGameState, depth - 1, switchPlayer(player), !maximizingPlayer);
					min = min(min, expectedScore);
					
				}
				
				total += getRollProb(rollCollection) * min;
				
			}
			
			return total;
			
		}
		
	}
	
	private double max(double val1, double val2) {
		if (val1 > val2) return val1;
		return val2;
	}

	private double min(double val1, double val2) {
		if (val1 < val2) return val1;
		return val2;
	}

	
	private GameState.Player switchPlayer(GameState.Player player){
		if (player == GameState.Player.P1) return GameState.Player.P2;
		return GameState.Player.P1;
	}
	
	
	private double getRollProb (List<Integer> roll) {
		if (roll.size() == 2) {
			return 1 / 36.0;
		}
		return 1 / 18.0;
	}
	
	
}
