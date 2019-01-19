package sample;

import com.sun.org.apache.bcel.internal.util.ClassPath;
import javafx.stage.DirectoryChooser;
import sample.interfaces.BooleanOperable;
import sample.interfaces.ByteOperable;
import sample.interfaces.IntegerOperable;
import sample.interfaces.StringOperable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileLoader {
    private List<Class<?>> classes = new ArrayList<>();

    public List<Method> getMethodNames() {
        List<Method> list = new ArrayList();

        for (Class<?> c : classes) {
            Method[] methods = c.getDeclaredMethods();
            for(Method m: methods) {
                list.add(m);
            }
        }

        return list;
    }

    public void addFilesFromDirectory(File directory) throws IOException {
        classes.clear();

        for(File fileEntry: directory.listFiles()) {
            if (fileEntry.isFile()) {
                if(fileEntry.getName().endsWith(".jar")) {
                    System.out.println("Added " + fileEntry.getName());
                    addJarFile(fileEntry);
                }
                if(fileEntry.getName().endsWith(".class")) {
                    System.out.println("Added " + fileEntry.getName());
                    addClassFile(fileEntry);
                }
            }
        }
    }

    public void addJarFile(File directory) throws IOException {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(directory);
            Enumeration<JarEntry> entries = jarFile.entries();

            URL[] urls = {new URL("jar:file:" + directory + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (entries.hasMoreElements()) {
                JarEntry je = entries.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }

                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                try {
                    Class<?> c = cl.loadClass(className);
                    if (isAssignable(c)) {
                        classes.add(c);
                    }

                } catch (ClassNotFoundException exp) {
                    continue;
                }

            }
        } catch (IOException exp) {
        } finally {
            if (null != jarFile)
                jarFile.close();
        }
    }

    public void addClassFile(File file) throws IOException{
        String path = file.getParent();
        String className = file.getName();
        className = className.substring(0,className.indexOf("."));

        File classFile = new File(path);

        URL url = classFile.toURI().toURL();
        URL[] urls = new URL[]{url};

        ClassLoader cl = new URLClassLoader(urls);
        Class<?> c = null;
        try {
            c = cl.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (isAssignable(c)) {
            classes.add(c);
        }
    }

    public boolean isAssignable(Class<?> c) {
        if (BooleanOperable.class.isAssignableFrom(c)) {
            return true;
        }
        if (ByteOperable.class.isAssignableFrom(c)) {
            return true;
        }
        if (IntegerOperable.class.isAssignableFrom(c)) {
            return true;
        }
        if (StringOperable.class.isAssignableFrom(c)) {
            return true;
        }

        return false;
    }
}
