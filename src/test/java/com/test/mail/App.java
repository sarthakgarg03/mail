package com.test.mail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

import org.testng.annotations.Test;

/**
 * Hello world!
 *
 */
public class App {
	@Test
	public void Mail() throws MessagingException, IOException {

		Boolean MailPresent = false;

		// create properties field
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		Store store = session.getStore();
		store.connect("imap.gmail.com", "garg.sarthak03@gmail.com", "shashank13");

		// create the folder object and open it
		Folder selectedFolder = store.getFolder("INBOX");
		selectedFolder.open(Folder.READ_WRITE);

		// retrieve the messages from the folder in an array and print it
		Message[] messages = selectedFolder.getMessages();
		System.out.println("messages.length---" + messages.length);
		System.out.println("Subject: " + messages[messages.length-1].getSubject());	
		for (int i = (messages.length-1); i > 0; i--) {
			Message message = messages[i];
			System.out.println("Subject: " + message.getSubject());
			// System.out.println("From: " + message.getFrom()[0]);
			// System.out.println("Text: " + message.getContent().toString());
			// System.out.println("FolderName: " + message.getFolder());

			// saving mail html to a .html file
			if (message.getSubject().contains("Succesfully Placed") && message.isSet(Flags.Flag.SEEN) == false) {
				System.out.println("Message found");
				Object content = message.getContent();
				if (content instanceof Multipart) {
					Multipart mp = (Multipart) content;
					for (int i1 = 0; i1 < mp.getCount(); i1++) {
						BodyPart bp = mp.getBodyPart(i1);
						if (Pattern.compile(Pattern.quote("text/html"), Pattern.CASE_INSENSITIVE)
								.matcher(bp.getContentType()).find()) {
							writeUsingBufferedWriter((String) bp.getContent(),"./Outputt/mail.html");
						}
					}

				}
                   message.setFlag(Flag.SEEN, true);
			}
		}
	}
	
	private void writeUsingBufferedWriter(String content,String PAth) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(PAth);
			bw = new BufferedWriter(fw);
			bw.write(content);
			System.out.println(" Mail Saved in the output folder");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}	
    }
}
