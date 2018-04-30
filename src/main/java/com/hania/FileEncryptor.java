package com.hania;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Can either encrypt or decrypt a file with base64.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class FileEncryptor {

    private static final Logger LOG = LoggerFactory.getLogger(FileEncryptor.class);

    public void encrypt(File inputFile) {
        byte[] inputBytes = getInputBytes(inputFile);
        saveInputBytes(inputFile, () -> Base64.getEncoder().encode(inputBytes));
    }

    public void decrypt(File inputFile) {
        byte[] inputBytes = getInputBytes(inputFile);
        saveInputBytes(inputFile, () -> Base64.getDecoder().decode(inputBytes));
    }

    private byte[] getInputBytes(File inputFile) {
        byte[] inputBytes = new byte[0];
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            inputBytes = new byte[(int) inputFile.length()];
            fileInputStream.read(inputBytes);
        } catch (IOException e) {
            LOG.error("", e);
        }
        return inputBytes;
    }

    private void saveInputBytes(File inputFile, Encryptor encryptor) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(inputFile)) {
            byte[] outputBytes = encryptor.getOutputBytes();
            fileOutputStream.write(outputBytes);
        } catch (IOException e) {
            LOG.error("", e);
        }
    }

    interface Encryptor {
        byte[] getOutputBytes();
    }
}
