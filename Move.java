
//public class that store the from, to involved in a move
public class Move {
	public int from;
	public int to;
	
	public Move(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	public String toString(){
		return "From: " + from + " To: " + to;
	}
	
	public boolean equals(Object obj) {
		
		if (obj == null || !(obj instanceof Move)) return false;
		
		Move that = (Move) obj;
		return that.from == this.from && that.to == this.to;
	}
}
