package com.netease.jv.cmpp.service;

import com.netease.jv.cmpp.handler.GsCmppMessageReceiveHandler;
import com.zx.sms.codec.cmpp.msg.CmppSubmitRequestMessage;
import com.zx.sms.connect.manager.EndpointConnector;
import com.zx.sms.connect.manager.EndpointEntity;
import com.zx.sms.connect.manager.EndpointManager;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointEntity;
import com.zx.sms.handler.api.BusinessHandlerInterface;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Gs cmpp service.
 */
@Service
@ImportResource(locations = {"classpath:config/comment-config.xml"})
public class GsCmppService {

    private final static Logger logger = LoggerFactory.getLogger(GsCmppService.class);

    @Value("${cmpp.host}")
    private String host;

    @Value("${cmpp.port}")
    private String port;

    @Value("${cmpp.userName}")
    private String userName;

    @Value("${cmpp.pwd}")
    private String pwd;

    @Value("${cmpp.spCode}")
    private String spCode;

    @Value("${cmpp.serviceId}")
    private String serviceId;

    @Resource
    private GsCmppMessageReceiveHandler gsCmppMessageReceiveHandler;

    private static final EndpointManager manager = EndpointManager.INS;

    private final static String GROUP_NAME = "jv-cmpp";

    private static final short CLIENT_MAX_CHANNEL = 1;

    /**
     * cmpp建立连接
     */
    @PostConstruct
    public void initConnect() {
        CMPPClientEndpointEntity client = new CMPPClientEndpointEntity();
        client.setId(userName);
        client.setHost(host);
        client.setPort(Integer.parseInt(port));
        client.setUserName(userName);
        client.setPassword(pwd);
        client.setServiceId(serviceId);
        client.setSpCode(spCode);
        client.setMaxChannels(CLIENT_MAX_CHANNEL);
        client.setVersion((short) 0x20);
        client.setWriteLimit(1);
        client.setGroupName(GROUP_NAME);
        client.setChartset(StandardCharsets.UTF_8);
        client.setRetryWaitTimeSec((short) 30);
        client.setUseSSL(false);
        client.setMaxRetryCnt((short) 0);
        client.setReSendFailMsg(false);
        client.setSupportLongmsg(EndpointEntity.SupportLongMessage.BOTH);

        List<BusinessHandlerInterface> list = new ArrayList<>();
        list.add(gsCmppMessageReceiveHandler);
        client.setBusinessHandlerSet(list);
        manager.addEndpointEntity(client);

        for (int i = 0; i < client.getMaxChannels(); i++) {
            manager.openEndpoint(client);
         }
        manager.startConnectionCheckTask();
    }

    /**
     * Dis connect.
     */
    public void disConnect() {
        // 连接断开
        manager.close();
    }

    /**
     * Send.
     *
     * @param mobile  the mobile
     * @param content the content
     */
    public boolean send(String mobile, String content) {

        String id = userName;
        CmppSubmitRequestMessage msg = new CmppSubmitRequestMessage();
        msg.setDestterminalId(mobile);
        msg.setMsgContent(content);
        msg.setRegisteredDelivery((short) 1);
        EndpointConnector<?> connector = EndpointManager.INS.getEndpointConnector(id);
        if (null == connector) {
            logger.error("cmpp send sms failed ! no connector is active");
            return false;
        }

        Channel channel = getChannel(connector);
        if (channel != null) {
            msg.setAttachment(System.currentTimeMillis());
            channel.writeAndFlush(msg);
            return true;
        } else {
            logger.error("cmpp send sms failed ! no channel is active");
            return false;
        }
    }

    private Channel getChannel(EndpointConnector<?> connector) {
        Channel ch = connector.fetch();
        if (null != ch && ch.isActive() && ch.isWritable()) {
            return ch;
        }
        return null;
    }
}
