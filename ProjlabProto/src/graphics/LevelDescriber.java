package graphics;

import java.io.File;

public class LevelDescriber {

	public final int number;
	public final File coordsFile;
	public final File cmdsFile;
	
	public LevelDescriber(int number, String fCoordsName, String fCmdsName) {
		this.number = number;
		File temp = new File(System.getProperty("user.dir"));
		temp = new File(temp, "maps");
		coordsFile = new File(temp, fCoordsName); 
		cmdsFile = new File(temp, fCmdsName);
	}
}
