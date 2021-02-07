package core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import database.DatabaseType;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private static final ThreadLocal<Gson> gsonThreadLocal = new ThreadLocal<>();

    public static Gson getGson() {
        Gson gson = gsonThreadLocal.get();
        if (gson == null) {
            gson = new Gson();
            gsonThreadLocal.set(gson);
        }
        return gson;
    }

    public static String inputStreamToJson(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.readLine();
    }

    public static void writeJsonToOutputStream(String json, OutputStream stream) throws IOException {
        DataOutputStream requestStream = new DataOutputStream(stream);
        requestStream.writeBytes(json + '\n');
    }

    public static void writeObjectToJSONFile(Object object, String fileName) throws IOException {
        Gson gson = getGson();
        FileWriter writer = new FileWriter(fileName);
        gson.toJson(object, writer);
        writer.close();
    }

    public static <T> T readObjectFromJSONFile(String fileName, Class<T> className) throws FileNotFoundException {
        Gson gson = Util.getGson();
        JsonReader reader = new JsonReader(new FileReader(fileName));
        return gson.fromJson(reader, className);
    }

    public static boolean createFile(String fileName) throws IOException {
        File file = new File(fileName);
        boolean dirCreated = file.getParentFile().mkdirs();
        boolean fileCreated = file.createNewFile();
        return dirCreated || fileCreated;
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static String dateToString(Date date) {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }
}
