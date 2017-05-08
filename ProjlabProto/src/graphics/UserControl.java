package graphics;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.BuildingSpot;
import main.Railway;

public class UserControl extends JPanel {
	
	private static final long serialVersionUID = 5224795428898415728L;
	
	private JButton BuildTunnel_Button;
	private JButton DestroyTunnel_Button;
	private JButton SwitchPerform_Button;
	private GameField gf;
	
	public UserControl() {
		BuildTunnel_Button = new JButton("Build Tunnel");
		DestroyTunnel_Button = new JButton("Destroy Tunnel");
		SwitchPerform_Button = new JButton("Switch");
		
		setLayout(null);
		this.setBounds(0, 0, 700, 30);
		
		this.add(BuildTunnel_Button);
		this.add(DestroyTunnel_Button);
		this.add(SwitchPerform_Button);
		
		BuildTunnel_Button.setBounds(0, 0, 110, 30);
		DestroyTunnel_Button.setBounds(110, 0, 120, 30);
		SwitchPerform_Button.setBounds(230, 0, 80, 30);
		
		BuildTunnel_Button.addActionListener(new buttonTunnelListener());
		DestroyTunnel_Button.addActionListener(new buttonTunnelDestroyListener());
		SwitchPerform_Button.addActionListener(new buttonSwitchListener());
		
	}
	
	public void PerformBuildTunnel(BuildingSpot buildingSpot1, BuildingSpot buildingSpot2)
	{
		gf.buildTunnel();
	}
	public void PerformSwitchTo(Railway ToHere)
	{
		gf.switchTo();
	}
	public void PerformDestroyTunnel()
	{
		gf.removeTunnel();
	}
	
	private class buttonTunnelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			GameFrame.frame.field.buildTunnel();
		}
		
	}
	private class buttonTunnelDestroyListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			GameFrame.frame.field.removeTunnel();
		}		
	}
	private class buttonSwitchListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			GameFrame.frame.field.switchTo();
		}		
	}
	
	@Override
	public Dimension getMinimumSize() { return new Dimension(300,30);}
	@Override
	public Dimension getPreferredSize() { return new Dimension(700,30);}
	@Override
	public Dimension getMaximumSize() { return new Dimension(50000,30);}
}	
