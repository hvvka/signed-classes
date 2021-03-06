package com.hania;

import javax.swing.*;
import java.awt.*;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class Hello {

    public static void main(String[] args) {
        System.setProperty("java.security.policy", "file:src/main/resources/hello.policy");
        System.setSecurityManager(new SecurityManager());
        String title = "Hey, I'm 100% secure";
        String message = "Hello, safe classes!";
        JOptionPane.showMessageDialog(new Frame(), message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
