package es.upm.fi.muii.localchat.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by claudiu on 06/01/2016.
 */
public class SerialBitmap  implements Serializable {

    private static final long serialVersionUID = 1L;
    public Bitmap bitmap;

    SerialBitmap(Bitmap aBitmap) {
        bitmap = aBitmap;
    }

    // Converts the Bitmap into a byte array for serialization
    public static byte []  serialize_bitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return  stream.toByteArray();
    }

    // Deserializes a byte array representing the Bitmap and decodes it
    public static Bitmap deserialize_bitmap(byte [] bitmapBytes) {
        return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }

}
