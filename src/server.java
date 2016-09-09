import java.net.*;

import javax.swing.JOptionPane;

import java.io.*;

public class server {
	public static void main (String[] args) throws IOException {

	int portNumber = 0;
	boolean listeningY = true;

	portNumber = Integer.parseInt(JOptionPane.showInputDialog ( "Enter port number that server is running on:"));
		try (
			ServerSocket a1Socket = new ServerSocket(portNumber);
			) {
				while (listeningY) {
					new serverThread(a1Socket.accept()).start();
				}
		}
		catch (IOException e) { 
			System.err.println ("Error! Exception caught (server side) while trying to listen for a connection or while trying to listen on the specified port number.");
			System.err.println("Exception Message: "+e.getMessage());
		}
	}
}
