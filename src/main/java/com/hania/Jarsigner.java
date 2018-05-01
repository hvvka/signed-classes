package com.hania;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class Jarsigner {

    private static final Logger LOG = LoggerFactory.getLogger(Jarsigner.class);

    private Jarsigner() {
        throw new IllegalStateException("Utility class!");
    }

    public static String verify(String jarPath) {
        Process process = createProcess(jarPath);
        StringBuilder stringBuilder = createMessage(process);
        return stringBuilder.toString();
    }

    private static Process createProcess(String jarPath) {
        String command = String.format("jarsigner -verbose -verify %s", jarPath);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            LOG.error("", e);
        }
        return process;
    }

    private static StringBuilder createMessage(Process process) {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        readStandardInput(stdin, stringBuilder);
        return stringBuilder;
    }

    private static void readStandardInput(BufferedReader stdin, StringBuilder stringBuilder) {
        String line;
        try {
            while ((line = stdin.readLine()) != null) {
                stringBuilder.append(line).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            LOG.error("", e);
        }
    }
}
