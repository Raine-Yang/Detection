package detection;

public interface ShareData {
	// the port to start the game
	public static int PORT = 8000;
	
	
	// labels for players
	public static int PLAYER1 = 1;
	public static int PLAYER2 = 2;
	public static int PLAYER3 = 3;
	
	
	// parameters for the game board
	public static int WIDTH = 15;
	public static int HEIGHT = 15;
	public static double FIELD_COVERAGE_RATE = 0.25;
	public static int SOUND_DACAY_RATE = 2;
	public static int TEMP_DECAY_RATE = 1;
	
	
	// ranges
	public static int SOUND_SENSOR_RANGE = 7;
	public static int TEMP_SENSOR_RANGE = 7;
	public static int MANG_SENSOR_RANGE = 5;
	
	
	// sound productions
	// sensor sound production
	public static int SONAR_SOUND = 3;
	public static int RADAR_SOUND = 5;
	public static int MAGNETIC_SENSOR_SOUND = 5;
	public static int SOUND_SENSOR_SOUND = 0;
	public static int TEMP_SENSOR_SOUND = 3;
	// movement sound production
	public static int G_MOVE_SOUND = 1;
	public static int R_MOVE_SOUND = 3;
	public static int M_MOVE_SOUND = 5;
	public static int T_MOVE_SOUND = 2;
	// weapon sound production
	public static int MACHINE_GUN_SOUND = 5;
	public static int LASER_SOUND = 2;
	public static int ROCKET_LAUNCHER_SOUND = 5;
	public static int MORTAR_SOUND = 5;
	public static int HOWITZER_SOUND = 6;
	public static int MISSILE_SOUND = 6;
	
	
	// temperature productions
	// sensor temp production
	public static int SONAR_TEMP = 3;
	public static int RADAR_TEMP = 5;
	public static int MAGNETIC_SENSOR_TEMP = 5;
	public static int SOUND_SENSOR_TEMP = 3;
	public static int TEMP_SENSOR_TEMP = 0;
	// movement temp production
	public static int G_MOVE_TEMP = 1;
	public static int R_MOVE_TEMP = 3;
	public static int M_MOVE_TEMP = 5;
	public static int T_MOVE_TEMP = 2;
	// weapon temp production
	public static int MACHINE_GUN_TEMP = 2;
	public static int LASER_TEMP = 6;
	public static int ROCKET_LAUNCHER_TEMP = 5;
	public static int MORTAR_TEMP = 5;
	public static int HOWITZER_TEMP = 6;
	public static int MISSILE_TEMP = 6;
	
	
	// energy costs
	// sensor energy cost
	public static int SONAR_ENERGY = 1;
	public static int RADAR_ENERGY = 7;
	public static int MAGNETIC_SENSOR_ENERGY = 5;
	public static int SOUND_SENSOR_ENERGY = 2;
	public static int TEMP_SENSOR_ENERGY = 3;
	// movement energy cost
	public static int G_MOVE_ENERGY = 1;
	public static int R_MOVE_ENERGY = 1;
	public static int M_MOVE_ENERGY = 2;
	public static int T_MOVE_ENERGY = 1;
	
	
	// parameters for weapons
	// ammo costs
	public static int MACHINE_GUN_AMMO = 1;
	public static int LASER_AMMO = 3;
	public static int ROCKET_LAUNCHER_AMMO = 3;
	public static int MORTAR_AMMO = 3;
	public static int HOWITZER_AMMO = 6;
	public static int MISSILE_AMMO = 6;
	
	// damage
	public static int MACHINE_GUN_DAMAGE = 60;
	public static int LASER_DAMAGE = 45;
	public static int ROCKET_LAUNCHER_DAMAGE = 90;
	public static int MORTAR_DAMAGE = 60;
	public static int HOWITZER_DAMAGE = 120;
	public static int MISSILE_DAMAGE = 20;
	
	// decay
	public static int MACHINE_GUN_DECAY = 15;
	public static int LASER_DECAY = 0;
	public static int ROCKET_LAUNCHER_DECAY = 10;
	public static int MORTAR_DECAY = 0;
	public static int HOWITZER_DECAY = 0;
	public static int MISSILE_DECAY = -10;
	
	// penetration
	public static int MACHINE_GUN_PENETRATION = 1;
	public static int LASER_PENETRATION = 4;
	public static int ROCKET_LAUNCHER_PENETRATION = 2;
	public static int MORTAR_PENETRATION = 3;
	public static int HOWITZER_PENETRATION = 5;
	public static int MISSILE_PENETRATION = 2;
	
	
	
	// Event types
	// sensor event types
	public static int SONAR = 1001;
	public static int SOUND_SENSOR = 1002;
	public static int TEMP_SENSOR = 1003;
	public static int MAGNETIC_SENSOR = 1004;
	public static int RADAR = 1005;
	// movement event types
	public static int MOVE = 2000;
	// weapon event types
	public static int MACHINE_GUN = 3001;
	public static int LASER = 3002;
	public static int ROCKET_LAUNCHER = 3003;
	public static int MORTAR = 3004;
	public static int HOWITZER = 3005;
	public static int MISSILE = 3006;
	// pause event type
	public static int PAUSE = 0;

	
	// direction indicators
	public static int UP = 1;
	public static int DOWN = 2;
	public static int LEFT = 3;
	public static int RIGHT = 4;
	
	
	
}
