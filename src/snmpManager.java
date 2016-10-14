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


public class snmpManager {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		////////////////////////////////////////////////////////////////////////////////////////
		
		long temps = 300000;                     // time before repeating the task : 2000 = 2 secondes
		long startTime = 0;                    // time before starting the task (0 : immediate start)
		Timer timer = new Timer();             // timer creation
		TimerTask tache = new TimerTask() {    // timer task creation and specification of what will be done
			@Override

			public void run() {
				
				////////////////////////////////////////////
				// TEST MAIL
				////////////////////////////////////////////
				
				String line; 
				String value;
				int valueInteger1 = 0;
				int valueInteger2 = 0;
				String ipAgent1;
				String ipAgent2;
				
				ipAgent1 = "127.0.0.1";
				ipAgent2 = "138.68.88.225";
				
				// Command used to get number of received IPv4 datagrams
				String commandline = "snmpget -v 2c -c ttm4128 " + ipAgent1 + " ipInReceives.0";
				// Have to be done for both agents --> 2 addresses
				
				String[] cmd = {"bash","-c",commandline};
				
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
				
				String text = "Value for agent1 : " + valueInteger1 + " \nValue for agent2 : " + valueInteger2;
				
				//sendEmail("SMTP", "Hello Marija !", "marijag@stud.ntnu.no");
				sendEmail("SMTP", text, "marijag@stud.ntnu.no");
				
			}
		};
		timer.scheduleAtFixedRate(tache,startTime,temps);  // beginning of the mechanism

	}

	// Method used to send the email
	public static void sendEmail(String subject, String text, String address) {

		final String fromEmail = "pjchovel@ntnu.no"; //requires valid emil id
		final String password = "11Juin1994"; // correct password for email id
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

