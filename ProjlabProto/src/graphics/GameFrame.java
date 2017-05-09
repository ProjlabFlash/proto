package graphics;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

import main.Controller;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = -9134923568351885820L;
	
	public static GameFrame frame;

	private GameFrame() {
		this.userControl = new UserControl();
		this.canvas = new GameCanvas();
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(userControl);
		contentPane.add(canvas);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		File imgFile = new File (System.getProperty("user.dir"));
		imgFile = new File (imgFile, "images");
		File tunnelImgFile = new File (imgFile, "BuildingSpot");
		tunnelImgFile = new File(tunnelImgFile, "Tunnel.png");
		imgFile = new File (imgFile, "Background");
		imgFile = new File (imgFile, "background.png");
		
		BufferedImage img = null;
		BufferedImage tunnelIcon = null;
		try {
			img = ImageIO.read(imgFile);
			tunnelIcon = ImageIO.read(tunnelImgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		field = new GameField(img, tunnelIcon);
		controller = new Controller();
	}
	
	public UserControl userControl;
	public GameField field;
	public GameCanvas canvas;
	public Controller controller;
	private static List<LevelDescriber> levels = new ArrayList<LevelDescriber>();
	
	public static void main(String args[]) {

		frame = new GameFrame();
		
		levels.add(new LevelDescriber(1, "coords_02.txt", "cmds_02.txt"));
		frame.loadLevel(1);
		
		frame.pack();
		frame.setVisible(true);
	}

	private void loadLevel(int i) {
		
		LevelDescriber ld = levels.get(i - 1);
		Controller.executeFromInput(ld.cmdsFile);
		field.clearField();
		try {
			field.mapParser(ld.coordsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
