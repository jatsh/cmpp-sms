package com.netease.jv.cmpp.handler;

import com.netease.jv.cmpp.mail.GsAlarmEmailService;
import com.zx.sms.codec.cmpp.msg.*;
import com.zx.sms.handler.api.AbstractBusinessHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@Sharable
@Component
public class GsCmppMessageReceiveHandler extends AbstractBusinessHandler {

    private static final Logger logger_business = LoggerFactory.getLogger("BUSINESS");
    private static final Logger logger = LoggerFactory.getLogger(GsCmppMessageReceiveHandler.class);

    @Override
    public String name() {
        return "GS-CMPP-MessageReceiveHandler";
    }

    @Resource
    private GsAlarmEmailService gsAlarmEmailService;

    public static AtomicInteger CMPP_SEND_FAIL = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof CmppConnectResponseMessage) {
            CmppConnectResponseMessage e = (CmppConnectResponseMessage) msg;
            if (e.getStatus() == 0) {
                logger_business.info("【CMPP_CONNECT_SUCCESS】 msg:{}", e);
            } else {
                logger.error("【CMPP_CONNECT_FAIL】 msg :{}", e);
            }
        } else if (msg instanceof CmppSubmitResponseMessage) {
            CmppSubmitResponseMessage e = (CmppSubmitResponseMessage) msg;
            logger_business.info("【CMPP_SUBMIT_RESPONSE】 msg :{}", e);
        } else if (msg instanceof CmppDeliverResponseMessage) {
            CmppDeliverResponseMessage e = (CmppDeliverResponseMessage) msg;
            logger_business.info("【CMPP_DELIVER_RESPONSE】 msg :{}", e);

        } else if (msg instanceof CmppDeliverRequestMessage) {
            CmppReportRequestMessage reportRequestMessage = ((CmppDeliverRequestMessage) msg).getReportRequestMessage();
            logger_business.info("【CMPP_DELIVER_REQUEST_MESSAGE】  msg :{}", reportRequestMessage);
            String stat = reportRequestMessage.getStat();
            if (stat.equals("ERRSIGN")) {
                int failCount = CMPP_SEND_FAIL.incrementAndGet();
                logger.error("【CMPP_DELIVER_FAIL】 msg :{}", reportRequestMessage);
                //发送异常30次，发送邮件报警
                if (failCount >= 100) {
                    CMPP_SEND_FAIL.set(0);
                    String content = "phone:" + reportRequestMessage.getDestterminalId() + "\n content：" + reportRequestMessage;
                    gsAlarmEmailService.sendAlarm("【CMPP】短信发送异常", content, false);
                }
            }
        } else {
            logger_business.info("CMPP_fireChannelRead msg :{}", msg);
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public GsCmppMessageReceiveHandler clone() throws CloneNotSupportedException {
        return (GsCmppMessageReceiveHandler) super.clone();
    }

}
