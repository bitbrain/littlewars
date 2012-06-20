/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://littlewars.my-reality.de
 * 
 * Helper for sending mails
 * 
 * @version 	0.5.11
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.components.helpers;

import java.net.UnknownHostException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHelper {

	private static MailHelper _instance;
	private static final String from = "info@localhost";
	
	static {
		_instance = new MailHelper();
	}
	
	public static MailHelper getInstance() {
		return _instance;
	}
	
	/**
	 * Sends an email to the given address
	 * @throws MessagingException 
	 * @throws AddressException 
	 * @throws UnknownHostException 
	 */
	public void sendMail(String target, String subject, String content) throws AddressException, MessagingException, UnknownHostException {
		// Initialize sending
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", "localhost");
		Session session = Session.getDefaultInstance(properties);
		
		// Create message
		MimeMessage message = new MimeMessage(session);
		
		/* Set the RFC 822 "From" header field using the 
		 * value of the InternetAdress.getLocalAdress method
		 */
		message.addHeader("MyHeaderName", "myHeaderValue");
		message.setFrom(new InternetAddress(from));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(target));
		message.setSubject(subject);
		message.setText(content);
		
		// Send the mail
		Transport.send(message);		
	}
}
