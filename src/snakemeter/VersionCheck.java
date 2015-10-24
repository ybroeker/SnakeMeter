package snakemeter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author yannick-broeker
 */
public class VersionCheck {

    public static final String CURRENT_VERSION = "0.3";

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
        JButton button = new JButton("Update verfügbar");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                panel.add(new JLabel("Eine neue Version ist unter"));

                JTextField textfield = new JTextField(VersionCheck.APP_PATH);
                textfield.setEditable(false);
                panel.add(textfield);

                panel.add(new JLabel("verfügbar"));

                JOptionPane.showMessageDialog(window, panel, "Update", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        window.addNewVersionButton(button);
    }
}
