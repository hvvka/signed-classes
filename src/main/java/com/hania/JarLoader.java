package com.hania;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class JarLoader extends SecureClassLoader {

    private static final Logger LOG = LoggerFactory.getLogger(JarLoader.class);

    private static int groupNum = 0;

    private boolean printLoadMessages = true;

    private URL urlBase;
    private Map<String, byte[]> classArrays;
    private Map<String, Certificate[]> classIds;
    private ThreadGroup threadGroup;

    JarLoader(String base) {
        super();
        try {
            if (!(base.endsWith("/")))
                base = base + "/";
            urlBase = new URL(base);
            classArrays = new Hashtable<>();
            classIds = new Hashtable<>();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(base, e);
        }
    }

    private byte[] getClassBytes(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        boolean eof = false;
        while (!eof) {
            try {
                int i = bufferedInputStream.read();
                if (i == -1)
                    eof = true;
                else byteArrayOutputStream.write(i);
            } catch (IOException e) {
                return new byte[0];
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    protected Class findClass(String name) {
        String urlName = name.replace('.', '/');
        byte[] buf;
        Class clazz;

        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            int i = name.lastIndexOf('.');
            if (i >= 0)
                securityManager.checkPackageDefinition(name.substring(0, i));
        }

        buf = classArrays.get(urlName);
        if (buf != null) {
            Certificate[] certificates = classIds.get(urlName);
            CodeSource codeSource = new CodeSource(urlBase, certificates);
            clazz = defineClass(name, buf, 0, buf.length, codeSource);
            return clazz;
        }

        try {
            URL url = new URL(urlBase, urlName + ".class");
            if (printLoadMessages)
                LOG.info("Loading {}", url);
            InputStream inputStream = url.openConnection().getInputStream();
            buf = getClassBytes(inputStream);
            CodeSource codeSource = new CodeSource(urlBase, (CodeSigner[]) null);
            clazz = defineClass(name, buf, 0, buf.length, codeSource);
            return clazz;
        } catch (IOException e) {
            LOG.error("Can't load {}", name, e);
            return null;
        }
    }

    void readJarFile(String name) {
        JarInputStream jarInputStream;
        JarEntry jarEntry;

        URL jarUrl;
        try {
            jarUrl = new URL(urlBase, name);
        } catch (MalformedURLException mue) {
            LOG.error("Unknown jar file {}", name);
            return;
        }
        if (printLoadMessages)
            LOG.info("Loading jar file {}", jarUrl);

        try {
            jarInputStream = new JarInputStream(jarUrl.openConnection().getInputStream());
        } catch (IOException e) {
            LOG.error("Can't open jar file {}", jarUrl);
            return;
        }

        try {
            while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                String jarName = jarEntry.getName();
                if (jarName.endsWith(".class"))
                    loadClassBytes(jarInputStream, jarName, jarEntry);
                // else ignore it; it could be an image or audio file
                jarInputStream.closeEntry();
            }
        } catch (IOException e) {
            LOG.error("Badly formatted jar file");
        }
    }

    private void loadClassBytes(JarInputStream jarInputStream, String jarName, JarEntry jarEntry) {
        if (printLoadMessages)
            LOG.info("Loading bytes from: {}", jarName);
        BufferedInputStream jarBuffered = new BufferedInputStream(jarInputStream);
        ByteArrayOutputStream jarOutput = new ByteArrayOutputStream();
        int b;
        try {
            while ((b = jarBuffered.read()) != -1)
                jarOutput.write(b);
            String className = jarName.substring(0, jarName.length() - 6);
            classArrays.put(className, jarOutput.toByteArray());
            Certificate[] certificates = jarEntry.getCertificates();
            if (certificates == null)
                certificates = new Certificate[0];
            classIds.put(className, certificates);
        } catch (IOException e) {
            LOG.error("Error reading entry: {}", jarName);
        }
    }

    void checkPackageAccess(String name) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null)
            securityManager.checkPackageAccess(name);
    }

    ThreadGroup getThreadGroup() {
        if (threadGroup == null)
            threadGroup = new ThreadGroup("JavaRuner ThreadGroup-" + groupNum++);
        return threadGroup;
    }

    String getHost() {
        return urlBase.getHost();
    }

    List<String> getClassNames() {
        return new ArrayList<>(classArrays.keySet());
    }

    Map<String, Certificate[]> getClassIds() {
        return classIds;
    }

    byte[] getClassBytes(String className) {
        return classArrays.get(className);
    }
}
