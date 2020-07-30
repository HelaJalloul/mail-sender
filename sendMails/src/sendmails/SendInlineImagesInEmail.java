    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendmails;

import java.io.IOException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendInlineImagesInEmail {

    public void sendMail(String destination) throws AddressException, MessagingException, IOException {

        final String from = Constants.USERNAME;
        String username = Constants.USERNAME;
        String password = Constants.PÄSSWORD;
        String host = "smtp.ionos.fr";
        final Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        Session session = null;

        final PasswordAuthentication pwdAuth = new PasswordAuthentication(username, password);
        session = Session.getInstance(props, (Authenticator) new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return pwdAuth;
            }
        });

        final Message message = (Message) new MimeMessage(session);
        message.setFrom((Address) new InternetAddress(from, "Service développement"));
        message.setRecipients(Message.RecipientType.TO, (Address[]) InternetAddress.parse(destination));
        message.setSubject("Equipements gratuits grâce à la loi sur la TRANSITION ÉNERGÉTIQUE et au dispositif des Certificats d'Economies d'Energie");
        BodyPart messageBodyPart = (BodyPart) new MimeBodyPart();
        messageBodyPart.setContent(Constants.MAIL_TEXT, "text/html; charset=UTF-8");
        final Multipart multipart = (Multipart) new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        //MimeBodyPart imagePart = new MimeBodyPart();
        //imagePart.setHeader("Content-ID", "<" + "image1" + ">");
        //imagePart.setDisposition(MimeBodyPart.INLINE);

        //String imageFilePath = Constants.LOGO;
        //try {
        //    imagePart.attachFile(imageFilePath);
        //} catch (IOException ex) {
       //     ex.printStackTrace();
        //}

        messageBodyPart = (BodyPart) new MimeBodyPart();
        DataSource source = new FileDataSource(Constants.DIR_PATH + Constants.RENS);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(Constants.RENS);
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = (BodyPart) new MimeBodyPart();
        source = new FileDataSource(Constants.DIR_PATH + Constants.RENS2);
        messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName(Constants.RENS2);
         multipart.addBodyPart(messageBodyPart);
        messageBodyPart = (BodyPart) new MimeBodyPart();
        source = new FileDataSource(Constants.DIR_PATH + Constants.ECO);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(Constants.ECO);
        multipart.addBodyPart(messageBodyPart);
        //multipart.addBodyPart(imagePart);

        message.setContent(multipart);
        Transport.send(message);

    }

}
