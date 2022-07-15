package com.zhh.crm.commons.bean;

/**
 * 响应消息类
 */
public class ResponseMsg {
    private String stateCode;
    private String message;
    private Object otherMsg;

    public ResponseMsg() {
    }

    public ResponseMsg(String stateCode, String message, Object otherMsg) {
        this.stateCode = stateCode;
        this.message = message;
        this.otherMsg = otherMsg;
    }

    @Override
    public String toString() {
        return "ResponseMsg{" +
                "stateCode='" + stateCode + '\'' +
                ", message='" + message + '\'' +
                ", otherMsg=" + otherMsg +
                '}';
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getOtherMsg() {
        return otherMsg;
    }

    public void setOtherMsg(Object otherMsg) {
        this.otherMsg = otherMsg;
    }
}
