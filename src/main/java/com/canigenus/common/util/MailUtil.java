package com.canigenus.common.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.httpclient.methods.GetMethod;

import com.canigenus.common.constants.GenericConstant;

public class MailUtil {
	@SuppressWarnings("deprecation")
	public static void sendMail(String url, String mail, String subject, String message) {

		try {
			StringBuilder urlBuilder = new StringBuilder(url);
			urlBuilder.append("?subject=" + URLEncoder.encode(subject) + "&message=" +URLEncoder.encode(message));
			final GetMethod getMethod = new GetMethod(urlBuilder.toString());
			GenericConstant.HTTP_CLIENT.executeMethod(getMethod);
			String str = getMethod.getResponseBodyAsString();
			System.out.println(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
			} finally {
		}
	}

	public static void sendMail(String email, String subject, String message,
			String from, final String userName, final String password)
			throws MessagingException {

		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.canigenus.com");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props,
				new Authenticator() {
					@Override
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(userName, password);
					}
				});

		session.setDebug(true);

		// create a message
		Message msg = new MimeMessage(session);

		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[1];
		/*
		 * for (int i = 0; i < recipients.length; i++) {
		 */
		addressTo[0] = new InternetAddress(email);
		/* } */
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);
	}

	public static void main(String args[]) throws Exception {
		
		String str="<html><body> Thank Your for Signing For Ads Project. Please Click on the Link Below to Activate the Account<br/> <br/> <a href='abc'>Click Here to Activate</a></body></html>";
		
		MailUtil.sendMail("http://localhost:8765/contacts/rest/contact/sendMessageWithBodyHtml/rahulranjanmca@gmail.com","rahulranjanmca@gmail.com", "Subject", str);
		/*MailUtil.sendMail("donotreply@canigenus.com", "mail@canigenus.com",
				"password*9", "Subjecrt", "TExt", "smtp", "smtp.canigenus.com",
				25, null);*/
		/*
		 * MailUtil.sendMail("rahulranjanmca@gmail.com", emailSubjectTxt,
		 * emailMsgTxt, emailFromAddress);
		 */
		System.out.print("I am calling");

	}

	public static void sentMailFromGmailSSL(String to, String from,
			String password, String subject, String body) {
		Properties p = new Properties();
		p.put("mail.smtp.starttls.enable", "true");
		sendMail(to, from, password, subject, body, "smtps", "smtp.gmail.com",
				465, p);

	}

	public static void sentMailFromGmailTLS(String to, String from,
			String password, String subject, String body) {
		sendMail(to, from, password, subject, body, "smtps", "smtp.gmail.com",
				465, null);
	}

	public static void sendMail(String to, String from, String password,
			String subject, String body, String protocol, String host,
			int port, Properties properties) {
		try {
			Properties propsSSL = new Properties();
			propsSSL.put("mail.transport.protocol", protocol);
			propsSSL.put("mail.smtps.host", host);
			propsSSL.put("mail.smtps.auth", "true");
			if (properties != null)
				propsSSL.putAll(properties);

			Session sessionSSL = Session.getInstance(propsSSL);

			sessionSSL.setDebug(true);

			Message messageTLS = new MimeMessage(sessionSSL);
			messageTLS.setFrom(new InternetAddress(from));

			messageTLS.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to)); // real
												// recipient

			messageTLS.setSubject(subject);
			messageTLS.setContent(body, "text/html; charset=utf-8");

			Transport transportTLS = sessionSSL.getTransport();

			transportTLS.connect(host, port, from, password); // account
																// used

			transportTLS.sendMessage(messageTLS, messageTLS.getAllRecipients());

			transportTLS.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
