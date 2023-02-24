package detection;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameClient implements Runnable, ShareData {
	
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	private Scanner input = new Scanner(System.in);
	private String host;	// change in online games
	private int port;
	private int identity;
	
	private Robot player;
	private int round = 0;
	
	
	// constructor: choose IP and port
	public GameClient(String ip, int port) {
		host = ip;
		this.port = port;
	}
	
	
	// connect to server
	private void connectToServer() {
		try {
			Socket socket = new Socket(host, port);
			fromServer = new ObjectInputStream(socket.getInputStream());
			toServer = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public void run() {
		try {
			connectToServer();
			// receive identity
			identity = (int)fromServer.readObject();
			
			char token = selectRobot();
			toServer.writeObject(token);	// send robot selection
			player = new Robot(token);
			player.spawn();
			ClientTerminal terminal = new ClientTerminal(toServer, player, identity);

			// the game loop
			/* 1 receive status information from server
			 * 2 display player status: health, energy, ammo, vicinity
			 * 3 send player action
			 * 4 display player action feedback: sensor result
			 * 5 recharge energy/ammo
			 */
			while (true) {
				// wait for the start signal
				fromServer.readObject();
				round++;
				System.out.println("Round " + round + " starts");
				// display status
				System.out.println(player);
				// display vicinity
				displayC((char[][])fromServer.readObject());
				// send commands
				if (player.isAlive()) {	// player selects the command
					boolean isValidCommand = false;	// check if the command is a valid one
					char command = ' ';
					while (!isValidCommand) {
						System.out.println("Select your command (R: sense, Z: move, F: fire, P: pause)");
						command = input.nextLine().charAt(0);
						isValidCommand = terminal.processCommand(command);
					}
					System.out.println("Command selected, wait for other players...");
				} else {	// send default command (pause)
					terminal.processCommand('P');
				}
				// receive and display server response
				displayServerResponse();
				// send alive information
				toServer.writeObject(player.isAlive());
				System.out.println("Round " + round + " ends");
				player.recharge();	// recharge robot
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	// receive and display the server response
	private void displayServerResponse() throws IOException, ClassNotFoundException {
		while (true) {
			Object response = fromServer.readObject();
			if (response instanceof SensorEvent) {	// receive a sensor response
				displayC(((SensorEvent)response).sensorResult);	// display sensor response
			} else if (response instanceof FireEvent) {	// receive a fire response
				int previousHP = player.health;
				FireEvent damage = (FireEvent)response;
				player.generateNetDamage(damage.damage, damage.penetration);
				System.out.println("You're under attack! HP - " + (previousHP - player.health));
			} else {
				break;	// receive the ending signal
			}
		}
	}
	
	
	private char selectRobot() {
		System.out.println("Select your robot type (G, R, M, T)");
		char robot = 'R';	// the default robot type
		char choice = input.nextLine().charAt(0);	// read user input
		if (choice == 'G' || choice == 'M' || choice == 'T' || choice == 'R') {	// accept valid choices
			robot = choice;
			System.out.println("selection succeed");
		} else {
			System.out.println("invalid choice, use default robot (R)");
		}
		return robot;
	}
	
	
	// display a char array
	private static void displayC(char[][] arr) {
		for (char[] row : arr) {
			for (char cell : row) {
				System.out.print(cell + " ");
			}
			System.out.println();
		}
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
	}

}
