import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;

public class Select_Your_Directory_GUI extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnAccept;
	private JButton btnBrowse;
	private JLabel lblPathname;
	
	private JFileChooser fileChooser;
	private File myFile;
	private JLabel lblPendingFile;
	private JLabel lblFilename;
	private JProgressBar progressBar;
	
	private PrintWriter output; 
	private BufferedReader input;
	
	private int filePort; 
	
	private static boolean dirReady = false;
	private static boolean fileReady = false;
	
	private FileReceiver fileRecv; 

	/**
	 * Create the frame.
	 */
	public Select_Your_Directory_GUI(PrintWriter output, BufferedReader input, int filePort) {
		this.filePort = filePort; 
		this.output = output;
		this.input = input; 
		
		setResizable(false);
		setTitle("Choose Your Directory");
		
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		contentPane.setBackground(new Color(47, 65, 114));
		
		Font myFont = new Font("Calibri", Font.PLAIN, 20);
		Font myFontForHeader = new Font("Calibri", Font.PLAIN, 24);
		
		btnAccept = new JButton("Accept");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnAccept, -10, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnAccept, -10, SpringLayout.EAST, contentPane);
		contentPane.add(btnAccept);
		btnAccept.setFont(myFont);
		btnAccept.setBackground(new Color(121, 134, 172));
		btnAccept.setForeground(new Color(215, 255, 243));
		btnAccept.setEnabled(false);
		
		btnBrowse = new JButton("Browse");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnBrowse, 0, SpringLayout.NORTH, btnAccept);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnBrowse, -6, SpringLayout.WEST, btnAccept);
		contentPane.add(btnBrowse);
		btnBrowse.setFont(myFont);
		btnBrowse.setBackground(new Color(121, 134, 172));
		btnBrowse.setForeground(new Color(215, 255, 243));
		
		JLabel lblDirectory = new JLabel("Directory");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDirectory, 15, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDirectory, 15, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblDirectory, 38, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblDirectory, 169, SpringLayout.WEST, contentPane);
		contentPane.add(lblDirectory);
		lblDirectory.setFont(myFontForHeader);
		lblDirectory.setForeground(new Color(233, 209, 172));
		
		lblPathname = new JLabel("Path_Name");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblPathname, 27, SpringLayout.SOUTH, lblDirectory);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblPathname, 0, SpringLayout.WEST, lblDirectory);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblPathname, -164, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblPathname, 34, SpringLayout.EAST, lblDirectory);
		contentPane.add(lblPathname);
		lblPathname.setFont(myFont);
		lblPathname.setForeground(new Color(215, 255, 243));
		
		lblPendingFile = new JLabel("Pending File");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblPendingFile, 0, SpringLayout.NORTH, lblDirectory);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblPendingFile, 0, SpringLayout.WEST, btnBrowse);
		contentPane.add(lblPendingFile);
		lblPendingFile.setFont(myFontForHeader);
		lblPendingFile.setForeground(new Color(233, 209, 172));
		
		lblFilename = new JLabel("File_Name");
		sl_contentPane.putConstraint(SpringLayout.EAST, lblFilename, 0, SpringLayout.EAST, btnAccept);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblFilename, 26, SpringLayout.SOUTH, lblPendingFile);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblFilename, 0, SpringLayout.WEST, btnBrowse);
		contentPane.add(lblFilename);
		lblFilename.setFont(myFont);
		lblFilename.setForeground(new Color(215, 255, 243));
		
		progressBar = new JProgressBar();
		sl_contentPane.putConstraint(SpringLayout.NORTH, progressBar, -45, SpringLayout.NORTH, btnBrowse);
		sl_contentPane.putConstraint(SpringLayout.WEST, progressBar, -287, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, progressBar, -31, SpringLayout.NORTH, btnBrowse);
		sl_contentPane.putConstraint(SpringLayout.EAST, progressBar, -141, SpringLayout.EAST, contentPane);
		contentPane.add(progressBar);
		progressBar.setVisible(false);
		progressBar.setIndeterminate(true);
		
		btnBrowse.addActionListener(this);
		btnAccept.addActionListener(this);
		
		setVisible(true);
	}
	
	public void mainLoop() {
		int state = 0; 
		long lastTimeSelectionReady = (long)(System.nanoTime() / 1e6); 
		while (isVisible()) {
			selectionReadyLoopBody(state);
			
			if (getTimeMilliseconds() - lastTimeSelectionReady > 300) {
				lastTimeSelectionReady = getTimeMilliseconds();
				state = (state + 1) % 3;
			}
			
			if (!messageWaitLoopBody()) {	/// only if there is no message should we wait 1ms 
				try {
					TimeUnit.MILLISECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean waitForSelection = true;
	private boolean waitForFileRecv = true; 

	public boolean selectionReadyLoopBody (int state) {
		if (waitForSelection || waitForFileRecv) {
			
			if(state == 0) {
				if(waitForSelection) 
					lblPathname.setText(".");
				
				if(waitForFileRecv) 
					lblFilename.setText(".");
			}
			else if (state == 1) {
				if(waitForSelection) 
					lblPathname.setText("..");
				
				if(waitForFileRecv) 
					lblFilename.setText("..");
			}
			else if (state == 2) {
				if(waitForSelection) 
					lblPathname.setText("...");
				
				if(waitForFileRecv) 
					lblFilename.setText("...");
			}
		}
		return true;
	}
	
	public long getTimeMilliseconds(){
		return (long)(System.nanoTime() / 1e6); 
	}
	
	public boolean messageWaitLoopBody() {
		boolean wasMessage = false;
		try{
			if (wasMessage = input.ready()) {
				String answer = input.readLine();
				
				System.out.println(answer);
				
				/// here we recv the file name
				if (answer.contentEquals("SEND_FILE")){
					waitForFileRecv = false;
					
					String fileName = input.readLine();
					
					fileReady = true;
					if (checkEverythingReady()) 
						btnAccept.setEnabled(true);
					
					lblFilename.setText(fileName);
				}
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		}
		return wasMessage; 
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnBrowse){
			int choice = fileChooser.showOpenDialog(contentPane);
			
			if(choice == JFileChooser.APPROVE_OPTION){
				waitForSelection = false;
				
				myFile = fileChooser.getSelectedFile();
				lblPathname.setText(myFile.getPath());
				
				dirReady = true;
				if (checkEverythingReady()) 
					btnAccept.setEnabled(true);
			}
		}
		if(e.getSource() == btnAccept){
			output.println("START_SEND_FILE");	/// we send the accept to send us this file
			
			fileRecv = new FileReceiver(lblPathname.getText() + "\\" + lblFilename.getText()
					, filePort, progressBar);
			
			try {
				fileRecv.start();
			} 
			catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public static boolean checkEverythingReady(){
		return (fileReady && dirReady);
	}	
}