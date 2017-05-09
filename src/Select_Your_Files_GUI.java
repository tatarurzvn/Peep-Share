import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class Select_Your_Files_GUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnSend;
	private JButton btnBrowse;
	private JLabel lblFilesize;
	private JLabel lblFilename;
	
	private JFileChooser fileChooser;
	private File selectedFile;
	private PrintWriter output; 
	private BufferedReader input;
	
	private int filePort;
	private String hostname;
	
	private boolean waitForSelection = true;
	
	/**
	 * Create the frame.
	 */
	public Select_Your_Files_GUI (PrintWriter output, BufferedReader input, String hostname, int filePort) {
		
		this.filePort = filePort;
		this.output = output;
		this.input = input; 
		this.hostname = hostname;
		
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		contentPane.setBackground(new Color(47, 65, 114));
		setTitle("Choose Your Files");
		
		Font myFont = new Font("Calibri", Font.PLAIN, 20);
		Font myFontForHeader = new Font("Calibri", Font.PLAIN, 24);
		
		btnSend = new JButton("Send");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnSend, -10, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnSend, -10, SpringLayout.EAST, contentPane);
		contentPane.add(btnSend);
		btnSend.setFont(myFont);
		btnSend.setBackground(new Color(121, 134, 172));
		btnSend.setForeground(new Color(215, 255, 243));
		
		btnBrowse = new JButton("Browse");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnBrowse, 0, SpringLayout.NORTH, btnSend);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnBrowse, -6, SpringLayout.WEST, btnSend);
		contentPane.add(btnBrowse);
		btnBrowse.setFont(myFont);
		btnBrowse.setBackground(new Color(121, 134, 172));
		btnBrowse.setForeground(new Color(215, 255, 243));
		
		JLabel lblFile = new JLabel("File");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblFile, 15, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblFile, 15, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblFile, 38, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblFile, 82, SpringLayout.WEST, contentPane);
		contentPane.add(lblFile);
		lblFile.setFont(myFontForHeader);
		lblFile.setForeground(new Color(233, 209, 172));
		
		JLabel lblSize = new JLabel("Size");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblSize, -4, SpringLayout.NORTH, lblFile);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblSize, 0, SpringLayout.WEST, btnBrowse);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblSize, 198, SpringLayout.EAST, lblFile);
		contentPane.add(lblSize);
		lblSize.setFont(myFontForHeader);
		lblSize.setForeground(new Color(233, 209, 172));
		
		lblFilename = new JLabel("File_Name");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblFilename, 38, SpringLayout.SOUTH, lblFile);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblFilename, 0, SpringLayout.WEST, lblFile);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblFilename, 61, SpringLayout.SOUTH, lblFile);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblFilename, 88, SpringLayout.WEST, lblFile);
		contentPane.add(lblFilename);
		lblFilename.setFont(myFont);
		lblFilename.setForeground(new Color(215, 255, 243));
		
		lblFilesize = new JLabel("File_Size");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblFilesize, 0, SpringLayout.NORTH, lblFilename);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblFilesize, 0, SpringLayout.WEST, btnBrowse);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblFilesize, -111, SpringLayout.EAST, contentPane);
		contentPane.add(lblFilesize);
		lblFilesize.setFont(myFont);
		lblFilesize.setForeground(new Color(215, 255, 243));
		
		btnBrowse.addActionListener(this);
		btnSend.addActionListener(this);
		
		setVisible(true);
		selectionReadyLoop();
	}
	
	public boolean selectionReadyLoop(){
		try{
			while(waitForSelection){
				lblFilename.setText(".");
				lblFilesize.setText(".");
				TimeUnit.MILLISECONDS.sleep(300);
				lblFilename.setText("..");
				lblFilesize.setText("..");
				TimeUnit.MILLISECONDS.sleep(300);
				lblFilename.setText("...");
				lblFilesize.setText("...");
				TimeUnit.MILLISECONDS.sleep(300);
			}
		}catch(InterruptedException e){}
		return true;
	}
	
	public void messageWaitLoop() {
		boolean wasMessage = false;
		try{
			while (isVisible()) {
				while (wasMessage = input.ready()) {
					String answer = input.readLine();
					
					System.out.println(answer);
					
					if (answer.contentEquals("START_SEND_FILE")) {

						FileSender fileSender = new FileSender(selectedFile, hostname, filePort);
						fileSender.sendFile();
					}
				}
				if (!wasMessage) {
					TimeUnit.MILLISECONDS.sleep(1);	/// replacer for sleep until input recv  				
				}	
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnBrowse){
			waitForSelection = false;
			int choice = fileChooser.showOpenDialog(contentPane);
			
			if(choice == JFileChooser.APPROVE_OPTION){
				selectedFile = fileChooser.getSelectedFile();
				lblFilename.setText(selectedFile.getName());
				lblFilesize.setText(String.valueOf(selectedFile.length()));
			}
		}
		if(e.getSource() == btnSend){
			output.println("SEND_FILE");
			output.println(lblFilename.getText());
		}
	}
}
