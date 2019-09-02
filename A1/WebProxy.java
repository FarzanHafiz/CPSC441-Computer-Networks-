/**
 * WebProxy Class
 * 
 * @author      Shah Farzan Al-Hafiz (10162590)
 * @version     1.1, 2 Feb 2017
 *
 */

import java.util.*;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class WebProxy {
	
	private int port;
	HashMap<String, String> hMap = new HashMap<String, String>();
	File f = null;
	String path = "";
     /**
     *  Constructor that initalizes the server listenig port
         *
     * @param port      Proxy server listening port
     */

	public WebProxy(int port) {

	/* Intialize server listening port */
	this.port = port;
	}

     /**
     * The webproxy logic goes here 
     */
	public void start(){
		
		  
		  Scanner inputStream = null;
		  DataOutputStream outputStream = null;
		  ServerSocket serverSocket = null;
		  try
		  {
			 // Wait for connection on port
			 System.out.println("Waiting for a connection.");
			 serverSocket = new ServerSocket(port);
			 Socket socket = serverSocket.accept();
			 
			 // Connection made, set up streams
			 inputStream = new Scanner(new InputStreamReader(socket.getInputStream()));
		 	 outputStream = new DataOutputStream(socket.getOutputStream());
		 	 
		 	 // Read a line from the client
		 	 String s = inputStream.nextLine();		// Reading the first line which is the URL
		 	 System.out.println(s);
		 	 String [] request = s.split(" ", 2);	// Split the line
		 	 hMap.put(request[0], request[1]);
		 	 
		 	 s = inputStream.nextLine();
			 while (!(s.isEmpty())){
				String [] request1 = s.split(": ", 2);
				hMap.put(request1[0], request1[1]);
				s = inputStream.nextLine();
				System.out.println(s);
			 }
			 
			 // error checking
			 if (!(hMap.containsKey("GET"))){
				 System.out.println("400 Bad Request.");
				 outputStream.write("400 Bad Request.".getBytes());
				 outputStream.close();
				 
			 }
			 
			 // checking if its in cache 
			 String currentPath = System.getProperty("user.dir");
			 String [] headerParse = hMap.get("GET").split("http:/");
			 String [] path1 = headerParse[1].split(" ");
			 path = currentPath + path1[0];
			 
			 
			f = new File(path);  
			
			// if it is, fetch it
			if(f.exists() && !f.isDirectory()) { 
				System.out.println("Fetching file from cache...");
				String clientResponse = "HTTP/1.1 200 OK\r\n Connection: close\r\n\r\n";
			 	
				int fileLength = (int)f.length();
				byte [] data = new byte[fileLength];
				
				FileInputStream readFile = new FileInputStream(f);
				readFile.read(data);
				
				outputStream.write(clientResponse.getBytes());
				outputStream.write(data);
			 	
 
			
			} 
			 
			// if not in cache, get it from origin server
			else {
				System.out.println("Fetching file from origin server...");		
				Socket originSocket = new Socket(hMap.get("Host"), 80);
				DataOutputStream serverOutput = new DataOutputStream(originSocket.getOutputStream());
				
				String requestServer = "GET " + hMap.get("GET") + "\r\n" + "Host: " + hMap.get("Host") + "\r\n\r\n";
				System.out.println(requestServer);
				serverOutput.write(requestServer.getBytes());
				
				// header response from origin server
				String serverResponse = "";
			
				while (!serverResponse.contains("\r\n\r\n")){
					serverResponse += (char)originSocket.getInputStream().read();
				}
				System.out.println(serverResponse);
				
				// getting the length of data
				int fileLength = Integer.parseInt(serverResponse.split("Content-Length: ")[1].split("\r\n")[0]);
				byte [] data = new byte[fileLength];
				
				originSocket.getInputStream().read(data);
				
				// sending response to client
				outputStream.write(serverResponse.getBytes());
				// sending data
				outputStream.write(data);
				outputStream.flush();
				
			
				// writing the file in path
				FileOutputStream writeFile = null;
				
				try{
					f.getParentFile().mkdirs();
					writeFile = new FileOutputStream(f);
					System.out.println(path);
					writeFile.write(data);
					
				}
				
				finally{
					writeFile.close();
				}
				
				
			

				
			 }

		}
			
		  catch (Exception e)
		  {
		     // If any exception occurs, display it
		     System.out.println("Error " + e);
		  }  
		  
	}  

	



/**
 * A simple test driver
*/
	public static void main(String[] args) {

                String server = "localhost"; // webproxy and client runs in the same machine
                int server_port = 0;
		try {
                // check for command line arguments
                	if (args.length == 1) {
                        	server_port = Integer.parseInt(args[0]);
                	}
                	else {
                        	System.out.println("wrong number of arguments, try again.");
                        	System.out.println("usage: java WebProxy port");
                        	System.exit(0);
                	}


                	WebProxy proxy = new WebProxy(server_port);

                	System.out.printf("Proxy server started...\n");
                	proxy.start();
        	} catch (Exception e)
		{
			System.out.println("Exception in main: " + e.getMessage());
                        e.printStackTrace();
	
		}
		
	
	}  
}
