import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JProgressBar;

public class FileReceiver extends Thread{
	String fileName; 
	private int port;
	private JProgressBar progressBar;
	private int memoryPatchSize = 128 * 1024 * 1024;
	
	public FileReceiver (String fileName, int port, JProgressBar progressBar) {
		this.fileName = fileName;
		this.port = port;
		this.progressBar = progressBar;
	}
	
	public void run(){
		try {
			progressBar.setVisible(true);
			
			Socket mySocket;
			ServerSocket MyService = new ServerSocket(port);
			
			// accepting  
			mySocket = MyService.accept();

			MyService.close();	/// Not tested, should work like this 
								/// I dont have a java compiler to try it out 
			
			InputStream inputStream = mySocket.getInputStream();
			
			long fileSize = 0;
			
			long byteSize = 0; 
			for (int i = 0; i < Long.SIZE; i++) {
				byteSize = (int)inputStream.read();
				fileSize = fileSize | (byteSize << (i * 8));
			}
			
			int dataAllocSize = 0;
		    
		    if (fileSize > (long)memoryPatchSize) {
		    	dataAllocSize = (int)memoryPatchSize;
		    }
		    else {
		    	dataAllocSize = (int)fileSize;
		    }
		    byte [] data = new byte[dataAllocSize];
			
			FileOutputStream fos = new FileOutputStream(fileName);
		    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos);
		   
		    int bytesRead = 0;
		    long curentTotal = 0; 
		    
		    System.out.println(data.length + " data.lenght");
		    System.out.println(data.length + " data.lenght");
		    System.out.println(fileSize+ " fileSize");
		    
		    while ((bytesRead = inputStream.read(data, 0, data.length)) > 0) {
		    	curentTotal += (long)bytesRead; 
		    	
			    bufferedOutputStream.write(data, 0, bytesRead);
			    bufferedOutputStream.flush();
		    }			
		    
		    mySocket.close();
		    bufferedOutputStream.close();
		    
		    if (curentTotal != fileSize) {
		    	try {
					throw new Exception("Files do not match size");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
		    progressBar.setVisible(false);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
