package snakemeter;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author yannick-broeker
 */
public class VersionCheck {

    public static final String CURRENT_VERSION = "0.1";

    public static final String VERSION_PATH = "https://raw.githubusercontent.com/ybroeker/SnakeMeter/master/version";

    public static final String APP_PATH = "https://github.com/ybroeker/SnakeMeter/blob/master/dist/SnakeMeter.jar?raw=true";
    
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
    
}
