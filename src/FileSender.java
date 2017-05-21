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
	
	private int memoryPatchSize = 128 * 1024 * 1024;
	
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
			
		    long fileSize = file.length();
		    long dataSent = 0; 
		    
		    int dataAllocSize = 0;
		    
		    if (fileSize > (long)memoryPatchSize) {
		    	dataAllocSize = (int)memoryPatchSize;
		    }
		    else {
		    	dataAllocSize = (int)fileSize;
		    }
		    byte [] data = new byte[dataAllocSize];
		    	
		    OutputStream outputStream = mySocket.getOutputStream();
			
		    for (int i = 0; i < Long.SIZE; i++) {
		    	outputStream.write((int)(fileSize >> (i * 8)));
		    }
		    
		    while (dataSent != fileSize) {
		    	int dataRead = bufferedInputStream.read(data, 0,  data.length);
		    	
		    	dataSent += (long)dataRead;
		    	
		    	outputStream.write(data, 0, dataRead);
			    outputStream.flush();
		    } 		    
		    
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
