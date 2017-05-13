package de.natternstube.snakemeter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author yannick-broeker
 */
public class VersionCheck {

    public static final String CURRENT_VERSION = "0.3.3";

    public static final String VERSION_PATH = "https://raw.githubusercontent.com/ybroeker/SnakeMeter/master/version";

    public static final String APP_PATH = "https://github.com/ybroeker/SnakeMeter/releases/latest";
    
    public boolean checkForNewerVersion() {
        
        String versionData;
        try {
            versionData = getVersionData();
        } catch (IOException ex) {
            //Logger.getLogger(VersionCheck.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
        
                if (!CURRENT_VERSION.equals(getVersionFromVersionData(versionData)) ) {            
            return true;
        }
        return false;
    }

    private String getVersionData() throws MalformedURLException, IOException {

        URL oracle = new URL(VERSION_PATH);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String versionData = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            versionData += inputLine;
        }
        in.close();
        return versionData;

    }

    private String getVersionFromVersionData(String versionData) {
        int index = versionData.indexOf("\n");
        if (index==-1) {
            return versionData;
        }
        return versionData.substring(0, index);
    }
    
    public void addNewVersionHint(Window window) {
        JButton button = new JButton(java.util.ResourceBundle.getBundle("snakemeter/Bundle").getString("UPDATE_AVAILABLE"));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                panel.add(new JLabel(java.util.ResourceBundle.getBundle("snakemeter/Bundle").getString("NEW_VERSION_UNDER")));

                JTextField textfield = new JTextField(VersionCheck.APP_PATH);
                textfield.setEditable(false);
                panel.add(textfield);

                panel.add(new JLabel(java.util.ResourceBundle.getBundle("snakemeter/Bundle").getString("AVAILABLE")));

                JOptionPane.showMessageDialog(window, panel, java.util.ResourceBundle.getBundle("snakemeter/Bundle").getString("UPDATE"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        window.addNewVersionButton(button);
    }
}
