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
    private JButton verifyJARButton;
    private JButton showCertificateButton;
    private JarManager jarManager;
    private File selectedJar;

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
        verifyJARButton = mainFrame.getVerifyJARButton();
        showCertificateButton = mainFrame.getShowCertificateButton();
    }

    private void initListeners() {
        initSelectFile();
        initSubmit();
        initEncrypt();
        initDecrypt();
        verifyJARButton.addActionListener(e -> WindowMessages.showJarCertificate(selectedJar.getAbsolutePath()));
        initCertificate();
    }

    private void initSelectFile() {
        selectFileButton.addActionListener(e -> {
            JarFileChooser jarFileChooser = new JarFileChooser();
            int returnValue = jarFileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedJar = jarFileChooser.getSelectedFile();
                jarPathTextField.setText(selectedJar.getName());
                submitButton.setEnabled(true);
                jarManager = new JarManager(selectedJar.getAbsolutePath());
            }
        });
    }

    private void initSubmit() {
        submitButton.addActionListener(e -> {
            classesComboBox.removeAllItems();
            jarManager.getClassNames().forEach(this::checkCertificate);
            if (classesComboBox.getSelectedItem() != null)
                mainFrame.setEnabledComponents(true);
            else
                verifyJARButton.setEnabled(true);
        });
    }

    private void checkCertificate(String name) {
        if (jarManager.getClassIds().get(name).length != 0)
            classesComboBox.addItem(name.replace("/", "."));
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

    private void initCertificate() {
        showCertificateButton.addActionListener(e -> {
            String selectedClass = String.valueOf(classesComboBox.getSelectedItem()).replace(".", "/");
            WindowMessages.showClassCertificate(jarManager.getClassIds().get(selectedClass));
        });
    }
}
