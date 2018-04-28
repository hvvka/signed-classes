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
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class JarLoader extends SecureClassLoader {

    private static final Logger LOG = LoggerFactory.getLogger(JarLoader.class);
    private static int groupNum = 0;
    private URL urlBase;
    private boolean printLoadMessages = true;
    private Hashtable<String, byte[]> classArrays;
    private Hashtable<String, Certificate[]> classIds;
    private ThreadGroup threadGroup;

    public JarLoader(String base, ClassLoader parent) {
        super(parent);
        try {
            if (!(base.endsWith("/")))
                base = base + "/";
            urlBase = new URL(base);
            classArrays = new Hashtable<>();
            classIds = new Hashtable<>();
        } catch (Exception e) {
            throw new IllegalArgumentException(base);
        }
    }

    private byte[] getClassBytes(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        boolean eof = false;
        while (!eof) {
            try {
                int i = bis.read();
                if (i == -1)
                    eof = true;
                else baos.write(i);
            } catch (IOException e) {
                return new byte[0];
            }
        }
        return baos.toByteArray();
    }

    @Override
    protected Class findClass(String name) {
        String urlName = name.replace('.', '/');
        byte[] buf;
        Class cl;

        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            int i = name.lastIndexOf('.');
            if (i >= 0)
                sm.checkPackageDefinition(name.substring(0, i));
        }

        buf = classArrays.get(urlName);
        if (buf != null) {
            Certificate[] ids = classIds.get(urlName);
            CodeSource cs = new CodeSource(urlBase, ids);
            cl = defineClass(name, buf, 0, buf.length, cs);
            return cl;
        }

        try {
            URL url = new URL(urlBase, urlName + ".class");
            if (printLoadMessages)
                LOG.info("Loading {}", url);
            InputStream is = url.openConnection().getInputStream();
            buf = getClassBytes(is);
            CodeSource cs = new CodeSource(urlBase, (CodeSigner[]) null);
            cl = defineClass(name, buf, 0, buf.length, cs);
            return cl;
        } catch (Exception e) {
            LOG.error("Can't load {}", name, e);
            return null;
        }
    }

    public void readJarFile(String name) {
        URL jarUrl;
        JarInputStream jis;
        JarEntry je;

        try {
            jarUrl = new URL(urlBase, name);
        } catch (MalformedURLException mue) {
            LOG.error("Unknown jar file {}", name);
            return;
        }
        if (printLoadMessages)
            LOG.error("Loading jar file {}", jarUrl);

        try {
            jis = new JarInputStream(
                    jarUrl.openConnection().getInputStream());
        } catch (IOException ioe) {
            LOG.info("Can't open jar file {}", jarUrl);
            return;
        }

        try {
            while ((je = jis.getNextJarEntry()) != null) {
                String jarName = je.getName();
                if (jarName.endsWith(".class"))
                    loadClassBytes(jis, jarName, je);
                // else ignore it; it could be an image or audio file
                jis.closeEntry();
            }
        } catch (IOException ioe) {
            LOG.error("Badly formatted jar file");
        }
    }

    private void loadClassBytes(JarInputStream jis,
                                String jarName, JarEntry je) {
        if (printLoadMessages)
            LOG.info("\t{}", jarName);
        BufferedInputStream jarBuf = new BufferedInputStream(jis);
        ByteArrayOutputStream jarOut = new ByteArrayOutputStream();
        int b;
        try {
            while ((b = jarBuf.read()) != -1)
                jarOut.write(b);
            String className = jarName.substring(0, jarName.length() - 6);
            classArrays.put(className, jarOut.toByteArray());
            Certificate[] c = je.getCertificates();
            if (c == null)
                c = new Certificate[0];
            classIds.put(className, c);
        } catch (IOException ioe) {
            LOG.error("Error reading entry {}", jarName);
        }
    }

    public void checkPackageAccess(String name) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null)
            sm.checkPackageAccess(name);
    }

    ThreadGroup getThreadGroup() {
        if (threadGroup == null)
            threadGroup = new ThreadGroup("JavaRuner ThreadGroup-" + groupNum++);
        return threadGroup;
    }

    String getHost() {
        return urlBase.getHost();
    }
}
