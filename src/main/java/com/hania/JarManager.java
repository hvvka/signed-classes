package com.hania;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class JarManager {

    private static final Logger LOG = LoggerFactory.getLogger(JarManager.class);

    private static final String JAR_PREFIX = "jar:";

    private static final String FILE_PREFIX = "file:";

    private final String jarPath;

    private JarLoader jarLoader;

    private ClassEncryptor classEncryptor;

    public JarManager(String jarPath) {
        this.jarPath = jarPath;
        loadJar();
        classEncryptor = new ClassEncryptor(JAR_PREFIX + FILE_PREFIX + jarPath);
    }

    private void loadJar() {
        File jarFile = new File(jarPath);
        String jarParentPath = jarFile.getParent();
        jarLoader = new JarLoader(FILE_PREFIX + jarParentPath);
        String jarName = jarFile.getName();
        jarLoader.readJarFile(jarName);
    }

    public List<String> getClassNames() {
        return jarLoader.getClassNames();
    }

    public Map<String, Certificate[]> getClassIds() {
        return jarLoader.getClassIds();
    }

    public void encryptClass(String className) {
        classEncryptor.encrypt(className, jarLoader.getClassBytes(className));
    }

    public void decryptClass(String className) {
        classEncryptor.decrypt(className, getEncryptedBytes(className + ".class"));
    }

    private byte[] getEncryptedBytes(String className) {
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        URI uri = URI.create(JAR_PREFIX + FILE_PREFIX + jarPath);
        try (FileSystem fileSystem = FileSystems.newFileSystem(uri, env)) {
            Path pathInJar = fileSystem.getPath(className);
            return Files.readAllBytes(pathInJar);
        } catch (IOException e) {
            LOG.error("", e);
        }
        return new byte[0];
    }
}
