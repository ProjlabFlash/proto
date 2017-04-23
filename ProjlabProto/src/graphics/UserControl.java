package graphics;

import javax.swing.JButton;
import javax.swing.JPanel;

public class UserControl extends JPanel {
	
	private JButton BuildTunnel_Button;
	private JButton DestroyTunnel_Button;
	private JButton SwitchPerform_Button;
	
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
	}

}
