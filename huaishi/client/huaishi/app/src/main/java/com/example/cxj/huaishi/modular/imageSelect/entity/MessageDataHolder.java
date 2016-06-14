package com.example.cxj.huaishi.modular.imageSelect.entity;

import android.os.Message;

/**
 * Created by cxj on 2016/5/5.
 */
public class MessageDataHolder {

    public String folderName;
    public int imageNum;

    public MessageDataHolder() {
    }

    public MessageDataHolder(String folderName, int imageNum) {
        this.folderName = folderName;
        this.imageNum = imageNum;
    }

    public static Message obtain(String folderName, int imageNum) {
        Message m = Message.obtain();
        MessageDataHolder messageDataHolder = new MessageDataHolder(folderName, imageNum);
        m.obj = messageDataHolder;
        return m;
    }

}
