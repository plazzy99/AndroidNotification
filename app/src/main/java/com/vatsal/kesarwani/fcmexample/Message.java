package com.vatsal.kesarwani.fcmexample;

public class Message {
    private CharSequence text;
    private long timeStamp;
    private CharSequence sender;

    public Message(CharSequence text, CharSequence sender) {
        this.text = text;
        this.timeStamp = System.currentTimeMillis();
        this.sender = sender;
    }

    public CharSequence getText() {
        return text;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public CharSequence getSender() {
        return sender;
    }
}
