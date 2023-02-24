package detection;

import java.io.*;
import java.net.*;


public class GameServer implements Runnable, ShareData {
	
	
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("server start at port " + PORT);
			System.out.println("server's ip:" + InetAddress.getLocalHost().getHostAddress());
			
			// wait for players to connect
			while (true) {
				Socket player1 = serverSocket.accept();
				System.out.println("player 1: " + player1.getInetAddress().getHostAddress());

				Socket player2 = serverSocket.accept();	// connect to player 2
				System.out.println("player 2: " + player2.getInetAddress().getHostAddress());
				
				Socket player3 = serverSocket.accept();	// connect to player 3
				System.out.println("player 3: " + player3.getInetAddress().getHostAddress());
				
				
				// start the game
				new Thread(new HandlePlayer(player1, player2, player3)).start();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Thread server = new Thread(new GameServer());
		server.start();
	}

}
