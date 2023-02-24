package detection;

import java.util.Arrays;

public class GameBoard implements ShareData{
	
	// the three players
	Player player1;
	Player player2;
	Player player3;
	
	// the whole game field
	private char[][] field;
	private SignalBoard soundField;
	private SignalBoard tempField;
	
	
	// initialize a new game board
	// @param: the car types of three players (G, R, M, T)
	public GameBoard(char player1Type, char player2Type, char player3Type) {
		// create blank field
		field = new char[WIDTH][HEIGHT];
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				field[i][j] = '_';
			}
		}
		
		
		// create blank sound field and temperature field
		soundField = new SignalBoard(SOUND_DACAY_RATE);
		tempField = new SignalBoard(TEMP_DECAY_RATE);
		
		// randomly cover the field with blocks
		int blocks = (int)(WIDTH * HEIGHT * FIELD_COVERAGE_RATE);
		while (blocks > 0) {
			int row = (int)(Math.random() * field.length);
			int col = (int)(Math.random() * field[0].length);
			if (field[row][col] == '_') {
				field[row][col] = '#';
				blocks--;
			}
		}
		// initialize players' locations
		player1 = new Player(field, player1Type);
		player2 = new Player(field, player2Type);
		player3 = new Player(field, player3Type);
		
		// mark the players
		
		field[player1.getPos()[0]][player1.getPos()[1]] = player1Type;
		field[player2.getPos()[0]][player2.getPos()[1]] = player2Type;
		field[player3.getPos()[0]][player3.getPos()[1]] = player3Type;
	}
	
	
	public char[][] displayField() {
		// copy the field and return a new char[][]
		char[][] copy = new char[WIDTH][HEIGHT];
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				copy[i][j] = field[i][j];
			}
		}
		return copy;
	}
		
	
	// display the vicinity of each player
	// @param: which player
	// @return: a 2D array that shows the cells around
	public char[][] displayVicinity(int player) {
		if (player == PLAYER1) {
			return displayVicinityHelper(player1.getPos()[0], player1.getPos()[1], player1.getToken());
		} else if (player == PLAYER2) {
			return displayVicinityHelper(player2.getPos()[0], player2.getPos()[1], player2.getToken());
		} else {
			return displayVicinityHelper(player3.getPos()[0], player3.getPos()[1], player3.getToken());
		}
	}
	
	// the helper method for displayVicinity
	private char[][] displayVicinityHelper(int row, int col, char type) {
		char[][] vicinity = new char[3][3];
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				int loci = i - row + 1;
				int locj = j - col + 1;
				if (i < 0 || i >= WIDTH || j < 0 || j >= HEIGHT) {	// out of bound
					vicinity[loci][locj] = 'X';
				} else if (i == row && j == col) {	// the player's location
					vicinity[loci][locj] = type;
				} else {	// the observable cells on the field
					vicinity[loci][locj] = field[i][j];
				}
			}
		}
		return vicinity;
	}
	
	
	// display a radar representation of the board
	// @return: a 2D array that shows the radar representation
	public char[][] displayRadar(int player) {
		// generate sound and temperature information
		generateTemp(player, RADAR_TEMP);
		generateSound(player, RADAR_SOUND);
		
		if (player == PLAYER1) {
			return displayRadarHelper();
		} else if (player == PLAYER2) {
			return displayRadarHelper();
		} else {
			return displayRadarHelper();
		}
	}
	
	// the helper method for displayRadar
	public char[][] displayRadarHelper() {
		char[][] result = new char[WIDTH][HEIGHT];
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				if (field[i][j] == '#') {
					result[i][j] = '_';
				} else {
					result[i][j] = field[i][j];
				}
			}
		}
		return result;
	}
	
	
	// display a magnetic field representation of the board
	// @param: which player
	// @return: a 2D array that shows the magnetic fied representation
	public char[][] displayMagneticSensor(int player) {
		// generate sound and temperature information
		generateTemp(player, MAGNETIC_SENSOR_TEMP);
		generateSound(player, MAGNETIC_SENSOR_SOUND);
		
		if (player == PLAYER1) {
			return displayMagneticSensorHelper(player1.getPos()[0], player1.getPos()[1], player1.getToken());
		} else if (player == PLAYER2) {
			return displayMagneticSensorHelper(player2.getPos()[0], player2.getPos()[1], player2.getToken());
		} else {
			return displayMagneticSensorHelper(player3.getPos()[0], player3.getPos()[1], player3.getToken());
		}
		
	}
	
	// the helper method for displayMagneticSensor
	private char[][] displayMagneticSensorHelper(int row, int col, char type) {
		int n = MANG_SENSOR_RANGE;
		char[][] result = new char[n][n];
		for (int i = row - (n / 2); i <= row + (n / 2); i++) {
			for (int j = col - (n / 2); j <= col + (n / 2); j++) {
				int loci = i - row + (n / 2);
				int locj = j - col + (n / 2);
				if (i < 0 || i >= WIDTH || j < 0 || j >= HEIGHT) {	// out of bound
					result[loci][locj] = 'X';
				} else if (i == row && j == col) {	// the player's location
					result[loci][locj] = type;
				} else if (field[i][j] != '_') { 	// mark the areas that are not blank
					result[loci][locj] = '*';
					// paralyze the scanned player
					if (i == player1.getPos()[0] && j == player1.getPos()[1]) {
						// TODO notify player1
					} else if (i == player2.getPos()[0] && j == player2.getPos()[1]) {
						// TODO notify player2
					} else if (i == player3.getPos()[0] && j == player3.getPos()[1]) {
						// TODO notify player3
					}
				} else {	// mark the blank area
					result[loci][locj] = '_';
				}
			}
		}
		return result;
	}
	
	
	// generate sound signal
	// @param: the signal source, the sound intensity
	public void generateSound(int player, int intensity) {
		if (player == PLAYER1) {
			soundField.generate(player1.getPos()[0], player1.getPos()[1], intensity);
		} else if (player == PLAYER2) {
			soundField.generate(player2.getPos()[0], player2.getPos()[1], intensity);
		} else if (player == PLAYER3) {
			soundField.generate(player3.getPos()[0], player3.getPos()[1], intensity);
		}
	}
	
	
	// generate temperature signal
	// @param: the signal source, the temperature intensity
	public void generateTemp(int player, int intensity) {
		if (player == PLAYER1) {
			tempField.generate(player1.getPos()[0], player1.getPos()[1], intensity);
		} else if (player == PLAYER2) {
			tempField.generate(player2.getPos()[0], player2.getPos()[1], intensity);
		} else if (player == PLAYER3) {
			tempField.generate(player3.getPos()[0], player3.getPos()[1], intensity);
		}
	}
	
	
	// display sound signal sensed by a player
	// @param: which player
	// @return: a 2D array that represents sound signal
	public char[][] displaySoundSensor(int player) {
		// generate sound and temperature information
		generateTemp(player, SOUND_SENSOR_TEMP);
		generateSound(player, SOUND_SENSOR_SOUND);
		
		if (player == PLAYER1) {
			return soundField.displaySensor(player1.getPos()[0], player1.getPos()[1], SOUND_SENSOR_RANGE);
		} else if (player == PLAYER2) {
			return soundField.displaySensor(player2.getPos()[0], player2.getPos()[1], SOUND_SENSOR_RANGE);
		} else {
			return soundField.displaySensor(player3.getPos()[0], player3.getPos()[1], SOUND_SENSOR_RANGE);
		}
	}
	
	
	// display temperature signal sensed by a player
	// @param: which player
	// @return: a 2D array that represents temperature signal
	public char[][] displayTempSensor(int player) {
		// generate sound and temperature information
		generateTemp(player, TEMP_SENSOR_TEMP);
		generateSound(player, TEMP_SENSOR_SOUND);
		
		if (player == PLAYER1) {
			return tempField.displaySensor(player1.getPos()[0], player1.getPos()[1], TEMP_SENSOR_RANGE);
		} else if (player == PLAYER2) {
			return tempField.displaySensor(player2.getPos()[0], player2.getPos()[1], TEMP_SENSOR_RANGE);
		} else {
			return tempField.displaySensor(player3.getPos()[0], player3.getPos()[1], TEMP_SENSOR_RANGE);
		}
	}
	
	
	// display sonar signal from a player
	// @param: which player
	// @return: an array that represents [left, up, right, down]
	public char[][] displaySonar(int player) {
		// generate sound and temperature information
		generateTemp(player, SONAR_TEMP);
		generateSound(player, SONAR_SOUND);
		
		if (player == PLAYER1) {
			return displaySonarHelper(player1.getPos()[0], player1.getPos()[1]);
		} else if (player == PLAYER2) {
			return displaySonarHelper(player2.getPos()[0], player2.getPos()[1]);
		} else {
			return displaySonarHelper(player3.getPos()[0], player3.getPos()[1]);
		}
	}
	
	// helper method for displaySonar
	public char[][] displaySonarHelper(int row, int col) {
		int[][] temp = new int[4][1];
		// find the left edge
		for (int i = Math.max(col - 1, 0); i >= 0; i--) {
			if (field[row][i] != '_') {
				temp[0][0] = col - i;
				break;
			} else if (i == 0) {
				temp[0][0] = col;
			}
		}
		// find the upper edge
		for (int i = Math.max(0, row - 1); i >= 0; i--) {
			if (field[i][col] != '_') {
				temp[1][0] = row - i;
				break;
			} else if (i == 0) {
				temp[1][0] = row;
			}
		}
		// find the right edge
		for (int i = Math.min(HEIGHT - 1, col + 1); i <= HEIGHT - 1; i++) {
			if (field[row][i] != '_') {
				temp[2][0] = i - col;
				break;
			} else if (i == HEIGHT - 1) {
				temp[2][0] = HEIGHT - col - 1;
			}
		}
		// find the lower edge
		for (int i = Math.min(WIDTH - 1, row + 1); i <= WIDTH - 1; i++) {
			if (field[i][col] != '_') {
				temp[3][0] = i - row;
				break;
			} else if (i == WIDTH - 1) {
				temp[3][0] = WIDTH - row - 1;
			}
		}
		

		// convert int to char
		char[][] result = new char[4][1];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = (char)(temp[i][j] + 48);
			}
		}
		return result;
	}
	
	
	// reduce the sound and temperature signals in the board
	public void decay() {
		soundField.decay();
		tempField.decay();
	}
	
	
	// move a player on the board
	// @param: which player, which direction
	// @return: whether the movement is successful
	public boolean movePlayer(int player, int direction) {		
		if (player == PLAYER1) {
			generateMovementSignal(player, player1.getToken());
			return movePlayerHelper(player1, direction);
		} else if (player == PLAYER2) {
			generateMovementSignal(player, player2.getToken());
			return movePlayerHelper(player2, direction);
		} else if (player == PLAYER3) {
			generateMovementSignal(player, player3.getToken());
			return movePlayerHelper(player3, direction);
		} else {
			return false;
		}
	}
	
	// the helper method for movePlayer
	// @param: the coordinates of the player
	// @return: whether the movement is successful
	private boolean movePlayerHelper(Player player, int direction) {	
		int row  = player.getPos()[0];
		int col = player.getPos()[1];
		char token = player.getToken();
		if (direction == UP) {
			if (row > 0 && field[row - 1][col] != '#') {
				field[row][col] = '_';
				field[row - 1][col] = token;
				player.setPos(row - 1, col);
				return true;
			}
		} else if (direction == DOWN) {
			if (row < HEIGHT - 1 && field[row + 1][col] != '#') {
				field[row][col] = '_';
				field[row + 1][col] = token;
				player.setPos(row + 1, col);
				return true;
			}
		} else if (direction == LEFT) {
			if (col > 0 && field[row][col - 1] != '#') {
				field[row][col] = '_';
				field[row][col - 1] = token;
				player.setPos(row, col - 1);
				return true;
			}
		} else if (direction == RIGHT) {
			if (col < WIDTH - 1 && field[row][col + 1] != '#') {
				field[row][col] = '_';
				field[row][col + 1] = token;
				player.setPos(row, col + 1);
				return true;
			}
		}
		
		// all other cases
		return false;
	}
	
	
	// determine the movement sound and temperature generated by robot type
	// @param: the car type
	private void generateMovementSignal(int player, char token) {
		if (token == 'G') {
			generateTemp(player, G_MOVE_TEMP);
			generateSound(player, G_MOVE_SOUND);
		} else if (token == 'R') {
			generateTemp(player, R_MOVE_TEMP);
			generateSound(player, R_MOVE_SOUND);
		} else if (token == 'M') {
			generateTemp(player, M_MOVE_TEMP);
			generateSound(player, M_MOVE_SOUND);
		} else if (token == 'T') {
			generateTemp(player, T_MOVE_TEMP);
			generateSound(player, T_MOVE_SOUND);
		}
	}
	
	
	// determine the unit hit by a straight weapon (machine gun, laser, rocket launcher, missile)
	// @param: which player, which direction, which weapon
	// @return: the FireEvent of weapon
	public FireEvent useStraightWeapon(int player, int direction, int weapon) {
		generateWeaponSignal(weapon, player);
		if (player == PLAYER1) {
			return useStraightWeaponHelper(player1, direction, weapon);
		} else if (player == PLAYER2) {
			return useStraightWeaponHelper(player2, direction, weapon);
		} else if (player == PLAYER3) {
			return useStraightWeaponHelper(player3, direction, weapon);
		}
		return null;	// not hitting any target
	}
	
	// the helper method for useStraightWeapon
	// @return: the player being hit, the damage, the penetration
	public FireEvent useStraightWeaponHelper(Player player, int direction, int weapon) {
		int[] hit = player.fire(weapon, direction, field);
		return generateFireEvent(hit);
	}
	
	
	// determine the unit hit by mortar
	// @param: which player, the row selection, the col selection, which weapon
	// @return: the target being hit, the damage
	public FireEvent useMortar(int player, int row, int col, int weapon) {
		generateWeaponSignal(weapon, player);
		if (player == PLAYER1) {
			return useMortarHelper(player1, row, col, weapon);
		} else if (player == PLAYER2) {
			return useMortarHelper(player2, row, col, weapon);
		} else if (player == PLAYER3) {
			return useMortarHelper(player3, row, col, weapon);
		}
		return null;	// not hitting any target
	}
	
	
	// the helper method for useMortar
	// @param: the player being hit, the damage, the penetration
	private FireEvent useMortarHelper(Player player, int row, int col, int weapon) {
		int hit[] = player.mortarFire(weapon, row, col);
		return generateFireEvent(hit);
	}
	
	
	// determine the unit hit by howitzer
	// @param: which player, the direction, the distance, which weapon
	// @return: the target being hit, the damage
	public FireEvent useHowitzer(int player, int direction, int distance, int weapon) {
		generateWeaponSignal(weapon, player);
		if (player == PLAYER1) {
			return useHowitzerHelper(player1, direction, distance, weapon);
		} else if (player == PLAYER2) {
			return useHowitzerHelper(player2, direction, distance, weapon);
		} else if (player == PLAYER3) {
			return useHowitzerHelper(player3, direction, distance, weapon);
		}
		return null;	// not hitting any target
	}
	
	
	// the helper method for useHowitzer
	// @return: the player being hit, the damage, the penetration
	private FireEvent useHowitzerHelper(Player player, int direction, int distance, int weapon) {
		int hit[] = player.howitzerFire(weapon, direction, distance);
		return generateFireEvent(hit);
	}
	
	
	private FireEvent generateFireEvent(int[] hit) {
		if (hit == null) {	// no response
			return null;
		}
		if (field[hit[0]][hit[1]] == '#') {	// clean a block
			field[hit[0]][hit[1]] = '_';
			return null;
		} else if (hit[0] == player1.getPos()[0] && hit[1] == player1.getPos()[1]) {	// hit player1
			return new FireEvent(PLAYER1, hit[2], hit[3]);
		} else if (hit[0] == player2.getPos()[0] && hit[1] == player2.getPos()[1]) {	// hit player2
			return new FireEvent(PLAYER2, hit[2], hit[3]);
		} else if (hit[0] == player3.getPos()[0] && hit[1] == player3.getPos()[1]) {	// hit player3
			return new FireEvent(PLAYER3, hit[2], hit[3]);
		}
		
		return null;	// invalid result
	}
	
	
	// generate the corresponding sound and temp signals for each weapon
	// @param: the weapon label, the player label
	public void generateWeaponSignal(int weapon, int player) {
		switch (weapon) {
			case MACHINE_GUN:
				generateSound(player, MACHINE_GUN_SOUND);
				generateTemp(player, MACHINE_GUN_TEMP);
				break;
			case LASER:
				generateSound(player, LASER_SOUND);
				generateTemp(player, LASER_TEMP);
				break;
			case ROCKET_LAUNCHER:
				generateSound(player, ROCKET_LAUNCHER_SOUND);
				generateTemp(player, ROCKET_LAUNCHER_TEMP);
				break;
			case MORTAR:
				generateSound(player, MORTAR_SOUND);
				generateTemp(player, MORTAR_TEMP);
				break;
			case HOWITZER:
				generateSound(player, HOWITZER_SOUND);
				generateTemp(player, HOWITZER_TEMP);
				break;
			case MISSILE:
				generateSound(player, MISSILE_SOUND);
				generateTemp(player, MISSILE_TEMP);
				break;
		}
	}
	
	
	// test method: display an array
	public static void displayC(char[][] arr) {
		for (char[] row : arr) {
			for (char cell : row) {
				System.out.print(cell + " ");
			}
			System.out.println();
		}
	}
	
	public static void displayI(int[][] arr) {
		for (int[] row : arr) {
			for (int cell : row) {
				System.out.print(cell + " ");
			}
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GameBoard test = new GameBoard('G', 'M', 'T');
		displayC(test.field);
		System.out.println(test.useMortar(PLAYER1, 2, 1, MORTAR));
		System.out.println(test.useMortar(PLAYER1, 1, 2, MORTAR));
		System.out.println(test.useMortar(PLAYER1, 2, 3, MORTAR));
		System.out.println(test.useMortar(PLAYER1, 3, 2, MORTAR));
		displayC(test.field);
	}


}
