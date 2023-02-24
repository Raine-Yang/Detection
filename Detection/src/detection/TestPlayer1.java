package detection;

public class TestPlayer1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Thread player1 = new Thread(new GameClient("localhost", 8000));
		player1.start();
	}

}
