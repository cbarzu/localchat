package es.upm.fi.muii.localchat.chat;

/**
 * Created by Titanium on 22/11/15.
 */
public class Message {

    private long idWriter;
    private String message;
    private long timestamp;

    public Message(long idWriter, String message, long timestamp) {

        this.idWriter = idWriter;
        this.message = message;
        this.timestamp = timestamp;
    }

    public long getWriter() {

        return idWriter;
    }

    public String getMessage() {

        return message;
    }

    public long getTimestamp() {

        return timestamp;
    }
}
