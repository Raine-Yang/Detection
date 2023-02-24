package detection;

public class TestEnergy implements ShareData {
	
	// determine whether the robot has sufficient energy to use a sensor
	// @param: the sensor used, the remaining energy
	// @return: the remaining energy
	public int testSensorEnergy(int sensor, int energy) {
		switch (sensor) {
			case SONAR:
				return energy - SONAR_ENERGY;
			case RADAR:
				return energy - RADAR_ENERGY;
			case MAGNETIC_SENSOR:
				return energy - MAGNETIC_SENSOR_ENERGY;
			case SOUND_SENSOR:
				return energy - SOUND_SENSOR_ENERGY;
			case TEMP_SENSOR:
				return energy - TEMP_SENSOR_ENERGY;
			default:	// unknown sensor
				return -1;
		}
	}
	
	
	public int testMovementEnergy(char token, int energy) {
		switch (token) {
			case 'G':
				return energy - G_MOVE_ENERGY;
			case 'R':
				return energy - R_MOVE_ENERGY;
			case 'M':
				return energy - M_MOVE_ENERGY;
			case 'T':
				return energy - T_MOVE_ENERGY;
			default:
				return -1;
		}
	}
	
	
	public int testFireAmmo(int weapon, int ammo) {
		switch (weapon) {
			case MACHINE_GUN:
				return ammo - MACHINE_GUN_AMMO;
			case LASER:
				return ammo - LASER_AMMO;
			case ROCKET_LAUNCHER:
				return ammo - ROCKET_LAUNCHER_AMMO;
			case MORTAR:
				return ammo - MORTAR_AMMO;
			case HOWITZER:
				return ammo - HOWITZER_AMMO;
			case MISSILE:
				return ammo - MISSILE_AMMO;
			default:
				return -1;
		}
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
