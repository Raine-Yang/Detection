package detection;

import java.util.*;
import java.io.*;
import java.net.*;

public class HandlePlayer implements Runnable, ShareData {
	
	// three players
	private Socket player1;
	private Socket player2;
	private Socket player3;
	
	// the game board
	private GameBoard field;
	
	// communication with players
	ObjectInputStream fromPlayer1;
	ObjectOutputStream toPlayer1;
	ObjectInputStream fromPlayer2;
	ObjectOutputStream toPlayer2;
	ObjectInputStream fromPlayer3;
	ObjectOutputStream toPlayer3;
	
	// the event priority queue
	PriorityQueue<Event> eventQueue = new PriorityQueue<Event>();
	
	// whether players are alive
	boolean player1Alive = true;
	boolean player2Alive = true;
	boolean player3Alive = true;
	int numOfPlayersAlive= 3;

	
	public HandlePlayer(Socket player1, Socket player2, Socket player3) {
		this.player1 = player1;
		this.player2 = player2;
		this.player3 = player3;
	}
	
	
	// start the game
	public void run() {
		try {
			// initialize communication connections
			toPlayer1 = new ObjectOutputStream(player1.getOutputStream());
			toPlayer2 = new ObjectOutputStream(player2.getOutputStream());
			toPlayer3 = new ObjectOutputStream(player3.getOutputStream());
			fromPlayer1 = new ObjectInputStream(player1.getInputStream());
			fromPlayer2 = new ObjectInputStream(player2.getInputStream());
			fromPlayer3 = new ObjectInputStream(player3.getInputStream());
			
			
			// send players identity notifications
			toPlayer1.writeObject(PLAYER1);
			toPlayer2.writeObject(PLAYER2);
			toPlayer3.writeObject(PLAYER3);
			
			
			// receive robot selections
			char player1Robot = (char)fromPlayer1.readObject();
			char player2Robot = (char)fromPlayer2.readObject();
			char player3Robot = (char)fromPlayer3.readObject();
			field = new GameBoard(player1Robot, player2Robot, player3Robot);
	
			
			// the game loop
			/* 1 send player status information: num of players, vicinity
			 * 2 read player actions: read Event
			 * 3 process player actions: process Event
			 * 4 send players action feedbacks: sensor results
			 * 5 reduce signal information
			 */
			while (numOfPlayersAlive > 1) {
				sendSignal(); // send starting signal
				sendVicinity();	// send player vicinity
				receivePlayerInputs();	// add player events to the event queue
				processEvent();	// process player events and send responses
				sendSignal();	// send ending signal
				receiveAliveInformation();	// receive alive information
				field.decay();	// reduce signal
				
			}
			
			// send winning message
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	// send vicinity information to all players
	private void sendVicinity() throws IOException {
		GameBoard.displayC(field.displayField());
		System.out.println();
		if (player1Alive) {
			toPlayer1.writeObject(field.displayVicinity(PLAYER1));	// alive player: show vicinity
		} else {
			toPlayer1.writeObject(field.displayField());	// dead player: show whole field
		}
		if (player2Alive) {
			toPlayer2.writeObject(field.displayVicinity(PLAYER2));
		} else {
			toPlayer2.writeObject(field.displayField());
		}
		if (player3Alive) {
			toPlayer3.writeObject(field.displayVicinity(PLAYER3));
		} else {
			toPlayer3.writeObject(field.displayField());
		}
	}
	
	
	// send the starting and ending signal
	private void sendSignal() throws IOException {
		toPlayer1.writeObject(1);
		toPlayer2.writeObject(1);
		toPlayer3.writeObject(1);
	}
	
	
	// receive client inputs and add them to event queue
	private void receivePlayerInputs() throws IOException, ClassNotFoundException {
		// read player events
		Event player1Event = (Event)fromPlayer1.readObject();
		Event player2Event = (Event)fromPlayer2.readObject();
		Event player3Event = (Event)fromPlayer3.readObject();
		// enqueue Events
		eventQueue.offer(player1Event);
		eventQueue.offer(player2Event);
		eventQueue.offer(player3Event);
	}
	
	
	// process client events in the queue
	private void processEvent() throws IOException {
		while(eventQueue.size() > 0) {
			Event event = eventQueue.remove();
			switch (event.getType()) {
				// sensor events
				case SONAR: 
					chooseOutput(event.getSource()).writeObject(new SensorEvent(event.getSource(), field.displaySonar(event.getSource())));
					break;
				case RADAR:
					chooseOutput(event.getSource()).writeObject(new SensorEvent(event.getSource(), field.displayRadar(event.getSource())));
					break;
				case MAGNETIC_SENSOR:
					chooseOutput(event.getSource()).writeObject(new SensorEvent(event.getSource(), field.displayMagneticSensor(event.getSource())));
					break;
				case SOUND_SENSOR:
					chooseOutput(event.getSource()).writeObject(new SensorEvent(event.getSource(), field.displaySoundSensor(event.getSource())));
					break;
				case TEMP_SENSOR:
					chooseOutput(event.getSource()).writeObject(new SensorEvent(event.getSource(), field.displayTempSensor(event.getSource())));
					break;
				// move event
				case MOVE:
					field.movePlayer(event.getSource(), event.getData());
					break;
				// pause event
				case PAUSE:
					break;
				// weapon events
				// straight weapons
				case MACHINE_GUN, LASER, ROCKET_LAUNCHER, MISSILE:
					FireEvent straightWeaponResponse = field.useStraightWeapon(event.getSource(), event.getData(), event.getType());
					if (straightWeaponResponse != null) {	// a player is hit
						chooseOutput(straightWeaponResponse.target).writeObject(straightWeaponResponse);	// tell the player being hit the damage
					}
					break;
				// mortar
				case MORTAR:
					FireEvent mortarResponse = field.useMortar(event.getSource(), event.getData(), event.getData2(), event.getType());
					if (mortarResponse != null) {	// a player is hit
						chooseOutput(mortarResponse.target).writeObject(mortarResponse);	// tell the player being hit the damage
					}
					break;
				case HOWITZER:
					FireEvent howitzerResponse = field.useHowitzer(event.getSource(), event.getData(), event.getData2(), event.getType());
					if (howitzerResponse != null) {
						chooseOutput(howitzerResponse.target).writeObject(howitzerResponse);
					}
			}
		}
	}
	
	
	// choose the player to be send by the label
	// @param: the player label
	// @return: the ObjectOutputStream to use
	private ObjectOutputStream chooseOutput(int player) throws IOException {
		if (player == PLAYER1) {
			return toPlayer1;
		} else if (player == PLAYER2) {
			return toPlayer2;
		} else if (player == PLAYER3) {
			return toPlayer3;
		} else {
			return null;	// an error occured
		}
	}
	
	
	// receive alive information
	private void receiveAliveInformation() throws IOException, ClassNotFoundException {
		player1Alive = (boolean)fromPlayer1.readObject();
		player2Alive = (boolean)fromPlayer2.readObject();
		player3Alive = (boolean)fromPlayer3.readObject();
		// recalculate numOfPlayersAlive
		numOfPlayersAlive = 0;
		if (player1Alive) { numOfPlayersAlive++; };
		if (player2Alive) { numOfPlayersAlive++; };
		if (player3Alive) { numOfPlayersAlive++; };
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
