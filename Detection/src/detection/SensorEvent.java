package detection;

public class SensorEvent implements ShareData, java.io.Serializable {
	
	
	public int source;
	public char[][] sensorResult;
	
	public SensorEvent(int source, char[][] sensorResult) {
		this.source = source;
		this.sensorResult = sensorResult;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
