package detection;

public class Player implements ShareData {
	
	private int[] coordinates;
	private char token;
	
	// different weapons for a player
	public Weapon machineGun = new Weapon(MACHINE_GUN_DAMAGE, MACHINE_GUN_PENETRATION, 
			MACHINE_GUN_DECAY, MACHINE_GUN_AMMO, MACHINE_GUN_SOUND, MACHINE_GUN_TEMP, false);
	public Weapon laser = new Weapon(LASER_DAMAGE, LASER_PENETRATION, 
			LASER_DECAY, LASER_AMMO, LASER_SOUND, LASER_TEMP, false);
	public Weapon rocketLauncher = new Weapon(ROCKET_LAUNCHER_DAMAGE, ROCKET_LAUNCHER_PENETRATION, 
			ROCKET_LAUNCHER_DECAY, ROCKET_LAUNCHER_AMMO, ROCKET_LAUNCHER_SOUND, ROCKET_LAUNCHER_TEMP, false);
	public Weapon missile = new Weapon(MISSILE_DAMAGE, MISSILE_PENETRATION, 
			MISSILE_DECAY, MISSILE_AMMO, MISSILE_SOUND, MISSILE_TEMP, true);
	public Weapon mortar = new Weapon(MORTAR_DAMAGE, MORTAR_PENETRATION, MORTAR_DECAY,
			MORTAR_AMMO, MORTAR_SOUND, MORTAR_TEMP, true);
	public Weapon howitzer = new Weapon(HOWITZER_DAMAGE, HOWITZER_PENETRATION, HOWITZER_DECAY,
			HOWITZER_AMMO, HOWITZER_SOUND, HOWITZER_TEMP, true);
	
	
	// player constructor 
	
	// TODO: two players could spawn at the same location
	public Player(char[][] field, char token) {
		// initialize a player at a random location at a field
		int row = (int)(Math.random() * field.length);
		int col = (int)(Math.random() * field[0].length);
		while (field[row][col] != '_') {	// reselect the position
			row = (int)(Math.random() * field.length);
			col = (int)(Math.random() * field[0].length);
		}
		coordinates = new int[] {row, col};
		this.token = token;
	}
	
	
	// return the player's coordinates
	public int[] getPos() {
		return coordinates;
	}
	
	
	// return the player's token
	public char getToken() {
		return token;
	}
	
	
	// change the player's coordinates
	// @param: row: the new row, col: the new col
	public void setPos(int row, int col) {
		coordinates[0] = row;
		coordinates[1] = col;
	}
	
	
	// the player fires
	// @param: the type of weapon, the direction of weapon, the game field
	// @return: the position being hit, the damage caused, the weapon's penetration
	public int[] fire(int weapon, int direction, char[][] field) {
		if (weapon == MACHINE_GUN) {
			return machineGun.fire(coordinates[0], coordinates[1], direction, field);
		} else if (weapon == LASER) {
			return laser.fire(coordinates[0], coordinates[1], direction, field);
		} else if (weapon == ROCKET_LAUNCHER) {
			return rocketLauncher.fire(coordinates[0], coordinates[1], direction, field);
		} else if (weapon == MISSILE) {
			return missile.fire(coordinates[0], coordinates[1], direction, field);
		}
		
		return null;	// unexpected weapon type
	}	
	
	
	// fire method for mortar
	public int[] mortarFire(int weapon, int row, int col) {
		return mortar.mortarFire(coordinates[0], coordinates[1], row, col);
	}
	
	
	// fire method for howitzer
	public int[] howitzerFire(int weapon, int direction, int distance) {
		return howitzer.howitzerFire(coordinates[0], coordinates[1], direction, distance);
	}


}
