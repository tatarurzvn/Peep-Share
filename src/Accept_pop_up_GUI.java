
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javax.swing.SpringLayout;
import javax.swing.JLabel;

public class Accept_pop_up_GUI extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private JPanel buttonPane;
	private JButton btnDecline;
	private JButton btnAccept;
	
	private boolean accepted; 
	private boolean btnClicked; 

	public Accept_pop_up_GUI() {
		accepted = false; 
		btnClicked = false; 
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		setTitle("Connection incoming...");
				
		setBounds(100, 100, 450, 289);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		getContentPane().setBackground(new Color(47, 65, 114));
		
		Font myFont = new Font("Calibri", Font.PLAIN, 20);		
		{
			buttonPane = new JPanel();
			springLayout.putConstraint(SpringLayout.NORTH, buttonPane, 251, SpringLayout.NORTH, getContentPane());
			springLayout.putConstraint(SpringLayout.WEST, buttonPane, 0, SpringLayout.WEST, getContentPane());
			springLayout.putConstraint(SpringLayout.EAST, buttonPane, 434, SpringLayout.WEST, getContentPane());
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
		}
		{
			btnDecline = new JButton("Decline");
			springLayout.putConstraint(SpringLayout.SOUTH, btnDecline, 0, SpringLayout.NORTH, buttonPane);
			springLayout.putConstraint(SpringLayout.EAST, btnDecline, -10, SpringLayout.EAST, getContentPane());
			getContentPane().add(btnDecline);
			btnDecline.setFont(myFont);
			btnDecline.setBackground(new Color(121, 134, 172));
			btnDecline.setForeground(new Color(215, 255, 243));
			btnDecline.addActionListener(this);
		}
		{
			btnAccept = new JButton("Accept");
			springLayout.putConstraint(SpringLayout.NORTH, btnAccept, 0, SpringLayout.NORTH, btnDecline);
			springLayout.putConstraint(SpringLayout.EAST, btnAccept, -6, SpringLayout.WEST, btnDecline);
			getContentPane().add(btnAccept);
			btnAccept.setFont(myFont);
			btnAccept.setBackground(new Color(121, 134, 172));
			btnAccept.setForeground(new Color(215, 255, 243));
			btnAccept.addActionListener(this);
		}
		{
			JLabel lblAConnectionWait = new JLabel("A connection waits for your approval...");
			springLayout.putConstraint(SpringLayout.NORTH, lblAConnectionWait, 42, SpringLayout.NORTH, getContentPane());
			springLayout.putConstraint(SpringLayout.WEST, lblAConnectionWait, 39, SpringLayout.WEST, getContentPane());
			springLayout.putConstraint(SpringLayout.SOUTH, lblAConnectionWait, 86, SpringLayout.NORTH, getContentPane());
			springLayout.putConstraint(SpringLayout.EAST, lblAConnectionWait, 283, SpringLayout.WEST, getContentPane());
			getContentPane().add(lblAConnectionWait);
			lblAConnectionWait.setForeground(new Color(233, 209, 172));
			lblAConnectionWait.setFont(myFont);
		}
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnAccept){
			System.out.println("accept! ");
			btnClicked = true;
			accepted = true; 
		}
		if(e.getSource() == btnDecline){
			System.out.println("decline! ");
			btnClicked = true;
			accepted = false;
		}
	}
	
	public void close(){
		dispose();
	}
	
	public boolean isAccepted() {
		return accepted;
	}
	
	public boolean isBtnClicked() {
		return btnClicked;
	}
}
