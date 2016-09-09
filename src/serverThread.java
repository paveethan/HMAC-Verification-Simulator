import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.bouncycastle.util.encoders.Hex;

@SuppressWarnings("unused")
public class serverThread extends Thread {
	JFrame frame;
	JLabel t1,t2, t3;
	JTextField t1tf, t2tf, t3tf;
	JPanel mainPanel = new JPanel (new GridLayout(3,2));
	Socket a1clientSocket;
	static boolean closed = false;

	public serverThread(Socket socket) {
		super("serverThread");
		a1clientSocket = socket;
	}

	public void run () {
		frame = new JFrame();
		t1 = new JLabel ("Message received: ");
		t1tf = new JTextField(50);
		t2 = new JLabel ("Hash from message: ");
		t2tf = new JTextField(50);
		t3 = new JLabel ("Calculated hash: ");
		t3tf = new JTextField(50);
		
		mainPanel.add(t1);
		mainPanel.add(t1tf);
		mainPanel.add(t2);
		mainPanel.add(t2tf);
		mainPanel.add(t3);
		mainPanel.add(t3tf);
		
		PrintWriter out;
		BufferedReader in;
		String key, inLine, outLine;
		ServerProtocol a1Protocol;
		
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.setSize (550, 450);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt){
				System.exit(0);
			}
		});

		frame.setVisible(true);

		try {
			out = new PrintWriter (a1clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(a1clientSocket.getInputStream()));

			a1Protocol = new ServerProtocol();
			key = a1Protocol.getKey();
			out.println(key);

			while ((inLine = in.readLine()) != null) {
				outLine = a1Protocol.processMsg(inLine);
				if (outLine.equals("clientClosed")) {
					closed = true;
				}
 				else {
 					t1tf.setText(a1Protocol.getMsg());
 					t2tf.setText(a1Protocol.getMsgHash());
 					t3tf.setText(a1Protocol.getActualHash());
					out.println(outLine);
				}
			}
			

		} 
		catch (IOException e) {
			if (closed) {
			}
			else {
				System.err.println ("Error! Exception caught while trying to listen for a connection or while trying to listen on the specified port number.");
				System.err.println("Exception Message: "+e.getMessage());
			}
		}

	}
}