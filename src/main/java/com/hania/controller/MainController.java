package com.hania.controller;

import com.hania.JarManager;
import com.hania.view.MainFrame;
import com.hania.view.WindowMessages;

import javax.swing.*;
import java.io.File;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */

public class MainController {

    private MainFrame mainFrame;
    private JButton selectFileButton;
    private JTextField jarPathTextField;
    private JButton submitButton;
    private JComboBox<String> classesComboBox;
    private JButton encryptButton;
    private JButton decryptButton;
    private JarManager jarManager;

    public MainController() {
        mainFrame = new MainFrame();
        initComponents();
        initListeners();
    }

    private void initComponents() {
        selectFileButton = mainFrame.getSelectFileButton();
        jarPathTextField = mainFrame.getJarPathTextField();
        submitButton = mainFrame.getSubmitButton();
        classesComboBox = mainFrame.getClassesComboBox();
        encryptButton = mainFrame.getEncryptButton();
        decryptButton = mainFrame.getDecryptButton();
    }

    private void initListeners() {
        initSelectFile();
        initSubmit();
        initEncrypt();
        initDecrypt();
    }

    private void initSelectFile() {
        selectFileButton.addActionListener(e -> {
            JarFileChooser jarFileChooser = new JarFileChooser();
            int returnValue = jarFileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jarFileChooser.getSelectedFile();
                jarPathTextField.setText(selectedFile.getName());
                submitButton.setEnabled(true);
                jarManager = new JarManager(selectedFile.getAbsolutePath());
            }
        });
    }

    private void initSubmit() {
        submitButton.addActionListener(e -> {
            mainFrame.setEnabledComponents(true);
            jarManager.getClassNames().forEach(name -> classesComboBox.addItem(name.replace("/", ".")));
        });
    }

    private void initEncrypt() {
        encryptButton.addActionListener(e -> {
            String selectedClass = String.valueOf(classesComboBox.getSelectedItem());
            jarManager.encryptClass(selectedClass.replace(".", "/"));
            WindowMessages.showSuccess(selectedClass, WindowMessages.Base64.ENCRYPTED);
        });
    }

    private void initDecrypt() {
        decryptButton.addActionListener(e -> {
            String selectedClass = String.valueOf(classesComboBox.getSelectedItem());
            jarManager.decryptClass(selectedClass.replace(".", "/"));
            WindowMessages.showSuccess(selectedClass, WindowMessages.Base64.DECRYPTED);
        });
    }
}
