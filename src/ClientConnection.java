import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JLabel;

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
			Socket mySocket = new Socket(hostname, port); 
			
			PrintWriter output = 
					new PrintWriter(mySocket.getOutputStream(), true);
			
			BufferedReader input = 
					new BufferedReader( new InputStreamReader(mySocket.getInputStream()));
	    		
			/// get accepted connection 
			/// if acc -> 
			
	        String answer = input.readLine(); 
	        System.out.println(answer);       
	        
	        if (answer.contentEquals("CONN_ACC")) {
	        	setResponse("Connection accepted");
	        	
	        	Object lock = new Object();
	        	Select_Your_Files_GUI frame = new Select_Your_Files_GUI(output, input, hostname, filePort);
	        	
	        	/// conn acc here
	        	System.out.println("Connection has been started");
	     
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
			    
			    frame.messageWaitLoop();
			    
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
