package FileIO;

import java.io.IOException;

public class Driver {

	public static void main(String[] args) {
		SuperiorFileIO sf = new SuperiorFileIO();
//		sf.runChess();
		try {
			sf.parseFromArray(args[0]);
		} catch (IOException e) {
			System.out.println("File error - file not found");
		}
	}

}
