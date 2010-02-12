/*
 * file PreferencesDialog.java
 *
 * This file is part of SourceHeader project.
 *
 * SourceHeader is software for easier maintaining source files' headers.
 * project web: http://code.google.com/p/source-header/
 * author: Steve Sindelar
 * licence: New BSD Licence
 * 
 * (c) Steve Sindelar
 */

package sourceheader.gui.dialogs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.*;
import sourceheader.gui.util.preferences.*;

/**
 * Preferences dialog lets user change some basic preferences.
 *
 * @author steve
 */
public class PreferencesDialog extends JDialog {
    final ApplicationPreferences preferences;
    private final JTextField alternatingPartsConfigFileTextField = new JTextField(15);
    private final JTextField specialCharacterTextField = new JTextField(5);

    public PreferencesDialog(JFrame parent, ApplicationPreferences preferences) {
        super(parent, "Preferences");
        this.preferences = preferences;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;

        c.gridy = 0;
        c.insets = new Insets(10, 5, 0, 0);
        this.add(new JLabel("Path to file with alternating blocks configuration"), c);
        c.insets = new Insets(0, 0, 0, 0);

        c.gridy++;
        this.add(this.initAlternatingBlocksPanel(), c);

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        c.gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 10, 5, 10);
        this.add(separator, c);
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 0, 0);

        c.gridy++;
        this.add(this.initSpecialCharacterPanel(), c);

        separator = new JSeparator(JSeparator.HORIZONTAL);
        c.gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 10, 5, 10);
        this.add(separator, c);
        c.fill = GridBagConstraints.NONE;

        c.gridy++;
        this.add(this.initButtonsPanel(), c);

        this.pack();
        this.setLocation(
                parent.getX() + parent.getWidth()/2 - this.getWidth()/2,
                parent.getY() + parent.getHeight()/2 - this.getHeight()/2);
        this.setMinimumSize(this.getSize());
        this.setVisible(true);
    }

    private JPanel initSpecialCharacterPanel() {
        this.specialCharacterTextField.setText(
                "" + this.preferences.getSpecialCharacter());

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Special character"));
        panel.add(this.specialCharacterTextField);
        
        return panel;
    }

    private JPanel initAlternatingBlocksPanel() {
        this.alternatingPartsConfigFileTextField.setText(
                this.preferences.getAlternatingPartsConfigFile());

        final JButton insertDefaultConfButton = new JButton("Insert default config");
        insertDefaultConfButton.addActionListener(new DefaultConfigButtonListener());
        insertDefaultConfButton.setEnabled(
                !alternatingPartsConfigFileTextField.getText().isEmpty());

        this.alternatingPartsConfigFileTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                insertDefaultConfButton.setEnabled(
                    !alternatingPartsConfigFileTextField.getText().isEmpty());
            }
        });

        JButton openButton = new JButton("...");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser dialog = new JFileChooser();
                if (dialog.showSaveDialog(PreferencesDialog.this) == JFileChooser.APPROVE_OPTION) {
                    String path = dialog.getSelectedFile().getPath();
                    alternatingPartsConfigFileTextField.setText(path);
                    insertDefaultConfButton.setEnabled(true);
                }
            }
        });

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(this.alternatingPartsConfigFileTextField);
        panel.add(openButton);
        panel.add(insertDefaultConfButton);

        return panel;
    }

    private JPanel initButtonsPanel() {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new OkButtonActionListener());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PreferencesDialog.this.dispose();
            }
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(okButton);
        panel.add(cancelButton);

        return panel;
    }

    private class DefaultConfigButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (alternatingPartsConfigFileTextField.getText().isEmpty()) {
                return;
            }

            try {
                AlternatingPartsHelper.saveDefaultConfigTo(
                        alternatingPartsConfigFileTextField.getText());
            }
            catch(IOException ex) {
                JOptionPane.showMessageDialog(
                        PreferencesDialog.this,
                        "Error occured: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class OkButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!specialCharacterTextField.getText().isEmpty()) {
                preferences.setSpecialCharacter(
                        specialCharacterTextField.getText().charAt(0));
            }

            if (!alternatingPartsConfigFileTextField.getText().isEmpty()) {
                preferences.setAlternatingPartsConfigFile(
                        alternatingPartsConfigFileTextField.getText());
            }

            JOptionPane.showMessageDialog(
                    PreferencesDialog.this,
                    "Changes will take affect after application restart.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);

            PreferencesDialog.this.dispose();
        }
    }
}
