package com.netease.jv.cmpp.mail;

import com.netease.jv.cmpp.consts.CharsetConsts;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

public class GsMailService {

    public GsMailService(JavaMailSenderImpl sender) {
        this.sender = sender;
    }

    private JavaMailSenderImpl sender;

    public void sendText(String to, String subject, String text) throws MessagingException {
        sendText(new String[] { to }, ArrayUtils.EMPTY_STRING_ARRAY, subject, text);
    }

    public void sendHtml(String to, String subject, String html) throws MessagingException {
        sendHtml(new String[] { to }, ArrayUtils.EMPTY_STRING_ARRAY, subject, html);
    }

    public void sendText(String[] to, String[] cc, String subject, String text) throws MessagingException {
        send(to, cc, subject, text, false);
    }

    public void sendHtml(String[] to, String[] cc, String subject, String html) throws MessagingException {
        send(to, cc, subject, html, true);
    }

    public void send(String[] to, String[] cc, String subject, String context, boolean html)
            throws MessagingException {
        MimeMessage msg = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, CharsetConsts.UTF_8_NAME);
        helper.setFrom(Objects.requireNonNull(sender.getUsername()));
        if (to != null && to.length > 0) {
            helper.setTo(to);
        }
        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }
        helper.setSubject(subject);
        helper.setText(context, html);
        sender.send(msg);
    }
}
