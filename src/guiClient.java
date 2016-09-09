import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.bouncycastle.util.encoders.Hex;

@SuppressWarnings("unused")
public class guiClient implements ActionListener {

	JFrame frame;
	TextField textField;
	JButton sendMsg, wrongMsg;
	JPanel mainPanel, buttonPanel;
	String serverMsg, hashMSG;
	ButtonGroup group;

	public static String hostName;
	public static int portNumber;

	public Socket a1Socket;
	public PrintWriter out;
	public BufferedReader in;
	
	String key = "J1O6MGJ6Nd";
	Boolean handShake = false;

	public static void main (String[] args) {
				
		hostName = "localhost";
		portNumber = Integer.parseInt(JOptionPane.showInputDialog ( "Enter port number that server is running on:")); 

		if (portNumber < 1025) {
			System.err.println ("Invalid Argument: Please enter a port number greater than 1025, less then 49151");
			System.exit(1);
		}
		else if (portNumber > 49151) {
			System.err.println ("Invalid Argument: Please enter a port number greater than 1025, less then 49151");
			System.exit(1);
		}
		new guiClient();
	}

	public guiClient() {

		frame = new JFrame();
		frame.setLayout(new BorderLayout());

		mainPanel = new JPanel(); 
		textField = new TextField(25);

		buttonPanel = new JPanel(new GridLayout(2,1));

		sendMsg = new JButton("Send message with hash to server to verify");
		sendMsg.setActionCommand("sendMsg");
		sendMsg.addActionListener(this);
		
		wrongMsg = new JButton("Send message with wrong/faked hash to verify");
		wrongMsg.setActionCommand("wrongMsg");
		wrongMsg.addActionListener(this);

		group = new ButtonGroup();
		group.add(sendMsg);
		group.add(wrongMsg);

		mainPanel.add(textField);
		
		buttonPanel.add(sendMsg);
		buttonPanel.add(wrongMsg);
		
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.setSize (450, 300);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt){
				if (handShake) {
					closeConnection();
				}
				System.exit(0);
			}
		});

		frame.setVisible(true);
		startConnection();
	}

	public void startConnection() {
		try  {
				a1Socket = new Socket(hostName, portNumber);
				out = new PrintWriter(a1Socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(a1Socket.getInputStream()));
				while ((serverMsg = in.readLine()) != null) {
					if (!(serverMsg.equals(null)))
					{
						if (serverMsg.equals(key)) {
							handShake = true;
						}
						else {
							if (handShake) {
								if (serverMsg.equals("invalidRequest")) {
									JOptionPane.showMessageDialog (null, "Invalid request sent");
								}
								else {
									String temp = serverMsg;
									temp = temp.substring(temp.indexOf("thisiswhereitstops")+18,temp.length());
									if (temp.equals(hashMSG)){
										JOptionPane.showMessageDialog(frame, "Succes! Message authenicity verified.");
									}
									else{
										JOptionPane.showMessageDialog(frame, "Failure! Message authenicity not verified.");
									}
								}
							}
						}
					}
				}
		}
		catch (UnknownHostException e) {
			System.err.println("Could not resolve host: "+hostName);
			System.exit(1);
		}
		catch (IOException e) {
			System.err.println("Coudln't get I/O connection to "+hostName);
			System.exit(1);

		}
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand ().equals ("sendMsg")) {
			String sendMsgTxt = textField.getText();
			DigestSHA3 md = new DigestSHA3(256); //same as DigestSHA3 md = new SHA3.Digest256();
			md.update(sendMsgTxt.getBytes());
			String SHA3hash = hashToString(md);
			hashMSG = SHA3hash;
			String concatTxt = sendMsgTxt + "thisiswhereitstops" + SHA3hash;
			out.println(concatTxt);
		}
		if (e.getActionCommand ().equals ("wrongMsg")) {
			String sendMsgTxt = textField.getText();
			String choice = JOptionPane.showInputDialog(frame, "type in \"fakehash\" to alter the hash, or type in \"fakemsg\" to alter the original message to demonstrate the HMAC function. Do not type it in with quotes!: ");
			if (choice.equals("fakehash")){
				String fakeHash = JOptionPane.showInputDialog(frame, "Please enter a nonsensical/fake hash value: ");
				hashMSG = fakeHash;
				String concatTxt = sendMsgTxt + "thisiswhereitstops" + fakeHash;
				out.println(concatTxt);
			}
			else if (choice.equals("fakemsg")){
				String fakeMsg = JOptionPane.showInputDialog(frame, "Please enter a nonsensical/fake message to send: ");
				DigestSHA3 md = new DigestSHA3(256); //same as DigestSHA3 md = new SHA3.Digest256();
				md.update(sendMsgTxt.getBytes());
				String SHA3hash = hashToString(md);
				hashMSG = SHA3hash;
				String concatTxt = fakeMsg + "thisiswhereitstops" + hashMSG;
				out.println(concatTxt);
			}
			
		}
	}
	 public static String hashToString(MessageDigest hash) {
	        return hashToString(hash.digest());
	    }

	    public static String hashToString(byte[] hash) {
	        StringBuffer buff = new StringBuffer();

	        for (byte b : hash) {
	            buff.append(String.format("%02x", b & 0xFF));
	        }

	        return buff.toString();
	    }
	public void closeConnection() {
		out.println("client closing");
	}
}
