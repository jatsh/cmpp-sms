package com.netease.jv.cmpp.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@Service
public class GsAlarmEmailService {

    @Value("${gs.alarm.email.to}")
    private String[] alarmTo;

    @Value("${gs.alarm.email.cc}")
    private String[] alarmCc;

    @Resource
    private GsMailService gsMailService;

    @Value("${gs.env}")
    private String hostEnv;

    public void sendAlarm(String subject, String context, boolean html) throws MessagingException {
        if (alarmTo == null || alarmTo.length == 0) {
            throw new RuntimeException("alarm email address is null");
        }
        gsMailService.send(this.alarmTo, this.alarmCc, "【" + hostEnv + "】" + subject, context, html);
    }
}
