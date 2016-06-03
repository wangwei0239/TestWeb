package com.example.wangwei.testweb;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by wangwei on 16/3/31.
 */

@Table(name="chatmessage")
public class ChatMsg {

    @Column(name="id", isId = true, autoGen = true)
    private int id;
    @Column(name="uid")
    private int uid;
    @Column(name="direction")
    private int direction;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
