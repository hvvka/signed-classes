package com.hania.view;

import javax.swing.*;
import java.awt.*;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class WindowMessages {

    private WindowMessages() {
        throw new IllegalStateException("Utility class!");
    }

    public static void showSuccess(String className, Base64 encryption) {
        String title = "Success!";
        String message = String.format("Class %s was %s successfully.", className, encryption.toString().toLowerCase());
        JOptionPane.showMessageDialog(new Frame(), message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public enum Base64 {
        ENCRYPTED,
        DECRYPTED
    }
}
