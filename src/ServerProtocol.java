//imports library requried
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.util.*;
import java.text.*;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.bouncycastle.util.encoders.Hex;

@SuppressWarnings("unused")
public class ServerProtocol {

	//declares and intiilizes variables needed
	public String output = "";
	public String key = "J1O6MGJ6Nd";
	public String messageHash = "";
	public String actualHash = "";
	public String msg = "";

	//Returns a string based on what the input string was
	public String processMsg(String message) {

		
		//if input message was "client closing" return "clientClosed"
		if (message.equals("client closing")) {
			output = "clientClosed";
		}
		//if input message was "wrongMsg" return "invalidRequest"
		else if (message.equals("wrongMsg")) {
			output = "invalidRequest";
		}
		//default returns null 
		else {
			output = message;
			String temp = output.substring(0, output.indexOf("thisiswhereitstops"));
			msg = temp;
			messageHash = output.substring(output.indexOf("thisiswhereitstops")+18, output.length());
			DigestSHA3 md = new DigestSHA3(256); //same as DigestSHA3 md = new SHA3.Digest256();
			md.update(temp.getBytes());
			String SHA3hash = hashToString(md);
			actualHash = SHA3hash;
			String concatOut = temp + "thisiswhereitstops"+ SHA3hash;
			output = concatOut;
		}
		//sends output back to server
		return output;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public String getMsgHash() {
		return messageHash;
	}
	
	public String getActualHash() {
		return actualHash;
	}

	//returns hardcoded key
	public String getKey () {
		return key;
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
}