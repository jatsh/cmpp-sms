package com.netease.jv.cmpp.consts;

import java.nio.charset.Charset;

/** 字符集相关常量 **/
public class CharsetConsts {

    public static final String UTF_8_NAME = "UTF-8";
    public static final String GBK_NAME = "GBK";
    public static final String GB_2312_NAME = "GB2312";
    public static final String ISO_8859_1_NAME = "ISO-8859-1";

    public static final Charset UTF_8 = Charset.forName(UTF_8_NAME);
    public static final Charset GBK = Charset.forName(GBK_NAME);
    public static final Charset GB_2313 = Charset.forName(GB_2312_NAME);
    public static final Charset ISO_8859_1 = Charset.forName(ISO_8859_1_NAME);

    public static final Charset DEFAULT = UTF_8;

}
