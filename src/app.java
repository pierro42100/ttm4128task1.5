import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;


public class app {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		////////////////////////////////////////////////////////////////////////////////////////
		
		long temps = 300000;                     // time before repeating the task : 2000 = 2 secondes
		long startTime = 0;                    // time before starting the task (0 : immediate start)
		Timer timer = new Timer();             // timer creation
		TimerTask tache = new TimerTask() {    // timer task creation and specification of what will be done
			@Override

			public void run() {

				String line; 
				String value;
				int valueInteger1 = 0; // Value for the first agent
				int valueInteger2 = 0; // Value for the second agent
				String ipAgent1; // ip address
				String ipAgent2; // ip address 
				
				ipAgent1 = "127.0.0.1"; // ip address
				ipAgent2 = "138.68.88.225"; // ip address
				
				// Command used to get number of received IPv4 datagrams for the first agent
				String commandline = "snmpget -v 2c -c ttm4128 " + ipAgent1 + " ipInReceives.0";
				
				String[] cmd = {"bash","-c",commandline};
				
				// Execute the command for getting the first agent value
				// Agent 1
				try { 
					Process p = Runtime.getRuntime().exec(cmd); //Execute the cmd
					BufferedReader input =
							new BufferedReader(new InputStreamReader(p.getInputStream())); 
					while ((line = input.readLine()) != null) { // Read data

						//System.out.println(line);
						value = line.substring(line.lastIndexOf(" ")+1); // Get the value
						valueInteger1 = Integer.valueOf(value); // convert string into integer
						//System.out.println(valueInteger); // print the integer value

					}
					input.close();
				} catch (Exception e) {

				}
				
				// Command used to get number of received IPv4 datagrams
				commandline = "snmpget -v 2c -c ttm4128 " + ipAgent2 + " ipInReceives.0";
				cmd[2] = commandline;
				
				// Execute the command for getting the second agent value
				// Agent 2
				try { 
					Process p = Runtime.getRuntime().exec(cmd); //Execute the cmd
					BufferedReader input =
							new BufferedReader(new InputStreamReader(p.getInputStream())); 
					while ((line = input.readLine()) != null) { // Read data

						//System.out.println(line);
						value = line.substring(line.lastIndexOf(" ")+1); // Get the value
						valueInteger2 = Integer.valueOf(value); // convert string into integer
						//System.out.println(valueInteger); // print the integer value

					}
					input.close();
				} catch (Exception e) {

				}
				
				// Message that will be sent
				
				String text = "##############################\n";
				text += "Agent 1 :\n";
				text += "IP Address : " + ipAgent1 + "\n";
				text += "Number of received IPv4 datagrams : " + valueInteger1 + "\n";
				
				text += "##############################\n";
				
				text += "Agent 2 :\n";
				text += "IP Address : " + ipAgent2 + "\n";
				text += "Number of received IPv4 datagrams : " + valueInteger2 + "\n";
				text += "##############################\n";
				
				//sendEmail("SMTP", "Hello Marija !", "marijag@stud.ntnu.no");
				//sendEmail("SMTP", text, "marijag@stud.ntnu.no");
				sendEmail("SMTP", text, "pjchovel@stud.ntnu.no");
				
			}
		};
		timer.scheduleAtFixedRate(tache,startTime,temps);  // beginning of the mechanism

	}

	// Method used to send the email
	public static void sendEmail(String subject, String text, String address) {

		final String fromEmail = "pjchovel@ntnu.no"; //requires valid emil id
		
		// DELETE password BEFORE SENDING IT :)
		final String password = ""; // correct password for email id
		final String toEmail = address; // can be any email id 
		
		System.out.println("TLSEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.office365.com"); //SMTP Host
		props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
                //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		
		EmailUtil.sendEmail(session, toEmail, subject, text);
		
	}
				
}

