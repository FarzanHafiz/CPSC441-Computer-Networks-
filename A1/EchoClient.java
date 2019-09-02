import java.util.Scanner;
import java.io.*;
import java.net.*;

/**
 * Use this client application with Echo server
 */
public class EchoClient {

	public static void main(String[] args) {
	

		String s;
		String response;
		Scanner inputStream = null;
		PrintWriter outputStream = null;
		Socket clientSocket = null;
		  try
		  {
			  // Connect to server on same machine, port 6789
			  clientSocket = new Socket("localhost",6789);
			  // Set up streams to send/receive data
			  inputStream = new Scanner(new
					  InputStreamReader(clientSocket.getInputStream()));
			  outputStream = new PrintWriter(new
					  DataOutputStream(clientSocket.getOutputStream()));
			  Scanner userInput = new Scanner(System.in);
			  while (true){
				  System.out.println("Enter Text for Echo Server");
				  s = userInput.nextLine();
	
				  // Send user input message to server
				  outputStream.println(s);
				  //Flush to make sure message is sent
				  outputStream.flush();
		
				  response = inputStream.nextLine();
				  System.out.println("Response From Echo Server:" +response);
			
				  // Exit if message from server is "bye"
				  if(response.equalsIgnoreCase("bye"))
					  break;
			
			  }
			  inputStream.close();
			  outputStream.close();
		  }
		  catch (Exception e)
		  {
		   // If any exception occurs, display it
		   System.out.println("Error " + e);
		  }	
		  finally 
			{
				if (clientSocket != null) 
				{
					try 
					{
						clientSocket.close();
					} 
					catch (IOException ex) 
					{
					// ignore
					}	

				}
			}
	}
}