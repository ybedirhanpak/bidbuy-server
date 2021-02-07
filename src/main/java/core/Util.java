package core;

import java.io.*;

public class Util {

    public static String inputStreamToJson(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.readLine();
    }

    public static void writeJsonToOutputStream(String json, OutputStream stream) throws IOException {
        DataOutputStream requestStream = new DataOutputStream(stream);
        requestStream.writeBytes(json + '\n');
    }

    public static void sleepThread() {
        try {
            System.out.println("Thread sleeping: " + Thread.currentThread());
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
