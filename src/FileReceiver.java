import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver extends Thread{
	String fileName; 
	private int port;
	
	public FileReceiver (String fileName, int port) {
		this.fileName = fileName;
		this.port = port;
	}
	
	public void run(){
		try {
			Socket mySocket;
			ServerSocket MyService = new ServerSocket(port);
			
			// accepting  
			mySocket = MyService.accept();

			MyService.close();	/// Not tested, should work like this 
								/// I dont have a java compiler to try it out 
			
			InputStream inputStream = mySocket.getInputStream();
			
			int fileSize = 0;
			
			int byteSize = inputStream.read();
			fileSize = fileSize | byteSize;
			
			byteSize = inputStream.read();
			fileSize = fileSize | (byteSize << 8);
			
			byteSize = inputStream.read();
			fileSize = fileSize | (byteSize << 16);
			
			byteSize = inputStream.read();
			fileSize = fileSize | (byteSize << 24);
			
			byte [] data = new byte [fileSize];
			
			FileOutputStream fos = new FileOutputStream(fileName);
		    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos);
		   
		    int bytesRead = 0;
		    int curentTotal = 0; 
		    
		    while ((bytesRead = inputStream.read(data, curentTotal, fileSize - curentTotal)) > 0) {
		    	curentTotal += bytesRead; 
		    }			
		    
		    mySocket.close();
		    
		    if (curentTotal != fileSize) {
		    	bufferedOutputStream.close();
		    	MyService.close();
		    	try {
					throw new Exception("Files do not match size");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
		    bufferedOutputStream.write(data, 0, curentTotal);
		    bufferedOutputStream.flush();
		    
		    bufferedOutputStream.close();
		    /// MyService.close(); this was here
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
