package detection;

public class Weapon implements ShareData {
	
	public int damage;
	public int penetration;
	public int decay;
	public int ammo;
	public int sound;
	public int heat;
	public boolean canCross;	// whether the weapon can be blocked by #
	
	public Weapon(int damage, int penetration, int decay, int ammo, int sound, int heat, boolean canCross) {
		this.damage = damage;
		this.penetration = penetration;
		this.decay = decay;
		this.ammo = ammo;
		this.sound = sound;
		this.heat = heat;
		this.canCross = canCross;
	}
	
	
	// @param: the coordinates of the player who fires, the fire direction, the game field
	// @return: the row and col of the spot hit, the damage
	public int[] fire(int startRow, int startCol, int direction, char[][] field) {
		int netDamage = damage;
		if (direction == UP) {	// upward
			for (int i = Math.max(0, startRow - 1); i >= 0; i--) {
				int pos = field[i][startCol];
				if (pos == '#' && !canCross) {	// hit the block
					return new int[] {i, startCol, Math.max(0, netDamage), penetration};
				} else if (pos == 'G' || pos == 'R' || pos == 'M' || pos == 'T') {	// hit an player
					return new int[] {i, startCol, Math.max(0, netDamage), penetration};
				} else if (i == 0) {	// hit the edge
					return new int[] {i, startCol, Math.max(0, netDamage), penetration};
				}
				netDamage -= decay;	// reduce the weapon's damage
			}
		} else if (direction == DOWN) {	// downward
			for (int i = Math.min(HEIGHT - 1, startRow + 1); i < HEIGHT; i++) {
				int pos = field[i][startCol];
				if (pos == '#' && !canCross) {	// hit the block
					return new int[] {i, startCol, Math.max(0, netDamage), penetration};
				} else if (pos == 'G' || pos == 'R' || pos == 'M' || pos == 'T') {	// hit an player
					return new int[] {i, startCol, Math.max(0, netDamage), penetration};
				} else if (i == HEIGHT - 1) {	// hit the edge
					return new int[] {i, startCol, Math.max(0, netDamage), penetration};
				}
				netDamage -= decay;	// reduce the weapon's damage
			}
		} else if (direction == LEFT) {	// leftward
			for (int i = Math.max(0, startCol - 1); i >= 0; i--) {
				int pos = field[startRow][i];
				if (pos == '#' && !canCross) {	// hit the block
					return new int[] {startRow, i, Math.max(0, netDamage), penetration};
				} else if (pos == 'G' || pos == 'R' || pos == 'M' || pos == 'T') {	// hit an player
					return new int[] {startRow, i, Math.max(0, netDamage), penetration};
				} else if (i == 0) {	// hit the edge
					return new int[] {startRow, i, Math.max(0, netDamage), penetration};
				}
				netDamage -= decay;	// reduce the weapon's damage
			}
		} else if (direction == RIGHT) { // rightward
			for (int i = Math.min(WIDTH - 1, startCol + 1); i < WIDTH; i++) {
				int pos = field[startRow][i];
				if (pos == '#' && !canCross) {	// hit the block
					return new int[] {startRow, i, Math.max(0, netDamage), penetration};
				} else if (pos == 'G' || pos == 'R' || pos == 'M' || pos == 'T') {	// hit an player
					return new int[] {startRow, i, Math.max(0, netDamage), penetration};
				} else if (i == WIDTH - 1) {	// hit the edge
					return new int[] {startRow, i, Math.max(0, netDamage), penetration};
				}
				netDamage -= decay;	// reduce the weapon's damage
			}
		}
		
			
		return null;	// an error occured
	}
	
	
	// @param: the coordinates of the player who fires, the fire position, the game field
	//			pos[0]: 0 - 4  pos[1]: 0 - 4
	// @return: the row and col of the spot hit, the damage
	public int[] mortarFire(int startRow, int startCol, int posX, int posY) {
		int row = startRow - 2 + posX;
		int col = startCol - 2 + posY;
		// position not out of bound, cannot hit yourself
		if (row >= 0 && row < WIDTH && col >= 0 && col < HEIGHT && !(posX == 2 && posY == 2)) {
			return new int[] {row, col, damage, penetration};
		} else {
			return null;
		}
	}
	
	
	// @param: the coordinates of the player who fires, the fire direction, the range
	// the row and col od the spot hit, the damage
	public int[] howitzerFire(int startRow, int startCol, int direction, int distance) {
		if (direction == UP) {
			int row = Math.max(0, startRow - distance);
			return new int[] {row, startCol, damage, penetration};
 		} else if (direction == DOWN) {
 			int row = Math.min(WIDTH - 1, startRow + distance);
 			return new int[] {row, startCol, damage, penetration};
		} else if (direction == LEFT) {
			int col = Math.max(0, startCol - distance);
			return new int[] {startRow, col, damage, penetration};
		} else if (direction == RIGHT) {
			int col = Math.min(HEIGHT - 1, startCol + distance);
			return new int[] {startRow, col, damage, penetration};
		}
		
		return null;
	}
}
