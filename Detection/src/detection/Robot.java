package detection;

public class Robot implements ShareData {
	
	public char token;
	public int health;
	public int armor;
	public int energy;
	public int ammo;
	public int energyRecharge;
	public int ammoRecharge;
	public int maxEnergy;
	public int maxAmmo;
	public int maxHealth;
	public int moveEnergy;
	public int moveSound;
	public int moveTemp;
	
	
	public Robot(char token) {
		this.token = token;
		if (token == 'G') {	// G type robot
			maxHealth = 100;
			armor = 1;
			maxEnergy = 15;
			maxAmmo = 10;
			energyRecharge = 4;
			ammoRecharge = 1;
			moveEnergy = 1;
			moveSound = 1;
			moveTemp = 1;
		} else if (token == 'R') { // R type robot
			maxHealth = 150;
			armor = 3;
			maxEnergy = 20;
			maxAmmo = 20;
			energyRecharge = 2;
			ammoRecharge = 2;
			moveEnergy = 1;
			moveSound = 3;
			moveTemp = 3;
		} else if (token == 'M') { // M type robot
			maxHealth = 200;
			armor = 5;
			maxEnergy = 20;
			maxAmmo = 30;
			energyRecharge = 2;
			ammoRecharge = 3;
			moveEnergy = 2;
			moveSound = 5;
			moveTemp = 5;
		} else if (token == 'T') { // T type robot
			maxHealth = 130;
			armor = 2;
			maxEnergy = 20;
			maxAmmo = 15;
			energyRecharge = 3;
			ammoRecharge = 2;
			moveEnergy = 1;
			moveSound = 2;
			moveTemp = 2;
		}
	}
	
	
	// initialize a robot
	public void spawn() {
		health = maxHealth;
		ammo = maxAmmo;
		energy = maxEnergy;
	}
	
	
	// determine the robot is alive
	public boolean isAlive() {
		return health > 0;
	}
	
	
	// recharge the energy and ammo
	public void recharge() {
		energy = Math.min(maxEnergy, energy + energyRecharge);
		ammo = Math.min(maxAmmo, ammo + ammoRecharge);
	}
	
	
	// display the robot status information
	public String toString() {
		return "health: " + health + "/" + maxHealth + " armor: " + armor
				+ " energy: " + energy + "/" + maxEnergy + " ammo: " + ammo + "/" + maxAmmo;
	}
	
	
	// calculate the net damage caused by an attack and reduce health
	// @param: the damage received, the penetration of the weapon
	public void generateNetDamage(int damage, int penetration) {
		if (penetration < armor) {	// part of damage is blocked by ammo
			health -= Math.max(0, damage - ((armor - penetration) * 10));
		} else {	// enough penetration, cause negative effect
			// TODO generate negative effect
			health -= damage;
		}
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
