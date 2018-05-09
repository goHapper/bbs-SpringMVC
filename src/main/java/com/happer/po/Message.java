package com.happer.po;

/**
 * 创建者：weikun【YST】
 * 日期：2017/8/5
 * 说说功能：
 */
import java.util.Date;

public class Message {
    private Integer uid;//发主贴的用户ID
    private Integer meid;//发回贴的用户id
    private String txt;
    private Date sendTime;
    private String rtitle;//主贴的title

    public String getRtitle() {
        return rtitle;
    }

    public void setRtitle(String rtitle) {
        this.rtitle = rtitle;
    }

    public Integer getMeid() {
        return meid;
    }

    public void setMeid(Integer meid) {
        this.meid = meid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public Date getSendTime() {
        return sendTime;
    }
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}