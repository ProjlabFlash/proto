package graphics;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
	
	public UserControl userControl;
	public GameField gameField;
	
	public GameFrame() {
		this.userControl = new UserControl();
		this.gameField = new GameField();
		this.setLayout(null);
		this.add(userControl);
		this.add(gameField);
	}
}
