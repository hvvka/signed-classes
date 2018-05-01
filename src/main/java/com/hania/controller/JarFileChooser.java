package com.hania.controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class JarFileChooser extends JFileChooser {

    JarFileChooser() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.jar", "jar", "java");
        this.setFileFilter(filter);
    }
}
