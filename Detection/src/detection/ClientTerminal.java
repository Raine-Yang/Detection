package detection;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientTerminal implements ShareData {
	
	ObjectOutputStream toServer;
	Robot player;
	int identity;
	Scanner input = new Scanner(System.in);
	
	public ClientTerminal(ObjectOutputStream toServer, Robot player, int identity) {
		this.toServer = toServer;
		this.player = player;
		this.identity = identity;
	}
	
	
	// read user command and sned command to the server
	// @param: the command to be processed
	// @return: whether the command is a valid choice
	public boolean processCommand(char command) throws IOException {
		
		// an unrecognized command
		if (command != 'R' && command != 'Z' && command != 'F' && command != 'P') {
			System.out.println("Unrecognized command");
			return false;
		}
		
		if (command == 'R') {	// sensor command
			return processSensorCommand();
		} else if (command == 'Z') {	// move command
			return processMovementCommand();
		} else if (command == 'F') {	// fire command
			return processFireCommand();	
		} else if (command == 'P') {	// pause command
			return processPauseCommand();	
		}
		
		return false;	// unknown case, defaut return type
	}
	
	
	// process the sensor command and send command to server
	// @return: whether a valid command is sent
	private boolean processSensorCommand() throws IOException {
		TestEnergy testEnergy = new TestEnergy();
		System.out.println("Select the type of sensor:");
		System.out.println("1: sonar(1) 2: sound sensor(2) 3: temperature sensor(3) 4: magnetic sensor(5) 5: radar(7) C: cancel");
		char choice = input.nextLine().charAt(0);
		if (choice != '1' && choice != '2' && choice != '3' && choice != '4' && choice != '5') {	// cancelled or unrecognized command
			System.out.println("Cancel selection");
			return false;
		} else {
			int eventType = choice - 48 + 1000;	// convert char to event label
			int remainEnergy = testEnergy.testSensorEnergy(eventType, player.energy);
			if (remainEnergy < 0) {
				System.out.println("No enough energy");
				return false;
			} else {
				player.energy = remainEnergy;	// update energy
				toServer.writeObject(new Event(identity, eventType));	// write event type to server
				return true;	// a valid move
			}
		}
	}
	
	
	// process the movement command and send command to server
	// @return: whether a valid command is sent
	private boolean processMovementCommand() throws IOException {
		TestEnergy testEnergy = new TestEnergy();
		System.out.println("Select direction: W: up S: down A: left D: right");
		char choice = input.nextLine().charAt(0);
		if (choice != 'W' && choice != 'S' && choice != 'A' && choice != 'D') {	// cancelled or unrecognized command
			System.out.println("Invalid direction");
			return false;
		} else {
			int eventType = MOVE;
			int eventDirection = chooseDirection(choice);
			int remainEnergy = testEnergy.testMovementEnergy(player.token, player.energy);
			if (remainEnergy < 0) {
				System.out.println("No enough energy");
				return false;
			} else {
				player.energy = remainEnergy;	// update energy
				toServer.writeObject(new Event(identity, eventType, eventDirection));	// write event to server
				return true;	// a valid move
			}
		}
	}
	
	
	// process the fire command and send command to server
	// @return: whether a valid command is sent
	private boolean processFireCommand() throws IOException {
		TestEnergy testEnergy = new TestEnergy();
		// select the weapon type
		System.out.println("Select the type of weapon:");
		System.out.println("1: machine gun(1) 2: laser gun(3) 3: rocket launcher(3) 4: mortar(3) 5: heavy howitzer(6) 6: missile(5) C: cancel");
		char weapon = input.nextLine().charAt(0);
		if (weapon != '1' && weapon != '2' && weapon != '3' && weapon != '4' && weapon != '5' && weapon != '6') {
			System.out.println("Cancel Selection");
			return false;
		} else {
			// select directions for straight weapons: machine gun, laser, rocket launcher, missile
			if (weapon == '1' || weapon == '2' || weapon == '3' || weapon == '6') {
				System.out.println("Select direction: W: up S: down A: left D: right");
				char direction = input.nextLine().charAt(0);
				if (direction != 'W' && direction != 'S' && direction != 'A' && direction != 'D') {	// cancelled or unrecognized command
					System.out.println("Invalid direction");
					return false;
				} else {
					int eventType = weapon - 48 + 3000;
					int eventDirection = chooseDirection(direction);
					int remainAmmo = testEnergy.testFireAmmo(eventType, player.ammo);
					if (remainAmmo < 0) {
						System.out.println("No enough ammo");
						return false;
					} else {
						player.ammo = remainAmmo;	// update energy
						toServer.writeObject(new Event(identity, eventType, eventDirection));	// write event to server
						return true;	// a valid command
					}
				}
			}
			// select mortar
			if (weapon == '4') {
				// print the coordinates for selection
				for (int i = 0; i <= 4; i++) {
					for (int j = 0; j <= 4; j++) {
						System.out.print("(" + i + "," + j + ") ");
					}
					System.out.println();
				}
				System.out.println("Choose the target coordinates, written in format (x,y)");
				String temp = input.nextLine();
				int x = temp.charAt(1) - 48;
				int y = temp.charAt(3) - 48;
				// the input coordinates are not in range
				if (x < 0 || x > 4 || y < 0 || y > 4) {
					System.out.println("invalid coordinates");
					return false;
				} else {
					int eventType = weapon - 48 + 3000;
					int remainAmmo = testEnergy.testFireAmmo(eventType, player.ammo);
					if (remainAmmo < 0) {
						System.out.println("No enough ammo");
						return false;
					} else {
						player.ammo = remainAmmo;	// update energy
						toServer.writeObject(new Event(identity, eventType, x, y));	// write event to server
						return true;	// a valid command
					}
				}
			}
			
			// choose howitzer
			if (weapon == '5') {
				System.out.println("Select direction: W: up S: down A: left D: right");
				char direction = input.nextLine().charAt(0);
				System.out.println("Select the firing range (2-6)");
				int distance = input.nextLine().charAt(0) - 48;
				if (direction != 'W' && direction != 'S' && direction != 'A' && direction != 'D' || distance < 2 || distance > 6) {	// cancelled or unrecognized command
					System.out.println("Invalid direction");
					return false;
				} else {
					int eventType = weapon - 48 + 3000;
					int eventDirection = chooseDirection(direction);
					int remainAmmo = testEnergy.testFireAmmo(eventType, player.ammo);
					if (remainAmmo < 0) {
						System.out.println("No enough ammo");
						return false;
					} else {
						player.ammo = remainAmmo;	// update energy
						toServer.writeObject(new Event(identity, eventType, eventDirection, distance));	// write event to server
						return true;	// a valid command
					}
				}
			}
		}
		
		// default return
		return false;
	}
	
	
	private boolean processPauseCommand() throws IOException {
		toServer.writeObject(new Event(identity, PAUSE));
		return true;
	}
	
	
	private int chooseDirection(char key) {
		switch(key) {
			case 'W':
				return UP;
			case 'S':
				return DOWN;
			case 'A':
			return LEFT;
			case 'D':
				return RIGHT;
			default:
				return -1;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
