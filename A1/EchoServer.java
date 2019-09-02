import java.util.Scanner;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class EchoServer {

	public static void main(String[] args) {
		  
		  String s;
		  Scanner inputStream = null;
		  PrintWriter outputStream = null;
		  ServerSocket serverSocket = null;
		  Socket socket = null;
		  try
		  {
			 // Wait for connection on port 6789
			 System.out.println("Waiting for a connection.");
			 serverSocket = new ServerSocket(6789);
			 socket = serverSocket.accept();
			 // Connection made, set up streams
			 inputStream = new Scanner(new InputStreamReader(socket.getInputStream()));
		 	 outputStream = new PrintWriter(new
			 DataOutputStream(socket.getOutputStream()));
		 	
			// Respond to messages from the client
			while(true)
			{
				s = inputStream.nextLine();
				System.out.println(s);
			
				// exit if message from client is "bye"
				if(s.equalsIgnoreCase("bye"))
				{
					outputStream.println("bye");
	                        outputStream.flush();
					break;
				}
	
				outputStream.println(s);
				outputStream.flush();
            
			} //while looop



			System.out.println("Closing connection");
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
                  if (socket != null)
                  {
                          try
                          {
                                  socket.close();
                          }
                          catch (IOException ex)
                          {
                          // ignore
                          }

                  }
                  if (serverSocket != null)
                  {
                          try
                          {
                                  serverSocket.close();
                          }
                          catch (IOException ex)
                          {
                          // ignore
                          }

                  }

          }
		 

	}

}