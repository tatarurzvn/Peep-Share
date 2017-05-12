import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class AddressFinder {
	public ArrayList<InetAddress> findLocalAddress(int port, int bindPort, int waitTimeMiliseconds) throws SocketException{
		ArrayList<InetAddress> addrArray = new ArrayList<InetAddress>(); 
		DatagramSocket sender = new DatagramSocket(bindPort);
		
		try {
			sender.setBroadcast(true);
			sender.setSoTimeout(waitTimeMiliseconds / 2);
			
			byte [] buffer = new byte[1]; 
			DatagramPacket packet = new DatagramPacket(buffer, 1
					, InetAddress.getByName("255.255.255.255"), port);
			
			sender.send(packet);
			long startTime = (long)(System.nanoTime() / 1e6); 
			
			while( (System.nanoTime() / 1e6) - startTime < waitTimeMiliseconds ){
				DatagramPacket recvPacket = new DatagramPacket(buffer, 1); 
				
				sender.receive(recvPacket);
				addrArray.add(recvPacket.getAddress()); 
			}
			
			sender.close(); 
		} catch (SocketTimeoutException e) {
            System.out.println("Socket UDP timed out!");
            sender.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sender.close();
		return addrArray;
	}
}
