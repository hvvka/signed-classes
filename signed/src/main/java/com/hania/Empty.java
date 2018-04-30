package com.hania;

import javax.swing.*;
import java.awt.*;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class Empty {

    Empty() {
        String title = "I'm an empty class";
        String message = "I don't do anything";
        JOptionPane.showMessageDialog(new Frame(), message, title, JOptionPane.ERROR_MESSAGE);
    }
}
