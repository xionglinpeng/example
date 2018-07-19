package com.example.thread.daemon;

import lombok.Data;

import java.util.Date;

@Data
public class Event implements Cloneable{

    private Date date;

    private String event;

    @Override
    public Event clone() {
        try {
            return (Event)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
