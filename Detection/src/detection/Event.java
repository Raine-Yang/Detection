package detection;

public class Event implements Comparable, ShareData, java.io.Serializable {
	
	private int source;	// the player who causes the event
	private int type;	// the type of event
	private int data; 
	private int data2;
	
	
	public Event(int source, int type) {
		this.source = source;
		this.type = type;
	}
	
	public Event(int source, int type, int data) {
		this.source = source;
		this.type = type;
		this.data = data;
	}
	
	public Event(int source, int type, int data1, int data2) {
		this.source = source;
		this.type = type;
		this.data = data1;
		this.data2 = data2;
	}
	
	
	public int getSource() {
		return source;
	}
	
	
	public int getType() {
		return type;
	}
	
	
	public int getData() {
		return data;
	}
	
	
	public int getData2() {
		return data2;
	}
	
	
	// type: sensor tyer start with 1000, move type start with 2000, fire type start with 3000
	// in priority queue, smallest element goes first
	public int compareTo(Object other) {
		return ((Event)other).getType() - type;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
