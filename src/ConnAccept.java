import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/***
	The result of the ServerAccept accepting a new connection from a ClientConnection
***/
public class ConnAccept extends Thread {
	Socket connectionSocket; 
	int filePort; 
	
	ConnAccept(Socket connectionSocket, int filePort){
		this.filePort = filePort;
		this.connectionSocket = connectionSocket; 
	}
	
	public void run(){
		/// start gui here
		
		try{
			Accept_pop_up_GUI acceptGUI = new Accept_pop_up_GUI(); 
			
			while(!acceptGUI.isBtnClicked()){
				// wait like a thread
//				System.out.println("Checking...");
				TimeUnit.MILLISECONDS.sleep(1);
			}
			
			if(acceptGUI.isAccepted()){
				acceptGUI.close();
				
				/// socket was given by ServerAccept here as a result of accepting a connection from ClientConnection
				/// as oposed to ClientConnection where we had to make a new socket(see ClientConnection)

				/// those two will write(output) to the internet and read(input) from the internet
				/// so we will give them to any class that wants to read/write to the other computer 
				PrintWriter output = 
						new PrintWriter(connectionSocket.getOutputStream(), true);
				BufferedReader input = 
						new BufferedReader( new InputStreamReader(connectionSocket.getInputStream()));
				
				/// send accepted connection
				output.println("CONN_ACC");
				
				/// start here the gui for the receiver
				Object lock = new Object();
				Select_Your_Directory_GUI frame = new Select_Your_Directory_GUI(output, input, filePort);
				
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
				
				connectionSocket.close();
				System.out.println("Done acc conn! ");
			}
			else{
				PrintWriter output = 
						new PrintWriter(connectionSocket.getOutputStream(), true);
	
				/// send accepted connection
				output.println("CONN_DECL");
				
				acceptGUI.close();
				connectionSocket.close();
			}
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
