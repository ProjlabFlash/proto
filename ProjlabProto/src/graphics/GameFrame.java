package graphics;

import java.awt.Container;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import main.Controller;

public class GameFrame extends JFrame {

	public static GameFrame frame;

	private GameFrame() {
		this.userControl = new UserControl();
		this.canvas = new GameCanvas();
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(userControl);
		contentPane.add(canvas);
		field = new GameField();
	}
	
	public UserControl userControl;
	public GameField field;
	public GameCanvas canvas;
	public Controller controller;

	
	public static void main(String args[]) throws IOException {

		GameFrame frame = new GameFrame();

		frame.pack();
		frame.setVisible(true);
	}
}
