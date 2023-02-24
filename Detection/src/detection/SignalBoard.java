package detection;

public class SignalBoard implements ShareData {
		
	
	protected int[][] field;	// the signal board
	protected int decayRate;	// the rate of signal decay
	
	
	// constructor: initialize the signal field
	public SignalBoard(int rate) {
		field = new int[WIDTH][HEIGHT];
		decayRate = rate;
	}
	
	
	// generate signal on the signal board
	// @params: row, col: the location of the signal source, intensity: the intensity of the signal
	public void generate(int row, int col, int intensity) {
		int n = intensity;
		for (int i = Math.max(0, row - n + 1); i <= Math.min(row + n - 1, WIDTH - 1); i++) {
			for (int j = Math.max(0, col - n + 1); j <= Math.min(col + n - 1, HEIGHT - 1); j++) {
				int distance = (int)(Math.sqrt(Math.pow(row - i, 2) + Math.pow(col - j, 2)));
				field[i][j] += Math.max(intensity - distance, 0);
			}
		}
	}
	
	
	// reduce the signal
	// @params: the decrease rate in each round
	public void decay() {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				field[i][j] = Math.max(0, field[i][j] - decayRate);
			}
		}
	}
	
	
	// display signals captured by a sensor
	public char[][] displaySensor(int row, int col, int range) {
		int n = range;
		int[][] temp = new int[n][n];
		for (int i = row - (n / 2); i <= row + (n / 2); i++) {
			for (int j = col - (n / 2); j <= col + (n / 2); j++) {
				int loci = i - row + (n / 2);
				int locj = j - col + (n / 2);
				if (i < 0 || i >= WIDTH || j < 0 || j >= HEIGHT) {	// out of bound
					temp[loci][locj] = -1;
				} else {
					temp[loci][locj] = field[i][j];
				}
			}
		}
		// convert int to char
		char[][] result = new char[n][n];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				int intensity = temp[i][j];
				if (intensity >= 0 && intensity <= 9) {
					result[i][j] = (char)(intensity + 48);
				} else {
					result[i][j] = 'X';
				}
			}
		}
		return result;
	}
	
	
	
	// test method: display an array
	private static void display(int[][] arr) {
		for (int[] row : arr) {
			for (int cell : row) {
				System.out.print(cell + " ");
			}
			System.out.println();
		}
	}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		
	}

}
