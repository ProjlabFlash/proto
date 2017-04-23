package graphics;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class GameField extends JPanel {

	public GameField() {
		
		
		this.setLayout(null);
		this.setBounds(0,30,700,700);
	
		this.setBackground(Color.LIGHT_GRAY);
	}
}
