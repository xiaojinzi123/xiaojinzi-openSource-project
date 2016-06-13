package com.example.cxj.huaishi.common;

public class Msg {

    public static final String OK = "ok";

    public static final String ERROR = "error";

    /**
     * 只有两个值<br>
     * 1."ok"<br>
     * 2."error"
     */
    private String msg = OK;

    /**
     * 信息的文本
     */
    private String msgText = OK;

    public Msg() {
        super();
    }

    public Msg(String msg) {
        super();
        this.msg = msg;
    }

    public Msg(String msg, String msgText) {
        super();
        this.msg = msg;
        this.msgText = msgText;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

}
