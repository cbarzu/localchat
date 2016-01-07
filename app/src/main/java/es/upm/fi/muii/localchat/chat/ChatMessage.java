package es.upm.fi.muii.localchat.chat;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ChatMessage implements Serializable{

    private static final long serialVersionUID = 123456789L;
    private long idWriter;
    private Object message;
    private long timestamp;
    private int messageType;

    public ChatMessage(long idWriter, Object message, long timestamp, int type) {
        this.messageType = type;
        this.idWriter = idWriter;
        this.message = message;
        this.timestamp = timestamp;
    }

    public void setMessage(Object o){
        this.message = o;
    }


    public void setWriter(long writer) {
        this.idWriter = writer;
    }

    public long getWriter() {

        return idWriter;
    }

    public Object getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int messageType (){
        return this.messageType;
    }

    public String toString(){
        return "Type:" + this.messageType();
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
