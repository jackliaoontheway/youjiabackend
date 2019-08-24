package com.polarj.common.service.impl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Setter;

@Service
@ConfigurationProperties(prefix = "polarj.email")
public final class EmailServiceImpl
{
    private @Setter String server;

    private @Setter String port;

    private @Setter String userName;

    private @Setter String password;

    private @Setter String from;

    private @Setter Boolean ttls;

    private @Setter Boolean ssl;

    private final Logger logger;

    public EmailServiceImpl()
    {
        logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    }

    public void sendEmail(String to, String bcc, String subject, String content) throws Exception
    {
        sendEmail(new String[] { to }, new String[] { bcc }, subject, content);
    }

    public void ajaxSendEmail(String to, String bcc, String subject, String content) throws Exception
    {
        ajaxSendEmail(new String[] { to }, new String[] { bcc }, subject, content);
    }

    public void sendEmail(String to, String subject, String content) throws Exception
    {
        sendEmail(new String[] { to }, subject, content);
    }

    public void ajaxSendEmail(String to, String subject, String content) throws Exception
    {
        ajaxSendEmail(new String[] { to }, subject, content);
    }

    public void ajaxSendEmail(final String[] to, final String[] bcc, final String subject, final String content)
            throws Exception
    {
        Thread sender = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    sendEmail(to, bcc, subject, content);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        });

        sender.start();
    }

    public void sendEmail(String[] to, String[] bcc, String subject, String content) throws Exception
    {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", server);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        if (ttls != null && ttls)
        {
            props.put("mail.smtp.starttls.enable", "true");
        }
        if (ssl != null && ssl)
        {
            props.put("mail.smtp.ssl.enable", "true");
        }
        Session s = Session.getInstance(props, new Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(userName, password);
            }
        });
        MimeMessage mes = new MimeMessage(s);
        mes.setFrom(new InternetAddress(from));
        for (String str : to)
        {
            if (str != null)
                mes.addRecipient(Message.RecipientType.TO, new InternetAddress(str.trim()));
        }
        if (bcc != null && bcc.length > 0)
        {
            for (String str : bcc)
            {
                if (str != null)
                    mes.addRecipient(Message.RecipientType.BCC, new InternetAddress(str.trim()));
            }
        }
        mes.setSubject(subject, "utf-8");
        mes.setContent(content, "text/html; charset=utf-8");
        Transport.send(mes);
    }

    public void sendEmail(String[] to, String subject, String content) throws Exception
    {
        sendEmail(to, null, subject, content);
    }

    public void ajaxSendEmail(String[] to, String subject, String content) throws Exception
    {
        ajaxSendEmail(to, null, subject, content);
    }

}
