package com.netease.jv.cmpp.logger;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

@Slf4j
public class GsIpClassicConverter extends ClassicConverter {

    public static final String UNKNOWN_IP = "UNKNOWN_IP";

    private String ip;

    public GsIpClassicConverter() {
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            this.ip = UNKNOWN_IP;
            log.error("GsIpClassicConverter fail", e);
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        return ip;
    }

}