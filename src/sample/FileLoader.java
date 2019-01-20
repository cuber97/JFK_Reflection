package sample;

import sample.interfaces.BooleanOperable;
import sample.interfaces.ByteOperable;
import sample.interfaces.IntegerOperable;
import sample.interfaces.StringOperable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class FileLoader {
    private List<Class<?>> classes = new ArrayList<>();
    private Controller controller;

    FileLoader(Controller controller) {
        this.controller = controller;
    }

    List<Method> getMethodNames() {
        List<Method> list = new ArrayList<>();

        for (Class<?> c : classes) {
            Method[] methods = c.getDeclaredMethods();
            list.addAll(Arrays.asList(methods));
        }

        return list;
    }

    void addFilesFromDirectory(File directory) throws IOException {
        classes.clear();

        if(directory.listFiles() == null) {
            return;
        }

        for (File fileEntry : Objects.requireNonNull(directory.listFiles())) {
            if(fileEntry == null) {
                return;
            }

            if (fileEntry.isFile()) {
                if (fileEntry.getName().endsWith(".jar")) {
                    System.out.println("Exploring " + fileEntry.getName());
                    addFileInfo(fileEntry);
                    addJarFile(fileEntry);
                }
                if (fileEntry.getName().endsWith(".class")) {
                    System.out.println("Exploring " + fileEntry.getName());
                    addFileInfo(fileEntry);
                    addClassFile(fileEntry);
                }
            }
        }
    }

    private void addJarFile(File directory) {
        try (JarFile jarFile = new JarFile(directory)) {
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
                    } else {
                        negativeInfo(c);
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addClassFile(File file) throws IOException {
        String path = file.getParent();
        String className = file.getName();
        className = className.substring(0, className.indexOf("."));

        File classFile = new File(path);

        URL url = classFile.toURI().toURL();
        URL[] urls = new URL[]{url};

        ClassLoader cl = new URLClassLoader(urls);
        Class<?> c;
        try {
            c = cl.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if (isAssignable(c)) {
            classes.add(c);
        } else {
            negativeInfo(c);
        }
    }

    private boolean isAssignable(Class<?> c) {
        if (BooleanOperable.class.isAssignableFrom(c)) {
            addClassInfo(c, BooleanOperable.class.getName());
            return true;
        }
        if (ByteOperable.class.isAssignableFrom(c)) {
            addClassInfo(c, ByteOperable.class.getName());
            return true;
        }
        if (IntegerOperable.class.isAssignableFrom(c)) {
            addClassInfo(c, IntegerOperable.class.getName());
            return true;
        }
        if (StringOperable.class.isAssignableFrom(c)) {
            addClassInfo(c, StringOperable.class.getName());
            return true;
        }
        return false;
    }

    private void addClassInfo(Class<?> c, String s) {
        controller.appendTextArea("Class ");
        controller.appendTextArea(c.getName());
        controller.appendTextArea(" implements interface ");
        controller.appendTextArea(s);
        controller.appendTextArea("\n");
    }

    private void addFileInfo(File fileEntry) {
        controller.appendTextArea("Exploring ");
        controller.appendTextArea(fileEntry.getName());
        controller.appendTextArea("\n");
    }

    private void negativeInfo(Class<?> c) {
        controller.appendTextArea("Class ");
        controller.appendTextArea(c.getName());
        controller.appendTextArea(" doesn't meet the contract.");
        controller.appendTextArea("\n");
    }
}
