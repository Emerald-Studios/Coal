package dk.sebsa.coal.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static InputStream loadFile(String location) throws IOException {
        try {
            if(location.startsWith("/")) {
                location = location.replaceFirst("/", "");
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

                return classLoader.getResourceAsStream(location);
            } else return new FileInputStream(location);
        } catch (Exception e) {
            throw new IOException("FileUtils, can't load file: " + location);
        }
    }
    public static List<String> readAllLinesList(InputStream is) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
        } br.close();

        return list;
    }
    public static String readAllLines(InputStream is) throws IOException {
        String e = "";
        for(String line : readAllLinesList(is)) {
            e += line + "\n";
        }

        return e;
    }
}
