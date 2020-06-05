package com.netease.jv.cmpp.controller;

import com.netease.jv.cmpp.service.GsCmppService;
import com.netease.jv.cmpp.utils.GsResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * The type Cmpp controller.
 */
@RestController
@RequestMapping("/action/cmpp")
public class CmppController {

    @Resource
    private GsCmppService gsCmppService;

    private final static String JV_CMPP_SALT = "gs_cmpp";

    /**
     * 联通Cmpp短信发送接口.
     *
     * @param phone   手机号
     * @param content 短信内容
     * @param sign    sign
     * @return the json object
     */
    @PostMapping("/submit")
    public GsResult cmppSubmit(@RequestParam String phone, @RequestParam String content, @RequestParam String sign) {

        if (StringUtils.isAnyBlank(phone, content, sign)) {
            return GsResult.newFail("invalid_param").build();
        }

        String rightSign = DigestUtils.md5Hex(StringUtils.join(JV_CMPP_SALT, phone, content));
        if (!sign.equals(rightSign)) {
            return GsResult.newFail("invalid_sign").build();
        }

        boolean sendResult = gsCmppService.send(phone, content);
        return sendResult ? GsResult.newSuccess().build() : GsResult.newFail("submit_fail").build();
    }

}
