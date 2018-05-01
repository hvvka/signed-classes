package com.hania;

import com.hania.controller.MainController;

import javax.swing.*;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class Runner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainController::new);
    }
}
