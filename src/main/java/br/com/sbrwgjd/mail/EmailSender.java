package br.com.sbrwgjd.mail;

import br.com.sbrwgjd.config.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.slf4j.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Component
public class EmailSender implements Serializable {

    Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private final JavaMailSender mailSender;
    private String to;
    private String subject;
    private String body;
    private ArrayList<InternetAddress> recipients = new ArrayList<>();
    private File attachmanet;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public EmailSender to(String to) {
        this.to = to;
        this.recipients = getRecipients(to);
        return this;
    }

    public EmailSender withubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailSender withMessage(String body) {
        this.body = body;
        return this;
    }

    public EmailSender setRecipients(ArrayList<InternetAddress> recipients) {
        this.recipients = recipients;
        return this;
    }

    public EmailSender attach(String fileDir) {
        this.attachmanet = new File(fileDir);
        return this;
    }

    public void send(EmailConfig config) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(config.getUsername());
            helper.setTo(recipients.toArray(new InternetAddress[0]));
            helper.setSubject(subject);
            helper.setText(body, true);
            if(attachmanet != null) {
                helper.addAttachment(attachmanet.getName(), attachmanet);
            }
            mailSender.send(message);
            logger.info("Email sent to %s with the subject '%s'%n", to, subject);
            reset();
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending the email", e);
        }

    }

    private void reset() {
        this.to = null;
        this.subject = null;
        this.body = null;
        this.recipients.clear();
        this.attachmanet = null;
    }

    //email@gmail.com;email2@gmail.com;email3@gmail.com
    private ArrayList<InternetAddress> getRecipients(String to) {
        String toWithoutSpaces = to.replaceAll("\\s", "");
        StringTokenizer tok = new StringTokenizer(toWithoutSpaces, ",");
        ArrayList<InternetAddress> recipientsList = new ArrayList<>();
        while (tok.hasMoreElements()) {
            try {
                recipientsList.add(new InternetAddress(tok.nextElement().toString()));
            } catch (AddressException e) {
                throw new RuntimeException(e);
            }
        }
        return recipientsList;
    }
}
