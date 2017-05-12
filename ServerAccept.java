import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerAccept extends Thread {
	private int port; 
	private int filePort;
	private boolean alive; 
	
	ArrayList<ConnAccept> connAcceptList; 
	
	ServerAccept(int port, int filePort) {
		this.port = port;
		this.filePort = filePort;
		
		connAcceptList = new ArrayList<ConnAccept>();
	}
	
	public void run() {
		alive = true; 
		
		try{
			Socket mySocket;
			ServerSocket MyService = new ServerSocket(this.port);
			
			while( alive ){
				System.out.println("Accepting Socket");
				
				mySocket = MyService.accept();
				
				ConnAccept connThread = new ConnAccept(mySocket, filePort); 
				connThread.start(); 
				
				connAcceptList.add(connThread);
			}
			
			MyService.close();
		}
		catch( IOException exception ){
			System.out.println(exception);	
		}
	}
}
