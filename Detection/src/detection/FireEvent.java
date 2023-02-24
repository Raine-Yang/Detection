package detection;

public class FireEvent implements ShareData, java.io.Serializable {
	
	public int target;
	public int damage;
	public int penetration;
	
	public FireEvent(int target, int damage, int penetration) {
		this.target = target;
		this.damage = damage;
		this.penetration = penetration;
	}
	
	
	public String toString() {
		return "target: " + target + " damage: " + damage + " penetration: " + penetration;
	}
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
