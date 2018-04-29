package com.hania;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class Runner {

    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        String jarPath = "file:/Users/hg/pwr/6sem/pwjjtz/lab-culer/lab7/jar/";
        String jarName = "signed-1.0-SNAPSHOT.jar";
        String className = "com.hania.Hello";

        JarLoader jarLoader = new JarLoader(jarPath);
        jarLoader.readJarFile(jarName);
        LOG.info("Host: {}", jarLoader.getHost());
        LOG.info("Thread group: {}", jarLoader.getThreadGroup());
        jarLoader.checkPackageAccess(jarName);
        LOG.info("Class: {}", jarLoader.findClass(className));
    }
}
