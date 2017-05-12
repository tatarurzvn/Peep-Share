import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class FileSender {
	private File file; 
	
	private int port; 
	private String hostname;
	
	FileSender (File file, String hostname, int port) {
		this.port = port;
		this.hostname = hostname;
		
		this.file = file; 
	}
	
	public void sendFile() throws Exception{
		try {
			Socket mySocket = new Socket(hostname, port); 
		    
			FileInputStream fin = new FileInputStream(file);
		    BufferedInputStream bufferedInputStream = new BufferedInputStream(fin);
			
		    System.out.println((1 << 30));
		    if (file.length() > (1 << 30)) {
		    	mySocket.close();
			    bufferedInputStream.close();
		    	throw new Exception("Program broke, something with the file");
		    }
		    
		    byte [] data = new byte[(int)file.length()];
		    bufferedInputStream.read(data, 0,  data.length);
		    
		    OutputStream outputStream = mySocket.getOutputStream();
		   
		    int fileSize = (int)file.length();
		    
		    outputStream.write(fileSize);
		    outputStream.write(fileSize >> 8);
		    outputStream.write(fileSize >> 16);
		    outputStream.write(fileSize >> 24);
		    
		    outputStream.write(data, 0, data.length);
		    outputStream.flush();
		    
		    mySocket.close();
		    bufferedInputStream.close();
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
