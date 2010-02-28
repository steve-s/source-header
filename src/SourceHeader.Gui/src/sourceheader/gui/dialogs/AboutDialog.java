/*
 * file AboutDialog.java
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
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.*;

/**
 * Dialog shows basic information about application.
 *
 * @author steve
 */
public class AboutDialog extends JDialog {

    private final String content = 
            "<html><h1>Source header</h1><br/> " +
            "<p>Makes source files headers maintaining easier!</p>" +
            "<p>version: 1.0 beta</p>" +
            "<p>author: Steve Sindelar (http://stevesindelar.cz)</p>" +
            "<p>licence: New BSD licence</p></html>";

    private final String linkContent =
            "<html><p>Project is hosted at " +
            "<a href=\"http://code.google.com/p/source-header/\" title=\"Visit project web\">google code</a>.</p></html>";

    public AboutDialog(JFrame parent) {
        super(parent, "About SourceHeader");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        this.setLayout(new BorderLayout(10, 10));

        JPanel labelsPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(content);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        
        JLabel linkLabel = new JLabel(linkContent);
        linkLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!Desktop.isDesktopSupported() ||
                    !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    JOptionPane.showMessageDialog(AboutDialog.this,
                            "Your desktor does not support this action");
                }
                else {
                    try {
                        Desktop.getDesktop().browse(new URI("http://code.google.com/p/source-header/"));
                    } catch (IOException ex) {}
                    catch (URISyntaxException ex) {}
                }
            }
        });

        labelsPanel.add(label, BorderLayout.CENTER);
        labelsPanel.add(linkLabel, BorderLayout.SOUTH);
        this.add(labelsPanel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AboutDialog.this.dispose();
            }
        });
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(okButton);
        this.add(buttonsPanel, BorderLayout.SOUTH);

        this.pack();
        this.setLocation(
                parent.getX() + parent.getWidth()/2 - this.getWidth()/2,
                parent.getY() + parent.getHeight()/2 - this.getHeight()/2);
        this.setResizable(false);
        this.setVisible(true);
    }

}
