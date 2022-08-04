package dk.sebsa.coal.util;

import dk.sebsa.coal.asset.AssetLocation;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author sebs
 */
public class ClassImporter<T> {
    private JavaCompiler compiler;
    public static String SYSTEM_LINE_SEPERATOR = System.getProperty("line.separator");
    public static String SYSTEM_TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");

    public ClassImporter() {
        compiler = ToolProvider.getSystemJavaCompiler();
    }

    public T importClass(AssetLocation location, Class<T> clazz) throws IOException {
        InputStream stream = location.asStream();
        if(stream == null) return null;

        // Get class path
        String[] name = location.location().replaceAll(Pattern.quote("\\"), "\\\\").split("\\\\");
        Path srcPath = Paths.get(SYSTEM_TEMP_DIRECTORY, name[name.length - 1]);

        // Read and compile class
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        Files.write(srcPath, reader.lines().collect(Collectors.joining(SYSTEM_LINE_SEPERATOR)).getBytes(StandardCharsets.UTF_8));

        compiler.run(null, null, null, srcPath.toString());
        Path p = srcPath.getParent().resolve(name[name.length - 1].split("\\.")[0] + ".class");

        // IDK anymore
        URL classURL = null;
        try {classURL = p.getParent().toFile().toURI().toURL();}
        catch (MalformedURLException e) {e.printStackTrace();}
        if(classURL == null) return null;
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {classURL});

        // Create Check if class will work
        Class<?> myClass = null;
        try {myClass = classLoader.loadClass(name[name.length - 1].split("\\.")[0]);}
        catch(ClassNotFoundException e1) {e1.printStackTrace();}
        if(myClass == null) return null;
        if(!clazz.isAssignableFrom(myClass)) return null;

        T l = null;
        try{l = (T) myClass.getConstructor().newInstance();}
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {e.printStackTrace();}
        if(l == null) return null;

        stream.close();
        reader.close();
        return l;
    }
}
