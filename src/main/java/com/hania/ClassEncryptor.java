package com.hania;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Can either encrypt or decrypt bytes in specified class from jar file with base64.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class ClassEncryptor {

    private static final Logger LOG = LoggerFactory.getLogger(ClassEncryptor.class);

    private final String jarPath;

    ClassEncryptor(String jarPath) {
        this.jarPath = jarPath;
    }

    void encrypt(String className, byte[] inputBytes) {
        saveInputBytes(className, () -> Base64.getEncoder().encode(inputBytes));
    }

    void decrypt(String className, byte[] inputBytes) {
        saveInputBytes(className, () -> Base64.getDecoder().decode(inputBytes));
    }

    private void saveInputBytes(String className, Encryptor encryptor) {
        File tmpClassFile = getTemporaryClassFile(className);
        byte[] outputBytes = encryptor.getOutputBytes();
        writeTempClassFile(tmpClassFile, outputBytes);
        ClassReplacement.replace(jarPath, className + ".class", tmpClassFile.getPath());
    }

    private File getTemporaryClassFile(String className) {
        File tempClassFile = null;
        try {
            tempClassFile = File.createTempFile(className, ".class");
        } catch (IOException e) {
            LOG.error("", e);
        }
        assert tempClassFile != null;
        return tempClassFile;
    }

    private void writeTempClassFile(File tempClassFile, byte[] outputBytes) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempClassFile)) {
            fileOutputStream.write(outputBytes);
        } catch (IOException e) {
            LOG.error("", e);
        }
    }

    interface Encryptor {
        byte[] getOutputBytes();
    }
}
