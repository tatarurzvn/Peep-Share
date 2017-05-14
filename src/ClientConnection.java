import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JLabel;

/***
	Will try to connect to ServerAccept, it is meant to be a new thread as the client connection will 
	start mainLoop of the class Select_Your_Files_GUI
***/
public class ClientConnection extends Thread {
	String hostname; 
	int port; 
	int filePort; 
	
	public JLabel lblResponse; 
	
	ClientConnection(String hostname, int port, int filePort){
		this.hostname = hostname; 
		this.port = port; 
		this.filePort = filePort;
	}
	
	public void setResponseLabel(JLabel lblResponse){
		this.lblResponse = lblResponse;
	}
	
	public void setResponse(String response){
		lblResponse.setText(response);
	}
	
	public void run(){
		/// start gui here
		
		try{	
			/// this one is the preparation for the connection and the connection to hostname:port, 2 in 1 
			Socket mySocket = new Socket(hostname, port); 
			
			/// those two will write(output) to the internet and read(input) from the internet
			/// so we will give them to any class that wants to read/write to the other computer 
			PrintWriter output = 
					new PrintWriter(mySocket.getOutputStream(), true);
			
			BufferedReader input = 
					new BufferedReader( new InputStreamReader(mySocket.getInputStream()));
	    		
	    	/// we first get a string telling us if the other computer accepted our connection
	        String answer = input.readLine(); 
	        System.out.println(answer);       
	        
	        if (answer.contentEquals("CONN_ACC")) {
	        	setResponse("Connection accepted");
	        	
	        	Object lock = new Object();
	        	Select_Your_Files_GUI frame = new Select_Your_Files_GUI(output, input, hostname, filePort);
	        	
	        	/// conn accepted here
	        	System.out.println("Connection has been started");
	     
	     		/// this thread's only purpose is to not end the instance of ClientConnection while the frame is visible as it will result in 
	     		/// frame is visible as it will result in closing the connection 
	        	Thread t = new Thread() {
			        public void run() {
			            synchronized(lock) {
			                while (frame.isVisible())
			                    try {
			                        lock.wait();
			                    } catch (InterruptedException e) {
			                        e.printStackTrace();
			                    }
			                System.out.println("Working now");
			            }
			        }
			    };
			    t.start();

			    frame.addWindowListener(new WindowAdapter() {

			        @Override
			        public void windowClosing(WindowEvent arg0) {
			            synchronized (lock) {
			                frame.setVisible(false);
			                lock.notify();
			            }
			        }
			    });
			    
			    /// the main loop waits for messages from the other computer
			    frame.mainLoop();
			    
			    t.join();
	        }
	        else if (answer.contentEquals("CONN_DECL")) {
	        	setResponse("Connection denied");
	        }
	        
			mySocket.close();
			System.out.println("Done client conn!");
	    }
	    catch (IOException e) {
	    	// TODO Auto-generated catch block
	    	System.out.println(e);	
	    } 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
