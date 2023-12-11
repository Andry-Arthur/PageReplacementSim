import java.io.IOException;
import java.util.Scanner;

public class SimReplacement {
	public static void main(String[] args) throws IOException {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Please pick a number of frames between 1 and 7 (0 to try all of them): ");
		int nFrames = scanner.nextInt();
		System.out.println("Please pick a number of page requests to simulate between 1 and 100: " );
		int nRequest = scanner.nextInt();
		scanner.close();

		//simulation starts
		PageReplacement pr = new PageReplacement(nFrames, nRequest);
		
		if(pr.nFrames == 0) {
			for(int i = 1; i < 8; ++i) {
				pr.runFIFO(i);
			}
			System.out.println("-----------------");
			for(int i = 1; i < 8; ++i) {
				pr.runLRU(i);
			}
			System.out.println("-----------------");
			for(int i = 1; i < 8; ++i) {
				pr.runOptimal(i);
			}
			System.out.println("-----------------");
		}
		else {
			pr.runFIFO();
			System.out.println("-----------------");
			pr.runLRU();
			System.out.println("-----------------");
			pr.runOptimal();
			System.out.println("-----------------");
		}
	}
}
