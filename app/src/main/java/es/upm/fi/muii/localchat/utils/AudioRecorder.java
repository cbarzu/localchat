/**
 * Localchat
 *
 * @author Ignacio Molina Cuquerella
 * @author Claudiu Barzu
 */

package es.upm.fi.muii.localchat.utils;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecorder  {

    private static AudioRecorder aud = null;

    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    public AudioRecorder () {

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LocalchatAudio"+ System.currentTimeMillis()+ ".3gp";
        myAudioRecorder= new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

    }

    public static synchronized AudioRecorder getInstance(String action) {

        if (action.equals("down")) {

            aud = new AudioRecorder();
            return aud;

        } else if (action.equals("up")) {

            AudioRecorder aux = aud;
            aud = null;
            return aux;
        }
        return null;
    }

    public void record() throws IOException {

        myAudioRecorder.prepare();
        myAudioRecorder.start();
    }

    public void stop() {

        myAudioRecorder.stop();
        myAudioRecorder.reset();
        myAudioRecorder.release();
    }

    public String getFilename() {
        return outputFile;
    }

    public byte[] getAudioBytes() {

        File f = new File(outputFile);
        long bufferSize = f.length();
        byte [] msg = new byte[(int)bufferSize];

        try {

            FileInputStream fi = new FileInputStream(this.outputFile);
            int leidos = fi.read(msg);

            if (leidos>0) {

                byte b[] = new byte[leidos];
                for (int i = 0; i < leidos; i++) {
                    b[i] = msg[i];
                }
                return b;
            }
            return null;

        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }


    public static String writeAudioToFile(byte [] audio ) {

        File f = new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "/LocalchatAudio");
        if (!f.exists()) {

            f.mkdir();
        }

        String filename = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                        "/LocalchatAudio/" + System.currentTimeMillis()+ ".3gp";

        try {

            FileOutputStream fo = new FileOutputStream(filename);
            fo.write(audio);
            fo.close();

            return filename;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}



