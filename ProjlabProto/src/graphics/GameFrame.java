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
import main.Controller.GameStateObserver;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = -9134923568351885820L;
	/**
	 * public frame 
	 */
	public static GameFrame frame;
	/**
	 * ctor: initializing the frame
	 */
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
		Controller.attachGameStateObserver(bso);
	}
	
	public UserControl userControl;
	public GameField field;
	public GameCanvas canvas;
	public Controller controller;
	private static List<LevelDescriber> levels = new ArrayList<LevelDescriber>();
	private int currentLevel = 1;
	/**
	 * programs entry point
	 * @param args
	 */
	public static void main(String args[]) {

		frame = new GameFrame();
		
		levels.add(new LevelDescriber(1, "coords_02.txt", "cmds_02.txt"));
		levels.add(new LevelDescriber(2, "coords_01.txt", "cmds_01.txt"));
		frame.loadLevel(1);
		
		frame.pack();
		frame.setVisible(true);
	}
	/**
	 * loads the level's gamefield and the sets it up along with the model
	 * @param i
	 */
	private void loadLevel(int i) {
		
		LevelDescriber ld = levels.get((i - 1) % levels.size());
		Controller.executeFromInput(ld.cmdsFile);
		field.clearField();
		try {
			field.mapParser(ld.coordsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.repaint();
	}
	
	GSObserver bso = new GSObserver();
	/**
	 * observer for the start of the game
	 * @author mmlaj
	 *
	 */
	private class GSObserver extends GameStateObserver {

		@Override
		public void win() {
			frame.loadLevel(++currentLevel);
		}

		@Override
		public void lose() {
			frame.loadLevel(currentLevel);
		}
		
	}
	
}
