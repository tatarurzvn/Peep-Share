import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPIdentifier extends Thread{
	int port; 
	boolean alive; 
	String name; 

	UDPIdentifier(int port, String name){
		this.port = port;
		this.name = name;
	}
	
	public void run(){
		alive = true; 
		try {
			DatagramSocket listener = new DatagramSocket(port);
			listener.setBroadcast(true);
			
			byte [] buffer = new byte[1]; 
			DatagramPacket packet = new DatagramPacket(buffer, 1);
			
			while( alive ){
				listener.receive(packet);
				
				DatagramPacket returnPacket = new DatagramPacket(buffer, 1	/// we should fill buffer with
						, packet.getAddress(), packet.getPort());			/// name
				
				listener.send(returnPacket);
			}
			
			listener.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
