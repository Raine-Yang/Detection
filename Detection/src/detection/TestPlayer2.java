package detection;

public class TestPlayer2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Thread player2 = new Thread(new GameClient("localhost", 8000));
		player2.start();
	}

}
