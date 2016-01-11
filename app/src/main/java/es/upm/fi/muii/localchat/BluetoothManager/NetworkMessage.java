/**
 * Localchat
 *
 * @author Ignacio Molina Cuquerella
 * @author Claudiu Barzu
 */

package es.upm.fi.muii.localchat.BluetoothManager;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class NetworkMessage implements Serializable {

    private static final long serialVersionUID = 123456789L;
    private String idWriter;
    private String target;
    private Object message;
    private long timestamp;
    private int messageType;

    public NetworkMessage(Object message, long timestamp, int type) {
        this.messageType = type;
        this.message = message;
        this.timestamp = timestamp;

        this.idWriter = "";
        // If empty, personal message
        this.target = "";
    }

    public void setMessage(Object o){
        this.message = o;
    }

    public void setWriter(String writer) {
        this.idWriter = writer;
    }

    public String getWriter() {

        return idWriter;
    }

    public void setTarget(String target) {

        this.target = target;
    }

    public String getTarget() {
        return this.target;
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
