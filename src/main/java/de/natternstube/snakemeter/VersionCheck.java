package de.natternstube.snakemeter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * @author yannick-broeker
 */
public class VersionCheck {

    public static final String APP_PATH = "https://github.com/ybroeker/SnakeMeter/releases/latest";

    public boolean checkForNewerVersion() {

        Version newestVersion = new Version(getVersionFromUrl());
        Version selfVersion = new Version(getSelfVersion());

        return (newestVersion.isNewerThan(selfVersion));
    }

    private String getSelfVersion() {
        Properties prop = new Properties();
        try {
            prop.load(Controller.class.getClassLoader().getResourceAsStream("version.properties"));
            return (String) prop.get("version");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    @org.jetbrains.annotations.NotNull
    private String getVersionFromUrl() {
        try {
            URL url = new URL(APP_PATH);
            URLConnection con = url.openConnection();
            con.getInputStream();
            String v = (con.getURL().toString());
            String[] elems = v.split("/");

            char[] chars = elems[elems.length - 1].toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (Character.isDigit(chars[i])) {
                    return (elems[elems.length - 1].substring(i));
                }
            }
            return "";
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void addNewVersionHint(Window window) {
        JButton button = new JButton(java.util.ResourceBundle.getBundle("Bundle").getString("UPDATE_AVAILABLE"));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                panel.add(new JLabel(java.util.ResourceBundle.getBundle("Bundle").getString("NEW_VERSION_UNDER")));

                JTextField textfield = new JTextField(VersionCheck.APP_PATH);
                textfield.setEditable(false);
                panel.add(textfield);

                panel.add(new JLabel(java.util.ResourceBundle.getBundle("Bundle").getString("AVAILABLE")));

                JOptionPane.showMessageDialog(window.getFrame(), panel, java.util.ResourceBundle.getBundle("Bundle").getString("UPDATE"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        window.addNewVersionButton(button);
    }
}
