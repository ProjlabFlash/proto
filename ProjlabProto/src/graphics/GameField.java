package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.JPanel;

import main.Railway;

public class GameField extends JPanel {

	private FieldImageIcon SelectedItems[];
	public GameField() {
		
		
		this.setLayout(null);
		this.setBounds(0,30,700,700);
	
		this.setBackground(Color.LIGHT_GRAY);
	}
	
	public Railway[] getSelectedItems(){
		Railway ret[] = new Railway[2];
		ret[0] = SelectedItems[0].getFieldObject();
		ret[1] = SelectedItems[1].getFieldObject();
		return ret;
	}
	
	
}
