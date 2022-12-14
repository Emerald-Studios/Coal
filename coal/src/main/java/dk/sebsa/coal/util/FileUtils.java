package dk.sebsa.coal.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.BufferUtils.createByteBuffer;

/**
 * @author sebs
 * @since 1.0.0
 */
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

    public static List<File> listFilesInFolder(final File folder) {
        List<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                files.addAll(listFilesInFolder(fileEntry));
            } else {
                files.add(fileEntry);
            }
        }

        return files;
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
        StringBuilder e = new StringBuilder();
        for(String line : readAllLinesList(is)) {
            e.append(line).append("\n");
        }

        return e.toString();
    }

    public static ByteBuffer isToBB(InputStream is, int bufferSize) throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(is);
        ByteBuffer buffer = createByteBuffer(bufferSize);

        while (true) {
            int bytes = rbc.read(buffer);
            if (bytes == -1) {
                break;
            }
            if (buffer.remaining() == 0) {
                buffer = resizeBuffer(buffer, buffer.capacity() * 2);
            }
        }
        buffer.flip();

        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
