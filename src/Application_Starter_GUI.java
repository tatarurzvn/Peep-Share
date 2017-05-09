import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;

public class Application_Starter_GUI implements ActionListener, ListSelectionListener{

	private JFrame frmPeepShare;
	private JTextField textField;
	private JButton btnConnect;
	private int port; 
	private int filePort;
	private UDPIdentifier identify; 
	private ServerAccept server; 
	private ArrayList<ClientConnection> connectionList; 
	private JLabel lblRequestStatus;
	private JLabel lblSelectFromThis;
	private JList<String> list;
	private DefaultListModel<String> ipAddresses;
	private ArrayList<InetAddress> addrList;
	private AddressFinder addrFind;
	private JButton btnRefresh;

	/**
	 * Create the application.
	 */
	public Application_Starter_GUI() {
		port = 4546;
		filePort = 4547;
		
		connectionList = new ArrayList<ClientConnection>(); 
		
		identify = new UDPIdentifier(2233, "myName");
		identify.start();
		
		server = new ServerAccept(port, filePort); 
		server.start(); 
		
		addrFind = new AddressFinder(); 
		addrList = new ArrayList<InetAddress>();
		try {
			addrList = addrFind.findLocalAddress(2233, 4455, 1000);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (InetAddress addr : addrList){
			System.out.println(addr.getHostAddress());
		}
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPeepShare = new JFrame();
		frmPeepShare.setResizable(false);
		frmPeepShare.setTitle("Peep Share");
		frmPeepShare.setBounds(100, 100, 450, 300);
		frmPeepShare.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmPeepShare.getContentPane().setLayout(springLayout);
		frmPeepShare.getContentPane().setBackground(new Color(47, 65, 114));
		
		Font myFont = new Font("Calibri", Font.PLAIN, 20);
		Font mySmallerFont = new Font("Calibri", Font.PLAIN, 14);
		
		btnConnect = new JButton("Connect");
		springLayout.putConstraint(SpringLayout.SOUTH, btnConnect, -10, SpringLayout.SOUTH, frmPeepShare.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnConnect, -10, SpringLayout.EAST, frmPeepShare.getContentPane());
		frmPeepShare.getContentPane().add(btnConnect);
		btnConnect.setBackground(new Color(121, 134, 172));
		btnConnect.setForeground(new Color(215, 255, 243));
		btnConnect.setFont(myFont);
		
		btnConnect.addActionListener(this);
		
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField, 63, SpringLayout.NORTH, frmPeepShare.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textField, 36, SpringLayout.WEST, frmPeepShare.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, textField, 93, SpringLayout.NORTH, frmPeepShare.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textField, 236, SpringLayout.WEST, frmPeepShare.getContentPane());
		frmPeepShare.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setBackground(new Color(6, 20, 57));
		textField.setForeground(new Color(215, 255, 243));
		textField.setFont(myFont);
		textField.setText("192.168.0.112"); // to delete
		
		JLabel lblEnterTheIp = new JLabel("Enter the IP below");
		springLayout.putConstraint(SpringLayout.NORTH, lblEnterTheIp, 34, SpringLayout.NORTH, frmPeepShare.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblEnterTheIp, 36, SpringLayout.WEST, frmPeepShare.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblEnterTheIp, -6, SpringLayout.NORTH, textField);
		springLayout.putConstraint(SpringLayout.EAST, lblEnterTheIp, 0, SpringLayout.EAST, textField);
		frmPeepShare.getContentPane().add(lblEnterTheIp);
		lblEnterTheIp.setForeground(new Color(233, 209, 172));
		lblEnterTheIp.setFont(myFont);
		
		lblRequestStatus = new JLabel("");
		springLayout.putConstraint(SpringLayout.NORTH, lblRequestStatus, 0, SpringLayout.NORTH, lblEnterTheIp);
		springLayout.putConstraint(SpringLayout.EAST, lblRequestStatus, 0, SpringLayout.EAST, btnConnect);
		frmPeepShare.getContentPane().add(lblRequestStatus);
		lblRequestStatus.setForeground(new Color(233, 209, 172));
		lblRequestStatus.setFont(myFont);
		
		lblSelectFromThis = new JLabel("Select from this list");
		springLayout.putConstraint(SpringLayout.NORTH, lblSelectFromThis, 6, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, lblSelectFromThis, 0, SpringLayout.WEST, textField);
		frmPeepShare.getContentPane().add(lblSelectFromThis);
		lblSelectFromThis.setForeground(new Color(233, 209, 172));
		lblSelectFromThis.setFont(myFont);
		
		ipAddresses = new DefaultListModel<String>();		
		for (InetAddress addr : addrList){
			ipAddresses.addElement(addr.getHostAddress() + "-" + addr.getHostName());
		}		
		list = new JList<String>(ipAddresses);
		springLayout.putConstraint(SpringLayout.NORTH, list, 6, SpringLayout.SOUTH, lblSelectFromThis);
		springLayout.putConstraint(SpringLayout.WEST, list, 0, SpringLayout.WEST, textField);
		frmPeepShare.getContentPane().add(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(new Color(47, 65, 114));
		list.setForeground(new Color(215, 255, 243));	
		list.setFont(mySmallerFont);
		list.addListSelectionListener(this);
		
		btnRefresh = new JButton("Refresh");
		springLayout.putConstraint(SpringLayout.SOUTH, btnRefresh, 0, SpringLayout.SOUTH, btnConnect);
		springLayout.putConstraint(SpringLayout.EAST, btnRefresh, -6, SpringLayout.WEST, btnConnect);
		frmPeepShare.getContentPane().add(btnRefresh);
		btnRefresh.setBackground(new Color(121, 134, 172));
		btnRefresh.setForeground(new Color(215, 255, 243));
		btnRefresh.setFont(myFont);
		btnRefresh.addActionListener(this);
		
		frmPeepShare.setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnConnect){
			//backend connection
			//textField.getText() should be the IP address
			//check the value before sending
			//new Accept_pop_up_GUI();
			
			ClientConnection testConn = new ClientConnection(textField.getText(), port, filePort);
			testConn.setResponseLabel(lblRequestStatus);
			testConn.start();
			
			lblRequestStatus.setText("Request sent");
			
			connectionList.add(testConn);
		}
		if(e.getSource() == btnRefresh){
			addrList.clear();
			ipAddresses.clear();
			try {
				addrList = addrFind.findLocalAddress(2233, 4455, 1000);
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (InetAddress addr : addrList){
				ipAddresses.addElement(addr.getHostAddress() + "-" + addr.getHostName());
			}	
		}
	}
	
	public void valueChanged(ListSelectionEvent e){
		if(!e.getValueIsAdjusting()){
			JList<?> source = (JList<?>)e.getSource();
			if(!source.isSelectionEmpty()){
				textField.setText(source.getSelectedValue().toString().substring(0, source.getSelectedValue().toString().indexOf("-")));
			}
		}
	}
}
