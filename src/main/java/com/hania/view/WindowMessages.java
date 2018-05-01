package com.hania.view;

import com.hania.Jarsigner;

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

    public static void showCertificate(String jarPath) {
        String title = "Certificate";
        String message = String.format("<html>%s", Jarsigner.verify(jarPath));
        JOptionPane optionPane = new NarrowOptionPane();
        optionPane.setMessage(message);
        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(null, title);
        dialog.setVisible(true);
    }

    public enum Base64 {
        ENCRYPTED,
        DECRYPTED
    }
}

class NarrowOptionPane extends JOptionPane {

    NarrowOptionPane() {
    }

    @Override
    public int getMaxCharactersPerLineCount() {
        return 100;
    }
}
