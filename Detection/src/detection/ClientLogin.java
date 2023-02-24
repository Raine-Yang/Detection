package detection;

import java.util.*;

public class ClientLogin {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner input = new Scanner(System.in);
		System.out.println("Enter the server's IP address");
		String serverIp = input.nextLine();
		System.out.println("Enter the server's port");
		int port = Integer.parseInt(input.nextLine());
		Thread player = new Thread(new GameClient(serverIp, port));
		player.start();
	}

}
