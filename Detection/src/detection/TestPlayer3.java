package detection;

public class TestPlayer3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Thread player3 = new Thread(new GameClient("localhost", 8000));
		player3.start();
	}

}
