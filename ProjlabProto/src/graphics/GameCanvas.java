package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GameCanvas extends JPanel{
	
	public GameCanvas() {
		setBorder(BorderFactory.createLineBorder(Color.black));
		setDoubleBuffered(true);
		addMouseListener(new gamecanvasMouseListener());
	}

	public Dimension getPreferredSize() {
		return new Dimension(250,200);
	}
	
	public Dimension getMinimumSize() {
		return new Dimension(200,200);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	private class gamecanvasMouseListener	implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent arg) {
			GameFrame.frame.field.select(arg.getX(), arg.getY());			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
