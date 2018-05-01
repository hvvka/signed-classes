package com.hania.view;

import javax.swing.*;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class MainFrame extends JFrame {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 350;

    private JPanel mainPanel;
    private JButton selectFileButton;
    private JTextField jarPathTextField;
    private JButton submitButton;
    private JComboBox<String> classesComboBox;
    private JButton encryptButton;
    private JButton decryptButton;
    private JLabel selectClassLabel;

    public MainFrame() {
        super("Signed classes");
        setSize(WIDTH, HEIGHT);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setEnabledComponents(false);
        jarPathTextField.setEditable(false);
        submitButton.setEnabled(false);
        setVisible(true);
    }

    public void setEnabledComponents(boolean isEnabled) {
        selectClassLabel.setEnabled(isEnabled);
        classesComboBox.setEnabled(isEnabled);
        encryptButton.setEnabled(isEnabled);
        decryptButton.setEnabled(isEnabled);
    }

    public JButton getSelectFileButton() {
        return selectFileButton;
    }

    public JTextField getJarPathTextField() {
        return jarPathTextField;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JComboBox<String> getClassesComboBox() {
        return classesComboBox;
    }

    public JButton getEncryptButton() {
        return encryptButton;
    }

    public JButton getDecryptButton() {
        return decryptButton;
    }
}
