package graphics;

import javax.swing.JFrame;

import main.Controller;

public class GameFrame extends JFrame {

	public static GameFrame frame;

	private GameFrame() {
		this.userControl = new UserControl();
		this.setLayout(null);
		this.add(userControl);
	}
	
	public UserControl userControl;
	public GameField field;
	public Controller controller;

	
	public static void main(String args[]) {

		//GameFrame frame = new GameFrame();
		//gameField = new GameField();
		//this.add(gameField);
	}
}
