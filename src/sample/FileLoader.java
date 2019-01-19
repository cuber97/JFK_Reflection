package sample;

import com.sun.org.apache.bcel.internal.util.ClassPath;
import javafx.scene.control.TextArea;
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
            for (Method m : methods) {
                list.add(m);
            }
        }

        return list;
    }

    public void addFilesFromDirectory(File directory, TextArea textArea) throws IOException {
        classes.clear();

        for (File fileEntry : directory.listFiles()) {
            if (fileEntry.isFile()) {
                if (fileEntry.getName().endsWith(".jar")) {
                    System.out.println("Exploring " + fileEntry.getName());
                    addFileInfo(fileEntry, textArea);
                    addJarFile(fileEntry, textArea);
                }
                if (fileEntry.getName().endsWith(".class")) {
                    System.out.println("Exploring " + fileEntry.getName());
                    addFileInfo(fileEntry, textArea);
                    addClassFile(fileEntry, textArea);
                }
            }
        }
    }

    public void addJarFile(File directory, TextArea textArea) throws IOException {
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
                    if (isAssignable(c, textArea)) {
                        classes.add(c);
                    } else {
                        negativeInfo(c, textArea);
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

    public void addClassFile(File file, TextArea textArea) throws IOException {
        String path = file.getParent();
        String className = file.getName();
        className = className.substring(0, className.indexOf("."));

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

        if (isAssignable(c, textArea)) {
            classes.add(c);
        } else {
            negativeInfo(c, textArea);
        }
    }

    public boolean isAssignable(Class<?> c, TextArea textArea) {
        if (BooleanOperable.class.isAssignableFrom(c)) {
            addClassInfo(c, BooleanOperable.class.getName(), textArea);
            return true;
        }
        if (ByteOperable.class.isAssignableFrom(c)) {
            addClassInfo(c, ByteOperable.class.getName(), textArea);
            return true;
        }
        if (IntegerOperable.class.isAssignableFrom(c)) {
            addClassInfo(c, IntegerOperable.class.getName(), textArea);
            return true;
        }
        if (StringOperable.class.isAssignableFrom(c)) {
            addClassInfo(c, StringOperable.class.getName(), textArea);
            return true;
        }
        return false;
    }

    private void addClassInfo(Class<?> c, String s, TextArea textArea) {
        StringBuilder stringBuilder = new StringBuilder(textArea.getText());
        stringBuilder.append("Class ");
        stringBuilder.append(c.getName());
        stringBuilder.append(" implements interface ");
        stringBuilder.append(s);
        stringBuilder.append("\n");
        textArea.setText(stringBuilder.toString());
    }

    private void addFileInfo(File fileEntry, TextArea textArea) {
        StringBuilder stringBuilder = new StringBuilder(textArea.getText());
        stringBuilder.append("Exploring ");
        stringBuilder.append(fileEntry.getName());
        stringBuilder.append("\n");
        textArea.setText(stringBuilder.toString());
    }

    private void negativeInfo(Class<?> c, TextArea textArea) {
        StringBuilder stringBuilder = new StringBuilder(textArea.getText());

        stringBuilder.append("Class ");
        stringBuilder.append(c.getName());
        stringBuilder.append(" doesn't meet the contract.");
        stringBuilder.append("\n");
    }
}
