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
}
